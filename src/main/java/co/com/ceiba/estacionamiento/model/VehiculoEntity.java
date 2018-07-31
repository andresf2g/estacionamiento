package co.com.ceiba.estacionamiento.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;

@Entity(name = "Vehiculo")
@Table(name = "tbl_vehiculos")
public class VehiculoEntity {
	@Id
	@Column(name = "placa")
	private String placa;

	@Column(name = "cilindraje")
	private Integer cilindraje;

	@Column(name = "tipo_vehiculo")
	@Enumerated(EnumType.STRING)
	private TipoVehiculo tipoVehiculo;

	@Column(name = "fecha_ingreso")
	private Date fechaIngreso;
	
	public VehiculoEntity() {
	}

	public VehiculoEntity(String placa, Integer cilindraje, TipoVehiculo tipoVehiculo, Date fechaIngreso) {
		super();
		this.placa = placa;
		this.cilindraje = cilindraje;
		this.tipoVehiculo = tipoVehiculo;
		this.fechaIngreso = fechaIngreso;
	}

	public String getPlaca() {
		return placa;
	}

	public void setPlaca(String placa) {
		this.placa = placa;
	}

	public Integer getCilindraje() {
		return cilindraje;
	}

	public void setCilindraje(Integer cilindraje) {
		this.cilindraje = cilindraje;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

}
