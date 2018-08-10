package co.com.ceiba.estacionamiento.business.validation;

import java.util.Arrays;

public class ValidadorSalidaVehiculo extends ValidadorVehiculo {
	
	public ValidadorSalidaVehiculo() {
		super.listaValidaciones = Arrays.asList(new VehiculoNoEstacionado());
	}

}
