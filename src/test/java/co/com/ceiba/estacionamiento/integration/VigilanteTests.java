package co.com.ceiba.estacionamiento.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.text.ParseException;
import java.util.List;

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

	private void registrarIngresoVehiculoParqueaderoDisponible(VehiculoTestDataBuilder vehiculoBuilder) {
		servicioVigilante.evacuarVehiculosParqueados();
		
		controladorEstacionamiento.registrarIngresoVehiculo(vehiculoBuilder.buildBody());
		
		assertNotNull(servicioVigilante.buscarVehiculoParqueado(vehiculoBuilder.build().getPlaca()));
	}
	
	@Test
	public void registrarIngresoMotoConParqueaderoDisponibleTest() {
		registrarIngresoVehiculoParqueaderoDisponible(new MotoTestDataBuilder().conFechaIngreso(null));
	}
	
	@Test
	public void registrarIngresoCarroConParqueaderoDisponibleTest() {
		registrarIngresoVehiculoParqueaderoDisponible(new CarroTestDataBuilder().conFechaIngreso(null));
	}
	
	@Test
	public void registrarIngresoMotoConParqueaderoLlenoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder motoBuilder = new MotoTestDataBuilder();
		insertarNVehiculos(motoBuilder, 10);
		motoBuilder.conPlaca("ZTE56D");

		ResponseEntity<List<String>> ingreso = controladorEstacionamiento.registrarIngresoVehiculo(motoBuilder.buildBody());
		
		assertEquals(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO, ingreso.getBody().get(0));
	}

	@Test
	public void registrarIngresoCarroConParqueaderoLlenoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carroBuilder = new CarroTestDataBuilder();
		insertarNVehiculos(carroBuilder, 20);
		carroBuilder.conPlaca("ZTE568");

		ResponseEntity<List<String>> ingreso = controladorEstacionamiento.registrarIngresoVehiculo(carroBuilder.buildBody());
		
		assertEquals(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO, ingreso.getBody().get(0));
	}

	@Test
	public void registrarIngresoVehiculoPlacaADiaCorrectoTest() {
		registrarIngresoVehiculoParqueaderoDisponible(new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-01 09:30"));
		registrarIngresoVehiculoParqueaderoDisponible(new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-02 09:30"));
	}

	@Test
	public void registrarIngresoVehiculoPlacaADiaIncorrectoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-03 09:30");
		
		ResponseEntity<List<String>> ingreso = controladorEstacionamiento.registrarIngresoVehiculo(carro.buildBody());
		
		assertEquals(VigilanteServiceException.PLACA_A_DIA_INCORRECTO, ingreso.getBody().get(0));
	}
	
	@Test
	public void registrarIngresoVehiculoIngresadoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(carro.build());
		
		ResponseEntity<List<String>> ingreso = controladorEstacionamiento.registrarIngresoVehiculo(carro.buildBody());
		
		assertEquals(VigilanteServiceException.VEHICULO_YA_INGRESADO, ingreso.getBody().get(0));
	}

	@Test
	public void registrarEgresoVehiculoIngresadoTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(carro.build());
		carro.conFechaEgreso("2018-05-06 14:40");
		
		controladorEstacionamiento.registrarEgresoVehiculo(carro.buildBody());

		assertNull(servicioVigilante.buscarVehiculoParqueado(carro.build().getPlaca()));
	}

	@Test
	public void registrarEgresoVehiculoNoIngresadoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder().conFechaEgreso("2018-05-06 14:40");
		
		ResponseEntity<List<String>> egreso = controladorEstacionamiento.registrarEgresoVehiculo(carro.buildBody());
		
		assertEquals(VigilanteServiceException.VEHICULO_NO_INGRESADO, egreso.getBody().get(0));
	}
	
	private void registrarEgresoVehiculoGenerico(VehiculoTestDataBuilder vehiculoBuilder, String fechaEgreso, boolean cilindrajeSuperior, String valorEsperado) {
		servicioVigilante.evacuarVehiculosParqueados();
		if (cilindrajeSuperior) {
			vehiculoBuilder.conCilindraje(650);
		}
		servicioVigilante.registrarIngresoVehiculo(vehiculoBuilder.build());
		vehiculoBuilder.conFechaEgreso(fechaEgreso);
		
		ResponseEntity<List<String>> egreso = controladorEstacionamiento.registrarEgresoVehiculo(vehiculoBuilder.buildBody());
		
		assertEquals(valorEsperado, egreso.getBody().get(0));
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
	public void registrarEgresoCarroCobrandoDiasConDiaAdicionalTest() {
		registrarEgresoVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-11 19:49", "16000.00");
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
		
		assertEquals(9, controladorEstacionamiento.listarVehiculosParqueados(null).getBody().size());
		assertEquals(6, controladorEstacionamiento.listarVehiculosParqueados(TipoVehiculo.CARRO.toString()).getBody().size());
		assertEquals(3, controladorEstacionamiento.listarVehiculosParqueados(TipoVehiculo.MOTO.toString()).getBody().size());
		try {
			controladorEstacionamiento.listarVehiculosParqueados("OTRO");
			fail();
		} catch (VigilanteServiceException e) {
			assertEquals(VigilanteServiceException.TIPO_VEHICULO, e.getMessage());
		}
	}
	
}
