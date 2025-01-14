package br.sistran.ncv.service;

import br.sistran.ncv.dto.AplicacaoDTO;
import br.sistran.ncv.dto.ApontamentoDTO;
import br.sistran.ncv.model.Apontamento;
import br.sistran.ncv.model.HistoricoApontamento;
import br.sistran.ncv.model.enums.TipoApontamento;
import br.sistran.ncv.repository.ApontamentoRepository;
import br.sistran.ncv.repository.HistoricoApontamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ApontamentoService {

    @Autowired
    private HistoricoApontamentoRepository historicoApontamentoRepository;

    @Autowired
    private ApontamentoRepository apontamentoRepository;

    public void salvarSnapshot(LocalDate data) {
        List<Apontamento> apontamentos = apontamentoRepository.findAll();
        List<HistoricoApontamento> historicoApontamentos = apontamentos.stream()
                .map(apontamento -> new HistoricoApontamento(
                        apontamento.getTipo(), // Tipo do apontamento
                        apontamento.getQuantidade(), // Quantidade do apontamento
                        apontamento.getAplicacao(), // Aplicação associada
                        data // Data do snapshot
                ))
                .collect(Collectors.toList());
        historicoApontamentoRepository.saveAll(historicoApontamentos);
    }


    public long calcularSegundos(Double horasTotais, Integer quantidade) {
        if (horasTotais == null || quantidade == null || quantidade == 0) {
            return 0;
        }
        return Math.round((horasTotais * 3600) / quantidade);
    }


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

    public List<ApontamentoDTO> identificarApontamentosMaisDemorados(List<AplicacaoDTO> aplicacoesDTO) {
        return aplicacoesDTO.stream()
                .flatMap(aplicacaoDTO -> aplicacaoDTO.getApontamentos().stream())
                .sorted((a1, a2) -> Long.compare(
                        calcularSegundos(a2.getQuantidade() != null ? a2.getQuantidade().doubleValue() : 0.0, a2.getQuantidade()),
                        calcularSegundos(a1.getQuantidade() != null ? a1.getQuantidade().doubleValue() : 0.0, a1.getQuantidade())
                ))
                .collect(Collectors.toList());
    }






}
