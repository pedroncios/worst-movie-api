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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ProducerService producerService;
    @Autowired
    private StudioService studioService;

    @Transactional
    public Movie createMovie(MovieDTO movieDTO, List<ProducerDTO> producers, List<StudioDTO> studios) throws NoProducerException, NoStudioException {
        if(producers == null || producers.isEmpty()) {
            throw new NoProducerException();
        }

        if(studios == null || studios.isEmpty()) {
            throw new NoStudioException();
        }

        Movie movie = new Movie(movieDTO);

        Set<Producer> savedProducers = new HashSet<>();
        for (ProducerDTO producerDTO : producers) {
            Optional<Producer> producer = producerService.getProducerFromDTO(producerDTO);
            savedProducers.add(producer.orElseGet(() -> producerService.createProducer(producerDTO)));
        }
        movie.setProducers(savedProducers);

        Set<Studio> savedStudios = new HashSet<>();
        for (StudioDTO studioDTO :studios) {
            Optional<Studio> studio = studioService.getStudioFromDTO(studioDTO);
            savedStudios.add(studio.orElseGet(() -> studioService.createStudio(studioDTO)));
        }
        movie.setStudios(savedStudios);

        return this.movieRepository.save(movie);
    }
}
