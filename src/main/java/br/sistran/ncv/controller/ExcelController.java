package br.sistran.ncv.controller;

import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.service.AplicacaoService;
import br.sistran.ncv.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@RequestMapping("/resources")
public class ExcelController {

    @Autowired
    private AplicacaoService aplicacaoService;

    @Autowired
    private ExcelService excelService;

//    @GetMapping("/estado-atual")
//    public ResponseEntity<byte[]> gerarExcelAplicacoes() {
//        // Obtém todas as aplicações
//        List<Aplicacao> aplicacoes = aplicacaoService.findAll();
//
//        // Gera o Excel em bytes
//        byte[] excelBytes = excelService.gerarExcelAplicacoes(aplicacoes);
//
//        // Formata a data atual para incluir no nome do arquivo
//        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
//        String nomeArquivo = String.format("status_aplicacoes_%s.xlsx", dataAtual);
//
//        // Define os headers da resposta
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Content-Disposition", "attachment; filename=" + nomeArquivo);
//
//        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
//    }

    @GetMapping("/estado-atual")
    public ResponseEntity<byte[]> gerarExcelAplicacoes() {
        List<Aplicacao> aplicacoes = aplicacaoService.findAll();

        byte[] excelBytes = excelService.gerarExcelAplicacoes(aplicacoes);

        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nomeArquivo = String.format("status_aplicacoes_%s.xlsx", dataAtual);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + nomeArquivo);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);

        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }



}
