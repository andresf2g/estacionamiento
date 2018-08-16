package co.com.ceiba.estacionamiento.business;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import co.com.ceiba.estacionamiento.business.price.PrecioEstacionamiento;
import co.com.ceiba.estacionamiento.business.validation.ValidacionVehiculo;
import co.com.ceiba.estacionamiento.model.VehiculoBuilder;
import co.com.ceiba.estacionamiento.model.VehiculoEntity;
import co.com.ceiba.estacionamiento.repository.VehiculoRepository;

public class VigilanteServiceImpl implements VigilanteService {

	private VehiculoRepository repositorioVehiculo;

	private List<ValidacionVehiculo> validacionesIngreso;
	private List<ValidacionVehiculo> validacionesSalida;
	private PrecioEstacionamiento precioEstacionamiento;

	public VigilanteServiceImpl(VehiculoRepository repositorioVehiculo, List<ValidacionVehiculo> validacionesIngreso,
			List<ValidacionVehiculo> validacionesSalida, PrecioEstacionamiento precioEstacionamiento) {
		this.repositorioVehiculo = repositorioVehiculo;
		this.validacionesIngreso = validacionesIngreso;
		this.validacionesSalida = validacionesSalida;
		this.precioEstacionamiento = precioEstacionamiento;
	}

	public void registrarIngresoVehiculo(Vehiculo vehiculo) {
		// dozer
		// JDBI
		validacionesIngreso.forEach(validador -> validador.validar(vehiculo));
		repositorioVehiculo.save(VehiculoBuilder.convertirAEntity(vehiculo));
	}

	public BigDecimal registrarSalidaVehiculo(Vehiculo vehiculo) {
		validacionesSalida.forEach(validador -> validador.validar(vehiculo));
		Vehiculo vehiculoEstacionado = buscarVehiculoParqueado(vehiculo.getPlaca());
		TiempoEstadia tiempoEstadia = DateUtils.obtenerTiempoEstadia(vehiculoEstacionado.getFechaIngreso(), vehiculo.getFechaSalida());
		BigDecimal valorPagar = precioEstacionamiento.calcularTotal(tiempoEstadia, vehiculo);
		repositorioVehiculo.deleteById(vehiculo.getPlaca());
		return valorPagar;
	}

	public List<Vehiculo> listarVehiculosParqueados(String placa) {
		List<Vehiculo> listadoVehiculos = new ArrayList<>();
		Iterable<VehiculoEntity> listadoEntidades;
		if (placa != null) {
			listadoEntidades = repositorioVehiculo.findByPlaca(placa);
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
