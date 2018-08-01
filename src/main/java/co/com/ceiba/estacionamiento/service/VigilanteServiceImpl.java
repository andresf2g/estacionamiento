package co.com.ceiba.estacionamiento.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ceiba.estacionamiento.business.TipoPrecio;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.Vigilante;
import co.com.ceiba.estacionamiento.business.VigilanteException;
import co.com.ceiba.estacionamiento.model.PrecioEntity;
import co.com.ceiba.estacionamiento.model.VehiculoBuilder;
import co.com.ceiba.estacionamiento.model.VehiculoEntity;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;
import co.com.ceiba.estacionamiento.repository.VehiculoRepository;

@Service
public class VigilanteServiceImpl implements VigilanteService {
	@Autowired
	private VehiculoRepository repositorioVehiculo;
	@Autowired
	private PrecioRepository repositorioPrecio;
	
	private Vigilante vigilanteBusiness = new Vigilante();
	
	public void registrarIngresoVehiculo(Vehiculo vehiculo) {
		if (!vigilanteBusiness.vehiculoPlacaDiaCorrecto(vehiculo.getPlaca(), vehiculo.getFechaIngreso())) {
			throw new VigilanteException(VigilanteException.PLACA_A_DIA_INCORRECTO);
		}
		if (vehiculo.getTipoVehiculo().equals(TipoVehiculo.MOTO) && repositorioVehiculo.findByTipoVehiculo(vehiculo.getTipoVehiculo()).size() > 10) {
			throw new VigilanteException(VigilanteException.INDISPONIBILIDAD_PARQUEADERO);
		}
		if (vehiculo.getTipoVehiculo().equals(TipoVehiculo.CARRO) && repositorioVehiculo.findByTipoVehiculo(vehiculo.getTipoVehiculo()).size() > 20) {
			throw new VigilanteException(VigilanteException.INDISPONIBILIDAD_PARQUEADERO);
		}
		if (repositorioVehiculo.findById(vehiculo.getPlaca()).isPresent()) {
			throw new VigilanteException(VigilanteException.VEHICULO_YA_INGRESADO);
		}
		repositorioVehiculo.save(VehiculoBuilder.convertirAEntity(vehiculo));
	}

	public BigDecimal registrarEgresoVehiculo(Vehiculo vehiculo, Date fechaEgreso) {
		BigDecimal valorPagar = BigDecimal.ZERO;
		if (!repositorioVehiculo.findById(vehiculo.getPlaca()).isPresent()) {
			throw new VigilanteException(VigilanteException.VEHICULO_NO_INGRESADO);
		}
		List<PrecioEntity> listaPrecios = repositorioPrecio.findByIdPrecioTipoVehiculo(vehiculo.getTipoVehiculo());
		int[] tiempoParqueo = vigilanteBusiness.calcularTiempoDiferencia(vehiculo.getFechaIngreso(), fechaEgreso);
		if (tiempoParqueo[0] > 0) {
			/* Tiempo en dias */
			valorPagar = valorPagar.add(obtenerPrecioParqueo(listaPrecios, TipoPrecio.DIA).multiply(new BigDecimal(tiempoParqueo[0])));
		}
		if (tiempoParqueo[1] > 0) {
			/* Tiempo horas */
			valorPagar = valorPagar.add(obtenerPrecioParqueo(listaPrecios, TipoPrecio.HORA).multiply(new BigDecimal(tiempoParqueo[1])));
		}
		if (vehiculo.getTipoVehiculo().equals(TipoVehiculo.MOTO) && vehiculo.getCilindraje() > 500) {
			/* Precio adicional por motos con cilindraje mayor */
			valorPagar = valorPagar.add(obtenerPrecioParqueo(listaPrecios, TipoPrecio.ADICIONAL));
		}
		repositorioVehiculo.deleteById(vehiculo.getPlaca());
		return valorPagar;
	}
	
	public List<Vehiculo> listarVehiculosParqueados(TipoVehiculo tipoVehiculo) {
		List<Vehiculo> listadoVehiculos = new ArrayList<>();
		if (tipoVehiculo != null) {
			List<VehiculoEntity> listadoEntidades = repositorioVehiculo.findByTipoVehiculo(tipoVehiculo);
			listadoEntidades.forEach(le -> listadoVehiculos.add(VehiculoBuilder.convertirADominio(le)));
		} else {
			Iterable<VehiculoEntity> listadoEntidades = repositorioVehiculo.findAll();
			listadoEntidades.forEach(le -> listadoVehiculos.add(VehiculoBuilder.convertirADominio(le)));
		}
		return listadoVehiculos;
	}
	
	private BigDecimal obtenerPrecioParqueo(List<PrecioEntity> listaPrecios, TipoPrecio tipoPrecio) {
		if (listaPrecios.isEmpty()) {
			throw new VigilanteException(VigilanteException.PRECIOS_NO_ENCONTRADOS);
		}
		Optional<PrecioEntity> precioEntity = listaPrecios.stream().filter(pe -> pe.getIdPrecio().getTipoPrecio().equals(tipoPrecio)).findFirst();
		if (!precioEntity.isPresent()) {
			throw new VigilanteException(VigilanteException.PRECIOS_NO_ENCONTRADOS);
		}
		return precioEntity.get().getValor();
	}

	public void evacuarVehiculosParqueados() {
		repositorioVehiculo.deleteAll();
	}

	public Vehiculo buscarVehiculoParqueado(String placa) {
		Vehiculo vehiculo = null;
		Optional<VehiculoEntity> vehiculoEntity = repositorioVehiculo.findById(placa);
		if (vehiculoEntity.isPresent()) {
			vehiculo = VehiculoBuilder.convertirADominio(vehiculoEntity.get());
		}
		return vehiculo;
	}
}
