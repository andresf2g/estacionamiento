package co.com.ceiba.estacionamiento.business;

public class VigilanteServiceException extends RuntimeException {

	private static final long serialVersionUID = 2823477504645049167L;
	
	public static final String VEHICULO_YA_INGRESADO = "El vehiculo ya esta estacionado";
	public static final String VEHICULO_NO_INGRESADO = "El vehiculo no esta estacionado";
	public static final String INDISPONIBILIDAD_PARQUEADERO = "No hay disponibilidad de estacionamiento para el vehiculo";
	public static final String PLACA_A_DIA_INCORRECTO = "Los vehiculos con placa iniciada en A solo pueden estacionar Domingos y Lunes";
	public static final String PRECIOS_NO_ENCONTRADOS = "No se encontraron precios para el vehiculo";
	public static final String TIPO_VEHICULO = "El tipo de vehiculo indicado no existe en el sistema";

	public VigilanteServiceException(String message) {
		super(message);
	}
}
