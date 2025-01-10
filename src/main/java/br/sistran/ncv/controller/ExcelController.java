package br.sistran.ncv.controller;

import br.sistran.ncv.dto.AplicacaoDTO;
import br.sistran.ncv.mapper.AplicacaoMapper;
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

    @GetMapping("/estado-atual")
    public ResponseEntity<byte[]> gerarExcelAplicacoes() {
        // Recupera a lista de AplicacaoDTOs
        List<AplicacaoDTO> aplicacoesDTO = aplicacaoService.findAll();

        // Converte os DTOs para entidades Aplicacao
        List<Aplicacao> aplicacoes = aplicacoesDTO.stream()
                .map(AplicacaoMapper::toEntity)
                .toList();

        // Gera o arquivo Excel
        byte[] excelBytes = excelService.gerarExcelAplicacoes(aplicacoes);

        // Configura o nome do arquivo e os cabeçalhos HTTP
        String dataAtual = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String nomeArquivo = String.format("status_aplicacoes_%s.xlsx", dataAtual);

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + nomeArquivo);
        headers.setContentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM);

        // Retorna o arquivo Excel gerado
        return new ResponseEntity<>(excelBytes, headers, HttpStatus.OK);
    }


}
