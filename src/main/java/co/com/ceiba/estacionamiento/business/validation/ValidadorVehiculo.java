package co.com.ceiba.estacionamiento.business.validation;

import java.util.ArrayList;
import java.util.List;

public abstract class ValidadorVehiculo {
	protected List<ValidacionVehiculo> listaValidaciones = new ArrayList<>();

	public List<ValidacionVehiculo> obtenerValidaciones() {
		return this.listaValidaciones;
	}
}
