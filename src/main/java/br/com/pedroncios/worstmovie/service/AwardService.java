package br.com.pedroncios.worstmovie.service;

import br.com.pedroncios.worstmovie.dto.AwardIntervalDTO;
import br.com.pedroncios.worstmovie.dto.IntervalDTO;
import br.com.pedroncios.worstmovie.dto.ProducerPrizesDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class AwardService {

    @Autowired
    ProducerService producerService;

    public AwardIntervalDTO getAwardIntervals() {
        List<ProducerPrizesDTO> producerPrizes = producerService.getProducersPrizes();

        List<IntervalDTO> maxIntervals = new ArrayList<>();
        List<IntervalDTO> minIntervals = new ArrayList<>();

        if (producerPrizes.isEmpty()) {
            return new AwardIntervalDTO(minIntervals, maxIntervals);
        }

        int maxInterval = 0;
        int minInterval = Integer.MAX_VALUE;

        for (ProducerPrizesDTO producerPrize : producerPrizes) {

            List<Integer> years = Arrays.stream(producerPrize.awardYears())
                    .map(Integer::parseInt)
                    .sorted()
                    .toList();

            for (int i=1; i<producerPrize.totalAwards(); i++) {
                int interval = years.get(i) - years.get(i - 1);

                if (interval > 0) {
                    if (interval > maxInterval) {
                        maxInterval = interval;
                        maxIntervals.clear();
                        maxIntervals.add(new IntervalDTO(producerPrize.name(), interval, years.get(i - 1), years.get(i)));
                    } else if (interval == maxInterval) {
                        maxIntervals.add(new IntervalDTO(producerPrize.name(), interval, years.get(i - 1), years.get(i)));
                    }

                    if (interval < minInterval) {
                        minInterval = interval;
                        minIntervals.clear();
                        minIntervals.add(new IntervalDTO(producerPrize.name(), interval, years.get(i - 1), years.get(i)));
                    } else if (interval == minInterval) {
                        minIntervals.add(new IntervalDTO(producerPrize.name(), interval, years.get(i - 1), years.get(i)));
                    }
                }
            }
        }

        return new AwardIntervalDTO(minIntervals, maxIntervals);
    }
}
