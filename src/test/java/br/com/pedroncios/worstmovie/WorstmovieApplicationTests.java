package br.com.pedroncios.worstmovie;

import br.com.pedroncios.worstmovie.controller.AwardController;
import br.com.pedroncios.worstmovie.dto.AwardIntervalDTO;
import br.com.pedroncios.worstmovie.dto.IntervalDTO;
import br.com.pedroncios.worstmovie.entity.Movie;
import br.com.pedroncios.worstmovie.entity.Producer;
import br.com.pedroncios.worstmovie.entity.Studio;
import br.com.pedroncios.worstmovie.repository.MovieRepository;
import br.com.pedroncios.worstmovie.repository.ProducerRepository;
import br.com.pedroncios.worstmovie.repository.StudioRepository;
import br.com.pedroncios.worstmovie.service.CSVService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WorstmovieApplicationTests {

	@Autowired
	private CSVService csvService;
	@Autowired
	private MovieRepository movieRepository;
	@Autowired
	private ProducerRepository producerRepository;
	@Autowired
	private StudioRepository studioRepository;

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	public void setup() {
		csvService.cleanAllData();
	}

	@Test
	void contextLoads() throws Exception {
		assertThat(csvService).isNotNull();
	}

	@Test
	public void loadCSVFileWithInvalidPathShouldThrowIOException() {
		Assertions.assertThrows(IOException.class, () -> csvService.importData("invalid\\path\\file.csv"));
	}

	@Test
	public void loadCSVWithOnlyInvalidDataShouldIgnoreAllRecords() throws IOException {
		csvService.importData("csv\\dados_invalidos_movielist.csv");
		assertThat(movieRepository.findAll().isEmpty()).isTrue();
		assertThat(producerRepository.findAll().isEmpty()).isTrue();
		assertThat(studioRepository.findAll().isEmpty()).isTrue();

		csvService.cleanAllData();

		csvService.importData("csv\\so_cabecalho_movielist.csv");
		assertThat(movieRepository.findAll().isEmpty()).isTrue();
		assertThat(producerRepository.findAll().isEmpty()).isTrue();
		assertThat(studioRepository.findAll().isEmpty()).isTrue();

		csvService.cleanAllData();

		csvService.importData("csv\\em_branco_movielist.csv");
		assertThat(movieRepository.findAll().isEmpty()).isTrue();
		assertThat(producerRepository.findAll().isEmpty()).isTrue();
		assertThat(studioRepository.findAll().isEmpty()).isTrue();
	}

	@Test
	public void moviesShouldBeSavedCorrectly() throws IOException {
		csvService.importData("csv\\3producers_movielist.csv");

		List<Movie> movieList = movieRepository.findAll();

		assertThat(movieList.size()).isEqualTo(10);

		for (int i=0; i<movieList.size(); i++) {
			assertThat(movieList.get(i).getTitle()).isEqualTo("Movie " + (i+1));
			assertThat(movieList.get(i).getAwardYear()).isEqualTo(2000 + i);
		}

		assertThat(movieList.get(0).isWinner()).isTrue();
		assertThat(movieList.get(1).isWinner()).isFalse();
		assertThat(movieList.get(2).isWinner()).isTrue();
		assertThat(movieList.get(3).isWinner()).isFalse();
		assertThat(movieList.get(4).isWinner()).isFalse();
		assertThat(movieList.get(5).isWinner()).isTrue();
		assertThat(movieList.get(6).isWinner()).isFalse();
		assertThat(movieList.get(7).isWinner()).isTrue();
		assertThat(movieList.get(8).isWinner()).isFalse();
		assertThat(movieList.get(9).isWinner()).isTrue();
	}

	@Test
	public void producersShouldBeSavedCorrectly() throws IOException {
		csvService.importData("csv\\3producers_movielist.csv");

		List<Producer> producerList = producerRepository.findAll();

		assertThat(producerList.size()).isEqualTo(3);

		for (int i=0; i<producerList.size(); i++) {
			assertThat(producerList.get(i).getName()).isEqualTo("Producer " + (i+1));
		}
	}

	@Test
	public void studiosShouldBeSavedCorrectly() throws IOException {
		csvService.importData("csv\\3producers_movielist.csv");

		List<Studio> studiosList = studioRepository.findAll();

		assertThat(studiosList.size()).isEqualTo(8);

		for (int i=0; i<studiosList.size(); i++) {
			assertThat(studiosList.get(i).getName()).isEqualTo("Studio " + (i+1));
		}
	}

	@Test
	public void intervalEndpointShouldReturnEmptyWhenDatabaseIsClean() {
		String url = "http://localhost:" + port + "/awards/intervals";
		ResponseEntity<AwardIntervalDTO> response = restTemplate.getForEntity(url, AwardIntervalDTO.class);

		assertThat(response.getStatusCode().value()).isEqualTo(200);

		AwardIntervalDTO responseBody = response.getBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.min()).isNotNull();
		assertThat(responseBody.max()).isNotNull();

		assertThat(responseBody.min().isEmpty()).isTrue();
		assertThat(responseBody.max().isEmpty()).isTrue();
	}

	@Test
	public void intervalEndpointShouldReturnEmptyWhenNoWinners() throws IOException {
		csvService.importData("csv\\sem_vencedor_movielist.csv");

		String url = "http://localhost:" + port + "/awards/intervals";
		ResponseEntity<AwardIntervalDTO> response = restTemplate.getForEntity(url, AwardIntervalDTO.class);

		assertThat(response.getStatusCode().value()).isEqualTo(200);

		AwardIntervalDTO responseBody = response.getBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.min()).isNotNull();
		assertThat(responseBody.max()).isNotNull();

		assertThat(responseBody.min().isEmpty()).isTrue();
		assertThat(responseBody.max().isEmpty()).isTrue();
	}

	@Test
	public void intervalEndpointShouldReturnSameInterval() throws IOException {
		csvService.importData("csv\\unico_vencedor_movielist.csv");

		String url = "http://localhost:" + port + "/awards/intervals";
		ResponseEntity<AwardIntervalDTO> response = restTemplate.getForEntity(url, AwardIntervalDTO.class);

		assertThat(response.getStatusCode().value()).isEqualTo(200);

		AwardIntervalDTO responseBody = response.getBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.min()).isNotNull();
		assertThat(responseBody.max()).isNotNull();

		assertThat(responseBody.min().size()).isEqualTo(1);
		assertThat(responseBody.max().size()).isEqualTo(1);

		assertThat(responseBody.min().get(0)).isEqualTo(responseBody.max().get(0));
	}

	@Test
	public void intervalEndpointShouldIgnoreZeroIntervals() throws IOException {
		csvService.importData("csv\\mesmo_ano_movielist.csv");

		String url = "http://localhost:" + port + "/awards/intervals";
		ResponseEntity<AwardIntervalDTO> response = restTemplate.getForEntity(url, AwardIntervalDTO.class);

		assertThat(response.getStatusCode().value()).isEqualTo(200);

		AwardIntervalDTO responseBody = response.getBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.min()).isNotNull();
		assertThat(responseBody.max()).isNotNull();

		assertThat(responseBody.min().size()).isEqualTo(1);
		assertThat(responseBody.max().size()).isEqualTo(1);

		assertThat(responseBody.min().get(0).interval()).isEqualTo(1);
		assertThat(responseBody.max().get(0).interval()).isEqualTo(9);
	}

	@Test
	public void intervalEndpoindShouldReturnValidDataWhenTie() throws IOException {
		csvService.importData("csv\\com_empate_movielist.csv");

		String url = "http://localhost:" + port + "/awards/intervals";
		ResponseEntity<AwardIntervalDTO> response = restTemplate.getForEntity(url, AwardIntervalDTO.class);

		assertThat(response.getStatusCode().value()).isEqualTo(200);

		AwardIntervalDTO responseBody = response.getBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.min()).isNotNull();
		assertThat(responseBody.max()).isNotNull();

		assertThat(responseBody.min().size()).isEqualTo(2);
		assertThat(responseBody.max().size()).isEqualTo(1);

		IntervalDTO firstMinInterval = responseBody.min().get(0);
		assertThat(firstMinInterval.producer()).isEqualTo("Producer 1");
		assertThat(firstMinInterval.interval()).isEqualTo(1);
		assertThat(firstMinInterval.previousWin()).isEqualTo(2000);
		assertThat(firstMinInterval.followingWin()).isEqualTo(2001);

		IntervalDTO secondMinInterval = responseBody.min().get(1);
		assertThat(secondMinInterval.producer()).isEqualTo("Producer 3");
		assertThat(secondMinInterval.interval()).isEqualTo(1);
		assertThat(secondMinInterval.previousWin()).isEqualTo(2002);
		assertThat(secondMinInterval.followingWin()).isEqualTo(2003);

		IntervalDTO maxInterval = responseBody.max().get(0);
		assertThat(maxInterval.producer()).isEqualTo("Producer 1");
		assertThat(maxInterval.interval()).isEqualTo(8);
		assertThat(maxInterval.previousWin()).isEqualTo(2001);
		assertThat(maxInterval.followingWin()).isEqualTo(2009);
	}

	@Test
	public void intervalEndpoindShouldReturnValidDataUsing_movielist_csv() throws IOException {
		csvService.importData("csv\\movielist.csv");

		String url = "http://localhost:" + port + "/awards/intervals";
		ResponseEntity<AwardIntervalDTO> response = restTemplate.getForEntity(url, AwardIntervalDTO.class);

		assertThat(response.getStatusCode().value()).isEqualTo(200);

		AwardIntervalDTO responseBody = response.getBody();

		assertThat(responseBody).isNotNull();
		assertThat(responseBody.min()).isNotNull();
		assertThat(responseBody.max()).isNotNull();

		assertThat(responseBody.min().size()).isEqualTo(1);
		assertThat(responseBody.max().size()).isEqualTo(1);

		IntervalDTO firstMinInterval = responseBody.min().get(0);
		assertThat(firstMinInterval.producer()).isEqualTo("Joel Silver");
		assertThat(firstMinInterval.interval()).isEqualTo(1);
		assertThat(firstMinInterval.previousWin()).isEqualTo(1990);
		assertThat(firstMinInterval.followingWin()).isEqualTo(1991);

		IntervalDTO maxInterval = responseBody.max().get(0);
		assertThat(maxInterval.producer()).isEqualTo("Matthew Vaughn");
		assertThat(maxInterval.interval()).isEqualTo(13);
		assertThat(maxInterval.previousWin()).isEqualTo(2002);
		assertThat(maxInterval.followingWin()).isEqualTo(2015);
	}
}
