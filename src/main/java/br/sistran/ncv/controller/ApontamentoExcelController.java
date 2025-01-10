package br.sistran.ncv.controller;

import br.sistran.ncv.dto.AplicacaoDTO;
import br.sistran.ncv.mapper.AplicacaoMapper;
import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.service.AplicacaoService;
import br.sistran.ncv.service.ApontamentoExcelService;
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
public class ApontamentoExcelController {

    @Autowired
    private AplicacaoService aplicacaoService;

    @Autowired
    private ApontamentoExcelService apontamentoExcelService;

    @GetMapping("/metricas")
    public ResponseEntity<byte[]> gerarRelatorioApontamentos() {
        List<AplicacaoDTO> aplicacoesDTO = aplicacaoService.findAll();
        List<Aplicacao> aplicacoes = aplicacoesDTO.stream()
                .map(AplicacaoMapper::toEntity)
                .toList();

        byte[] excelBytes = apontamentoExcelService.gerarExcelRelatorio(aplicacoes);

        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nomeArquivo = String.format("relatorio_apontamentos_%s.xlsx", dataAtual);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + nomeArquivo);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);
        
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }

}

