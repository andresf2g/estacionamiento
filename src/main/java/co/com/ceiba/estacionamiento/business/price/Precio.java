package co.com.ceiba.estacionamiento.business.price;

import java.math.BigDecimal;
import java.util.Optional;

import co.com.ceiba.estacionamiento.business.TipoPrecio;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;
import co.com.ceiba.estacionamiento.model.IdPrecio;
import co.com.ceiba.estacionamiento.model.PrecioEntity;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;

public abstract class Precio {
	
	private PrecioRepository repositorioPrecio;
	
	public Precio(PrecioRepository repositorioPrecio) {
		this.repositorioPrecio = repositorioPrecio;
	}
	
	protected BigDecimal obtenerPrecioEstacionamiento(TipoVehiculo tipoVehiculo, TipoPrecio tipoPrecio) {
		Optional<PrecioEntity> precio = repositorioPrecio.findById(new IdPrecio(tipoVehiculo, tipoPrecio));
		if (!precio.isPresent()) {
			throw new VigilanteServiceException(VigilanteServiceException.PRECIO_NO_ENCONTRADO);
		}
		return precio.get().getValor();
	}
	
	public abstract BigDecimal calcularSubtotal(Vehiculo vehiculo);
}
