package co.com.ceiba.estacionamiento.integration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.text.ParseException;
import java.util.List;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
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
import co.com.ceiba.estacionamiento.repository.PrecioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VigilanteTests {

	@Autowired
    private EstacionamientoController controladorEstacionamiento;
	
	@Autowired
	private VigilanteService servicioVigilante;
	
	@Autowired
	private PrecioRepository repositorioPrecio;
	
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
	public void registrarSalidaVehiculoIngresadoTest() throws ParseException {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(carro.build());
		carro.conFechaSalida("2018-07-10 19:35");
		
		controladorEstacionamiento.registrarSalidaVehiculo(carro.buildBody());

		assertNull(servicioVigilante.buscarVehiculoParqueado(carro.build().getPlaca()));
	}

	@Test
	public void registrarSalidaVehiculoNoIngresadoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder().conFechaSalida("2018-07-10 19:35");
		
		ResponseEntity<List<String>> salida = controladorEstacionamiento.registrarSalidaVehiculo(carro.buildBody());
		
		assertEquals(VigilanteServiceException.VEHICULO_NO_INGRESADO, salida.getBody().get(0));
	}
	
	@Test
	public void registrarSalidaVehiculoSinPrecioConfiguradoTest() {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(carro.build());
		carro.conFechaSalida("2018-07-10 19:35");
		repositorioPrecio.deleteAll();
		
		ResponseEntity<List<String>> salida = controladorEstacionamiento.registrarSalidaVehiculo(carro.buildBody());
		
		assertEquals(VigilanteServiceException.PRECIO_NO_ENCONTRADO, salida.getBody().get(0));
	}
	
	private void registrarSalidaVehiculoGenerico(VehiculoTestDataBuilder vehiculoBuilder, String fechaSalida, boolean cilindrajeSuperior, String valorEsperado) {
		servicioVigilante.evacuarVehiculosParqueados();
		if (cilindrajeSuperior) {
			vehiculoBuilder.conCilindraje(650);
		}
		servicioVigilante.registrarIngresoVehiculo(vehiculoBuilder.build());
		vehiculoBuilder.conFechaSalida(fechaSalida);
		
		ResponseEntity<List<String>> salida = controladorEstacionamiento.registrarSalidaVehiculo(vehiculoBuilder.buildBody());
		
		assertEquals(valorEsperado, salida.getBody().get(0));
	}
	
	private void registrarSalidaVehiculoGenerico(VehiculoTestDataBuilder vehiculoBuilder, String fechaSalida, String valorEsperado) {
		registrarSalidaVehiculoGenerico(vehiculoBuilder, fechaSalida, false, valorEsperado);
	}
	
	@Test
	public void registrarSalidaCarroCobrandoDiaTest() {
		registrarSalidaVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-10 19:35", "8000.00");
	}

	@Test
	public void registrarSalidaCarroCobrandoHorasTest() {
		registrarSalidaVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-10 13:48", "4000.00");
	}

	@Test
	public void registrarSalidaCarroCobrandoDiasHorasTest() {
		registrarSalidaVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-11 12:50", "11000.00");
	}
	
	@Test
	public void registrarSalidaCarroCobrandoDiasConDiaAdicionalTest() {
		registrarSalidaVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-11 19:49", "16000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeInferiorCobrandoDiaTest() {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 19:35", "4000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeInferiorCobrandoHorasTest() {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 13:48", "2000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeInferiorCobrandoDiasHorasTest() {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-11 13:50", "6000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeSuperiorCobrandoDiaTest() {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 19:35", true, "6000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeSuperiorCobrandoHorasTest() {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 13:48", true, "4000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeSuperiorCobrandoDiasHorasTest() {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-11 13:50", true, "8000.00");
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
	}
	
}
