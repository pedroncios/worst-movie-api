package br.com.pedroncios.worstmovie.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor
@Table(name = "producers")
@Entity(name = "producers")
public class Producer {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @ManyToMany(mappedBy = "producers", fetch = FetchType.EAGER)
    private List<Movie> movies;

    public Producer(String name) {
        this.name = name;
    }

    public void addMovie(Movie movie) {
        if (this.movies == null) {
            this.movies = new ArrayList<>();
        }

        this.movies.add(movie);
    }
}
