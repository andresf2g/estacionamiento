package co.com.ceiba.estacionamiento.repository;

import org.springframework.data.repository.CrudRepository;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.model.VehiculoEntity;

public interface VehiculoRepository extends CrudRepository<VehiculoEntity, String> {
	public Iterable<VehiculoEntity> findByTipoVehiculo(TipoVehiculo tipoVehiculo);
	public int countByTipoVehiculo(TipoVehiculo tipoVehiculo);
}
