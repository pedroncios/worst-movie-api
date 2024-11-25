package br.com.pedroncios.worstmovie.service;

import br.com.pedroncios.worstmovie.dto.MovieDTO;
import br.com.pedroncios.worstmovie.dto.ProducerDTO;
import br.com.pedroncios.worstmovie.dto.StudioDTO;
import br.com.pedroncios.worstmovie.repository.ProducerRepository;
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

    private static final String CSV_FILE_PATH
            = "D:\\Development\\Java\\Spring\\worst-movie-api\\movielist.csv";

    private static final int H_YEAR = 0;
    private static final int H_TITLE = 1;
    private static final int H_STUDIOS = 2;
    private static final int H_PRODUCERS = 3;
    private static final int H_WINNER = 4;

    @Autowired
    private MovieService movieService;
    @Autowired
    private ProducerRepository producerRepository;

    public void readDataLineByLine()
    {
        try {
            File file = new File(CSV_FILE_PATH);
            FileReader filereader = new FileReader(file);
            CSVParser parser = new CSVParserBuilder().withSeparator(';').build();
            CSVReader csvReader = new CSVReaderBuilder(filereader)
                    .withCSVParser(parser)
                    .build();

            String[] nextRecord = csvReader.readNext();
            while ((nextRecord = csvReader.readNext()) != null) {
                List<ProducerDTO> producerDTOList = parseProducers(nextRecord[H_PRODUCERS]);
                if (producerDTOList == null || producerDTOList.isEmpty()) {
                    Utils.log("WARN", "Não foi possível carregar os produtores do filme \"" + nextRecord[H_TITLE] + "\". Atenção! O filme será ignorado");
                    continue;
                }

                List<StudioDTO> studioDTOList = new ArrayList<>();
                studioDTOList.add(new StudioDTO(nextRecord[H_STUDIOS]));

                MovieDTO movieDTO = new MovieDTO(nextRecord[H_TITLE], Integer.parseInt(nextRecord[H_YEAR]), nextRecord[H_WINNER].equals("yes"));
                this.movieService.saveMovie(movieDTO, producerDTOList, studioDTOList);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private List<ProducerDTO> parseProducers(String producers) {

        if (producers == null || producers.isBlank()) {
            return null;
        }

        List<ProducerDTO> producerDTOList = new ArrayList<>();
        String[] producersNames = producers.split(",| and ");

        for (String name : producersNames) {
            String trimmedName = name.trim();
            if (!trimmedName.isEmpty()) {
                producerDTOList.add(new ProducerDTO(trimmedName));
            }
        }

        return producerDTOList;
    }
}
