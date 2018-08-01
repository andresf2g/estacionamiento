package co.com.ceiba.estacionamiento.controller;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.Vigilante;
import co.com.ceiba.estacionamiento.service.VigilanteService;

@RestController
public class ParqueaderoController {
	@Autowired
	private VigilanteService servicioVigilante;
	
	@RequestMapping("/parqueadero/listarVehiculosParqueados")
	public List<Vehiculo> listarVehiculosParqueados() {
		return servicioVigilante.listarVehiculosParqueados(null);
	}
	
	@RequestMapping("/parqueadero/registrarIngresoVehiculo")
	public void registrarIngresoVehiculo(@PathVariable String placa, @PathVariable String tipoVehiculo, @PathVariable Integer cilindraje, @PathVariable String fechaIngreso) {
		Date ingreso;
		try {
			ingreso = Vigilante.SDF.parse(fechaIngreso);
		} catch (ParseException e) {
			ingreso = new Date();
		}
		servicioVigilante.registrarIngresoVehiculo(new Vehiculo(placa, cilindraje, TipoVehiculo.valueOf(tipoVehiculo), ingreso));
	}
	
	@RequestMapping("/parqueadero/registrarEgresoVehiculo")
	public BigDecimal registrarEgresoVehiculo(@PathVariable String placa, @PathVariable String fechaEgreso) {
		Vehiculo vehiculo = servicioVigilante.buscarVehiculoParqueado(placa);
		Date egreso;
		try {
			egreso = Vigilante.SDF.parse(fechaEgreso);
		} catch (ParseException e) {
			egreso = new Date();
		}
		if (vehiculo == null) {
			return BigDecimal.ZERO;
		}
		return servicioVigilante.registrarEgresoVehiculo(vehiculo, egreso);
	}
}
