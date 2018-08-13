package co.com.ceiba.estacionamiento.builders;

import java.util.Date;

import co.com.ceiba.estacionamiento.business.DateUtils;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public abstract class VehiculoTestDataBuilder {
	private static final String FECHA_INGRESO = "2018-07-10 10:00";
	
	protected String placa;
	protected Integer cilindraje;
	protected TipoVehiculo tipoVehiculo;
	protected Date fechaIngreso;
	protected Date fechaSalida;
	protected String prefijoPlaca;
	
	public VehiculoTestDataBuilder() {
		this.fechaIngreso = DateUtils.convertirTextoAFecha(FECHA_INGRESO);
	}
	
	public VehiculoTestDataBuilder conPlaca(String placa) {
		this.placa = placa;
		return this;
	}

	public VehiculoTestDataBuilder conCilindraje(Integer cilindraje) {
		this.cilindraje = cilindraje;
		return this;
	}
	
	public VehiculoTestDataBuilder conFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = DateUtils.convertirTextoAFecha(fechaIngreso);
		return this;
	}
	
	public VehiculoTestDataBuilder conFechaSalida(String fechaSalida) {
		this.fechaSalida = DateUtils.convertirTextoAFecha(fechaSalida);
		return this;
	}
	
	public String getPrefijoPlaca() {
		return this.prefijoPlaca;
	}
	
	public Vehiculo build() {
		return new Vehiculo(this.placa, this.tipoVehiculo, this.cilindraje, this.fechaIngreso, this.fechaSalida);
	}
	
}
