package br.sistran.ncv.service;

import br.sistran.ncv.dto.AplicacaoDTO;
import br.sistran.ncv.dto.ApontamentoDTO;
import br.sistran.ncv.model.enums.TipoApontamento;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApontamentoService {

    /**
     * Calcula os segundos gastos por apontamento resolvido para cada aplicação.
     *
     * @param aplicacoesDTO Lista de aplicações (DTO).
     * @return Mapa contendo os apontamentos e o tempo médio de resolução em segundos.
     */
    public Map<AplicacaoDTO, Map<TipoApontamento, Long>> calcularSegundosPorApontamento(List<AplicacaoDTO> aplicacoesDTO) {
        Map<AplicacaoDTO, Map<TipoApontamento, Long>> resultado = new HashMap<>();

        for (AplicacaoDTO aplicacaoDTO : aplicacoesDTO) {
            Map<TipoApontamento, Long> temposPorApontamento = aplicacaoDTO.getApontamentos().stream()
                    .collect(Collectors.toMap(
                            ApontamentoDTO::getTipo,
                            apontamento -> calcularSegundos(aplicacaoDTO.getHorasTotais(), apontamento.getQuantidade())
                    ));
            resultado.put(aplicacaoDTO, temposPorApontamento);
        }

        return resultado;
    }

    /**
     * Calcula a média de segundos gastos por tipo de apontamento em todas as aplicações.
     *
     * @param aplicacoesDTO Lista de aplicações (DTO).
     * @return Mapa com o tipo de apontamento e a média de segundos por apontamento.
     */
    public Map<TipoApontamento, Double> calcularMediaSegundosPorTipo(List<AplicacaoDTO> aplicacoesDTO) {
        Map<TipoApontamento, List<Long>> temposPorTipo = new HashMap<>();

        for (AplicacaoDTO aplicacaoDTO : aplicacoesDTO) {
            for (ApontamentoDTO apontamentoDTO : aplicacaoDTO.getApontamentos()) {
                long segundos = calcularSegundos(aplicacaoDTO.getHorasTotais(), apontamentoDTO.getQuantidade());
                temposPorTipo.computeIfAbsent(apontamentoDTO.getTipo(), k -> new ArrayList<>()).add(segundos);
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
     * @param aplicacoesDTO Lista de aplicações (DTO).
     * @return Lista de apontamentos (DTO) ordenados pelo maior tempo de resolução.
     */
    public List<ApontamentoDTO> identificarApontamentosMaisDemorados(List<AplicacaoDTO> aplicacoesDTO) {
        return aplicacoesDTO.stream()
                .flatMap(aplicacaoDTO -> aplicacaoDTO.getApontamentos().stream())
                .sorted((a1, a2) -> Long.compare(
                        calcularSegundos(a2.getQuantidade() != null ? a2.getQuantidade().doubleValue() : 0.0, a2.getQuantidade()),
                        calcularSegundos(a1.getQuantidade() != null ? a1.getQuantidade().doubleValue() : 0.0, a1.getQuantidade())
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
