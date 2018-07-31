package co.com.ceiba.estacionamiento.business;

public class VigilanteException extends RuntimeException {

	private static final long serialVersionUID = 2823477504645049167L;
	
	public static final String VEHICULO_YA_INGRESADO = "El vehiculo ya ha sido ingresado";
	public static final String VEHICULO_NO_INGRESADO = "El vehiculo no se ha ingresado";
	public static final String INDISPONIBILIDAD_PARQUEADERO = "No hay disponibilidad de parqueadero para el vehiculo";
	public static final String PLACA_A_DIA_INCORRECTO = "Los vehiculos con placa iniciada en A solo pueden parquear Domingos y Lunes";
	public static final String PRECIOS_NO_ENCONTRADOS = "No se encontraron precios para el vehiculo";

	public VigilanteException(String message) {
		super(message);
	}
}
