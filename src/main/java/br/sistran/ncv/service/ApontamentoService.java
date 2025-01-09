package br.sistran.ncv.service;

import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.Apontamento;
import br.sistran.ncv.model.enums.TipoApontamento;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ApontamentoService {

    /**
     * Calcula os segundos gastos por apontamento resolvido para cada aplicação.
     *
     * @param aplicacoes Lista de aplicações.
     * @return Mapa contendo os apontamentos e o tempo médio de resolução em segundos.
     */
    public Map<Aplicacao, Map<TipoApontamento, Long>> calcularSegundosPorApontamento(List<Aplicacao> aplicacoes) {
        Map<Aplicacao, Map<TipoApontamento, Long>> resultado = new HashMap<>();

        for (Aplicacao aplicacao : aplicacoes) {
            Map<TipoApontamento, Long> temposPorApontamento = aplicacao.getApontamentos().stream()
                    .collect(Collectors.toMap(
                            Apontamento::getTipo,
                            apontamento -> calcularSegundos(aplicacao.getHorasTotais(), apontamento.getQuantidade())
                    ));
            resultado.put(aplicacao, temposPorApontamento);
        }

        return resultado;
    }

    /**
     * Calcula a média de segundos gastos por tipo de apontamento em todas as aplicações.
     *
     * @param aplicacoes Lista de aplicações.
     * @return Mapa com o tipo de apontamento e a média de segundos por apontamento.
     */
    public Map<TipoApontamento, Double> calcularMediaSegundosPorTipo(List<Aplicacao> aplicacoes) {
        Map<TipoApontamento, List<Long>> temposPorTipo = new HashMap<>();

        for (Aplicacao aplicacao : aplicacoes) {
            for (Apontamento apontamento : aplicacao.getApontamentos()) {
                long segundos = calcularSegundos(aplicacao.getHorasTotais(), apontamento.getQuantidade());
                temposPorTipo.computeIfAbsent(apontamento.getTipo(), k -> new ArrayList<>()).add(segundos);
            }
        }

        return temposPorTipo.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().stream().mapToLong(Long::longValue).average().orElse(0.0)
                ));
    }

    /**
     * Identifica os apontamentos mais demorados em termos de tempo de resolução.
     *
     * @param aplicacoes Lista de aplicações.
     * @return Lista de apontamentos ordenados pelo maior tempo de resolução.
     */
    public List<Apontamento> identificarApontamentosMaisDemorados(List<Aplicacao> aplicacoes) {
        return aplicacoes.stream()
                .flatMap(aplicacao -> aplicacao.getApontamentos().stream())
                .sorted((a1, a2) -> Long.compare(
                        calcularSegundos(a2.getAplicacao().getHorasTotais(), a2.getQuantidade()),
                        calcularSegundos(a1.getAplicacao().getHorasTotais(), a1.getQuantidade())
                ))
                .collect(Collectors.toList());
    }

    /**
     * Calcula os segundos gastos para um apontamento.
     *
     * @param horasTotais Total de horas da aplicação.
     * @param quantidade  Quantidade de apontamentos.
     * @return Segundos por apontamento.
     */
    public long calcularSegundos(Double horasTotais, Integer quantidade) {
        if (horasTotais == null || quantidade == null || quantidade == 0) {
            return 0;
        }
        return Math.round((horasTotais * 3600) / quantidade);
    }
}
