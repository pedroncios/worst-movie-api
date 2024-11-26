package br.com.pedroncios.worstmovie.repository;

import br.com.pedroncios.worstmovie.dto.ProducerPrizesDTO;
import br.com.pedroncios.worstmovie.entity.Producer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProducerRepository extends JpaRepository<Producer, UUID> {
    Optional<Producer> findFirstByName(String name);

    @Query("""
            SELECT new br.com.pedroncios.worstmovie.dto.ProducerPrizesDTO(p.name, f.awardYear)
            FROM producers p
            JOIN p.movies f
            WHERE f.winner = true
            ORDER BY p.name
            """)
    List<ProducerPrizesDTO> findProducersPrizes();
}
