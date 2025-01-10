package br.sistran.ncv.mapper;

import br.sistran.ncv.dto.*;
import br.sistran.ncv.model.Aplicacao;

import java.util.stream.Collectors;

public class AplicacaoMapper {

    public static AplicacaoDTO toDTO(Aplicacao aplicacao) {
        if (aplicacao == null) {
            return null;
        }
        return new AplicacaoDTO(
                aplicacao.getId(),
                aplicacao.getNomeAplicacao(),
                aplicacao.getDataChegada(),
                aplicacao.getRepositorio(),
                aplicacao.getIc(),
                aplicacao.getBsResponsavelNome(),
                aplicacao.getStatusAplicacaoDescricao(),
                aplicacao.getHistoricoDeMudanca().stream()
                        .map(HistoricoDeMudancaMapper::toDTO)
                        .collect(Collectors.toList()),
                aplicacao.getLancamentosHoras().stream()
                        .map(LancamentoHorasMapper::toDTO)
                        .collect(Collectors.toList()),
                aplicacao.getApontamentos().stream()
                        .map(ApontamentoMapper::toDTO)
                        .collect(Collectors.toList()),
                aplicacao.getHorasTotais()
        );
    }

    public static Aplicacao toEntity(AplicacaoDTO dto) {
        if (dto == null) {
            return null;
        }
        Aplicacao aplicacao = new Aplicacao();
        aplicacao.setId(dto.getId());
        aplicacao.setNomeAplicacao(dto.getNomeAplicacao());
        aplicacao.setDataChegada(dto.getDataChegada());
        aplicacao.setRepositorio(dto.getRepositorio());
        aplicacao.setIc(dto.getIc());
        // Os campos `bsResponsavelCodigo` e `statusAplicacaoCodigo` precisam ser mapeados a partir de suas descrições.
        aplicacao.setHistoricoDeMudanca(dto.getHistoricoDeMudanca().stream()
                .map(HistoricoDeMudancaMapper::toEntity)
                .collect(Collectors.toList()));
        aplicacao.setLancamentosHoras(dto.getLancamentosHoras().stream()
                .map(LancamentoHorasMapper::toEntity)
                .collect(Collectors.toList()));
        aplicacao.setApontamentos(dto.getApontamentos().stream()
                .map(ApontamentoMapper::toEntity)
                .collect(Collectors.toList()));
        return aplicacao;
    }
}
