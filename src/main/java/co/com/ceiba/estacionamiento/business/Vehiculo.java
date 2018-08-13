package co.com.ceiba.estacionamiento.business;

import java.util.Date;

public class Vehiculo {
	private String placa;
	private TipoVehiculo tipoVehiculo;
	private Integer cilindraje;
	private Date fechaIngreso;
	private Date fechaSalida;

	public Date getFechaSalida() {
		return fechaSalida;
	}

	public Vehiculo(String placa, TipoVehiculo tipoVehiculo, Integer cilindraje, Date fechaIngreso, Date fechaSalida) {
		super();
		this.placa = placa;
		this.tipoVehiculo = tipoVehiculo;
		this.cilindraje = cilindraje;
		this.fechaIngreso = fechaIngreso;
		this.fechaSalida = fechaSalida;
	}

	public Vehiculo() {
	}

	public Vehiculo(String placa, TipoVehiculo tipoVehiculo, Integer cilindraje, Date fechaIngreso) {
		super();
		this.placa = placa;
		this.tipoVehiculo = tipoVehiculo;
		this.cilindraje = cilindraje;
		this.fechaIngreso = fechaIngreso;
	}

	public String getPlaca() {
		return placa;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public Integer getCilindraje() {
		return cilindraje;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

}
