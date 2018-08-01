package co.com.ceiba.estacionamiento.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public interface VigilanteService {
	public void registrarIngresoVehiculo(Vehiculo vehiculo);
	public BigDecimal registrarEgresoVehiculo(String placa, Date fechaEgreso);
	public List<Vehiculo> listarVehiculosParqueados(TipoVehiculo tipoVehiculo);
	public void evacuarVehiculosParqueados();
	public Vehiculo buscarVehiculoParqueado(String placa);
}