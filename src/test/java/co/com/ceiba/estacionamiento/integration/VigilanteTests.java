package co.com.ceiba.estacionamiento.integration;

import java.text.ParseException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import co.com.ceiba.estacionamiento.builders.CarroTestDataBuilder;
import co.com.ceiba.estacionamiento.builders.MotoTestDataBuilder;
import co.com.ceiba.estacionamiento.builders.VehiculoTestDataBuilder;
import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteService;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;
import co.com.ceiba.estacionamiento.controller.EstacionamientoController;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VigilanteTests {

	@Autowired
    private EstacionamientoController controladorEstacionamiento;
	
	@Autowired
	private VigilanteService servicioVigilante;
	
	private void insertarNVehiculos(VehiculoTestDataBuilder vehiculoBuilder, int n) {
		for (int i = 0; i < n; i++) {
			vehiculoBuilder.conPlaca(vehiculoBuilder.getPrefijoPlaca() + "45" + i);
			servicioVigilante.registrarIngresoVehiculo(vehiculoBuilder.build());
		}
	}

	@Test
	public void registrarIngresoMotoConParqueaderoDisponibleTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder moto = new MotoTestDataBuilder();
		
		controladorEstacionamiento.registrarIngresoVehiculo(moto.buildBody());
		
		Assert.assertNotNull(servicioVigilante.buscarVehiculoParqueado(moto.build().getPlaca()));
	}
	
	@Test
	public void registrarIngresoCarroConParqueaderoDisponibleTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		
		controladorEstacionamiento.registrarIngresoVehiculo(carro.buildBody());
		
		Assert.assertNotNull(servicioVigilante.buscarVehiculoParqueado(carro.build().getPlaca()));
	}
	
	@Test
	public void registrarIngresoMotoConParqueaderoLlenoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder motoBuilder = new MotoTestDataBuilder();
		insertarNVehiculos(motoBuilder, 10);
		motoBuilder.conPlaca("ZTE56D");

		ResponseEntity<String> ingreso = controladorEstacionamiento.registrarIngresoVehiculo(motoBuilder.buildBody());
		
		Assert.assertEquals(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO, ingreso.getBody());
	}

	@Test
	public void registrarIngresoCarroConParqueaderoLlenoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carroBuilder = new CarroTestDataBuilder();
		insertarNVehiculos(carroBuilder, 20);
		carroBuilder.conPlaca("ZTE568");

		ResponseEntity<String> ingreso = controladorEstacionamiento.registrarIngresoVehiculo(carroBuilder.buildBody());
		
		Assert.assertEquals(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO, ingreso.getBody());
	}

	@Test
	public void registrarIngresoVehiculoPlacaADiaCorrectoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-01 09:30");
		
		controladorEstacionamiento.registrarIngresoVehiculo(carro.buildBody());
		
		Assert.assertNotNull(servicioVigilante.buscarVehiculoParqueado(carro.build().getPlaca()));
	}

	@Test
	public void registrarIngresoVehiculoPlacaADiaIncorrectoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-03 09:30");
		
		ResponseEntity<String> ingreso = controladorEstacionamiento.registrarIngresoVehiculo(carro.buildBody());
		
		Assert.assertEquals(VigilanteServiceException.PLACA_A_DIA_INCORRECTO, ingreso.getBody());
	}
	
	@Test
	public void registrarIngresoVehiculoIngresadoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(carro.build());
		
		ResponseEntity<String> ingreso = controladorEstacionamiento.registrarIngresoVehiculo(carro.buildBody());
		
		Assert.assertEquals(VigilanteServiceException.VEHICULO_YA_INGRESADO, ingreso.getBody());
	}

	@Test
	public void registrarEgresoVehiculoIngresadoTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(carro.build());
		carro.conFechaEgreso("2018-05-06 14:40");
		
		controladorEstacionamiento.registrarEgresoVehiculo(carro.buildBody());

		Assert.assertNull(servicioVigilante.buscarVehiculoParqueado(carro.build().getPlaca()));
	}

	@Test
	public void registrarEgresoVehiculoNoIngresadoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder().conFechaEgreso("2018-05-06 14:40");
		
		ResponseEntity<String> egreso = controladorEstacionamiento.registrarEgresoVehiculo(carro.buildBody());
		
		Assert.assertEquals(VigilanteServiceException.VEHICULO_NO_INGRESADO, egreso.getBody());
	}
	
	private void registrarEgresoVehiculoGenerico(VehiculoTestDataBuilder vehiculoBuilder, String fechaEgreso, boolean cilindrajeSuperior, String valorEsperado) {
		servicioVigilante.evacuarVehiculosParqueados();
		if (cilindrajeSuperior) {
			vehiculoBuilder.conCilindraje(650);
		}
		servicioVigilante.registrarIngresoVehiculo(vehiculoBuilder.build());
		vehiculoBuilder.conFechaEgreso(fechaEgreso);
		
		ResponseEntity<String> egreso = controladorEstacionamiento.registrarEgresoVehiculo(vehiculoBuilder.buildBody());
		
		Assert.assertEquals(valorEsperado, egreso.getBody());
	}
	
	private void registrarEgresoVehiculoGenerico(VehiculoTestDataBuilder vehiculoBuilder, String fechaEgreso, String valorEsperado) {
		registrarEgresoVehiculoGenerico(vehiculoBuilder, fechaEgreso, false, valorEsperado);
	}
	
	@Test
	public void registrarEgresoCarroCobrandoDiaTest() {
		registrarEgresoVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-10 19:35", "8000.00");
	}

	@Test
	public void registrarEgresoCarroCobrandoHorasTest() {
		registrarEgresoVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-10 13:48", "4000.00");
	}

	@Test
	public void registrarEgresoCarroCobrandoDiasHorasTest() {
		registrarEgresoVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-11 12:50", "11000.00");
	}

	@Test
	public void registrarEgresoMotoCilindrajeInferiorCobrandoDiaTest() {
		registrarEgresoVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 19:35", "4000.00");
	}

	@Test
	public void registrarEgresoMotoCilindrajeInferiorCobrandoHorasTest() {
		registrarEgresoVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 13:48", "2000.00");
	}

	@Test
	public void registrarEgresoMotoCilindrajeInferiorCobrandoDiasHorasTest() {
		registrarEgresoVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-11 13:50", "6000.00");
	}

	@Test
	public void registrarEgresoMotoCilindrajeSuperiorCobrandoDiaTest() {
		registrarEgresoVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 19:35", true, "6000.00");
	}

	@Test
	public void registrarEgresoMotoCilindrajeSuperiorCobrandoHorasTest() {
		registrarEgresoVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 13:48", true, "4000.00");
	}

	@Test
	public void registrarEgresoMotoCilindrajeSuperiorCobrandoDiasHorasTest() {
		registrarEgresoVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-11 13:50", true, "8000.00");
	}
	
	@Test
	public void listarVehiculosParqueadosTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carroBuilder = new CarroTestDataBuilder();
		insertarNVehiculos(carroBuilder, 6);
		carroBuilder = new MotoTestDataBuilder();
		insertarNVehiculos(carroBuilder, 3);
		
		Assert.assertEquals(9, controladorEstacionamiento.listarVehiculosParqueados(null).getBody().size());
		Assert.assertEquals(6, controladorEstacionamiento.listarVehiculosParqueados(TipoVehiculo.CARRO.toString()).getBody().size());
		Assert.assertEquals(3, controladorEstacionamiento.listarVehiculosParqueados(TipoVehiculo.MOTO.toString()).getBody().size());
		Assert.assertNull(controladorEstacionamiento.listarVehiculosParqueados("OTRO").getBody());
	}
	
}
