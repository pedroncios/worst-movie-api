package br.com.pedroncios.worstmovie;

import br.com.pedroncios.worstmovie.service.CSVService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class WorstmovieApplication {

	@Autowired
	private CSVService csvService;

	public static void main(String[] args) {
		SpringApplication.run(WorstmovieApplication.class, args);
	}

	@EventListener(ApplicationReadyEvent.class)
	private void loadCSVData() {
		this.csvService.readDataLineByLine();
	}
}
