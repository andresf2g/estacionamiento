package co.com.ceiba.estacionamiento;

import java.text.SimpleDateFormat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EstacionamientoApplication {

	public static void main(String[] args) {
		SpringApplication.run(EstacionamientoApplication.class, args);
	}

	public static SimpleDateFormat formatoFecha() {
		return new SimpleDateFormat("yyyy-MM-dd HH:mm");
	}
}
