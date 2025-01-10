package br.sistran.ncv.mapper;

import br.sistran.ncv.dto.LancamentoHorasDTO;
import br.sistran.ncv.model.LancamentoHoras;

public class LancamentoHorasMapper {

    public static LancamentoHorasDTO toDTO(LancamentoHoras lancamentoHoras) {
        if (lancamentoHoras == null) {
            return null;
        }
        return new LancamentoHorasDTO(
                lancamentoHoras.getId(),
                lancamentoHoras.getDesenvolvedor(),
                lancamentoHoras.getHoras(),
                lancamentoHoras.getDataLancamento()
        );
    }

    public static LancamentoHoras toEntity(LancamentoHorasDTO dto) {
        if (dto == null) {
            return null;
        }
        LancamentoHoras lancamentoHoras = new LancamentoHoras();
        lancamentoHoras.setId(dto.getId());
        lancamentoHoras.setDesenvolvedor(dto.getDesenvolvedor());
        lancamentoHoras.setHoras(dto.getHoras());
        lancamentoHoras.setDataLancamento(dto.getDataLancamento());
        return lancamentoHoras;
    }
}
