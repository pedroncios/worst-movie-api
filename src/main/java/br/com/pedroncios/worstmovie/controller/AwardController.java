package br.com.pedroncios.worstmovie.controller;

import br.com.pedroncios.worstmovie.dto.AwardIntervalDTO;
import br.com.pedroncios.worstmovie.service.AwardService;
import br.com.pedroncios.worstmovie.service.ProducerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/awards")
public class AwardController {

    @Autowired
    private ProducerService producerService;
    @Autowired
    private AwardService awardService;

    @GetMapping
    @RequestMapping("/intervals")
    public AwardIntervalDTO getAwardInterval() {
        return awardService.getAwardIntervals();
    }
}
