package br.com.pedroncios.worstmovie.dto;

public record IntervalDTO(String producer,
                          Integer interval,
                          Integer previousWin,
                          Integer followingWin) {
}
