package co.com.ceiba.estacionamiento.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import co.com.ceiba.estacionamiento.business.TipoPrecio;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;

@Embeddable
public class IdPrecio implements Serializable {
	private static final long serialVersionUID = 327773458203351070L;

	@Column(name = "tipo_vehiculo")
	@Enumerated(EnumType.STRING)
	private TipoVehiculo tipoVehiculo;

	@Column(name = "tipo_precio")
	@Enumerated(EnumType.STRING)
	private TipoPrecio tipoPrecio;

	public IdPrecio(TipoVehiculo tipoVehiculo, TipoPrecio tipoPrecio) {
		super();
		this.tipoVehiculo = tipoVehiculo;
		this.tipoPrecio = tipoPrecio;
	}

	public TipoVehiculo getTipoVehiculo() {
		return tipoVehiculo;
	}

	public void setTipoVehiculo(TipoVehiculo tipoVehiculo) {
		this.tipoVehiculo = tipoVehiculo;
	}

	public TipoPrecio getTipoPrecio() {
		return tipoPrecio;
	}

	public void setTipoPrecio(TipoPrecio tipoPrecio) {
		this.tipoPrecio = tipoPrecio;
	}

}
