package br.sistran.ncv.model;

import br.sistran.ncv.model.enums.BSResponsavel;
import br.sistran.ncv.model.enums.StatusAplicacao;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;

import java.time.LocalDate;
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

    private HistoricoDeMudanca emDesenvolvimento;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "aplicacao_id")
    private List<LancamentoHoras> lancamentosHoras = new ArrayList<>();

    public Aplicacao() {
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
        this.historicoDeMudanca = populaHistorico(historicoDeMudanca);
    }

    public Aplicacao(Long id, String nomeAplicacao, LocalDate dataChegada, String repositorio, String ic,
                     Integer bsResponsavelCodigo, Integer statusAplicacaoCodigo) {
        this.id = id;
        this.nomeAplicacao = nomeAplicacao;
        this.dataChegada = dataChegada;
        this.repositorio = repositorio;
        this.ic = ic;
        this.bsResponsavelCodigo = bsResponsavelCodigo;
        this.statusAplicacaoCodigo = statusAplicacaoCodigo;
        this.bsResponsavelNome = BSResponsavel.toEnum(bsResponsavelCodigo).getNomeResponsavel();
        this.statusAplicacaoDescricao = StatusAplicacao.toEnum(statusAplicacaoCodigo).getDescricao();
    }

    public Aplicacao(Long id, String nomeAplicacao) {
        this.id = id;
        this.nomeAplicacao = nomeAplicacao;
    }

    // Getters e Setters

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

    public String getBsResponsavelNome() {
        if (this.bsResponsavelCodigo != null) {
            return BSResponsavel.toEnum(this.bsResponsavelCodigo).getNomeResponsavel();
        }
        return null;
    }

    public Integer getStatusAplicacaoCodigo() {
        return statusAplicacaoCodigo;
    }

    public void setStatusAplicacaoCodigo(Integer statusAplicacaoCodigo) {
        this.statusAplicacaoCodigo = statusAplicacaoCodigo;
        this.statusAplicacaoDescricao = StatusAplicacao.toEnum(statusAplicacaoCodigo).getDescricao();
    }

    public String getStatusAplicacaoDescricao() {
        if (this.statusAplicacaoCodigo != null) {
            return StatusAplicacao.toEnum(this.statusAplicacaoCodigo).getDescricao();
        }
        return null;
    }

    public void setBsResponsavelNome(String bsResponsavelNome) {
        this.bsResponsavelNome = bsResponsavelNome;
    }

    public void setStatusAplicacaoDescricao(String statusAplicacaoDescricao) {
        this.statusAplicacaoDescricao = statusAplicacaoDescricao;
    }

    public List<HistoricoDeMudanca> getHistoricoDeMudanca() {
        return historicoDeMudanca;
    }

    public void setHistoricoDeMudanca(List<HistoricoDeMudanca> historicoDeMudanca) {
        this.historicoDeMudanca = historicoDeMudanca;
    }

    public void adicionarHistorico(Integer status, LocalDate data) {
        this.historicoDeMudanca.add(new HistoricoDeMudanca(status, data));
    }

    public List<HistoricoDeMudanca> populaHistorico(List<HistoricoDeMudanca> historicoDeMudanca) {
        if (historicoDeMudanca.isEmpty()) {
            HistoricoDeMudanca emDesenvolvimento = new HistoricoDeMudanca(0, LocalDate.now());
            historicoDeMudanca.add(emDesenvolvimento);
        }
        return historicoDeMudanca;
    }

    public HistoricoDeMudanca getEmDesenvolvimento() {
        return emDesenvolvimento;
    }

    public void setEmDesenvolvimento(HistoricoDeMudanca emDesenvolvimento) {
        this.emDesenvolvimento = emDesenvolvimento;
    }

    public List<LancamentoHoras> getLancamentosHoras() {
        return lancamentosHoras;
    }

    public void setLancamentosHoras(List<LancamentoHoras> lancamentosHoras) {
        this.lancamentosHoras = lancamentosHoras;
    }

    // Métodos para gerenciamento de horas

    public void adicionarHoras(String desenvolvedor, Double horas, LocalDate dataLancamento) {
        this.lancamentosHoras.add(new LancamentoHoras(desenvolvedor, horas, dataLancamento));
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

    public Map<String, Map<Double, List<LocalDate>>> getHorasDetalhadasPorDesenvolvedor() {
        return lancamentosHoras.stream()
                .collect(Collectors.groupingBy(
                        LancamentoHoras::getDesenvolvedor,
                        Collectors.groupingBy(
                                LancamentoHoras::getHoras,
                                Collectors.mapping(LancamentoHoras::getDataLancamento, Collectors.toList())
                        )
                ));
    }

}
