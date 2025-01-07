package br.sistran.ncv.service;

import br.sistran.ncv.model.Aplicacao;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

    public byte[] gerarExcelAplicacoes(List<Aplicacao> aplicacoes) {
        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Aplicações");

            // Estilo para o cabeçalho
            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerStyle.setFont(headerFont);

            // Estilo de cores para os estados
            Map<String, CellStyle> stateStyles = criarEstilosParaEstados(workbook);

            // Cabeçalhos
            Row headerRow = sheet.createRow(0);
            Cell cell0 = headerRow.createCell(0);
            cell0.setCellValue("Nome da Aplicação");
            cell0.setCellStyle(headerStyle);

            Cell cell1 = headerRow.createCell(1);
            cell1.setCellValue("Estado Atual");
            cell1.setCellStyle(headerStyle);

            Cell cell2 = headerRow.createCell(2);
            cell2.setCellValue("Consumo de Horas");
            cell2.setCellStyle(headerStyle);

            // Preenchendo os dados
            int rowNum = 1;
            double totalHoras = 0.0;

            for (Aplicacao aplicacao : aplicacoes) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(aplicacao.getNomeAplicacao());

                // Estado com estilo de cor
                Cell estadoCell = row.createCell(1);
                estadoCell.setCellValue(aplicacao.getStatusAplicacaoDescricao());
                CellStyle estadoStyle = stateStyles.getOrDefault(
                        aplicacao.getStatusAplicacaoDescricao(),
                        stateStyles.get("DEFAULT")
                );
                estadoCell.setCellStyle(estadoStyle);

                double horasTotais = aplicacao.getHorasTotais() != null ? aplicacao.getHorasTotais() : 0.0;
                row.createCell(2).setCellValue(horasTotais);

                // Soma para métricas
                totalHoras += horasTotais;
            }

            // Adicionar métricas no final
            int metricsRowNum = rowNum + 1;
            Row metricsRow = sheet.createRow(metricsRowNum);
            metricsRow.createCell(0).setCellValue("Total de Aplicações:");
            metricsRow.createCell(1).setCellValue(aplicacoes.size());

            Row totalHorasRow = sheet.createRow(metricsRowNum + 1);
            totalHorasRow.createCell(0).setCellValue("Consumo Total de Horas:");
            totalHorasRow.createCell(1).setCellValue(totalHoras);

            // Ajustar largura das colunas
            sheet.autoSizeColumn(0);
            sheet.autoSizeColumn(1);
            sheet.autoSizeColumn(2);

            // Retornar Excel como bytes
            try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                workbook.write(outputStream);
                return outputStream.toByteArray();
            }
        } catch (Exception e) {
            throw new RuntimeException("Erro ao gerar Excel", e);
        }
    }

    private Map<String, CellStyle> criarEstilosParaEstados(Workbook workbook) {
        Map<String, CellStyle> styles = new HashMap<>();

        styles.put("Em Desenvolvimento", criarEstilo(workbook, IndexedColors.ORANGE));
        styles.put("Disponibilizada para testes", criarEstilo(workbook, IndexedColors.BLUE));
        styles.put("Em Homologação", criarEstilo(workbook, IndexedColors.VIOLET));
        styles.put("Em Implantação", criarEstilo(workbook, IndexedColors.BLACK));
        styles.put("Implantada", criarEstilo(workbook, IndexedColors.GREEN));
        styles.put("Impedimento", criarEstilo(workbook, IndexedColors.RED));
        styles.put("DEFAULT", criarEstilo(workbook, IndexedColors.GREY_50_PERCENT));

        return styles;
    }

    private CellStyle criarEstilo(Workbook workbook, IndexedColors color) {
        CellStyle style = workbook.createCellStyle();
        Font font = workbook.createFont();
        font.setColor(color.getIndex());
        style.setFont(font);
        return style;
    }
}
