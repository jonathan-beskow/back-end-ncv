package br.sistran.ncv.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Entity
public class LancamentoHoras {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String desenvolvedor;
    private Double horas;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataLancamento;

    public LancamentoHoras() {
    }

    public LancamentoHoras(String desenvolvedor, Double horas, LocalDate dataLancamento) {
        this.desenvolvedor = desenvolvedor;
        this.horas = horas;
        this.dataLancamento = dataLancamento;
    }

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

    // Método para definir a data com validação
    @JsonSetter("dataLancamento")
    public void setDataLancamento(String dataLancamento) {
        DateTimeFormatter expectedFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter alternativeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter alternativeFormatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        try {
            // Tenta o formato esperado primeiro
            this.dataLancamento = LocalDate.parse(dataLancamento, expectedFormatter);
        } catch (DateTimeParseException e1) {
            try {
                // Tenta outro formato (ISO-8601)
                this.dataLancamento = LocalDate.parse(dataLancamento, alternativeFormatter1);
            } catch (DateTimeParseException e2) {
                try {
                    // Tenta mais um formato alternativo
                    this.dataLancamento = LocalDate.parse(dataLancamento, alternativeFormatter2);
                } catch (DateTimeParseException e3) {
                    // Define uma data padrão ou lança uma exceção personalizada
                    this.dataLancamento = null; // ou LocalDate.now()
                }
            }
        }
    }

    public void setDataLancamento(LocalDate dataLancamento) {
        this.dataLancamento = dataLancamento;
    }
}
