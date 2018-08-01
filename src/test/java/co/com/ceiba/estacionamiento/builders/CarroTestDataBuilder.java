package co.com.ceiba.estacionamiento.builders;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;

public class CarroTestDataBuilder extends VehiculoTestDataBuilder {

	private static final String PLACA = "EFG854";
	private static final String PREFIJO_PLACA = "EFG";
	private static final TipoVehiculo TIPO_VEHICULO = TipoVehiculo.CARRO;

	public CarroTestDataBuilder() {
		super();
		super.placa = PLACA;
		super.tipoVehiculo = TIPO_VEHICULO;
		super.prefijoPlaca = PREFIJO_PLACA;
	}
}
