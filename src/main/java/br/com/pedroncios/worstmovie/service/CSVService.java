package br.com.pedroncios.worstmovie.service;

import br.com.pedroncios.worstmovie.dto.MovieDTO;
import br.com.pedroncios.worstmovie.dto.ProducerDTO;
import br.com.pedroncios.worstmovie.dto.StudioDTO;
import br.com.pedroncios.worstmovie.repository.MovieRepository;
import br.com.pedroncios.worstmovie.repository.ProducerRepository;
import br.com.pedroncios.worstmovie.repository.StudioRepository;
import br.com.pedroncios.worstmovie.util.Utils;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

@Service
public class CSVService {

    @Value("${resource.csv.path}")
    private String csvFilePath;

    // Colunas do CSV
    private static final int H_YEAR = 0;
    private static final int H_TITLE = 1;
    private static final int H_STUDIOS = 2;
    private static final int H_PRODUCERS = 3;
    private static final int H_WINNER = 4;
    private static final int TOTAL_COLUMNS = 5;

    @Autowired
    private MovieService movieService;
    @Autowired
    private ProducerService producerService;
    @Autowired
    private StudioService studioService;

    public void importData() throws IOException {
        importData(csvFilePath);
    }

    public void importData(String resourceRelativePath) throws IOException {
        this.importData(new ClassPathResource(resourceRelativePath).getFile());
    }

    public void importData(File file)
    {
        try {
            FileReader filereader = new FileReader(file);
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();

            String[] nextRecord = csvReader.readNext(); // Pula o header
            while ((nextRecord = csvReader.readNext()) != null) {
                if (nextRecord.length == 1 &&  nextRecord[0].isEmpty()) {
                    continue;
                }

                if (nextRecord.length < TOTAL_COLUMNS) {
                    Utils.log("WARN", "Não foi possível carregar o filme pois há dados faltantes. Atenção! O filme será ignorado");
                    continue;
                }

                int year;
                try {
                    year = Integer.parseInt(nextRecord[H_YEAR]);
                } catch (NumberFormatException numberFormatException) {
                    Utils.log("WARN", "Não foi possível carregar o ano do filme \"" + nextRecord[H_TITLE] + "\". Atenção! O filme será ignorado");
                    continue;
                }

                List<ProducerDTO> producerDTOList = Utils.parseObjects(nextRecord[H_PRODUCERS], ProducerDTO::new);
                if (producerDTOList.isEmpty()) {
                    Utils.log("WARN", "Não foi possível carregar os produtores do filme \"" + nextRecord[H_TITLE] + "\". Atenção! O filme será ignorado");
                    continue;
                }

                List<StudioDTO> studioDTOList = Utils.parseObjects(nextRecord[H_STUDIOS], StudioDTO::new);
                if (studioDTOList.isEmpty()) {
                    Utils.log("WARN", "Não foi possível carregar os estúdios do filme \"" + nextRecord[H_TITLE] + "\". Atenção! O filme será ignorado");
                    continue;
                }

                MovieDTO movieDTO = new MovieDTO(nextRecord[H_TITLE], year, nextRecord[H_WINNER].equals("yes"));
                this.movieService.createMovie(movieDTO, producerDTOList, studioDTOList);
            }

            Utils.log("INFO", "CSV carregado com sucesso!");
        }
        catch (Exception e) {
            Utils.log("ERRO", "Não foi possível abrir ou carregar o arquivo CSV especificado: " + file.getAbsolutePath());
        }
    }

    public void cleanAllData() {
        this.movieService.cleanAllMovies();
        this.producerService.cleanAllProducers();
        this.studioService.cleanAllStudios();
    }
}
