package co.com.ceiba.estacionamiento.business.validation;

import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;

public class VehiculoNoEstacionado implements ValidacionVehiculo {

	@Override
	public void validar(Vehiculo vehiculo) {
		if (vehiculo == null) {
			throw new VigilanteServiceException(VigilanteServiceException.VEHICULO_NO_INGRESADO);
		}
	}

}
