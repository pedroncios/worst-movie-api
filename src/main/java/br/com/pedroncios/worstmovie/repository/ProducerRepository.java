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

    @Query(value = """
                        SELECT p.name,
                               COUNT(m.id) AS totalAwards,
                               STRING_AGG(CAST(m.award_year AS VARCHAR), ', ') AS awardYears
                        FROM producer p
                        JOIN movie_producer mp ON mp.producer_id = p.id
                        JOIN movie m ON m.id = mp.movie_id
                        WHERE m.winner = true
                        GROUP BY p.name
                        HAVING COUNT(m.id) > 1
                    """, nativeQuery = true)
    List<Object[]> findProducersWithMultiplePrizes();
}
