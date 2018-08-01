package co.com.ceiba.estacionamiento.integration;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.com.ceiba.estacionamiento.EstacionamientoApplication;
import co.com.ceiba.estacionamiento.builders.CarroTestDataBuilder;
import co.com.ceiba.estacionamiento.builders.MotoTestDataBuilder;
import co.com.ceiba.estacionamiento.builders.VehiculoTestDataBuilder;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteService;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VigilanteTests {

	@Autowired
	private VigilanteService servicioVigilante;
	
	private Date parsearFecha(String fecha) throws ParseException {
		return EstacionamientoApplication.formatoFecha().parse(fecha);
	}
	
	private void insertarNVehiculos(VehiculoTestDataBuilder vehiculoBuilder, int n) {
		for (int i = 0; i < n; i++) {
			vehiculoBuilder.conPlaca(vehiculoBuilder.getPrefijoPlaca() + "45" + i);
			servicioVigilante.registrarIngresoVehiculo(vehiculoBuilder.build());
		}
	}

	@Test
	public void registrarIngresoMotoConParqueaderoDisponibleTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo moto = new MotoTestDataBuilder().build();
		
		servicioVigilante.registrarIngresoVehiculo(moto);
		
		Assert.assertNotNull(servicioVigilante.buscarVehiculoParqueado(moto.getPlaca()));
	}

	@Test
	public void registrarIngresoMotoConParqueaderoLlenoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder motoBuilder = new MotoTestDataBuilder();
		insertarNVehiculos(motoBuilder, 10);

		try {
			motoBuilder.conPlaca("ZTE56D");
			servicioVigilante.registrarIngresoVehiculo(motoBuilder.build());
			Assert.fail();
		} catch (VigilanteServiceException e) {
			Assert.assertEquals(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO, e.getMessage());
		}
	}

	@Test
	public void registrarIngresoCarroConParqueaderoDisponibleTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo carro = new CarroTestDataBuilder().build();
		
		servicioVigilante.registrarIngresoVehiculo(carro);
		
		Assert.assertNotNull(servicioVigilante.buscarVehiculoParqueado(carro.getPlaca()));
	}

	@Test
	public void registrarIngresoCarroConParqueaderoLlenoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carroBuilder = new CarroTestDataBuilder();
		insertarNVehiculos(carroBuilder, 20);

		try {
			carroBuilder.conPlaca("ZTE568");
			servicioVigilante.registrarIngresoVehiculo(carroBuilder.build());
			Assert.fail();
		} catch (VigilanteServiceException e) {
			Assert.assertEquals(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO, e.getMessage());
		}
	}

	@Test
	public void registrarIngresoVehiculoPlacaADiaCorrectoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo carro = new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-01 09:30").build();
		
		servicioVigilante.registrarIngresoVehiculo(carro);
		
		Assert.assertNotNull(servicioVigilante.buscarVehiculoParqueado(carro.getPlaca()));
	}

	@Test
	public void registrarIngresoVehiculoPlacaADiaIncorrectoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo carro = new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-03 09:30").build();
		
		try {
			servicioVigilante.registrarIngresoVehiculo(carro);
			Assert.fail();
		} catch (VigilanteServiceException e) {
			Assert.assertEquals(VigilanteServiceException.PLACA_A_DIA_INCORRECTO, e.getMessage());
		}
	}

	@Test
	public void registrarIngresoVehiculoIngresadoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo carro = new CarroTestDataBuilder().build();
		servicioVigilante.registrarIngresoVehiculo(carro);
		
		try {
			servicioVigilante.registrarIngresoVehiculo(carro);
			Assert.fail();
		} catch (VigilanteServiceException e) {
			Assert.assertEquals(VigilanteServiceException.VEHICULO_YA_INGRESADO, e.getMessage());
		}
	}

	@Test
	public void registrarEgresoVehiculoIngresadoTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo carro = new CarroTestDataBuilder().build();
		servicioVigilante.registrarIngresoVehiculo(carro);
		Assert.assertNotNull(servicioVigilante.buscarVehiculoParqueado(carro.getPlaca()));
		
		servicioVigilante.registrarEgresoVehiculo(carro.getPlaca(), parsearFecha("2018-05-06 14:40"));

		Assert.assertNull(servicioVigilante.buscarVehiculoParqueado(carro.getPlaca()));
	}

	@Test
	public void registrarEgresoVehiculoNoIngresadoTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo carro = new CarroTestDataBuilder().build();
		
		try {
			servicioVigilante.registrarEgresoVehiculo(carro.getPlaca(), parsearFecha("2018-05-06 14:40"));
			Assert.fail();
		} catch (VigilanteServiceException e) {
			Assert.assertEquals(VigilanteServiceException.VEHICULO_NO_INGRESADO, e.getMessage());
		}
	}

	@Test
	public void registrarEgresoCarroCobrandoDiaTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo carro = new CarroTestDataBuilder().build();
		servicioVigilante.registrarIngresoVehiculo(carro);
		
		BigDecimal valorPagar = servicioVigilante.registrarEgresoVehiculo(carro.getPlaca(), parsearFecha("2018-07-10 19:35"));
		
		Assert.assertEquals(8000, valorPagar.doubleValue(), 0);
	}

	@Test
	public void registrarEgresoCarroCobrandoHorasTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo carro = new CarroTestDataBuilder().build();
		servicioVigilante.registrarIngresoVehiculo(carro);
		
		BigDecimal valorPagar = servicioVigilante.registrarEgresoVehiculo(carro.getPlaca(), parsearFecha("2018-07-10 13:48"));
		
		Assert.assertEquals(4000, valorPagar.doubleValue(), 0);
	}

	@Test
	public void registrarEgresoCarroCobrandoDiasHorasTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo carro = new CarroTestDataBuilder().build();
		servicioVigilante.registrarIngresoVehiculo(carro);
		
		BigDecimal valorPagar = servicioVigilante.registrarEgresoVehiculo(carro.getPlaca(), parsearFecha("2018-07-11 12:50"));
		
		Assert.assertEquals(11000, valorPagar.doubleValue(), 0);
	}

	@Test
	public void registrarEgresoMotoCilindrajeInferiorCobrandoDiaTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo moto = new MotoTestDataBuilder().build();
		servicioVigilante.registrarIngresoVehiculo(moto);
		
		BigDecimal valorPagar = servicioVigilante.registrarEgresoVehiculo(moto.getPlaca(), parsearFecha("2018-07-10 19:35"));
		
		Assert.assertEquals(4000, valorPagar.doubleValue(), 0);
	}

	@Test
	public void registrarEgresoMotoCilindrajeInferiorCobrandoHorasTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo moto = new MotoTestDataBuilder().build();
		servicioVigilante.registrarIngresoVehiculo(moto);
		
		BigDecimal valorPagar = servicioVigilante.registrarEgresoVehiculo(moto.getPlaca(), parsearFecha("2018-07-10 13:48"));
		
		Assert.assertEquals(2000, valorPagar.doubleValue(), 0);
	}

	@Test
	public void registrarEgresoMotoCilindrajeInferiorCobrandoDiasHorasTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo moto = new MotoTestDataBuilder().build();
		servicioVigilante.registrarIngresoVehiculo(moto);
		
		BigDecimal valorPagar = servicioVigilante.registrarEgresoVehiculo(moto.getPlaca(), parsearFecha("2018-07-11 13:50"));
		
		Assert.assertEquals(6000, valorPagar.doubleValue(), 0);
	}

	@Test
	public void registrarEgresoMotoCilindrajeSuperiorCobrandoDiaTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo moto = new MotoTestDataBuilder().conCilindraje(650).build();
		servicioVigilante.registrarIngresoVehiculo(moto);
		
		BigDecimal valorPagar = servicioVigilante.registrarEgresoVehiculo(moto.getPlaca(), parsearFecha("2018-07-10 19:35"));
		
		Assert.assertEquals(6000, valorPagar.doubleValue(), 0);
	}

	@Test
	public void registrarEgresoMotoCilindrajeSuperiorCobrandoHorasTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo moto = new MotoTestDataBuilder().conCilindraje(650).build();
		servicioVigilante.registrarIngresoVehiculo(moto);
		
		BigDecimal valorPagar = servicioVigilante.registrarEgresoVehiculo(moto.getPlaca(), parsearFecha("2018-07-10 13:48"));
		
		Assert.assertEquals(4000, valorPagar.doubleValue(), 0);
	}

	@Test
	public void registrarEgresoMotoCilindrajeSuperiorCobrandoDiasHorasTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		Vehiculo moto = new MotoTestDataBuilder().conCilindraje(650).build();
		servicioVigilante.registrarIngresoVehiculo(moto);
		
		BigDecimal valorPagar = servicioVigilante.registrarEgresoVehiculo(moto.getPlaca(), parsearFecha("2018-07-11 13:50"));
		
		Assert.assertEquals(8000, valorPagar.doubleValue(), 0);
	}
	
	@Test
	public void listarVehiculosParqueadosTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carroBuilder = new CarroTestDataBuilder();
		insertarNVehiculos(carroBuilder, 6);
		carroBuilder = new MotoTestDataBuilder();
		insertarNVehiculos(carroBuilder, 3);
		
		Assert.assertEquals(9, servicioVigilante.listarVehiculosParqueados(null).size());
		Assert.assertEquals(6, servicioVigilante.listarVehiculosParqueados(TipoVehiculo.CARRO).size());
		Assert.assertEquals(3, servicioVigilante.listarVehiculosParqueados(TipoVehiculo.MOTO).size());
	}
}
