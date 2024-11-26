package br.com.pedroncios.worstmovie.dto;

import java.util.List;

public record AwardIntervalDTO(List<IntervalDTO> min, List<IntervalDTO> max) {
}
