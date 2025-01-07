package br.sistran.ncv.model;

import br.sistran.ncv.model.enums.StatusAplicacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Embeddable;

import java.time.LocalDate;

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

    public void setData(LocalDate data) {
        this.data = data;
    }

    // Exibe a descrição no JSON
    public String getStatusDescricao() {
        if (status != null) {
            return StatusAplicacao.toEnum(status).getDescricao();
        }
        return null;
    }

}