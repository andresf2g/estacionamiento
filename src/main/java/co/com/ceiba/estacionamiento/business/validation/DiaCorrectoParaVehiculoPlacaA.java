package co.com.ceiba.estacionamiento.business.validation;

import java.util.Calendar;

import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;

public class DiaCorrectoParaVehiculoPlacaA implements ValidacionVehiculo {

	@Override
	public void validarVehiculoCorrecto(Vehiculo vehiculo) {
		if (vehiculo.getPlaca().toUpperCase().startsWith("A")) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(vehiculo.getFechaIngreso());
			if (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY && calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
				throw new VigilanteServiceException(VigilanteServiceException.PLACA_A_DIA_INCORRECTO);
			}
		}
	}

}
