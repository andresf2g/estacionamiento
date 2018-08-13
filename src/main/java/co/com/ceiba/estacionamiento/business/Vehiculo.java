package co.com.ceiba.estacionamiento.business;

import java.util.Date;

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
