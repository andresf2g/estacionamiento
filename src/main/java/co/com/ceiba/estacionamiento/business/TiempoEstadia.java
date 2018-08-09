package co.com.ceiba.estacionamiento.business;

public class TiempoEstadia {
	private int dias;
	private int horas;

	public TiempoEstadia(int dias, int horas) {
		super();
		this.dias = dias;
		this.horas = horas;
	}

	public int getDias() {
		return dias;
	}

	public int getHoras() {
		return horas;
	}

}
