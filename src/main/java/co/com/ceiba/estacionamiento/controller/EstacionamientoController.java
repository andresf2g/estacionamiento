package co.com.ceiba.estacionamiento.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.com.ceiba.estacionamiento.EstacionamientoApplication;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;
import co.com.ceiba.estacionamiento.business.VigilanteService;

@RestController
public class EstacionamientoController {
	private static final Log LOGGER = LogFactory.getLog(EstacionamientoController.class);
	
	@Autowired
	private VigilanteService servicioVigilante;
	
	@RequestMapping("/estacionamiento/listarVehiculosParqueados")
	public ResponseEntity<List<VehiculoRequestBody>> listarVehiculosParqueados(@RequestParam(required=false) String tipoVehiculo) {
		TipoVehiculo tipo = null;
		if (tipoVehiculo != null) {
			try {
				tipo = TipoVehiculo.valueOf(tipoVehiculo);
			} catch (Exception e) {
				LOGGER.error(e);
				return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
			}
		}
		List<VehiculoRequestBody> resultadoVehiculos = new ArrayList<>();
		servicioVigilante.listarVehiculosParqueados(tipo).stream().forEach(v -> resultadoVehiculos.add(new VehiculoRequestBody(v.getPlaca(), v.getTipoVehiculo().toString(), v.getCilindraje(), v.getFechaIngreso())));
		return new ResponseEntity<>(resultadoVehiculos, HttpStatus.OK);
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/estacionamiento/registrarIngresoVehiculo")
	public ResponseEntity<String> registrarIngresoVehiculo(@ModelAttribute VehiculoRequestBody vehiculo) {
		Date ingreso;
		try {
			ingreso = EstacionamientoApplication.formatoFecha().parse(vehiculo.getFechaIngreso());
		} catch (ParseException e) {
			ingreso = new Date();
		}
		try {
			servicioVigilante.registrarIngresoVehiculo(new Vehiculo(vehiculo.getPlaca(), vehiculo.getCilindraje(), TipoVehiculo.valueOf(vehiculo.getTipoVehiculo()), ingreso));
			return new ResponseEntity<>(HttpStatus.OK);
		} catch (VigilanteServiceException e) {
			LOGGER.info(e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
	
	@RequestMapping(method = RequestMethod.POST, value = "/estacionamiento/registrarEgresoVehiculo")
	public ResponseEntity<String> registrarEgresoVehiculo(@ModelAttribute VehiculoRequestBody vehiculoBody) {
		Date egreso;
		try {
			egreso = EstacionamientoApplication.formatoFecha().parse(vehiculoBody.getFechaEgreso());
		} catch (ParseException e) {
			egreso = new Date();
		}
		try {
			return new ResponseEntity<>(servicioVigilante.registrarEgresoVehiculo(vehiculoBody.getPlaca(), egreso).toString(), HttpStatus.OK);
		} catch (VigilanteServiceException e) {
			LOGGER.info(e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
		}
	}
}
