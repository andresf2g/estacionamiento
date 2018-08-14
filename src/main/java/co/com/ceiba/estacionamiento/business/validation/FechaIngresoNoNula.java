package co.com.ceiba.estacionamiento.business.validation;

import co.com.ceiba.estacionamiento.business.DateUtils;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public class FechaIngresoNoNula implements ValidacionVehiculo {

	@Override
	public void validar(Vehiculo vehiculo) {
		if (vehiculo.getFechaIngreso() == null) {
			vehiculo.setFechaIngreso(DateUtils.obtenerFechaActual());
		}
	}

}
