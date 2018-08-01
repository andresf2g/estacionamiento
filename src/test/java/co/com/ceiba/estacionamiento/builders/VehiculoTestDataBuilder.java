package co.com.ceiba.estacionamiento.builders;

import java.text.ParseException;
import java.util.Date;

import co.com.ceiba.estacionamiento.EstacionamientoApplication;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public abstract class VehiculoTestDataBuilder {
	private static final String FECHA_INGRESO = "2018-07-10 10:00";
	
	protected String placa;
	protected Integer cilindraje;
	protected TipoVehiculo tipoVehiculo;
	protected Date fechaIngreso;
	protected String prefijoPlaca;
	
	public VehiculoTestDataBuilder() {
		try {
			this.fechaIngreso = EstacionamientoApplication.formatoFecha().parse(FECHA_INGRESO);
		} catch (ParseException e) {
			this.fechaIngreso = new Date();
		}
	}
	
	public VehiculoTestDataBuilder conPlaca(String placa) {
		this.placa = placa;
		return this;
	}

	public VehiculoTestDataBuilder conCilindraje(Integer cilindraje) {
		this.cilindraje = cilindraje;
		return this;
	}
	
	public VehiculoTestDataBuilder conTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
		return this;
	}
	
	public VehiculoTestDataBuilder conFechaIngreso(String fechaIngreso) {
		try {
			this.fechaIngreso = EstacionamientoApplication.formatoFecha().parse(fechaIngreso);
		} catch (ParseException e) {
			this.fechaIngreso = new Date();
		}
		return this;
	}
	
	public String getPrefijoPlaca() {
		return this.prefijoPlaca;
	}
	
	public Vehiculo build() {
		return new Vehiculo(this.placa, this.cilindraje, this.tipoVehiculo, this.fechaIngreso);
	}
}
