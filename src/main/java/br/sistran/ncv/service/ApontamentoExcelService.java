package br.sistran.ncv.service;

import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.Apontamento;
import br.sistran.ncv.model.enums.TipoApontamento;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@Service
public class ApontamentoExcelService {

    private final ApontamentoService apontamentoService;

    public ApontamentoExcelService(ApontamentoService apontamentoService) {
        this.apontamentoService = apontamentoService;
    }

    public byte[] gerarExcelRelatorio(List<Aplicacao> aplicacoes) {
        try (Workbook workbook = new XSSFWorkbook()) {
            criarAbaSegundosPorApontamento(workbook, aplicacoes);

            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar Excel", e);
        }
    }

    private void criarAbaSegundosPorApontamento(Workbook workbook, List<Aplicacao> aplicacoes) {
        Sheet sheet = workbook.createSheet("Segundos por Apontamento");
        int rowNum = 0;

        // Estilo para o cabeçalho
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerStyle.setFont(headerFont);
        headerStyle.setAlignment(HorizontalAlignment.CENTER);

        // Estilo para minutos (duas casas decimais)
        CellStyle decimalStyle = workbook.createCellStyle();
        decimalStyle.setDataFormat(workbook.createDataFormat().getFormat("0.00"));

        // Cabeçalho
        Row headerRow = sheet.createRow(rowNum++);
        criarCelula(headerRow, 0, "Aplicação", headerStyle);
        criarCelula(headerRow, 1, "Tipo de Apontamento", headerStyle);
        criarCelula(headerRow, 2, "Segundos", headerStyle);
        criarCelula(headerRow, 3, "Minutos", headerStyle);

        for (Aplicacao aplicacao : aplicacoes) {
            // Ordenar os apontamentos: CRÍTICO, ALTO, MÉDIO, BAIXO
            List<Apontamento> apontamentosOrdenados = aplicacao.getApontamentos();
            apontamentosOrdenados.sort(Comparator.comparingInt(apontamento -> apontamento.getTipo().ordinal()));

            double totalSegundos = 0;

            for (Apontamento apontamento : apontamentosOrdenados) {
                long segundos = apontamentoService.calcularSegundos(
                        aplicacao.getHorasTotais(),
                        apontamento.getQuantidade()
                );
                double minutos = segundos / 60.0;

                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(aplicacao.getNomeAplicacao());
                row.createCell(1).setCellValue(apontamento.getTipo().name());
                row.createCell(2).setCellValue(segundos);

                Cell minutosCell = row.createCell(3);
                minutosCell.setCellValue(minutos);
                minutosCell.setCellStyle(decimalStyle); // Aplica o estilo com duas casas decimais

                totalSegundos += segundos;
            }

            // Adicionar o total por aplicação ao final
            double totalHoras = totalSegundos / 3600.0;
            Row totalRow = sheet.createRow(rowNum++);
            totalRow.createCell(0).setCellValue("Total para " + aplicacao.getNomeAplicacao());
            totalRow.createCell(2).setCellValue(totalSegundos);

            Cell totalHorasCell = totalRow.createCell(3);
            totalHorasCell.setCellValue(totalHoras);
            totalHorasCell.setCellStyle(decimalStyle); // Aplica o estilo com duas casas decimais
        }

        // Ajustar largura das colunas
        for (int i = 0; i <= 3; i++) {
            sheet.autoSizeColumn(i);
        }
    }


    private void criarCelula(Row row, int column, String value, CellStyle style) {
        Cell cell = row.createCell(column);
        cell.setCellValue(value);
        cell.setCellStyle(style);
    }
}
