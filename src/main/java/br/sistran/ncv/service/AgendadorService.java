package br.sistran.ncv.service;

import br.sistran.ncv.model.HistoricoApontamento;
import br.sistran.ncv.model.enums.TipoApontamento;
import br.sistran.ncv.repository.HistoricoApontamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AgendadorService {

    @Autowired
    private ApontamentoService apontamentoService;

    @Autowired
    private HistoricoApontamentoRepository historicoApontamentoRepository;

    @Scheduled(cron = "0 0 0 * * MON") // Segunda-feira às 00:00
    public void capturarSegunda() {
        apontamentoService.salvarSnapshot(LocalDate.now());
    }


    public Map<String, Integer> compararApontamentos(LocalDate segunda, LocalDate sexta) {
        // Obtém os apontamentos de segunda e sexta-feira
        List<HistoricoApontamento> segundaApontamentos = historicoApontamentoRepository.findByData(segunda);
        List<HistoricoApontamento> sextaApontamentos = historicoApontamentoRepository.findByData(sexta);

        // Mapeia os apontamentos de segunda-feira
        Map<Long, Map<TipoApontamento, Integer>> segundaMap = segundaApontamentos.stream()
                .collect(Collectors.groupingBy(
                        HistoricoApontamento::getAplicacaoId,
                        Collectors.toMap(
                                HistoricoApontamento::getTipo,
                                HistoricoApontamento::getQuantidade,
                                Integer::sum // Combina valores duplicados (se existirem)
                        )
                ));

        // Mapeia os apontamentos de sexta-feira
        Map<Long, Map<TipoApontamento, Integer>> sextaMap = sextaApontamentos.stream()
                .collect(Collectors.groupingBy(
                        HistoricoApontamento::getAplicacaoId,
                        Collectors.toMap(
                                HistoricoApontamento::getTipo,
                                HistoricoApontamento::getQuantidade,
                                Integer::sum // Combina valores duplicados (se existirem)
                        )
                ));

        // Comparação de diferenças
        Map<String, Integer> diferencas = new HashMap<>();
        sextaMap.forEach((aplicacaoId, sextaNiveis) -> {
            Map<TipoApontamento, Integer> segundaNiveis = segundaMap.getOrDefault(aplicacaoId, new HashMap<>());
            sextaNiveis.forEach((nivel, quantidadeSexta) -> {
                int quantidadeSegunda = segundaNiveis.getOrDefault(nivel, 0);
                diferencas.put(
                        "Aplicacao " + aplicacaoId + " - " + nivel,
                        quantidadeSexta - quantidadeSegunda
                );
            });
        });

        return diferencas;
    }

}
