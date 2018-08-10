package co.com.ceiba.estacionamiento.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import co.com.ceiba.estacionamiento.business.price.PrecioEstacionamiento;
import co.com.ceiba.estacionamiento.business.validation.ValidadorEgresoVehiculo;
import co.com.ceiba.estacionamiento.business.validation.ValidadorIngresoVehiculo;
import co.com.ceiba.estacionamiento.model.VehiculoBuilder;
import co.com.ceiba.estacionamiento.model.VehiculoEntity;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;
import co.com.ceiba.estacionamiento.repository.VehiculoRepository;

@Service
public class VigilanteServiceImpl implements VigilanteService {
	@Autowired
	private VehiculoRepository repositorioVehiculo;
	@Autowired
	private PrecioRepository repositorioPrecio;

	public void registrarIngresoVehiculo(Vehiculo vehiculo) {
		new ValidadorIngresoVehiculo(repositorioVehiculo).obtenerValidaciones().forEach(validador -> validador.validar(vehiculo));
		repositorioVehiculo.save(VehiculoBuilder.convertirAEntity(vehiculo));
	}

	public BigDecimal registrarEgresoVehiculo(String placa, String fechaEgreso) {
		Vehiculo vehiculo = buscarVehiculoParqueado(placa);
		new ValidadorEgresoVehiculo().obtenerValidaciones().forEach(validador -> validador.validar(vehiculo));
		TiempoEstadia tiempoEstadia = DateUtils.obtenerTiempoEstadia(vehiculo.getFechaIngreso(), DateUtils.convertirTextoAFecha(fechaEgreso));
		BigDecimal valorPagar = new PrecioEstacionamiento(repositorioPrecio, tiempoEstadia).calcularTotal(vehiculo);
		repositorioVehiculo.deleteById(placa);
		return valorPagar;
	}

	public List<Vehiculo> listarVehiculosParqueados(String tipoVehiculo) {
		List<Vehiculo> listadoVehiculos = new ArrayList<>();
		Iterable<VehiculoEntity> listadoEntidades;
		if (tipoVehiculo != null) {
			listadoEntidades = repositorioVehiculo.findByTipoVehiculo(TipoVehiculo.valueOf(tipoVehiculo));
		} else {
			listadoEntidades = repositorioVehiculo.findAll();
		}
		listadoEntidades.forEach(vehiculoEntity -> listadoVehiculos.add(VehiculoBuilder.convertirADominio(vehiculoEntity)));
		return listadoVehiculos;
	}

	public void evacuarVehiculosParqueados() {
		repositorioVehiculo.deleteAll();
	}

	public Vehiculo buscarVehiculoParqueado(String placa) {
		Optional<VehiculoEntity> vehiculoEntity = repositorioVehiculo.findById(placa);
		if (vehiculoEntity.isPresent()) {
			return VehiculoBuilder.convertirADominio(vehiculoEntity.get());
		}
		return null;
	}

}
