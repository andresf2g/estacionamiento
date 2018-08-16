package co.com.ceiba.estacionamiento.business.price;

import java.math.BigDecimal;

import co.com.ceiba.estacionamiento.business.TiempoEstadia;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;

public class PrecioEstacionamiento {
	private PrecioRepository repositorioPrecio;

	public PrecioEstacionamiento(PrecioRepository repositorioPrecio) {
		this.repositorioPrecio = repositorioPrecio;
	}

	public BigDecimal calcularTotal(TiempoEstadia tiempoEstadia, Vehiculo vehiculo) {
		BigDecimal valorPagar = BigDecimal.ZERO;
		valorPagar = valorPagar.add(new PrecioDia(repositorioPrecio, tiempoEstadia.getDias()).calcularSubtotal(vehiculo));
		valorPagar = valorPagar.add(new PrecioHora(repositorioPrecio, tiempoEstadia.getHoras()).calcularSubtotal(vehiculo));
		valorPagar = valorPagar.add(new PrecioMotoAltoCilindraje(repositorioPrecio).calcularSubtotal(vehiculo));
		return valorPagar;
	}
}
