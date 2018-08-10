package co.com.ceiba.estacionamiento.business.validation;

import java.util.Arrays;

public class ValidadorEgresoVehiculo extends ValidadorVehiculo {
	
	public ValidadorEgresoVehiculo() {
		super.listaValidaciones = Arrays.asList(new VehiculoNoEstacionado());
	}

}
