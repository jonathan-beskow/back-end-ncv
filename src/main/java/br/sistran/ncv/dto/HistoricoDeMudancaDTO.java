package br.sistran.ncv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class HistoricoDeMudancaDTO {

    private String statusDescricao;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    public HistoricoDeMudancaDTO() {
    }

    public HistoricoDeMudancaDTO(String statusDescricao, LocalDate data) {
        this.statusDescricao = statusDescricao;
        this.data = data;
    }

    public String getStatusDescricao() {
        return statusDescricao;
    }

    public void setStatusDescricao(String statusDescricao) {
        this.statusDescricao = statusDescricao;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
