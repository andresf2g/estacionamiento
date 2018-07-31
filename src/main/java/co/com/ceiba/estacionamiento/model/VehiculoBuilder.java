package co.com.ceiba.estacionamiento.model;

import co.com.ceiba.estacionamiento.business.Vehiculo;

public class VehiculoBuilder {

	private VehiculoBuilder() {
	}

	public static Vehiculo convertirADominio(VehiculoEntity vehiculoEntity) {
		Vehiculo vehiculo = null;
		if (vehiculoEntity != null) {
			vehiculo = new Vehiculo(vehiculoEntity.getPlaca(), vehiculoEntity.getCilindraje(), vehiculoEntity.getTipoVehiculo(), vehiculoEntity.getFechaIngreso());
		}
		return vehiculo;
	}

	public static VehiculoEntity convertirAEntity(Vehiculo vehiculo) {
		return new VehiculoEntity(vehiculo.getPlaca(), vehiculo.getCilindraje(), vehiculo.getTipoVehiculo(), vehiculo.getFechaIngreso());
	}
}
