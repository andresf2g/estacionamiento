package co.com.ceiba.estacionamiento.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
	
	public void registrarIngresoVehiculo(Vehiculo vehiculo) {
		if (!vehiculoPlacaDiaCorrecto(vehiculo.getPlaca(), vehiculo.getFechaIngreso())) {
			throw new VigilanteServiceException(VigilanteServiceException.PLACA_A_DIA_INCORRECTO);
		}
		if (vehiculo.getTipoVehiculo().equals(TipoVehiculo.MOTO) && repositorioVehiculo.findByTipoVehiculo(vehiculo.getTipoVehiculo()).size() > 10) {
			throw new VigilanteServiceException(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO);
		}
		if (vehiculo.getTipoVehiculo().equals(TipoVehiculo.CARRO) && repositorioVehiculo.findByTipoVehiculo(vehiculo.getTipoVehiculo()).size() > 20) {
			throw new VigilanteServiceException(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO);
		}
		if (repositorioVehiculo.findById(vehiculo.getPlaca()).isPresent()) {
			throw new VigilanteServiceException(VigilanteServiceException.VEHICULO_YA_INGRESADO);
		}
		repositorioVehiculo.save(VehiculoBuilder.convertirAEntity(vehiculo));
	}

	public BigDecimal registrarEgresoVehiculo(String placa, Date fechaEgreso) {
		BigDecimal valorPagar = BigDecimal.ZERO;
		Optional<VehiculoEntity> vehiculo = repositorioVehiculo.findById(placa); 
		if (!vehiculo.isPresent()) {
			throw new VigilanteServiceException(VigilanteServiceException.VEHICULO_NO_INGRESADO);
		}
		List<PrecioEntity> listaPrecios = repositorioPrecio.findByIdPrecioTipoVehiculo(vehiculo.get().getTipoVehiculo());
		int[] tiempoParqueo = calcularTiempoDiferencia(vehiculo.get().getFechaIngreso(), fechaEgreso);
		if (tiempoParqueo[0] > 0) {
			/* Tiempo en dias */
			valorPagar = valorPagar.add(obtenerPrecioParqueo(listaPrecios, TipoPrecio.DIA).multiply(new BigDecimal(tiempoParqueo[0])));
		}
		if (tiempoParqueo[1] > 0) {
			/* Tiempo horas */
			valorPagar = valorPagar.add(obtenerPrecioParqueo(listaPrecios, TipoPrecio.HORA).multiply(new BigDecimal(tiempoParqueo[1])));
		}
		if (vehiculo.get().getTipoVehiculo().equals(TipoVehiculo.MOTO) && vehiculo.get().getCilindraje() > 500) {
			/* Precio adicional por motos con cilindraje mayor */
			valorPagar = valorPagar.add(obtenerPrecioParqueo(listaPrecios, TipoPrecio.ADICIONAL));
		}
		repositorioVehiculo.deleteById(placa);
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
			throw new VigilanteServiceException(VigilanteServiceException.PRECIOS_NO_ENCONTRADOS);
		}
		Optional<PrecioEntity> precioEntity = listaPrecios.stream().filter(pe -> pe.getIdPrecio().getTipoPrecio().equals(tipoPrecio)).findFirst();
		if (!precioEntity.isPresent()) {
			throw new VigilanteServiceException(VigilanteServiceException.PRECIOS_NO_ENCONTRADOS);
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
	
	public int[] calcularTiempoDiferencia(Date fechaIngreso, Date fechaEgreso) {
		long timeDifference = fechaEgreso.getTime() - fechaIngreso.getTime();
		int hours = (int) Math.ceil(((double) timeDifference) / 1000 / 60 / 60);
		if (hours > 8) {
			if (hours / 24 == 0) {
				return new int[] { 1, 0 };
			} else if (hours - ((hours / 24) * 24) > 8) {
				return new int[] { (hours / 24) + 1, 0 };
			}
			return new int[] { hours / 24, hours - ((hours / 24) * 24) };
		}
		return new int[] { 0, hours };
	}

	public boolean vehiculoPlacaDiaCorrecto(String placa, Date fechaIngreso) {
		if (placa.toUpperCase().startsWith("A")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(fechaIngreso);
			return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
		}
		return true;
	}
}
