package co.com.ceiba.estacionamiento.business;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Vigilante {

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
	
	public static SimpleDateFormat formatoFecha() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}
}
