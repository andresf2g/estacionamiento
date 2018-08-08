package co.com.ceiba.estacionamiento.business;

import java.util.Date;

import co.com.ceiba.estacionamiento.EstacionamientoApplication;

public class Vehiculo {
	private String placa;
	private Integer cilindraje;
	private TipoVehiculo tipoVehiculo;
	private Date fechaIngreso;

	public Vehiculo(String placa, Integer cilindraje, TipoVehiculo tipoVehiculo, Date fechaIngreso) {
		super();
		this.placa = placa;
		this.cilindraje = cilindraje;
		this.tipoVehiculo = tipoVehiculo;
		this.fechaIngreso = fechaIngreso;
	}
	
	public Vehiculo(String placa, Integer cilindraje, TipoVehiculo tipoVehiculo, String fechaIngreso) {
		super();
		this.placa = placa;
		this.cilindraje = cilindraje;
		this.tipoVehiculo = tipoVehiculo;
		try {
			this.fechaIngreso = EstacionamientoApplication.formatoFecha().parse(fechaIngreso);
		} catch (Exception e) {
			this.fechaIngreso = new Date();
		}
	}

	public String getPlaca() {
		return placa;
	}

	public Integer getCilindraje() {
		return cilindraje;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

}
