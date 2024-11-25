package br.com.pedroncios.worstmovie.service;

import br.com.pedroncios.worstmovie.dto.MovieDTO;
import br.com.pedroncios.worstmovie.dto.ProducerDTO;
import br.com.pedroncios.worstmovie.dto.StudioDTO;
import br.com.pedroncios.worstmovie.entity.Movie;
import br.com.pedroncios.worstmovie.entity.Producer;
import br.com.pedroncios.worstmovie.entity.Studio;
import br.com.pedroncios.worstmovie.exceptions.NoProducerException;
import br.com.pedroncios.worstmovie.exceptions.NoStudioException;
import br.com.pedroncios.worstmovie.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ProducerService producerService;
    @Autowired
    private StudioService studioService;

    public Movie saveMovie(MovieDTO movieDTO, List<ProducerDTO> producers, List<StudioDTO> studios) throws NoProducerException, NoStudioException {
        if(producers == null || producers.isEmpty()) {
            throw new NoProducerException();
        }

        if(studios == null || studios.isEmpty()) {
            throw new NoStudioException();
        }

        Movie movie = new Movie(movieDTO);

        for (ProducerDTO producerDTO : producers) {
            Optional<Producer> producer = producerService.getProducerFromDTO(producerDTO);
            Producer p = producer.orElseGet(() -> new Producer(producerDTO.name()));
            /*producer.ifPresent(p -> {
                this.producerService.saveProducer(p);
            });*/
            movie.addProducer(p);
        }

//        for (StudioDTO studioDTO : studios) {
//            Optional<Studio> studio = studioService.getStudioFromDTO(studioDTO);
//            studio.ifPresent(s -> {
//                this.studioService.saveStudio(s);
//            });
//            movie.addStudio(studio.orElseGet(() -> new Studio(studioDTO.name())));
//        }

        return this.movieRepository.save(movie);
    }
}
