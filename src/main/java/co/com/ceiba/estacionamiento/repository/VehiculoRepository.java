package co.com.ceiba.estacionamiento.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.model.VehiculoEntity;

public interface VehiculoRepository extends CrudRepository<VehiculoEntity, String> {
	public List<VehiculoEntity> findByTipoVehiculo(TipoVehiculo tipoVehiculo);
}
