package co.com.ceiba.estacionamiento.business;

import java.math.BigDecimal;
import java.util.List;

public interface VigilanteService {
	public void registrarIngresoVehiculo(Vehiculo vehiculo);
	public BigDecimal registrarSalidaVehiculo(String placa, String fechaSalida);
	public List<Vehiculo> listarVehiculosParqueados(String tipoVehiculo);
	public void evacuarVehiculosParqueados();
	public Vehiculo buscarVehiculoParqueado(String placa);
}