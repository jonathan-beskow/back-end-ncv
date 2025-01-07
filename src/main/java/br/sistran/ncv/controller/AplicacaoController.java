package br.sistran.ncv.controller;

import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.LancamentoHoras;
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
