package br.com.pedroncios.worstmovie.service;

import br.com.pedroncios.worstmovie.dto.StudioDTO;
import br.com.pedroncios.worstmovie.entity.Studio;
import br.com.pedroncios.worstmovie.repository.StudioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudioService {

    @Autowired
    private StudioRepository studioRepository;

    public Optional<Studio> getStudioFromDTO(StudioDTO dto) {
        return this.studioRepository.findFirstByName(dto.name());
    }

    public Studio createStudio(StudioDTO dto) {
        return this.studioRepository.save(new Studio(dto.name()));
    }
}
