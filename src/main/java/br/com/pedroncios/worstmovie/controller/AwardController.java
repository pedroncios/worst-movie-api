package br.com.pedroncios.worstmovie.controller;

import br.com.pedroncios.worstmovie.dto.AwardIntervalDTO;
import br.com.pedroncios.worstmovie.dto.IntervalDTO;
import br.com.pedroncios.worstmovie.dto.ProducerPrizesDTO;
import br.com.pedroncios.worstmovie.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/award")
public class AwardController {

    @Autowired
    private ProducerService producerService;

    @GetMapping
    @RequestMapping("/interval")
    public AwardIntervalDTO getAwardInterval() {
        IntervalDTO i1 = new IntervalDTO("Producer 1", 1, 2000, 2001);
        IntervalDTO i2 = new IntervalDTO("Producer 2", 1, 2010, 2011);
        IntervalDTO i3 = new IntervalDTO("Producer 1", 10, 2000, 2010);
        IntervalDTO i4 = new IntervalDTO("Producer 2", 10, 2010, 2020);

        return(new AwardIntervalDTO(Arrays.asList(i1, i2), Arrays.asList(i3, i4)));
    }

    @GetMapping
    @RequestMapping("/prizes")
    public List<ProducerPrizesDTO> getPrizes() {
        return producerService.getProducersPrizes();
    }
}
