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

	public PrecioEntity() {
	}

	public PrecioEntity(IdPrecio idPrecio, BigDecimal valor) {
		super();
		this.idPrecio = idPrecio;
		this.valor = valor;
	}

	public IdPrecio getIdPrecio() {
		return idPrecio;
	}

	public void setIdPrecio(IdPrecio idPrecio) {
		this.idPrecio = idPrecio;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}

}
