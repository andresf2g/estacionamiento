package co.com.ceiba.estacionamiento.controller;

import java.util.ArrayList;
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

import co.com.ceiba.estacionamiento.business.VigilanteService;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;

@RestController
public class EstacionamientoController {
	private static final Log LOGGER = LogFactory.getLog(EstacionamientoController.class);
	
	@Autowired
	private VigilanteService servicioVigilante;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@GetMapping("/estacionamiento/listarVehiculosParqueados")
	public ResponseEntity<List<VehiculoRequestBody>> listarVehiculosParqueados(@RequestParam(required=false) String tipoVehiculo) {
		List<VehiculoRequestBody> resultadoVehiculos = new ArrayList<>();
		servicioVigilante.listarVehiculosParqueados(tipoVehiculo).stream().forEach(vehiculo -> resultadoVehiculos.add(new VehiculoRequestBody(vehiculo)));
		return new ResponseEntity<>(resultadoVehiculos, HttpStatus.OK);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/estacionamiento/registrarIngresoVehiculo")
	public ResponseEntity<List<String>> registrarIngresoVehiculo(@RequestBody VehiculoRequestBody vehiculoBody) {
		try {
			servicioVigilante.registrarIngresoVehiculo(vehiculoBody.obtenerVehiculo());
			return new ResponseEntity<>(Arrays.asList("El vehiculo ha sido ingresado"), HttpStatus.OK);
		} catch (VigilanteServiceException e) {
			LOGGER.info(e);
			return new ResponseEntity<>(Arrays.asList(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@PostMapping(value = "/estacionamiento/registrarEgresoVehiculo")
	public ResponseEntity<List<String>> registrarEgresoVehiculo(@RequestBody VehiculoRequestBody vehiculoBody) {
		try {
			return new ResponseEntity<>(Arrays.asList(servicioVigilante.registrarEgresoVehiculo(vehiculoBody.getPlaca(), vehiculoBody.getFechaEgreso()).toString()), HttpStatus.OK);
		} catch (VigilanteServiceException e) {
			LOGGER.info(e);
			return new ResponseEntity<>(Arrays.asList(e.getMessage()), HttpStatus.BAD_REQUEST);
		}
	}
}
