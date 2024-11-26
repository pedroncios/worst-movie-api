package br.com.pedroncios.worstmovie.service;

import br.com.pedroncios.worstmovie.dto.MovieDTO;
import br.com.pedroncios.worstmovie.dto.ProducerDTO;
import br.com.pedroncios.worstmovie.dto.StudioDTO;
import br.com.pedroncios.worstmovie.util.Utils;
import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {

    private static final String CSV_FILE_PATH = "D:\\Development\\Java\\Spring\\worst-movie-api\\3producers_movielist.csv";

    // Colunas do CSV
    private static final int H_YEAR = 0;
    private static final int H_TITLE = 1;
    private static final int H_STUDIOS = 2;
    private static final int H_PRODUCERS = 3;
    private static final int H_WINNER = 4;

    @Autowired
    private MovieService movieService;

    public void readDataLineByLine()
    {
        try {
            File file = new File(CSV_FILE_PATH);
            FileReader filereader = new FileReader(file);
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();

            String[] nextRecord = csvReader.readNext(); // Pula o header
            while ((nextRecord = csvReader.readNext()) != null) {
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

                MovieDTO movieDTO = new MovieDTO(nextRecord[H_TITLE], Integer.parseInt(nextRecord[H_YEAR]), nextRecord[H_WINNER].equals("yes"));
                this.movieService.createMovie(movieDTO, producerDTOList, studioDTOList);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
