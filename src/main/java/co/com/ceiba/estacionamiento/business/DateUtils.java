package co.com.ceiba.estacionamiento.business;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
	
	private DateUtils() {
	}

	public static SimpleDateFormat formatoFecha() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}

	public static Date convertirTextoAFecha(String fecha) {
		try {
			return DateUtils.formatoFecha().parse(fecha);
		} catch (Exception e) {
			return new Date();
		}
	}

	public static String convertirFechaATexto(Date fecha) {
		return DateUtils.formatoFecha().format(fecha);
	}

}
