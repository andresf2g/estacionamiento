package co.com.ceiba.estacionamiento.integration;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import co.com.ceiba.estacionamiento.builders.CarroTestDataBuilder;
import co.com.ceiba.estacionamiento.builders.MotoTestDataBuilder;
import co.com.ceiba.estacionamiento.builders.VehiculoTestDataBuilder;
import co.com.ceiba.estacionamiento.business.VigilanteService;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;
import co.com.ceiba.estacionamiento.repository.PrecioRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class VigilanteTests {

	private String listarVehiculos = "/estacionamiento/listarVehiculosParqueados";
	private String registrarIngreso = "/estacionamiento/registrarIngresoVehiculo";
	private String registrarSalida = "/estacionamiento/registrarSalidaVehiculo";
	
	@Autowired
    private MockMvc mockMvc;
	
	@Autowired
	private VigilanteService servicioVigilante;
	
	@Autowired
	private PrecioRepository repositorioPrecio;
	
	private String convertirAJson(final Object obj) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(obj);
	}  
	
	private ResultActions llamarServicioIngresoVehiculo(VehiculoTestDataBuilder vehiculoBuilder) throws Exception {
		return mockMvc.perform(post(registrarIngreso).content(convertirAJson(vehiculoBuilder.build())).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print());
	}
	
	private ResultActions llamarServicioSalidaVehiculo(VehiculoTestDataBuilder vehiculoBuilder) throws Exception {
		return mockMvc.perform(post(registrarSalida).content(convertirAJson(vehiculoBuilder.build())).contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON)).andDo(print());
	}

	private void insertarNVehiculos(VehiculoTestDataBuilder vehiculoBuilder, int n) {
		for (int i = 0; i < n; i++) {
			vehiculoBuilder.conPlaca(vehiculoBuilder.getPrefijoPlaca() + "45" + i);
			servicioVigilante.registrarIngresoVehiculo(vehiculoBuilder.build());
		}
	}

	private void registrarIngresoVehiculoParqueaderoDisponible(VehiculoTestDataBuilder vehiculoBuilder) throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		
		ResultActions ingreso = llamarServicioIngresoVehiculo(vehiculoBuilder);
		
		ingreso.andExpect(status().isOk());
		assertNotNull(servicioVigilante.buscarVehiculoParqueado(vehiculoBuilder.build().getPlaca()));
	}
	
	@Test
	public void registrarIngresoMotoConParqueaderoDisponibleTest() throws Exception {
		registrarIngresoVehiculoParqueaderoDisponible(new MotoTestDataBuilder().conFechaIngreso(null));
	}
	
	@Test
	public void registrarIngresoCarroConParqueaderoDisponibleTest() throws Exception {
		registrarIngresoVehiculoParqueaderoDisponible(new CarroTestDataBuilder().conFechaIngreso(null));
	}
	
	@Test
	public void registrarIngresoMotoConParqueaderoLlenoTest() throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder motoBuilder = new MotoTestDataBuilder();
		insertarNVehiculos(motoBuilder, 10);
		motoBuilder.conPlaca("ZTE56D");

		ResultActions ingreso = llamarServicioIngresoVehiculo(motoBuilder);
		
		ingreso.andExpect(status().isBadRequest()).andExpect(jsonPath("$[0]", equalTo(VigilanteServiceException.INDISPONIBILIDAD_ESTACIONAMIENTO)));
	}

	@Test
	public void registrarIngresoCarroConParqueaderoLlenoTest() throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carroBuilder = new CarroTestDataBuilder();
		insertarNVehiculos(carroBuilder, 20);
		carroBuilder.conPlaca("ZTE568");

		ResultActions ingreso = llamarServicioIngresoVehiculo(carroBuilder);
		
		ingreso.andExpect(status().isBadRequest()).andExpect(jsonPath("$[0]", equalTo(VigilanteServiceException.INDISPONIBILIDAD_ESTACIONAMIENTO)));
	}

	@Test
	public void registrarIngresoVehiculoPlacaADiaCorrectoTest() throws Exception {
		registrarIngresoVehiculoParqueaderoDisponible(new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-01 09:30"));
		registrarIngresoVehiculoParqueaderoDisponible(new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-02 09:30"));
	}

	@Test
	public void registrarIngresoVehiculoPlacaADiaIncorrectoTest() throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder().conPlaca("ABC854").conFechaIngreso("2018-07-03 09:30");
		
		ResultActions ingreso = llamarServicioIngresoVehiculo(carro);
		
		ingreso.andExpect(status().isBadRequest()).andExpect(jsonPath("$[0]", equalTo(VigilanteServiceException.PLACA_A_DIA_INCORRECTO)));
	}
	
	@Test
	public void registrarIngresoVehiculoIngresadoTest() throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(carro.build());
		
		ResultActions ingreso = llamarServicioIngresoVehiculo(carro);
		
		ingreso.andExpect(status().isBadRequest()).andExpect(jsonPath("$[0]", equalTo(VigilanteServiceException.VEHICULO_YA_INGRESADO)));
	}

	@Test
	public void registrarVehiculoConExcepcionInternaTest() throws Exception {
		ResultActions registro = llamarServicioIngresoVehiculo(new CarroTestDataBuilder().conPlaca(null));
		
		registro.andExpect(status().isInternalServerError());
	}
	
	@Test
	public void registrarSalidaVehiculoIngresadoTest() throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(carro.build());
		
		ResultActions salida = llamarServicioSalidaVehiculo(carro);

		salida.andExpect(status().isOk());
		assertNull(servicioVigilante.buscarVehiculoParqueado(carro.build().getPlaca()));
	}

	@Test
	public void registrarSalidaVehiculoNoIngresadoTest() throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder().conFechaSalida("2018-07-10 19:35");
		
		ResultActions salida = llamarServicioSalidaVehiculo(carro);
		
		salida.andExpect(status().isBadRequest()).andExpect(jsonPath("$[0]", equalTo(VigilanteServiceException.VEHICULO_NO_INGRESADO)));
	}
	
	@Test
	public void registrarSalidaVehiculoSinPrecioConfiguradoTest() throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder carro = new CarroTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(carro.build());
		carro.conFechaSalida("2018-07-10 19:35");
		repositorioPrecio.deleteAll();
		
		ResultActions salida = llamarServicioSalidaVehiculo(carro);

		salida.andExpect(status().isBadRequest()).andExpect(jsonPath("$[0]", equalTo(VigilanteServiceException.PRECIO_NO_ENCONTRADO)));
	}
	
	private void registrarSalidaVehiculoGenerico(VehiculoTestDataBuilder vehiculoBuilder, String fechaSalida, boolean cilindrajeSuperior, String valorEsperado) throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		if (cilindrajeSuperior) {
			vehiculoBuilder.conCilindraje(650);
		}
		servicioVigilante.registrarIngresoVehiculo(vehiculoBuilder.build());
		vehiculoBuilder.conFechaSalida(fechaSalida);
		
		ResultActions salida = llamarServicioSalidaVehiculo(vehiculoBuilder);
		
		salida.andExpect(status().isOk()).andExpect(jsonPath("$[0]", equalTo(valorEsperado)));
	}
	
	private void registrarSalidaVehiculoGenerico(VehiculoTestDataBuilder vehiculoBuilder, String fechaSalida, String valorEsperado) throws Exception {
		registrarSalidaVehiculoGenerico(vehiculoBuilder, fechaSalida, false, valorEsperado);
	}
	
	@Test
	public void registrarSalidaCarroCobrandoDiaTest() throws Exception {
		registrarSalidaVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-10 19:35", "8000.00");
	}

	@Test
	public void registrarSalidaCarroCobrandoHorasTest() throws Exception {
		registrarSalidaVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-10 13:48", "4000.00");
	}

	@Test
	public void registrarSalidaCarroCobrandoDiasHorasTest() throws Exception {
		registrarSalidaVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-11 12:50", "11000.00");
	}
	
	@Test
	public void registrarSalidaCarroCobrandoDiasConDiaAdicionalTest() throws Exception {
		registrarSalidaVehiculoGenerico(new CarroTestDataBuilder(), "2018-07-11 19:49", "16000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeInferiorCobrandoDiaTest() throws Exception {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 19:35", "4000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeInferiorCobrandoHorasTest() throws Exception {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 13:48", "2000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeInferiorCobrandoDiasHorasTest() throws Exception {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-11 13:50", "6000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeSuperiorCobrandoDiaTest() throws Exception {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 19:35", true, "6000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeSuperiorCobrandoHorasTest() throws Exception {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-10 13:48", true, "4000.00");
	}

	@Test
	public void registrarSalidaMotoCilindrajeSuperiorCobrandoDiasHorasTest() throws Exception {
		registrarSalidaVehiculoGenerico(new MotoTestDataBuilder(), "2018-07-11 13:50", true, "8000.00");
	}
	
	@Test
	public void listarVehiculosParqueadosTest() throws Exception {
		servicioVigilante.evacuarVehiculosParqueados();
		VehiculoTestDataBuilder vehiculoBuilder = new CarroTestDataBuilder();
		insertarNVehiculos(vehiculoBuilder, 6);
		vehiculoBuilder = new MotoTestDataBuilder();
		servicioVigilante.registrarIngresoVehiculo(vehiculoBuilder.build());
		
		mockMvc.perform(get(listarVehiculos)).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(7)));
		mockMvc.perform(get(listarVehiculos).param("placa", "BCD98E")).andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$", hasSize(1)));
	}
	
}
