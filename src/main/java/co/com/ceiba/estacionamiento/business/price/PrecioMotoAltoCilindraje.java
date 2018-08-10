package co.com.ceiba.estacionamiento.business.price;

import java.math.BigDecimal;

import co.com.ceiba.estacionamiento.business.TipoPrecio;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;

public class PrecioMotoAltoCilindraje extends Precio {

	public PrecioMotoAltoCilindraje(PrecioRepository repositorioPrecio) {
		super(repositorioPrecio);
	}

	@Override
	public BigDecimal calcularSubtotal(Vehiculo vehiculo) {
		if (vehiculo.getTipoVehiculo().equals(TipoVehiculo.MOTO) && vehiculo.getCilindraje() > 500) {
			return obtenerPrecioEstacionamiento(vehiculo.getTipoVehiculo(), TipoPrecio.ADICIONAL);
		}
		return BigDecimal.ZERO;
	}

}
