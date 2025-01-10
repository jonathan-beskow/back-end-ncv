package br.sistran.ncv.dto;

import br.sistran.ncv.model.enums.TipoApontamento;

public class ApontamentoDTO {

    private Long id;
    private TipoApontamento tipo;
    private Integer quantidade;
    private String aplicacaoNome;

    // Construtor padrão
    public ApontamentoDTO() {
    }

    // Construtor completo
    public ApontamentoDTO(Long id, TipoApontamento tipo, Integer quantidade, String aplicacaoNome) {
        this.id = id;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.aplicacaoNome = aplicacaoNome;
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
}
