package co.com.ceiba.estacionamiento.unit;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;

import org.junit.Test;

import co.com.ceiba.estacionamiento.EstacionamientoApplication;
import co.com.ceiba.estacionamiento.business.VigilanteServiceImpl;

public class VigilanteTests {
	
	private Date parsearFecha(String fecha) throws ParseException {
		return EstacionamientoApplication.formatoFecha().parse(fecha);
	}
	
	@Test
	public void calcularTiempoHorasTest() throws ParseException {
		int[] tiempoDiferencia = new VigilanteServiceImpl().calcularTiempoDiferencia(parsearFecha("2018-05-06 12:00"), parsearFecha("2018-05-06 15:15"));
		assertArrayEquals(new int[] {0, 4}, tiempoDiferencia);
	}
	
	@Test
	public void calcularTiempoDiaTest() throws ParseException {
		int[] tiempoDiferencia = new VigilanteServiceImpl().calcularTiempoDiferencia(parsearFecha("2018-05-06 10:00"), parsearFecha("2018-05-06 19:15"));
		assertArrayEquals(new int[] {1, 0}, tiempoDiferencia);
	}
	
	@Test
	public void calcularTiempoDiasHorasTest() throws ParseException {
		int[] tiempoDiferencia = new VigilanteServiceImpl().calcularTiempoDiferencia(parsearFecha("2018-05-06 12:00"), parsearFecha("2018-05-07 15:49"));
		assertArrayEquals(new int[] {1, 4}, tiempoDiferencia);
	}
	
	@Test
	public void calcularTiempoDiasConDiaAdicionalTest() throws ParseException {
		int[] tiempoDiferencia = new VigilanteServiceImpl().calcularTiempoDiferencia(parsearFecha("2018-05-06 10:00"), parsearFecha("2018-05-07 19:49"));
		assertArrayEquals(new int[] {2, 0}, tiempoDiferencia);
	}
	
	@Test
	public void validarVehiculoPlacaADiaCorrectoTest() throws ParseException {
		assertTrue(new VigilanteServiceImpl().vehiculoPlacaDiaCorrecto("AXT995", parsearFecha("2018-07-30 15:40")));
	}
	
	@Test
	public void validarVehiculoPlacaADiaIncorrectoTest() throws ParseException {
		assertFalse(new VigilanteServiceImpl().vehiculoPlacaDiaCorrecto("AXT995", parsearFecha("2018-07-31 15:40")));
	}
	
	@Test
	public void validarVehiculoPlacaDiferenteATest() throws ParseException {
		assertTrue(new VigilanteServiceImpl().vehiculoPlacaDiaCorrecto("DXT995", parsearFecha("2018-07-31 15:40")));
	}

}
