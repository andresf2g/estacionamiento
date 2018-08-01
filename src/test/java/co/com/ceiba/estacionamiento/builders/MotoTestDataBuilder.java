package co.com.ceiba.estacionamiento.builders;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;

public class MotoTestDataBuilder extends VehiculoTestDataBuilder {

	private static final String PLACA = "BCD98E";
	private static final String PREFIJO_PLACA = "BCD";
	private static final Integer CILINDRAJE = 200;
	private static final TipoVehiculo TIPO_VEHICULO = TipoVehiculo.MOTO;
	
	public MotoTestDataBuilder() {
		super();
		super.placa = PLACA;
		super.cilindraje = CILINDRAJE;
		super.tipoVehiculo = TIPO_VEHICULO;
		super.prefijoPlaca = PREFIJO_PLACA;
	}
}
