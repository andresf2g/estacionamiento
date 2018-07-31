package co.com.ceiba.estacionamiento.builders;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.com.ceiba.estacionamiento.business.TipoVehiculo;
import co.com.ceiba.estacionamiento.business.Vehiculo;

public class MotoTestDataBuilder {
	
	private static final String PLACA = "BCD98E";
	private static final Integer CILINDRAJE = 200;
	private static final TipoVehiculo TIPO_VEHICULO = TipoVehiculo.MOTO;
	private static final String FECHA_INGRESO = "2018-05-06 10:00";
	private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	
	private String placa;
	private Integer cilindraje;
	private TipoVehiculo tipoVehiculo;
	private Date fechaIngreso;
	
	public MotoTestDataBuilder() {
		this.placa = PLACA;
		this.cilindraje = CILINDRAJE;
		this.tipoVehiculo = TIPO_VEHICULO;
		try {
			this.fechaIngreso = SDF.parse(FECHA_INGRESO);
		} catch (ParseException e) {
			this.fechaIngreso = new Date();
		}
	}
	
	public MotoTestDataBuilder conPlaca(String placa) {
		this.placa = placa;
		return this;
	}
	
	public Vehiculo build() {
		return new Vehiculo(this.placa, this.cilindraje, this.tipoVehiculo, this.fechaIngreso);
	}
}
