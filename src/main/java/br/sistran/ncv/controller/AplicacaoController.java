package br.sistran.ncv.controller;

import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.Apontamento;
import br.sistran.ncv.model.LancamentoHoras;
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

    // Contar apontamentos por tipo
    @GetMapping("/{id}/apontamentos/tipos")
    public ResponseEntity<Map<TipoApontamento, Integer>> contarApontamentosPorTipo(@PathVariable Long id) {
        Map<TipoApontamento, Integer> apontamentosPorTipo = aplicacaoService.contarApontamentosPorTipo(id);
        return ResponseEntity.ok(apontamentosPorTipo);
    }

    // Adicionar apontamento
    @PostMapping("/{id}/apontamentos")
    public ResponseEntity<Void> adicionarApontamento(
            @PathVariable Long id,
            @RequestBody Apontamento apontamento
    ) {
        aplicacaoService.adicionarApontamento(id, apontamento.getTipo(), apontamento.getQuantidade());
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{idAplicacao}/apontamentos")
    public ResponseEntity<Map<String, Integer>> atualizarApontamento(
            @PathVariable Long idAplicacao,
            @RequestBody Map<String, Object> body) {

        // Validar se os campos 'id' e 'quantidade' estão presentes no corpo da requisição
        if (!body.containsKey("id") || !body.containsKey("quantidade")) {
            return ResponseEntity.badRequest().body(Map.of("erro", -1)); // Retorna um valor padrão inválido
        }

        try {
            // Obter os valores do corpo da requisição
            Long idApontamento = Long.valueOf(body.get("id").toString());
            Integer novaQuantidade = Integer.valueOf(body.get("quantidade").toString());

            // Chamar o método de atualização no serviço
            Map<String, Integer> apontamentosAtualizados = aplicacaoService.atualizarApontamentoPorId(idAplicacao, idApontamento, novaQuantidade);

            // Retornar os apontamentos atualizados
            return ResponseEntity.ok(apontamentosAtualizados);

        } catch (Exception e) {
            // Retornar erro com valor padrão
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
        System.out.println("chegou no find by id");
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
