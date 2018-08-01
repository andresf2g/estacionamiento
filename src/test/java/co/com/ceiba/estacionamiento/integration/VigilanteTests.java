package co.com.ceiba.estacionamiento.integration;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import co.com.ceiba.estacionamiento.builders.MotoTestDataBuilder;
import co.com.ceiba.estacionamiento.business.Vehiculo;
import co.com.ceiba.estacionamiento.business.VigilanteServiceException;
import co.com.ceiba.estacionamiento.business.VigilanteService;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VigilanteTests {

	@Autowired
	private VigilanteService servicioVigilante;

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
		MotoTestDataBuilder motoBuilder = new MotoTestDataBuilder();
		
		servicioVigilante.registrarIngresoVehiculo(motoBuilder.build());
		try {
			motoBuilder.conPlaca("ZTE56D");
			servicioVigilante.registrarIngresoVehiculo(motoBuilder.build());
		} catch (VigilanteServiceException e) {
			Assert.assertEquals(VigilanteServiceException.INDISPONIBILIDAD_PARQUEADERO, e.getMessage());
		}
	}

//	@Test
//	public void registrarIngresoCarroConParqueaderoDisponibleTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarIngresoCarroConParqueaderoLlenoTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarIngresoVehiculoPlacaADiaCorrectoTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarIngresoVehiculoPlacaADiaIncorrectoTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarIngresoVehiculoIngresadoTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoVehiculoIngresadoTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoVehiculoNoIngresadoTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoCarroCobrandoDiaTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoCarroCobrandoHorasTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoCarroCobrandoDiasHorasTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoMotoCilindrajeInferiorCobrandoDiaTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoMotoCilindrajeInferiorCobrandoHorasTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoMotoCilindrajeInferiorCobrandoDiasHorasTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoMotoCilindrajeSuperiorCobrandoDiaTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoMotoCilindrajeSuperiorCobrandoHorasTest() {
//		fail("Not yet implemented");
//	}
//
//	@Test
//	public void registrarEgresoMotoCilindrajeSuperiorCobrandoDiasHorasTest() {
//		fail("Not yet implemented");
//	}
}
