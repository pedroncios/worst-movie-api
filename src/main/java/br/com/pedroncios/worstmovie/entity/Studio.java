package br.com.pedroncios.worstmovie.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Table(name = "studios")
@Entity(name = "studios")
public class Studio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @ManyToMany(mappedBy = "studios", fetch = FetchType.EAGER)
    private List<Movie> movies;

    public Studio(String name) {
        this.name = name;
    }

    public void addMovie(Movie movie) {
        if (this.movies == null) {
            this.movies = new ArrayList<>();
        }

        this.movies.add(movie);
    }
}
