package br.sistran.ncv.model;

import br.sistran.ncv.model.enums.BSResponsavel;
import br.sistran.ncv.model.enums.StatusAplicacao;
import br.sistran.ncv.model.enums.TipoApontamento;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonSetter;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Entity(name = "aplicacao")
public class Aplicacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomeAplicacao;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataChegada;

    private String repositorio;

    private String ic;

    @ElementCollection
    @CollectionTable(name = "historico_mudanca", joinColumns = @JoinColumn(name = "aplicacao_id"))
    private List<HistoricoDeMudanca> historicoDeMudanca = new ArrayList<>();

    private Integer bsResponsavelCodigo;

    private Integer statusAplicacaoCodigo;

    @Transient
    private String bsResponsavelNome;

    @Transient
    private String statusAplicacaoDescricao;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "aplicacao_id")
    private List<LancamentoHoras> lancamentosHoras = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "aplicacao_id") // Confirmação do relacionamento com a aplicação
    private List<Apontamento> apontamentos = new ArrayList<>();

    // Construtores
    public Aplicacao() {
        inicializarApontamentos();
    }

    public Aplicacao(Long id, String nomeAplicacao, LocalDate dataChegada, String repositorio, String ic,
                     Integer bsResponsavelCodigo, Integer statusAplicacaoCodigo, List<HistoricoDeMudanca> historicoDeMudanca) {
        this.id = id;
        this.nomeAplicacao = nomeAplicacao;
        this.dataChegada = dataChegada;
        this.repositorio = repositorio;
        this.ic = ic;
        this.bsResponsavelCodigo = bsResponsavelCodigo;
        this.statusAplicacaoCodigo = statusAplicacaoCodigo;
        this.bsResponsavelNome = BSResponsavel.toEnum(bsResponsavelCodigo).getNomeResponsavel();
        this.statusAplicacaoDescricao = StatusAplicacao.toEnum(statusAplicacaoCodigo).getDescricao();
        this.historicoDeMudanca = historicoDeMudanca;
        inicializarApontamentos(); // Garante que os apontamentos padrão sejam inicializados
    }

    private void inicializarApontamentos() {
        for (TipoApontamento tipo : TipoApontamento.values()) {
            this.apontamentos.add(new Apontamento(tipo, 0, this));
        }
    }

    public void adicionarOuAtualizarApontamento(TipoApontamento tipo, Integer quantidade) {
        Apontamento apontamentoExistente = this.apontamentos.stream()
                .filter(apontamento -> apontamento.getTipo().equals(tipo))
                .findFirst()
                .orElse(null);

        if (apontamentoExistente != null) {
            apontamentoExistente.setQuantidade(quantidade);
        } else {
            this.apontamentos.add(new Apontamento(tipo, quantidade, this));
        }
    }

    public int contarTotalApontamentos() {
        return apontamentos.stream()
                .mapToInt(Apontamento::getQuantidade)
                .sum();
    }

    public Map<TipoApontamento, Integer> contarApontamentosPorTipo() {
        return this.apontamentos.stream()
                .collect(Collectors.groupingBy(
                        Apontamento::getTipo,
                        Collectors.summingInt(Apontamento::getQuantidade)
                ));
    }

    public void adicionarHistorico(Integer status, LocalDate data) {
        historicoDeMudanca.add(new HistoricoDeMudanca(status, data));
    }

    public Double getHorasTotais() {
        return lancamentosHoras.stream()
                .mapToDouble(LancamentoHoras::getHoras)
                .sum();
    }

    public Map<String, Double> getHorasPorDesenvolvedor() {
        return lancamentosHoras.stream()
                .collect(Collectors.groupingBy(
                        LancamentoHoras::getDesenvolvedor,
                        Collectors.summingDouble(LancamentoHoras::getHoras)
                ));
    }

    public void adicionarHoras(String desenvolvedor, Double horas, LocalDate dataLancamento) {
        this.lancamentosHoras.add(new LancamentoHoras(desenvolvedor, horas, dataLancamento));
    }

    public String getStatusAplicacaoDescricao() {
        return Optional.ofNullable(statusAplicacaoCodigo)
                .map(StatusAplicacao::toEnum)
                .map(StatusAplicacao::getDescricao)
                .orElse("Status Desconhecido");
    }

    public void atualizarApontamento(Apontamento apontamentoAtualizado) {
        apontamentos = apontamentos.stream()
                .map(apontamento -> apontamento.getId().equals(apontamentoAtualizado.getId())
                        ? apontamentoAtualizado
                        : apontamento)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNomeAplicacao() {
        return nomeAplicacao;
    }

    public void setNomeAplicacao(String nomeAplicacao) {
        this.nomeAplicacao = nomeAplicacao;
    }

    public LocalDate getDataChegada() {
        return dataChegada;
    }

    @JsonSetter("dataChegada")
    public void setDataChegada(String dataChegada) {
        DateTimeFormatter expectedFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter alternativeFormatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter alternativeFormatter2 = DateTimeFormatter.ofPattern("MM/dd/yyyy");

        try {
            this.dataChegada = LocalDate.parse(dataChegada, expectedFormatter);
        } catch (DateTimeParseException e1) {
            try {
                this.dataChegada = LocalDate.parse(dataChegada, alternativeFormatter1);
            } catch (DateTimeParseException e2) {
                try {
                    this.dataChegada = LocalDate.parse(dataChegada, alternativeFormatter2);
                } catch (DateTimeParseException e3) {
                    this.dataChegada = null; // ou LocalDate.now()
                }
            }
        }
    }

    public void setDataChegada(LocalDate dataChegada) {
        this.dataChegada = dataChegada;
    }

    public String getRepositorio() {
        return repositorio;
    }

    public void setRepositorio(String repositorio) {
        this.repositorio = repositorio;
    }

    public String getIc() {
        return ic;
    }

    public void setIc(String ic) {
        this.ic = ic;
    }

    public Integer getBsResponsavelCodigo() {
        return bsResponsavelCodigo;
    }

    public void setBsResponsavelCodigo(Integer bsResponsavelCodigo) {
        this.bsResponsavelCodigo = bsResponsavelCodigo;
        this.bsResponsavelNome = BSResponsavel.toEnum(bsResponsavelCodigo).getNomeResponsavel();
    }

    public Integer getStatusAplicacaoCodigo() {
        return statusAplicacaoCodigo;
    }

    public void setStatusAplicacaoCodigo(Integer statusAplicacaoCodigo) {
        this.statusAplicacaoCodigo = statusAplicacaoCodigo;
        this.statusAplicacaoDescricao = StatusAplicacao.toEnum(statusAplicacaoCodigo).getDescricao();
    }

    public List<LancamentoHoras> getLancamentosHoras() {
        return lancamentosHoras;
    }

    public void setLancamentosHoras(List<LancamentoHoras> lancamentosHoras) {
        this.lancamentosHoras = lancamentosHoras;
    }

    public List<Apontamento> getApontamentos() {
        return apontamentos;
    }

    public void setApontamentos(List<Apontamento> apontamentos) {
        this.apontamentos = apontamentos;
    }

    public List<HistoricoDeMudanca> getHistoricoDeMudanca() {
        return historicoDeMudanca;
    }

    public void setHistoricoDeMudanca(List<HistoricoDeMudanca> historicoDeMudanca) {
        this.historicoDeMudanca = historicoDeMudanca;
    }
}
