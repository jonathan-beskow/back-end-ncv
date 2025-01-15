package br.sistran.ncv.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class DateParser {

    /**
     * Realiza o parsing de uma string de data em diferentes formatos suportados.
     *
     * @param dateString A string representando a data a ser parseada.
     * @return Uma instância de LocalDate correspondente à data parseada.
     * @throws IllegalArgumentException Se nenhum formato válido for encontrado.
     */
    public static LocalDate parseDate(String dateString) {
        // Lista de formatos aceitos
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("MM-dd-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
        );

        // Itera pelos formatos e tenta realizar o parsing
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {
                // Continua para o próximo formato se falhar
            }
        }

        // Se nenhum formato for válido, lança uma exceção
        throw new IllegalArgumentException("Formato de data inválido: " + dateString);
    }
}
