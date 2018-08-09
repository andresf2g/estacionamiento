package co.com.ceiba.estacionamiento.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ceiba.estacionamiento.model.IdPrecio;
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
	
	private boolean esDiaCorrectoParaVehiculoPlacaA(Vehiculo vehiculo) {
		if (vehiculo.getPlaca().toUpperCase().startsWith("A")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(vehiculo.getFechaIngreso());
			return calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY;
		}
		return true;
	}
	
	private boolean hayDisponibilidadEstacionamiento(Vehiculo vehiculo) {
		int vehiculosParqueados = repositorioVehiculo.countByTipoVehiculo(vehiculo.getTipoVehiculo());
		return (vehiculo.getTipoVehiculo().equals(TipoVehiculo.MOTO) && vehiculosParqueados < 10)
				|| (vehiculo.getTipoVehiculo().equals(TipoVehiculo.CARRO) && vehiculosParqueados < 20);
	}
	
	private boolean estaVehiculoEstacionado(Vehiculo vehiculo) {
		return repositorioVehiculo.findById(vehiculo.getPlaca()).isPresent();
	}
	
	public void registrarIngresoVehiculo(Vehiculo vehiculo) {
		if (!esDiaCorrectoParaVehiculoPlacaA(vehiculo)) {
			throw new VigilanteServiceException(VigilanteServiceException.PLACA_A_DIA_INCORRECTO);
		}
		if (!hayDisponibilidadEstacionamiento(vehiculo)) {
			throw new VigilanteServiceException(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO);
		}
		if (estaVehiculoEstacionado(vehiculo)) {
			throw new VigilanteServiceException(VigilanteServiceException.VEHICULO_YA_INGRESADO);
		}
		repositorioVehiculo.save(VehiculoBuilder.convertirAEntity(vehiculo));
	}
	
	private TiempoEstadia obtenerTiempoEstadia(Date fechaIngreso, Date fechaEgreso) {
		long timeDifference = fechaEgreso.getTime() - fechaIngreso.getTime();
		int hours = (int) Math.ceil(((double) timeDifference) / 1000 / 60 / 60);
		if (hours > 8) {
			if (hours / 24 == 0) {
				return new TiempoEstadia(1, 0);
			} else if (hours - ((hours / 24) * 24) > 8) {
				return new TiempoEstadia((hours / 24) + 1, 0);
			}
			return new TiempoEstadia(hours / 24, hours - ((hours / 24) * 24));
		}
		return new TiempoEstadia(0, hours);
	}
	
	private BigDecimal obtenerPrecioEstacionamiento(TipoVehiculo tipoVehiculo, TipoPrecio tipoPrecio, int tiempo) {
		Optional<PrecioEntity> precio = repositorioPrecio.findById(new IdPrecio(tipoVehiculo, tipoPrecio));
		if (!precio.isPresent()) {
			throw new VigilanteServiceException(VigilanteServiceException.PRECIO_NO_ENCONTRADO);
		}
		return precio.get().getValor().multiply(new BigDecimal(tiempo));
	}
	
	private BigDecimal obtenerPrecioAdicionalEstacionamiento(Vehiculo vehiculo) {
		if (vehiculo.getTipoVehiculo().equals(TipoVehiculo.MOTO) && vehiculo.getCilindraje() > 500) {
			return obtenerPrecioEstacionamiento(vehiculo.getTipoVehiculo(), TipoPrecio.ADICIONAL, 1);
		}
		return BigDecimal.ZERO;
	}
	
	public BigDecimal registrarEgresoVehiculo(String placa, String fechaEgreso) {
		BigDecimal valorPagar = BigDecimal.ZERO;
		Vehiculo vehiculo = buscarVehiculoParqueado(placa); 
		if (vehiculo == null) {
			throw new VigilanteServiceException(VigilanteServiceException.VEHICULO_NO_INGRESADO);
		}
		TiempoEstadia tiempoEstadia = obtenerTiempoEstadia(vehiculo.getFechaIngreso(), DateUtils.convertirTextoAFecha(fechaEgreso));
		valorPagar = valorPagar.add(obtenerPrecioEstacionamiento(vehiculo.getTipoVehiculo(), TipoPrecio.DIA, tiempoEstadia.getDias()));
		valorPagar = valorPagar.add(obtenerPrecioEstacionamiento(vehiculo.getTipoVehiculo(), TipoPrecio.HORA, tiempoEstadia.getHoras()));
		valorPagar = valorPagar.add(obtenerPrecioAdicionalEstacionamiento(vehiculo));
		repositorioVehiculo.deleteById(placa);
		return valorPagar;
	}
	
	public List<Vehiculo> listarVehiculosParqueados(String tipoVehiculo) {
		List<Vehiculo> listadoVehiculos = new ArrayList<>();
		Iterable<VehiculoEntity> listadoEntidades;
		if (tipoVehiculo != null) {
			listadoEntidades = repositorioVehiculo.findByTipoVehiculo(TipoVehiculo.valueOf(tipoVehiculo));
		} else {
			listadoEntidades = repositorioVehiculo.findAll();
		}
		listadoEntidades.forEach(vehiculoEntity -> listadoVehiculos.add(VehiculoBuilder.convertirADominio(vehiculoEntity)));
		return listadoVehiculos;
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
