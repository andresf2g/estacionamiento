package co.com.ceiba.estacionamiento.business.validation;

import java.util.Date;

import co.com.ceiba.estacionamiento.business.DateUtils;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public class FechaSalidaNoNula implements ValidacionVehiculo {

	private Date fechaSalida;

	public FechaSalidaNoNula(Date fechaSalida) {
		this.fechaSalida = fechaSalida;
	}

	@Override
	public void validar(Vehiculo vehiculo) {
		vehiculo.setFechaSalida(fechaSalida);
		if (vehiculo.getFechaSalida() == null) {
			vehiculo.setFechaSalida(DateUtils.obtenerFechaActual());
		}
	}

}
