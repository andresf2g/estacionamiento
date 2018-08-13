package co.com.ceiba.estacionamiento.controller;

import java.util.Date;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public class VehiculoRequestBody {
	private String placa;
	private String tipoVehiculo;
	private Integer cilindraje;
	private Date fechaIngreso;
	private Date fechaSalida;

	public VehiculoRequestBody() {
	}
	
	public VehiculoRequestBody(String placa, String tipoVehiculo, Integer cilindraje, Date fechaIngreso, Date fechaSalida) {
		this.placa = placa;
		this.tipoVehiculo = tipoVehiculo;
		this.cilindraje = cilindraje;
		this.fechaIngreso = fechaIngreso;
		if (fechaSalida != null) {
			this.fechaSalida = fechaSalida;
		}
	}
	
	public VehiculoRequestBody(Vehiculo vehiculo) {
		this.placa = vehiculo.getPlaca();
		this.tipoVehiculo = vehiculo.getTipoVehiculo().toString();
		this.cilindraje = vehiculo.getCilindraje();
		this.fechaIngreso = vehiculo.getFechaIngreso();
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

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public Date getFechaSalida() {
		return fechaSalida;
	}

}
