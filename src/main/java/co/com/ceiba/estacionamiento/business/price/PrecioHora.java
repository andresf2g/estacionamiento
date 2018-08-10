package co.com.ceiba.estacionamiento.business.price;

import java.math.BigDecimal;

import co.com.ceiba.estacionamiento.business.TipoPrecio;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;

public class PrecioHora extends Precio {
	
	private int horas;

	public PrecioHora(PrecioRepository repositorioPrecio, int horas) {
		super(repositorioPrecio);
		this.horas = horas;
	}

	@Override
	public BigDecimal calcularSubtotal(Vehiculo vehiculo) {
		if (horas > 0) {
			return obtenerPrecioEstacionamiento(vehiculo.getTipoVehiculo(), TipoPrecio.HORA).multiply(new BigDecimal(horas));
		}
		return BigDecimal.ZERO;
	}
	
}
