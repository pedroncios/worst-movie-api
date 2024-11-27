package br.com.pedroncios.worstmovie.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@Getter
@Table(name = "studio")
@Entity(name = "studio")
public class Studio {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String name;

    @ManyToMany(mappedBy = "studios", fetch = FetchType.EAGER)
    private Set<Movie> movies = new HashSet<>();

    public Studio(String name) {
        this.name = name;
    }
}
