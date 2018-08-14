package co.com.ceiba.estacionamiento.business.validation;

import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;
import co.com.ceiba.estacionamiento.repository.VehiculoRepository;

public class VehiculoNoEstacionado implements ValidacionVehiculo {

	private VehiculoRepository repositorioVehiculo;

	public VehiculoNoEstacionado(VehiculoRepository repositorioVehiculo) {
		this.repositorioVehiculo = repositorioVehiculo;
	}

	@Override
	public void validar(Vehiculo vehiculo) {
		if (repositorioVehiculo.findById(vehiculo.getPlaca()).isPresent()) {
			throw new VigilanteServiceException(VigilanteServiceException.VEHICULO_YA_INGRESADO);
		}
	}

}
