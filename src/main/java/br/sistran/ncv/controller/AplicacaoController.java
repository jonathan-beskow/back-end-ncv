package br.sistran.ncv.controller;

import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.LancamentoHoras;
import br.sistran.ncv.model.enums.TipoApontamento;
import br.sistran.ncv.service.AplicacaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    // Contar apontamentos por tipo
    @GetMapping("/{id}/apontamentos/tipos")
    public ResponseEntity<Map<TipoApontamento, Integer>> contarApontamentosPorTipo(@PathVariable Long id) {
        Map<TipoApontamento, Integer> apontamentosPorTipo = aplicacaoService.contarApontamentosPorTipo(id);
        return ResponseEntity.ok(apontamentosPorTipo);
    }

    // Adicionar apontamento
//    @PostMapping("/{id}/apontamentos")
//    public ResponseEntity<Void> adicionarApontamento(
//            @PathVariable Long id,
//            @RequestBody Map<String, Object> apontamentoData
//    ) {
//        System.out.println("Chegou no add apontamentos");
//        Long idApontamento = Long.valueOf(apontamentoData.get("id").toString());
//        Integer quantidade = Integer.valueOf(apontamentoData.get("quantidade").toString());
//        aplicacaoService.adicionarApontamento(id, quantidade);
//        return ResponseEntity.noContent().build();
//    }

    @PostMapping("/{id}/apontamentos")
    public ResponseEntity<String> adicionarApontamento(
            @PathVariable("id") Long idAplicacao,
            @RequestBody Map<String, Object> requestBody) {
        try {
            // Extrair valores do corpo da requisição
            Integer tipoId = (Integer) requestBody.get("id");
            Integer quantidade = (Integer) requestBody.get("quantidade");

            // Converter o ID do tipo para TipoApontamento
            TipoApontamento tipoApontamento = converterTipoApontamento(tipoId);

            // Chamar o serviço para adicionar o apontamento
            aplicacaoService.adicionarApontamento(idAplicacao, tipoApontamento, quantidade);

            return ResponseEntity.ok("Apontamento adicionado com sucesso.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Tipo de apontamento inválido: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro ao adicionar apontamento: " + e.getMessage());
        }
    }

    private TipoApontamento converterTipoApontamento(Integer tipoId) {
        return switch (tipoId) {
            case 1 -> TipoApontamento.CRITICO;
            case 2 -> TipoApontamento.ALTO;
            case 3 -> TipoApontamento.MEDIO;
            case 4 -> TipoApontamento.BAIXO;
            default -> throw new IllegalArgumentException("ID do tipo inválido: " + tipoId);
        };
    }


    @PutMapping("/{idAplicacao}/apontamentos")
    public ResponseEntity<Map<String, Integer>> atualizarApontamento(
            @PathVariable Long idAplicacao,
            @RequestBody Map<String, Object> body) {
        if (!body.containsKey("id") || !body.containsKey("quantidade")) {
            return ResponseEntity.badRequest().body(Map.of("erro", -1)); // Retorna um valor padrão inválido
        }
        try {
            Long idApontamento = Long.valueOf(body.get("id").toString());
            Integer novaQuantidade = Integer.valueOf(body.get("quantidade").toString());
            Map<String, Integer> apontamentosAtualizados = aplicacaoService.atualizarApontamentoPorId(idAplicacao, idApontamento, novaQuantidade);
            return ResponseEntity.ok(apontamentosAtualizados);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", -1));
        }
    }


    // Remover apontamento
    @DeleteMapping("/{id}/apontamentos/{apontamentoId}")
    public ResponseEntity<Void> removerApontamento(
            @PathVariable Long id,
            @PathVariable Long apontamentoId
    ) {
        aplicacaoService.removerApontamento(id, apontamentoId);
        return ResponseEntity.noContent().build();
    }

    // Relacionar horas por apontamentos resolvidos
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
    public ResponseEntity<Void> adicionarHoras(@PathVariable Long id, @RequestBody LancamentoHoras lancamentoHoras) {
        aplicacaoService.adicionarHoras(id, lancamentoHoras);
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
    public ResponseEntity<List<Aplicacao>> findAll() {
        var obj = aplicacaoService.findAll();
        return ResponseEntity.ok(obj);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Aplicacao> findById(@PathVariable Long id) {
        var obj = aplicacaoService.findById(id);
        return ResponseEntity.ok(obj);
    }


    @PostMapping("/create")
    public ResponseEntity<Aplicacao> create(@Valid @RequestBody Aplicacao aplicacao) {
        Aplicacao newObj = aplicacaoService.create(aplicacao);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newObj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Aplicacao> update(@PathVariable Long id, @Valid @RequestBody Aplicacao aplicacao) {
        var newObj = aplicacaoService.update(id, aplicacao);
        return ResponseEntity.ok(newObj);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Aplicacao> deleteById(@PathVariable Long id) {
        aplicacaoService.delete(id);
        return ResponseEntity.ok().build();
    }

}
