package co.com.ceiba.estacionamiento.business.validation;

import java.util.Arrays;
import java.util.Date;

public class ValidadorSalidaVehiculo extends ValidadorVehiculo {

	public ValidadorSalidaVehiculo(Date fechaSalida) {
		super.listaValidaciones = Arrays.asList(new FechaSalidaNoNula(fechaSalida), new VehiculoEstacionado());
	}

}
