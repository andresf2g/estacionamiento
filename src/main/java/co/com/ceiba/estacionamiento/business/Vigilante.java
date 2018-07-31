package co.com.ceiba.estacionamiento.business;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import co.com.ceiba.estacionamiento.model.PrecioEntity;
import co.com.ceiba.estacionamiento.model.VehiculoBuilder;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;
import co.com.ceiba.estacionamiento.repository.VehiculoRepository;

public class Vigilante {
	private VehiculoRepository repositorioVehiculo;
	private PrecioRepository repositorioPrecio;
	
	public Vigilante() {
	}

	public Vigilante(VehiculoRepository repositorioVehiculo, PrecioRepository repositorioPrecio) {
		this.repositorioVehiculo = repositorioVehiculo;
		this.repositorioPrecio = repositorioPrecio;
	}

	public void registrarIngresoVehiculo(Vehiculo vehiculo) {
		if (!vehiculoPlacaDiaCorrecto(vehiculo.getPlaca(), vehiculo.getFechaIngreso())) {
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
		int[] tiempoParqueo = calcularTiempoDiferencia(vehiculo.getFechaIngreso(), fechaEgreso);
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
