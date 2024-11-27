package br.com.pedroncios.worstmovie.service;

import br.com.pedroncios.worstmovie.dto.AwardIntervalDTO;
import br.com.pedroncios.worstmovie.dto.ProducerDTO;
import br.com.pedroncios.worstmovie.dto.ProducerPrizesDTO;
import br.com.pedroncios.worstmovie.entity.Producer;
import br.com.pedroncios.worstmovie.repository.ProducerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProducerService {

    @Autowired
    private ProducerRepository producerRepository;

    public Optional<Producer> getProducerFromDTO(ProducerDTO dto) {
        return producerRepository.findFirstByName(dto.name());
    }

    public Producer createProducer(ProducerDTO dto) {
        return this.producerRepository.save(new Producer(dto.name()));
    }

    public List<ProducerPrizesDTO> getProducersPrizes() {
        List<Object[]> results = producerRepository.findProducersWithMultiplePrizes();
        return results.stream()
                .map(result -> new ProducerPrizesDTO(
                        (String) result[0], // Nome do produtor
                        ((Number) result[1]).longValue(), // Número de prêmios
                        result[2].toString().split(", ") // Anos dos prêmios
                ))
                .toList();
    }

    public void cleanAllProducers() {
        this.producerRepository.deleteAll();
    }
}
