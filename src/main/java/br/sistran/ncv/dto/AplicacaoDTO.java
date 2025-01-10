package br.sistran.ncv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;
import java.util.List;

public class AplicacaoDTO {

    private Long id;
    private String nomeAplicacao;

    @JsonFormat(pattern = "dd/MM/yyyy")
    private LocalDate dataChegada;

    private String repositorio;
    private String ic;
    private String bsResponsavelNome;
    private String statusAplicacaoDescricao;
    private List<HistoricoDeMudancaDTO> historicoDeMudanca;
    private List<LancamentoHorasDTO> lancamentosHoras;
    private List<ApontamentoDTO> apontamentos;
    private Double horasTotais;

    // Construtor padrão
    public AplicacaoDTO() {
    }

    // Construtor completo
    public AplicacaoDTO(Long id, String nomeAplicacao, LocalDate dataChegada, String repositorio, String ic, String bsResponsavelNome,
                        String statusAplicacaoDescricao, List<HistoricoDeMudancaDTO> historicoDeMudanca,
                        List<LancamentoHorasDTO> lancamentosHoras, List<ApontamentoDTO> apontamentos, Double horasTotais) {
        this.id = id;
        this.nomeAplicacao = nomeAplicacao;
        this.dataChegada = dataChegada;
        this.repositorio = repositorio;
        this.ic = ic;
        this.bsResponsavelNome = bsResponsavelNome;
        this.statusAplicacaoDescricao = statusAplicacaoDescricao;
        this.historicoDeMudanca = historicoDeMudanca;
        this.lancamentosHoras = lancamentosHoras;
        this.apontamentos = apontamentos;
        this.horasTotais = horasTotais;
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

    public String getBsResponsavelNome() {
        return bsResponsavelNome;
    }

    public void setBsResponsavelNome(String bsResponsavelNome) {
        this.bsResponsavelNome = bsResponsavelNome;
    }

    public String getStatusAplicacaoDescricao() {
        return statusAplicacaoDescricao;
    }

    public void setStatusAplicacaoDescricao(String statusAplicacaoDescricao) {
        this.statusAplicacaoDescricao = statusAplicacaoDescricao;
    }

    public List<HistoricoDeMudancaDTO> getHistoricoDeMudanca() {
        return historicoDeMudanca;
    }

    public void setHistoricoDeMudanca(List<HistoricoDeMudancaDTO> historicoDeMudanca) {
        this.historicoDeMudanca = historicoDeMudanca;
    }

    public List<LancamentoHorasDTO> getLancamentosHoras() {
        return lancamentosHoras;
    }

    public void setLancamentosHoras(List<LancamentoHorasDTO> lancamentosHoras) {
        this.lancamentosHoras = lancamentosHoras;
    }

    public List<ApontamentoDTO> getApontamentos() {
        return apontamentos;
    }

    public void setApontamentos(List<ApontamentoDTO> apontamentos) {
        this.apontamentos = apontamentos;
    }

    public Double getHorasTotais() {
        return horasTotais;
    }

    public void setHorasTotais(Double horasTotais) {
        this.horasTotais = horasTotais;
    }
}
