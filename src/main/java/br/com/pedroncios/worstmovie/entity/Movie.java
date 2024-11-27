package br.com.pedroncios.worstmovie.entity;

import br.com.pedroncios.worstmovie.dto.MovieDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Setter
@Getter
@Table(name = "movie")
@Entity(name = "movie")
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private int awardYear;
    private String title;
    private boolean winner;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "movie_producer",
            joinColumns = @JoinColumn(name="movie_id"),
            inverseJoinColumns = @JoinColumn(name="producer_id"))
    private Set<Producer> producers = new HashSet<>();

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "movie_studio",
            joinColumns = @JoinColumn(name="movie_id"),
            inverseJoinColumns = @JoinColumn(name="studio_id"))
    private Set<Studio> studios = new HashSet<>();

    public Movie(MovieDTO dto) {
        this.title = dto.title();
        this.awardYear = dto.awardYear();
        this.winner = dto.winner();
    }
}
