package co.com.ceiba.estacionamiento.controller;

import java.util.Date;

import co.com.ceiba.estacionamiento.EstacionamientoApplication;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public class VehiculoRequestBody {
	private String placa;
	private String tipoVehiculo;
	private Integer cilindraje;
	private String fechaIngreso;
	private String fechaEgreso;

	public VehiculoRequestBody() {
	}
	
	public VehiculoRequestBody(String placa, String tipoVehiculo, Integer cilindraje, Date fechaIngreso, Date fechaEgreso) {
		this.placa = placa;
		this.tipoVehiculo = tipoVehiculo;
		this.cilindraje = cilindraje;
		this.fechaIngreso = EstacionamientoApplication.formatoFecha().format(fechaIngreso);
		if (fechaEgreso != null) {
			this.fechaEgreso = EstacionamientoApplication.formatoFecha().format(fechaEgreso);
		}
	}
	
	public VehiculoRequestBody(Vehiculo vehiculo) {
		this.placa = vehiculo.getPlaca();
		this.tipoVehiculo = vehiculo.getTipoVehiculo().toString();
		this.cilindraje = vehiculo.getCilindraje();
		this.fechaIngreso = EstacionamientoApplication.formatoFecha().format(vehiculo.getFechaIngreso());
	}
	
	public Vehiculo obtenerVehiculo() {
		return new Vehiculo(getPlaca(), getCilindraje(), TipoVehiculo.valueOf(getTipoVehiculo()), getFechaIngreso());
	}

	public String getPlaca() {
		return placa;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public Integer getCilindraje() {
		return cilindraje;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public String getFechaEgreso() {
		return fechaEgreso;
	}

}
