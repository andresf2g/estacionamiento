package co.com.ceiba.estacionamiento.controller;

import java.util.Date;

import co.com.ceiba.estacionamiento.EstacionamientoApplication;

public class VehiculoRequestBody {
	private String placa;
	private String tipoVehiculo;
	private Integer cilindraje;
	private String fechaIngreso;
	private String fechaEgreso;

	public VehiculoRequestBody() {
	}
	
	public VehiculoRequestBody(String placa, String tipoVehiculo, Integer cilindraje, Date fechaIngreso) {
		this.placa = placa;
		this.tipoVehiculo = tipoVehiculo;
		this.cilindraje = cilindraje;
		this.fechaIngreso = EstacionamientoApplication.formatoFecha().format(fechaIngreso);
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public String getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(String tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public Integer getCilindraje() {
		return cilindraje;
	}

	public void setCilindraje(Integer cilindraje) {
		this.cilindraje = cilindraje;
	}

	public String getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(String fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public String getFechaEgreso() {
		return fechaEgreso;
	}

	public void setFechaEgreso(String fechaEgreso) {
		this.fechaEgreso = fechaEgreso;
	}

}
