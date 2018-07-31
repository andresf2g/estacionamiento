package co.com.ceiba.estacionamiento.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.model.IdPrecio;
import co.com.ceiba.estacionamiento.model.PrecioEntity;

public interface PrecioRepository extends CrudRepository<PrecioEntity, IdPrecio> {
	List<PrecioEntity> findByIdPrecioTipoVehiculo(TipoVehiculo tipoVehiculo);
}