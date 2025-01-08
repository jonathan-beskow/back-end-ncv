package br.sistran.ncv.model;

import br.sistran.ncv.model.enums.TipoApontamento;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Apontamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoApontamento tipo;

    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aplicacao_id", nullable = false)
    private Aplicacao aplicacao;

    // Construtor padrão: Inicializa quantidade como 0
    public Apontamento() {
        this.quantidade = 0; // Inicializa a quantidade como 0
    }

    // Construtor com todos os argumentos
    public Apontamento(TipoApontamento tipo, Integer quantidade, Aplicacao aplicacao) {
        this.tipo = tipo;
        this.quantidade = (quantidade != null) ? quantidade : 0; // Garante que a quantidade seja inicializada como 0 se for nula
        this.aplicacao = aplicacao;
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

    public Aplicacao getAplicacao() {
        return aplicacao;
    }

    public void setAplicacao(Aplicacao aplicacao) {
        this.aplicacao = aplicacao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apontamento that = (Apontamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Apontamento{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", quantidade=" + quantidade +
                ", aplicacao=" + (aplicacao != null ? aplicacao.getNomeAplicacao() : "null") +
                '}';
    }




}
