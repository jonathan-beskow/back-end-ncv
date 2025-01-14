package br.sistran.ncv.model;

import br.sistran.ncv.model.enums.TipoApontamento;
import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Objects;

@Entity
public class HistoricoApontamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private TipoApontamento tipo;

    private Integer quantidade;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aplicacao_id", nullable = false)
    @JsonBackReference
    private Aplicacao aplicacao;

    @Column(nullable = false)
    private LocalDate data;

    // Construtor padrão
    public HistoricoApontamento() {
        this.quantidade = 0; // Inicializa a quantidade como 0
    }

    // Construtor com todos os argumentos
    public HistoricoApontamento(TipoApontamento tipo, Integer quantidade, Aplicacao aplicacao, LocalDate data) {
        this.tipo = tipo;
        this.quantidade = (quantidade != null) ? quantidade : 0; // Garante que a quantidade seja inicializada como 0 se for nula
        this.aplicacao = aplicacao;
        this.data = data;
    }

    // Método para obter o ID da aplicação
    public Long getAplicacaoId() {
        return aplicacao != null ? aplicacao.getId() : null;
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

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HistoricoApontamento that = (HistoricoApontamento) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "HistoricoApontamento{" +
                "id=" + id +
                ", tipo=" + tipo +
                ", quantidade=" + quantidade +
                ", aplicacao=" + (aplicacao != null ? aplicacao.getNomeAplicacao() : "null") +
                ", data=" + data +
                '}';
    }
}
