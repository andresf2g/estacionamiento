package co.com.ceiba.estacionamiento.business.price;

import java.math.BigDecimal;

import co.com.ceiba.estacionamiento.business.TipoPrecio;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;

public class PrecioDia extends Precio {

	private int dias;

	public PrecioDia(PrecioRepository repositorioPrecio, int dias) {
		super(repositorioPrecio);
		this.dias = dias;
	}

	@Override
	public BigDecimal calcularSubtotal(Vehiculo vehiculo) {
		if (dias > 0) {
			return obtenerPrecioEstacionamiento(vehiculo.getTipoVehiculo(), TipoPrecio.DIA).multiply(new BigDecimal(dias));
		}
		return BigDecimal.ZERO;
	}

}
