package br.sistran.ncv.dto;

import br.sistran.ncv.model.enums.TipoApontamento;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public class HistoricoApontamentoDTO {

    private Long id;
    private TipoApontamento tipo;
    private Integer quantidade;
    private String aplicacaoNome;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate data;

    // Construtor padrão
    public HistoricoApontamentoDTO() {
    }

    // Construtor completo
    public HistoricoApontamentoDTO(Long id, TipoApontamento tipo, Integer quantidade, String aplicacaoNome, LocalDate data) {
        this.id = id;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.aplicacaoNome = aplicacaoNome;
        this.data = data;
    }

    // Getters e Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TipoApontamento getTipo() {
        return tipo;
    }

    public void setTipo(TipoApontamento tipo) {
        this.tipo = tipo;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public String getAplicacaoNome() {
        return aplicacaoNome;
    }

    public void setAplicacaoNome(String aplicacaoNome) {
        this.aplicacaoNome = aplicacaoNome;
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }
}
