package br.com.pedroncios.worstmovie.repository;

import br.com.pedroncios.worstmovie.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProducerRepository extends JpaRepository<Producer, UUID> {
    Optional<Producer> findFirstByName(String name);
}
