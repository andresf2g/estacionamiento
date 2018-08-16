package co.com.ceiba.estacionamiento.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteService;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class EstacionamientoController {
	@Autowired
	private VigilanteService servicioVigilante;

	@GetMapping("/estacionamiento/listarVehiculosParqueados")
	public ResponseEntity<List<Vehiculo>> listarVehiculosParqueados(@RequestParam(required = false) String placa) {
		return new ResponseEntity<>(servicioVigilante.listarVehiculosParqueados(placa), HttpStatus.OK);
	}

	@PostMapping(value = "/estacionamiento/registrarIngresoVehiculo")
	public ResponseEntity<List<String>> registrarIngresoVehiculo(@RequestBody Vehiculo vehiculoBody) {
		servicioVigilante.registrarIngresoVehiculo(vehiculoBody);
		return new ResponseEntity<>(Arrays.asList("El vehiculo ha sido ingresado"), HttpStatus.OK);
	}

	@PostMapping(value = "/estacionamiento/registrarSalidaVehiculo")
	public ResponseEntity<List<String>> registrarSalidaVehiculo(@RequestBody Vehiculo vehiculoBody) {
		return new ResponseEntity<>(Arrays.asList(servicioVigilante.registrarSalidaVehiculo(vehiculoBody).toString()), HttpStatus.OK);
	}
}
