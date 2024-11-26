package br.com.pedroncios.worstmovie.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
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
    private Set<Movie> movies = new HashSet<>();

    public Producer(String name) {
        this.name = name;
    }
}
