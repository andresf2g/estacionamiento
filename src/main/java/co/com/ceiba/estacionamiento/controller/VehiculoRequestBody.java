package co.com.ceiba.estacionamiento.controller;

import java.util.Date;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.DateUtils;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public class VehiculoRequestBody {
	private String placa;
	private String tipoVehiculo;
	private Integer cilindraje;
	private String fechaIngreso;
	private String fechaSalida;

	public VehiculoRequestBody() {
	}
	
	public VehiculoRequestBody(String placa, String tipoVehiculo, Integer cilindraje, Date fechaIngreso, Date fechaSalida) {
		this.placa = placa;
		this.tipoVehiculo = tipoVehiculo;
		this.cilindraje = cilindraje;
		this.fechaIngreso = DateUtils.convertirFechaATexto(fechaIngreso);
		if (fechaSalida != null) {
			this.fechaSalida = DateUtils.convertirFechaATexto(fechaSalida);
		}
	}
	
	public VehiculoRequestBody(Vehiculo vehiculo) {
		this.placa = vehiculo.getPlaca();
		this.tipoVehiculo = vehiculo.getTipoVehiculo().toString();
		this.cilindraje = vehiculo.getCilindraje();
		this.fechaIngreso = DateUtils.convertirFechaATexto(vehiculo.getFechaIngreso());
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

	public String getFechaSalida() {
		return fechaSalida;
	}

}
