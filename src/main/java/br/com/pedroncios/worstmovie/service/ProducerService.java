package br.com.pedroncios.worstmovie.service;

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
        return this.producerRepository.findProducersPrizes();
    }
}
