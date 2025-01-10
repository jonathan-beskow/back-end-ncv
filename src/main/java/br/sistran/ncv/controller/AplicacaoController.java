package br.sistran.ncv.controller;

import br.sistran.ncv.dto.AplicacaoDTO;
import br.sistran.ncv.dto.ApontamentoDTO;
import br.sistran.ncv.dto.LancamentoHorasDTO;
import br.sistran.ncv.model.enums.TipoApontamento;
import br.sistran.ncv.service.AplicacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
public class AplicacaoController {

    @Autowired
    private AplicacaoService aplicacaoService;

    @GetMapping("/{id}/apontamentos/total")
    public ResponseEntity<Integer> contarTotalApontamentos(@PathVariable Long id) {
        int totalApontamentos = aplicacaoService.contarTotalApontamentos(id);
        return ResponseEntity.ok(totalApontamentos);
    }

    @GetMapping("/{id}/apontamentos/tipos")
    public ResponseEntity<Map<TipoApontamento, Integer>> contarApontamentosPorTipo(@PathVariable Long id) {
        Map<TipoApontamento, Integer> apontamentosPorTipo = aplicacaoService.contarApontamentosPorTipo(id);
        return ResponseEntity.ok(apontamentosPorTipo);
    }

    @PostMapping("/{id}/apontamentos")
    public ResponseEntity<String> adicionarApontamento(
            @PathVariable("id") Long idAplicacao,
            @RequestBody ApontamentoDTO apontamentoDTO) {
        aplicacaoService.adicionarApontamento(idAplicacao, apontamentoDTO);
        return ResponseEntity.ok("Apontamento adicionado com sucesso.");
    }

    @PutMapping("/{idAplicacao}/apontamentos")
    public ResponseEntity<Map<String, Integer>> atualizarApontamento(
            @PathVariable Long idAplicacao,
            @RequestBody Map<String, Object> body) {
        if (!body.containsKey("id") || !body.containsKey("quantidade")) {
            return ResponseEntity.badRequest().body(Map.of("erro", -1));
        }
        Long idApontamento = Long.valueOf(body.get("id").toString());
        Integer novaQuantidade = Integer.valueOf(body.get("quantidade").toString());
        Map<String, Integer> apontamentosAtualizados = aplicacaoService.atualizarApontamentoPorId(idAplicacao, idApontamento, novaQuantidade);
        return ResponseEntity.ok(apontamentosAtualizados);
    }

    @DeleteMapping("/{id}/apontamentos/{apontamentoId}")
    public ResponseEntity<Void> removerApontamento(
            @PathVariable Long id,
            @PathVariable Long apontamentoId) {
        aplicacaoService.removerApontamento(id, apontamentoId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/relacao/horas-apontamentos")
    public ResponseEntity<Map<String, Double>> relacionarHorasPorApontamentosResolvidos(@PathVariable Long id) {
        Map<String, Double> relacao = aplicacaoService.relacionarHorasPorApontamentosResolvidos(id);
        return ResponseEntity.ok(relacao);
    }

    @GetMapping("/{id}/horas/detalhadas")
    public ResponseEntity<Map<String, Map<Double, List<LocalDate>>>> getHorasDetalhadasPorDesenvolvedor(@PathVariable Long id) {
        Map<String, Map<Double, List<LocalDate>>> detalhes = aplicacaoService.getHorasDetalhadasPorDesenvolvedor(id);
        return ResponseEntity.ok(detalhes);
    }


    @PostMapping("/{id}/horas")
    public ResponseEntity<Void> adicionarHoras(@PathVariable Long id, @RequestBody LancamentoHorasDTO lancamentoHorasDTO) {
        aplicacaoService.adicionarHoras(id, lancamentoHorasDTO);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/horas/totais")
    public ResponseEntity<Double> getHorasTotais(@PathVariable Long id) {
        Double horasTotais = aplicacaoService.getHorasTotais(id);
        return ResponseEntity.ok(horasTotais);
    }

    @GetMapping("/{id}/horas/desenvolvedores")
    public ResponseEntity<Map<String, Double>> getHorasPorDesenvolvedor(@PathVariable Long id) {
        Map<String, Double> horasPorDesenvolvedor = aplicacaoService.getHorasPorDesenvolvedor(id);
        return ResponseEntity.ok(horasPorDesenvolvedor);
    }

    @GetMapping
    public ResponseEntity<List<AplicacaoDTO>> findAll() {
        List<AplicacaoDTO> obj = aplicacaoService.findAll();
        return ResponseEntity.ok(obj);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AplicacaoDTO> findById(@PathVariable Long id) {
        AplicacaoDTO obj = aplicacaoService.findById(id);
        return ResponseEntity.ok(obj);
    }

    @PostMapping("/create")
    public ResponseEntity<Void> create(@Valid @RequestBody AplicacaoDTO aplicacaoDTO) {
        AplicacaoDTO newObj = aplicacaoService.create(aplicacaoDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<AplicacaoDTO> update(@PathVariable Long id, @Valid @RequestBody AplicacaoDTO aplicacaoDTO) {
        AplicacaoDTO newObj = aplicacaoService.update(id, aplicacaoDTO);
        return ResponseEntity.ok(newObj);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        aplicacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

