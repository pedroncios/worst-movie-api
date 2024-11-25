package br.com.pedroncios.worstmovie.repository;

import br.com.pedroncios.worstmovie.entity.Movie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MovieRepository extends JpaRepository<Movie, UUID> {
}
