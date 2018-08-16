package co.com.ceiba.estacionamiento;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import co.com.ceiba.estacionamiento.business.VigilanteService;
import co.com.ceiba.estacionamiento.business.VigilanteServiceImpl;
import co.com.ceiba.estacionamiento.business.price.PrecioEstacionamiento;
import co.com.ceiba.estacionamiento.business.validation.DiaCorrectoParaVehiculoPlacaA;
import co.com.ceiba.estacionamiento.business.validation.DisponibilidadEstacionamiento;
import co.com.ceiba.estacionamiento.business.validation.FechaIngresoNoNula;
import co.com.ceiba.estacionamiento.business.validation.FechaSalidaNoNula;
import co.com.ceiba.estacionamiento.business.validation.ValidacionVehiculo;
import co.com.ceiba.estacionamiento.business.validation.VehiculoEstacionado;
import co.com.ceiba.estacionamiento.business.validation.VehiculoNoEstacionado;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;
import co.com.ceiba.estacionamiento.repository.VehiculoRepository;

@Configuration
public class EstacionamientoConfiguration {

	@Bean
	public VigilanteService crearVigilante(VehiculoRepository repositorioVehiculo, PrecioRepository repositorioPrecio) {
		List<ValidacionVehiculo> validacionesIngreso = Arrays.asList(new FechaIngresoNoNula(),
				new DiaCorrectoParaVehiculoPlacaA(), new DisponibilidadEstacionamiento(repositorioVehiculo),
				new VehiculoNoEstacionado(repositorioVehiculo));

		List<ValidacionVehiculo> validacionesSalida = Arrays.asList(new VehiculoEstacionado(repositorioVehiculo),
				new FechaSalidaNoNula());
		
		PrecioEstacionamiento precioEstacionamiento = new PrecioEstacionamiento(repositorioPrecio);

		return new VigilanteServiceImpl(repositorioVehiculo, validacionesIngreso, validacionesSalida, precioEstacionamiento);
	}

}
