package br.com.pedroncios.worstmovie.repository;

import br.com.pedroncios.worstmovie.entity.Studio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudioRepository extends JpaRepository<Studio, UUID> {
    Optional<Studio> findFirstByName(String name);
}
