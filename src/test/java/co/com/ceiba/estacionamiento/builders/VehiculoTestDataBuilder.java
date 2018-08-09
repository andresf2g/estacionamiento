package co.com.ceiba.estacionamiento.builders;

import java.util.Date;

import co.com.ceiba.estacionamiento.EstacionamientoApplication;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.controller.VehiculoRequestBody;

public abstract class VehiculoTestDataBuilder {
	private static final String FECHA_INGRESO = "2018-07-10 10:00";
	
	protected String placa;
	protected Integer cilindraje;
	protected TipoVehiculo tipoVehiculo;
	protected Date fechaIngreso;
	protected Date fechaEgreso;
	protected String prefijoPlaca;
	
	public VehiculoTestDataBuilder() {
		try {
			this.fechaIngreso = EstacionamientoApplication.formatoFecha().parse(FECHA_INGRESO);
		} catch (Exception e) {
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
	
	public VehiculoTestDataBuilder conFechaIngreso(String fechaIngreso) {
		try {
			this.fechaIngreso = EstacionamientoApplication.formatoFecha().parse(fechaIngreso);
		} catch (Exception e) {
			this.fechaIngreso = new Date();
		}
		return this;
	}
	
	public VehiculoTestDataBuilder conFechaEgreso(String fechaEgreso) {
		try {
			this.fechaEgreso = EstacionamientoApplication.formatoFecha().parse(fechaEgreso);
		} catch (Exception e) {
			this.fechaEgreso = new Date();
		}
		return this;
	}
	
	public String getPrefijoPlaca() {
		return this.prefijoPlaca;
	}
	
	public Vehiculo build() {
		return new Vehiculo(this.placa, this.cilindraje, this.tipoVehiculo, this.fechaIngreso);
	}
	
	public VehiculoRequestBody buildBody() {
		return new VehiculoRequestBody(this.placa, this.tipoVehiculo.toString(), this.cilindraje, this.fechaIngreso, this.fechaEgreso);
	}
}
