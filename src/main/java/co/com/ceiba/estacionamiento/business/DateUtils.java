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

}
