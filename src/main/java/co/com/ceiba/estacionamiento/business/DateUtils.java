package co.com.ceiba.estacionamiento.business;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class DateUtils {
	private static final Log LOGGER = LogFactory.getLog(DateUtils.class);
	
	private DateUtils() {
	}

	public static SimpleDateFormat formatoFecha() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}

	public static Date convertirTextoAFecha(String fecha) {
		try {
			return DateUtils.formatoFecha().parse(fecha);
		} catch (Exception e) {
			LOGGER.info(e);
			return new Date();
		}
	}

	public static String convertirFechaATexto(Date fecha) {
		return DateUtils.formatoFecha().format(fecha);
	}
	
	public static TiempoEstadia obtenerTiempoEstadia(Date fechaIngreso, Date fechaSalida) {
		long timeDifference = fechaSalida.getTime() - fechaIngreso.getTime();
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

}
