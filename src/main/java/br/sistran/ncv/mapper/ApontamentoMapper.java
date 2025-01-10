package br.sistran.ncv.mapper;

import br.sistran.ncv.dto.ApontamentoDTO;
import br.sistran.ncv.model.Apontamento;

public class ApontamentoMapper {

    public static ApontamentoDTO toDTO(Apontamento apontamento) {
        if (apontamento == null) {
            return null;
        }
        return new ApontamentoDTO(
                apontamento.getId(),
                apontamento.getTipo(),
                apontamento.getQuantidade(),
                apontamento.getAplicacao() != null ? apontamento.getAplicacao().getNomeAplicacao() : null
        );
    }

    public static Apontamento toEntity(ApontamentoDTO dto) {
        if (dto == null) {
            return null;
        }
        Apontamento apontamento = new Apontamento();
        apontamento.setId(dto.getId());
        apontamento.setTipo(dto.getTipo());
        apontamento.setQuantidade(dto.getQuantidade());
        // O campo `aplicacao` precisa ser configurado manualmente, pois `Aplicacao` é uma entidade complexa.
        return apontamento;
    }
}
