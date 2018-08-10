package co.com.ceiba.estacionamiento.business.validation;

import java.util.Arrays;

import co.com.ceiba.estacionamiento.repository.VehiculoRepository;

public class ValidadorIngresoVehiculo extends ValidadorVehiculo {

	public ValidadorIngresoVehiculo(VehiculoRepository repositorioVehiculo) {
		super.listaValidaciones = Arrays.asList(new DiaCorrectoParaVehiculoPlacaA(),
				new DisponibilidadEstacionamiento(repositorioVehiculo), new VehiculoEstacionado(repositorioVehiculo)); 
	}
	
}
