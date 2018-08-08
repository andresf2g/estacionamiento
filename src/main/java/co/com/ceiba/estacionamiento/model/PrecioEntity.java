package co.com.ceiba.estacionamiento.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity(name = "Precio")
@Table(name = "tbl_precios")
public class PrecioEntity {
	@EmbeddedId
	private IdPrecio idPrecio;

	@Column(name = "valor")
	private BigDecimal valor;

	public IdPrecio getIdPrecio() {
		return idPrecio;
	}

	public BigDecimal getValor() {
		return valor;
	}

}
