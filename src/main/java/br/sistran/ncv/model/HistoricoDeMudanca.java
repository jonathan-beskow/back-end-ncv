package br.sistran.ncv.model;

import br.sistran.ncv.model.enums.StatusAplicacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonSetter;

import jakarta.persistence.Embeddable;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@Embeddable
public class HistoricoDeMudanca {

    private Integer status;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    public HistoricoDeMudanca() {
    }

    public HistoricoDeMudanca(Integer status, LocalDate data) {
        this.status = status;
        this.data = data;
    }

    @JsonIgnore // Não exibe o código no JSON
    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDate getData() {
        return data;
    }

    @JsonSetter("data")
    public void setData(String data) {
        DateTimeFormatter expectedFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter alternativeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter alternativeFormatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        try {
            this.data = LocalDate.parse(data, expectedFormatter);
        } catch (DateTimeParseException e1) {
            try {
                this.data = LocalDate.parse(data, alternativeFormatter1);
            } catch (DateTimeParseException e2) {
                try {
                    this.data = LocalDate.parse(data, alternativeFormatter2);
                } catch (DateTimeParseException e3) {
                    this.data = null; // ou LocalDate.now()
                }
            }
        }
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public String getStatusDescricao() {
        if (status != null) {
            return StatusAplicacao.toEnum(status).getDescricao();
        }
        return null;
    }
}
