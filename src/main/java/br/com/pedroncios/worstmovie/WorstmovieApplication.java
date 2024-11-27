package br.com.pedroncios.worstmovie;

import br.com.pedroncios.worstmovie.service.CSVService;
import br.com.pedroncios.worstmovie.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

import java.io.IOException;

@SpringBootApplication
public class WorstmovieApplication {

	@Autowired
	private CSVService csvService;

	public static void main(String[] args) {
		SpringApplication.run(WorstmovieApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	private void loadCSVData() {
		try {
			this.csvService.importData();
		} catch (IOException ioException) {
			Utils.log("ERRO", "Não foi possível abrir ou carregar o arquivo CSV especificado: " + ioException.getMessage());
		}
	}
}
