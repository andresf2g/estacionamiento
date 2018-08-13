package co.com.ceiba.estacionamiento.business;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface VigilanteService {
	public void registrarIngresoVehiculo(Vehiculo vehiculo);
	public BigDecimal registrarSalidaVehiculo(String placa, Date fechaSalida);
	public List<Vehiculo> listarVehiculosParqueados(String placa);
	public void evacuarVehiculosParqueados();
	public Vehiculo buscarVehiculoParqueado(String placa);
}