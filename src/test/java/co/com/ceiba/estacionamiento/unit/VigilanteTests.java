package co.com.ceiba.estacionamiento.unit;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

import co.com.ceiba.estacionamiento.business.Vigilante;

public class VigilanteTests {
	private final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");

	private Date parsearFecha(String fecha) throws ParseException {
		return SDF.parse(fecha);
	}
	
	@Test
	public void calcularTiempoHorasTest() throws ParseException {
		Vigilante vigilante = new Vigilante();
		int[] tiempoDiferencia = vigilante.calcularTiempoDiferencia(parsearFecha("2018-05-06 12:00"), parsearFecha("2018-05-06 15:15"));
		Assert.assertArrayEquals(new int[] {0, 4}, tiempoDiferencia);
	}
	
	@Test
	public void calcularTiempoDiaTest() throws ParseException {
		Vigilante vigilante = new Vigilante();
		int[] tiempoDiferencia = vigilante.calcularTiempoDiferencia(parsearFecha("2018-05-06 10:00"), parsearFecha("2018-05-06 19:15"));
		Assert.assertArrayEquals(new int[] {1, 0}, tiempoDiferencia);
	}
	
	@Test
	public void calcularTiempoDiasHorasTest() throws ParseException {
		Vigilante vigilante = new Vigilante();
		int[] tiempoDiferencia = vigilante.calcularTiempoDiferencia(parsearFecha("2018-05-06 12:00"), parsearFecha("2018-05-07 15:49"));
		Assert.assertArrayEquals(new int[] {1, 4}, tiempoDiferencia);
	}
	
	@Test
	public void calcularTiempoDiasConDiaAdicionalTest() throws ParseException {
		Vigilante vigilante = new Vigilante();
		int[] tiempoDiferencia = vigilante.calcularTiempoDiferencia(parsearFecha("2018-05-06 10:00"), parsearFecha("2018-05-07 19:49"));
		Assert.assertArrayEquals(new int[] {2, 0}, tiempoDiferencia);
	}
	
	@Test
	public void validarVehiculoPlacaADiaCorrectoTest() throws ParseException {
		Vigilante vigilante = new Vigilante();
		Assert.assertTrue(vigilante.vehiculoPlacaDiaCorrecto("AXT995", parsearFecha("2018-07-30 15:40")));
	}
	
	@Test
	public void validarVehiculoPlacaADiaIncorrectoTest() throws ParseException {
		Vigilante vigilante = new Vigilante();
		Assert.assertFalse(vigilante.vehiculoPlacaDiaCorrecto("AXT995", parsearFecha("2018-07-31 15:40")));
	}
	
	@Test
	public void validarVehiculoPlacaDiferenteATest() throws ParseException {
		Vigilante vigilante = new Vigilante();
		Assert.assertTrue(vigilante.vehiculoPlacaDiaCorrecto("DXT995", parsearFecha("2018-07-31 15:40")));
	}

}
