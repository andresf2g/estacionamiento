package co.com.ceiba.estacionamiento.controller;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;

@RestController
public class EstacionamientoController {
	private static final Log LOGGER = LogFactory.getLog(EstacionamientoController.class);
	
	@Autowired
	private VigilanteService servicioVigilante;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/estacionamiento/listarVehiculosParqueados")
	public ResponseEntity<List<Vehiculo>> listarVehiculosParqueados(@RequestParam(required=false) String placa) {
		return new ResponseEntity<>(servicioVigilante.listarVehiculosParqueados(placa), HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/estacionamiento/registrarIngresoVehiculo")
	public ResponseEntity<List<String>> registrarIngresoVehiculo(@RequestBody Vehiculo vehiculoBody) {
		try {
			servicioVigilante.registrarIngresoVehiculo(vehiculoBody);
			return new ResponseEntity<>(Arrays.asList("El vehiculo ha sido ingresado"), HttpStatus.OK);
		} catch (VigilanteServiceException e) {
			LOGGER.info(e);
			return new ResponseEntity<>(Arrays.asList(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/estacionamiento/registrarSalidaVehiculo")
	public ResponseEntity<List<String>> registrarSalidaVehiculo(@RequestBody Vehiculo vehiculoBody) {
		try {
			return new ResponseEntity<>(Arrays.asList(servicioVigilante.registrarSalidaVehiculo(vehiculoBody.getPlaca(), vehiculoBody.getFechaSalida()).toString()), HttpStatus.OK);
		} catch (VigilanteServiceException e) {
			LOGGER.info(e);
			return new ResponseEntity<>(Arrays.asList(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
}
