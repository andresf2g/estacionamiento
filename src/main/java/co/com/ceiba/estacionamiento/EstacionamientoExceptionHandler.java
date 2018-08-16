package co.com.ceiba.estacionamiento;

import java.util.Arrays;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import co.com.ceiba.estacionamiento.business.VigilanteServiceException;

@ControllerAdvice
public class EstacionamientoExceptionHandler {
	@ExceptionHandler(VigilanteServiceException.class)
	public final ResponseEntity<List<String>> manejarExcepcionVigilante(VigilanteServiceException e) {
		return new ResponseEntity<>(Arrays.asList(e.getMessage()), HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(Exception.class)
	public final ResponseEntity<List<String>> manejarExcepcionGeneral(Exception e) {
		return new ResponseEntity<>(Arrays.asList(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
