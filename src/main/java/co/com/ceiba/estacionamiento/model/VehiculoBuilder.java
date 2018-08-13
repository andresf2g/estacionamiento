package co.com.ceiba.estacionamiento.model;

import co.com.ceiba.estacionamiento.business.Vehiculo;

public final class VehiculoBuilder {

	private VehiculoBuilder() {
	}

	public static Vehiculo convertirADominio(VehiculoEntity vehiculoEntity) {
		return new Vehiculo(vehiculoEntity.getPlaca(), vehiculoEntity.getTipoVehiculo(), vehiculoEntity.getCilindraje(), vehiculoEntity.getFechaIngreso());
	}

	public static VehiculoEntity convertirAEntity(Vehiculo vehiculo) {
		return new VehiculoEntity(vehiculo.getPlaca(), vehiculo.getCilindraje(), vehiculo.getTipoVehiculo(), vehiculo.getFechaIngreso());
	}
}
