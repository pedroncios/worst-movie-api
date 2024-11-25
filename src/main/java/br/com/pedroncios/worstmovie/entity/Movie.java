package br.com.pedroncios.worstmovie.entity;

import br.com.pedroncios.worstmovie.dto.MovieDTO;
import br.com.pedroncios.worstmovie.dto.ProducerDTO;
import br.com.pedroncios.worstmovie.dto.StudioDTO;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Table(name = "movies")
@Entity(name = "movies")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int awardYear;
    private String title;
    private boolean winner;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "movie_producer", joinColumns=
            {@JoinColumn(name="movie_id")}, inverseJoinColumns=
            {@JoinColumn(name="producer_id")})
    private List<Producer> producers;

    @ManyToMany(cascade = CascadeType.MERGE)
    @JoinTable(name = "movie_studio", joinColumns=
            {@JoinColumn(name="movie_id")}, inverseJoinColumns=
            {@JoinColumn(name="studio_id")})
    private List<Studio> studios;

    public Movie(MovieDTO dto) {
        this.title = dto.title();
        this.awardYear = dto.awardYear();
        this.winner = dto.winner();
        this.producers = new ArrayList<>();
        this.studios = new ArrayList<>();
    }

    public void addProducer(Producer producer) {
        if (this.producers == null) {
            this.producers = new ArrayList<>();
        }

        this.producers.add(producer);
    }

    public void addStudio(Studio studio) {
        if (this.studios == null) {
            this.studios = new ArrayList<>();
        }

        this.studios.add(studio);
    }
}
