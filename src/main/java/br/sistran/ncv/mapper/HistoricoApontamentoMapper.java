package br.sistran.ncv.mapper;

import br.sistran.ncv.dto.HistoricoApontamentoDTO;
import br.sistran.ncv.model.HistoricoApontamento;

public class HistoricoApontamentoMapper {

    // Método para converter de Entidade para DTO
    public static HistoricoApontamentoDTO toDTO(HistoricoApontamento historicoApontamento) {
        if (historicoApontamento == null) {
            return null;
        }
        return new HistoricoApontamentoDTO(
                historicoApontamento.getId(),
                historicoApontamento.getTipo(),
                historicoApontamento.getQuantidade(),
                historicoApontamento.getAplicacao() != null ? historicoApontamento.getAplicacao().getNomeAplicacao() : null,
                historicoApontamento.getData()
        );
    }

    public static HistoricoApontamento toEntity(HistoricoApontamentoDTO dto) {
        if (dto == null) {
            return null;
        }
        HistoricoApontamento historicoApontamento = new HistoricoApontamento();
        historicoApontamento.setId(dto.getId());
        historicoApontamento.setTipo(dto.getTipo());
        historicoApontamento.setQuantidade(dto.getQuantidade());
        // O campo `aplicacao` deve ser configurado manualmente, pois não é diretamente manipulável via DTO.
        historicoApontamento.setData(dto.getData());
        return historicoApontamento;
    }
}
