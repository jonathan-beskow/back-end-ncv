package br.sistran.ncv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class LancamentoHorasDTO {

    private Long id;
    private String desenvolvedor;
    private Double horas;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataLancamento;

    public LancamentoHorasDTO() {
    }

    public LancamentoHorasDTO(Long id, String desenvolvedor, Double horas, LocalDate dataLancamento) {
        this.id = id;
        this.desenvolvedor = desenvolvedor;
        this.horas = horas;
        this.dataLancamento = dataLancamento;
    }

    // Getters e Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDesenvolvedor() {
        return desenvolvedor;
    }

    public void setDesenvolvedor(String desenvolvedor) {
        this.desenvolvedor = desenvolvedor;
    }

    public Double getHoras() {
        return horas;
    }

    public void setHoras(Double horas) {
        this.horas = horas;
    }

    public LocalDate getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
}
