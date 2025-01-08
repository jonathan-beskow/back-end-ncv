package br.sistran.ncv.service;

import br.sistran.ncv.exception.ObjectNotFoundException;
import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.Apontamento;
import br.sistran.ncv.model.HistoricoDeMudanca;
import br.sistran.ncv.model.LancamentoHoras;
import br.sistran.ncv.model.enums.TipoApontamento;
import br.sistran.ncv.repository.AplicacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AplicacaoService {

    @Autowired
    private AplicacaoRepository aplicacaoRepository;

    @Transactional
    public void adicionarHoras(Long id, LancamentoHoras lancamentoHoras) {
        Aplicacao aplicacao = buscarAplicacaoPorId(id);
        aplicacao.adicionarHoras(
                lancamentoHoras.getDesenvolvedor(),
                lancamentoHoras.getHoras(),
                lancamentoHoras.getDataLancamento()
        );
        aplicacaoRepository.save(aplicacao);
    }

    public Double getHorasTotais(Long id) {
        Aplicacao aplicacao = buscarAplicacaoPorId(id);
        return aplicacao.getHorasTotais();
    }

    public Map<String, Double> getHorasPorDesenvolvedor(Long id) {
        Aplicacao aplicacao = buscarAplicacaoPorId(id);
        return aplicacao.getHorasPorDesenvolvedor();
    }

    public Map<String, Map<Double, List<LocalDate>>> getHorasDetalhadasPorDesenvolvedor(Long id) {
        Aplicacao aplicacao = buscarAplicacaoPorId(id);
        return aplicacao.getLancamentosHoras().stream()
                .collect(Collectors.groupingBy(
                        LancamentoHoras::getDesenvolvedor,
                        Collectors.groupingBy(
                                LancamentoHoras::getHoras,
                                Collectors.mapping(LancamentoHoras::getDataLancamento, Collectors.toList())
                        )
                ));
    }

    public Aplicacao findById(Long id) {
        return buscarAplicacaoPorId(id);
    }

    public List<Aplicacao> findAll() {
        return aplicacaoRepository.findAll();
    }

    @Transactional
    public Aplicacao create(Aplicacao aplicacao) {
        HistoricoDeMudanca emDesenvolvimento = new HistoricoDeMudanca(0, LocalDate.now());
        aplicacao.adicionarHistorico(emDesenvolvimento.getStatus(), emDesenvolvimento.getData());
        aplicacao.setId(null);
        return aplicacaoRepository.save(aplicacao);
    }

    @Transactional
    public Aplicacao update(Long id, Aplicacao aplicacao) {
        Aplicacao newObj = buscarAplicacaoPorId(id);

        if (!Objects.equals(newObj.getStatusAplicacaoCodigo(), aplicacao.getStatusAplicacaoCodigo())) {
            newObj.adicionarHistorico(aplicacao.getStatusAplicacaoCodigo(), LocalDate.now());
        }

        newObj.setNomeAplicacao(aplicacao.getNomeAplicacao());
        newObj.setIc(aplicacao.getIc());
        newObj.setRepositorio(aplicacao.getRepositorio());
        newObj.setDataChegada(populaDate(aplicacao));
        newObj.setStatusAplicacaoCodigo(aplicacao.getStatusAplicacaoCodigo());
        newObj.setBsResponsavelCodigo(aplicacao.getBsResponsavelCodigo());

        return aplicacaoRepository.saveAndFlush(newObj);
    }

    @Transactional
    public void delete(Long id) {
        Aplicacao aplicacao = buscarAplicacaoPorId(id);
        aplicacaoRepository.delete(aplicacao);
    }

    public int contarTotalApontamentos(Long idAplicacao) {
        Aplicacao aplicacao = buscarAplicacaoPorId(idAplicacao);
        return aplicacao.contarTotalApontamentos();
    }

    public Map<TipoApontamento, Integer> contarApontamentosPorTipo(Long idAplicacao) {
        Aplicacao aplicacao = buscarAplicacaoPorId(idAplicacao);

        // Filtra os apontamentos apenas da aplicação atual e agrupa por tipo
        return aplicacao.getApontamentos().stream()
                .collect(Collectors.groupingBy(
                        Apontamento::getTipo,
                        Collectors.summingInt(Apontamento::getQuantidade)
                ));
    }

    @Transactional
    public void adicionarApontamento(Long idAplicacao, TipoApontamento tipo, Integer quantidade) {
        Aplicacao aplicacao = buscarAplicacaoPorId(idAplicacao);
        aplicacao.adicionarOuAtualizarApontamento(tipo, quantidade);
        aplicacaoRepository.save(aplicacao);
    }


    @Transactional
    public void removerApontamento(Long idAplicacao, Long idApontamento) {
        Aplicacao aplicacao = buscarAplicacaoPorId(idAplicacao);
        boolean removed = aplicacao.getApontamentos().removeIf(a -> a.getId().equals(idApontamento));
        if (!removed) {
            throw new IllegalArgumentException("Apontamento não encontrado.");
        }
        aplicacaoRepository.save(aplicacao);
    }

    @Transactional
    public Map<String, Integer> atualizarApontamentoPorId(Long idAplicacao, Long idApontamento, Integer novaQuantidade) {
        Aplicacao aplicacao = buscarAplicacaoPorId(idAplicacao);

        // Buscar o apontamento pelo ID
        Apontamento apontamentoExistente = aplicacao.getApontamentos().stream()
                .filter(apontamento -> apontamento.getId().equals(idApontamento))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Apontamento com ID " + idApontamento + " não encontrado."));

        // Atualizar a quantidade do apontamento
        apontamentoExistente.setQuantidade(novaQuantidade);

        // Salvar a aplicação com os apontamentos atualizados
        aplicacaoRepository.saveAndFlush(aplicacao);

        // Recalcular os apontamentos agrupados por tipo
        Map<TipoApontamento, Integer> apontamentosPorTipo = aplicacao.getApontamentos().stream()
                .collect(Collectors.groupingBy(
                        Apontamento::getTipo,
                        Collectors.summingInt(Apontamento::getQuantidade)
                ));

        // Organizar o resultado na ordem desejada
        Map<String, Integer> apontamentosOrdenados = new LinkedHashMap<>();
        apontamentosOrdenados.put("CRITICO", apontamentosPorTipo.getOrDefault(TipoApontamento.CRITICO, 0));
        apontamentosOrdenados.put("ALTO", apontamentosPorTipo.getOrDefault(TipoApontamento.ALTO, 0));
        apontamentosOrdenados.put("MEDIO", apontamentosPorTipo.getOrDefault(TipoApontamento.MEDIO, 0));
        apontamentosOrdenados.put("BAIXO", apontamentosPorTipo.getOrDefault(TipoApontamento.BAIXO, 0));

        // Retornar os apontamentos atualizados
        return apontamentosOrdenados;
    }


    private Aplicacao buscarAplicacaoPorId(Long id) {
        return aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Aplicação não encontrada!"));
    }

    public static LocalDate populaDate(Aplicacao aplicacao) {
        return Optional.ofNullable(aplicacao.getDataChegada()).orElse(LocalDate.now());
    }

    public static Integer populaStatus(Aplicacao aplicacao) {
        return Optional.ofNullable(aplicacao.getStatusAplicacaoCodigo()).orElse(0);
    }

    public Map<String, Double> relacionarHorasPorApontamentosResolvidos(Long id) {
        Aplicacao aplicacao = buscarAplicacaoPorId(id);

        double totalHorasConsumidas = aplicacao.getHorasTotais();
        int totalApontamentosResolvidos = aplicacao.getApontamentos().stream()
                .mapToInt(Apontamento::getQuantidade)
                .sum();

        double horasPorApontamento = totalApontamentosResolvidos > 0
                ? totalHorasConsumidas / totalApontamentosResolvidos
                : 0.0;

        Map<String, Double> relacao = new HashMap<>();
        relacao.put("TotalHorasConsumidas", totalHorasConsumidas);
        relacao.put("TotalApontamentosResolvidos", (double) totalApontamentosResolvidos);
        relacao.put("HorasPorApontamento", horasPorApontamento);

        return relacao;
    }

}
