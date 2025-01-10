package br.sistran.ncv.mapper;

import br.sistran.ncv.dto.HistoricoDeMudancaDTO;
import br.sistran.ncv.model.HistoricoDeMudanca;

public class HistoricoDeMudancaMapper {

    public static HistoricoDeMudancaDTO toDTO(HistoricoDeMudanca historico) {
        if (historico == null) {
            return null;
        }
        return new HistoricoDeMudancaDTO(
                historico.getStatusDescricao(),
                historico.getData()
        );
    }

    public static HistoricoDeMudanca toEntity(HistoricoDeMudancaDTO dto) {
        if (dto == null) {
            return null;
        }
        HistoricoDeMudanca historico = new HistoricoDeMudanca();
        historico.setData(dto.getData());
        return historico;
    }
}
