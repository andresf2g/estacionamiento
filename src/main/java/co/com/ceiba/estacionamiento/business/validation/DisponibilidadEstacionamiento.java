package co.com.ceiba.estacionamiento.business.validation;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;
import co.com.ceiba.estacionamiento.repository.VehiculoRepository;

public class DisponibilidadEstacionamiento implements ValidacionVehiculo {

	private VehiculoRepository repositorioVehiculo;
	
	public DisponibilidadEstacionamiento(VehiculoRepository repositorioVehiculo) {
		this.repositorioVehiculo = repositorioVehiculo;
	}
	
	@Override
	public void validar(Vehiculo vehiculo) {
		int vehiculosParqueados = repositorioVehiculo.countByTipoVehiculo(vehiculo.getTipoVehiculo());
		if ((vehiculo.getTipoVehiculo().equals(TipoVehiculo.MOTO) && vehiculosParqueados >= 10)
				|| (vehiculo.getTipoVehiculo().equals(TipoVehiculo.CARRO) && vehiculosParqueados >= 20)) {
			throw new VigilanteServiceException(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO);
		}
	}

}
