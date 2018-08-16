package co.com.ceiba.estacionamiento.business.validation;

import co.com.ceiba.estacionamiento.business.DateUtils;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public class FechaSalidaNoNula implements ValidacionVehiculo {

	@Override
	public void validar(Vehiculo vehiculo) {
		if (vehiculo.getFechaSalida() == null) {
			vehiculo.setFechaSalida(DateUtils.obtenerFechaActual());
		}
	}

}
