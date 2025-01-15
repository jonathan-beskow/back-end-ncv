package br.sistran.ncv.service;

import br.sistran.ncv.dto.AplicacaoDTO;
import br.sistran.ncv.dto.ApontamentoDTO;
import br.sistran.ncv.dto.LancamentoHorasDTO;
import br.sistran.ncv.exception.ObjectNotFoundException;
import br.sistran.ncv.mapper.AplicacaoMapper;
import br.sistran.ncv.mapper.LancamentoHorasMapper;
import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.Apontamento;
import br.sistran.ncv.model.HistoricoDeMudanca;
import br.sistran.ncv.model.LancamentoHoras;
import br.sistran.ncv.model.enums.TipoApontamento;
import br.sistran.ncv.repository.AplicacaoRepository;
import br.sistran.ncv.utils.DateParser;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class AplicacaoService {

    @Autowired
    private AplicacaoRepository aplicacaoRepository;


    @Transactional
    public void adicionarHorasNaAplicacao(Long idAplicacao, LancamentoHorasDTO lancamentoHorasDTO) {
        // Validar e converter a data do lançamento
        LocalDate dataLancamento = DateParser.parseDate(lancamentoHorasDTO.getDataLancamento().toString());
        lancamentoHorasDTO.setDataLancamento(dataLancamento);

        // Buscar a aplicação pelo ID
        Aplicacao aplicacao = aplicacaoRepository.findById(idAplicacao)
                .orElseThrow(() -> new ObjectNotFoundException("Aplicação não encontrada com ID: " + idAplicacao));

        // Validar os dados do DTO
        if (lancamentoHorasDTO.getHoras() == null || lancamentoHorasDTO.getHoras() <= 0) {
            throw new IllegalArgumentException("Horas devem ser um valor positivo.");
        }
        if (lancamentoHorasDTO.getDesenvolvedor() == null || lancamentoHorasDTO.getDesenvolvedor().isEmpty()) {
            throw new IllegalArgumentException("O desenvolvedor não pode ser nulo ou vazio.");
        }

        // Criar uma nova entidade de LancamentoHoras
        LancamentoHoras lancamentoHoras = new LancamentoHoras(
                lancamentoHorasDTO.getDesenvolvedor(),
                lancamentoHorasDTO.getHoras(),
                dataLancamento
        );

        // Adicionar o lançamento de horas à aplicação
        aplicacao.getLancamentosHoras().add(lancamentoHoras);

        // Persistir as alterações na aplicação
        aplicacaoRepository.saveAndFlush(aplicacao);
    }






    @Transactional
    public void adicionarHoras(Long id, LancamentoHorasDTO lancamentoHorasDTO) {
        // Valida e converte a data
        LocalDate dataLancamento = validarEConverterData(lancamentoHorasDTO.getDataLancamento().toString());
        lancamentoHorasDTO.setDataLancamento(dataLancamento); // Atualiza o DTO com a data válida

        // Recupera a AplicacaoDTO e converte para entidade
        AplicacaoDTO aplicacaoDTO = buscarAplicacaoPorId(id);
        Aplicacao aplicacao = AplicacaoMapper.toEntity(aplicacaoDTO);

        // Converte o DTO para entidade
        LancamentoHoras lancamentoHoras = LancamentoHorasMapper.toEntity(lancamentoHorasDTO);

        // Adiciona as horas à aplicação
        aplicacao.adicionarHoras(
                lancamentoHoras.getDesenvolvedor(),
                lancamentoHoras.getHoras(),
                dataLancamento
        );

        // Salva a aplicação atualizada
        aplicacaoRepository.save(aplicacao);
    }


    public Double getHorasTotais(Long id) {
        // Recupera o AplicacaoDTO diretamente
        AplicacaoDTO aplicacaoDTO = buscarAplicacaoPorId(id);

        // Retorna o total de horas diretamente do DTO
        return aplicacaoDTO.getHorasTotais();
    }


    public Map<String, Double> getHorasPorDesenvolvedor(Long id) {
        // Obtém o AplicacaoDTO diretamente
        AplicacaoDTO aplicacaoDTO = buscarAplicacaoPorId(id);

        // Calcula as horas por desenvolvedor utilizando o DTO
        return aplicacaoDTO.getLancamentosHoras().stream()
                .collect(Collectors.groupingBy(
                        LancamentoHorasDTO::getDesenvolvedor,
                        Collectors.summingDouble(LancamentoHorasDTO::getHoras)
                ));
    }


    public Map<String, Map<Double, List<LocalDate>>> getHorasDetalhadasPorDesenvolvedor(Long id) {
        // Recupera o AplicacaoDTO diretamente
        AplicacaoDTO aplicacaoDTO = buscarAplicacaoPorId(id);

        // Processa os detalhes das horas utilizando o DTO
        return aplicacaoDTO.getLancamentosHoras().stream()
                .collect(Collectors.groupingBy(
                        LancamentoHorasDTO::getDesenvolvedor,
                        Collectors.groupingBy(
                                LancamentoHorasDTO::getHoras,
                                Collectors.mapping(LancamentoHorasDTO::getDataLancamento, Collectors.toList())
                        )
                ));
    }


    public AplicacaoDTO findById(Long id) {
        // Recupera o AplicacaoDTO diretamente
        return buscarAplicacaoPorId(id);
    }


    public List<AplicacaoDTO> findAll() {
        List<Aplicacao> aplicacoes = aplicacaoRepository.findAll();
        return aplicacoes.stream()
                .map(AplicacaoMapper::toDTO) // Converte cada entidade para DTO
                .collect(Collectors.toList());
    }


    @Transactional
    public AplicacaoDTO create(AplicacaoDTO aplicacaoDTO) {
        Aplicacao aplicacao = AplicacaoMapper.toEntity(aplicacaoDTO);

        HistoricoDeMudanca emDesenvolvimento = new HistoricoDeMudanca(0, LocalDate.now());
        aplicacao.adicionarHistorico(emDesenvolvimento.getStatus(), emDesenvolvimento.getData());
        aplicacao.setId(null);
        Aplicacao savedAplicacao = aplicacaoRepository.save(aplicacao);
        return AplicacaoMapper.toDTO(savedAplicacao);
    }


    @Transactional
    public AplicacaoDTO update(Long id, AplicacaoDTO aplicacaoDTO) {
        // Recupera a entidade Aplicacao pelo ID diretamente do repositório
        Aplicacao newObj = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Aplicação não encontrada!"));

        // Converte o DTO recebido para a entidade
        Aplicacao aplicacao = AplicacaoMapper.toEntity(aplicacaoDTO);

        // Atualiza o histórico de mudanças se o status foi alterado
        if (!Objects.equals(newObj.getStatusAplicacaoCodigo(), aplicacao.getStatusAplicacaoCodigo())) {
            newObj.adicionarHistorico(aplicacao.getStatusAplicacaoCodigo(), LocalDate.now());
        }

        // Atualiza os campos da entidade
        newObj.setNomeAplicacao(aplicacao.getNomeAplicacao());
        newObj.setIc(aplicacao.getIc());
        newObj.setRepositorio(aplicacao.getRepositorio());
        newObj.setDataChegada(populaDate(aplicacao));
        newObj.setStatusAplicacaoCodigo(aplicacao.getStatusAplicacaoCodigo());
        newObj.setBsResponsavelCodigo(aplicacao.getBsResponsavelCodigo());

        // Salva as alterações no banco de dados
        Aplicacao updatedAplicacao = aplicacaoRepository.saveAndFlush(newObj);

        // Converte a entidade atualizada de volta para DTO e retorna
        return AplicacaoMapper.toDTO(updatedAplicacao);
    }


    @Transactional
    public AplicacaoDTO delete(Long id) {
        // Recupera a entidade Aplicacao pelo ID diretamente do repositório
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Aplicação não encontrada!"));

        // Converte a entidade para DTO antes de deletar
        AplicacaoDTO aplicacaoDTO = AplicacaoMapper.toDTO(aplicacao);

        // Realiza a exclusão no repositório
        aplicacaoRepository.delete(aplicacao);

        // Retorna o DTO da aplicação excluída
        return aplicacaoDTO;
    }


    public int contarTotalApontamentos(Long idAplicacao) {
        AplicacaoDTO aplicacaoDTO = buscarAplicacaoPorId(idAplicacao);
        return aplicacaoDTO.getApontamentos().stream()
                .mapToInt(ApontamentoDTO::getQuantidade)
                .sum();
    }


    public Map<TipoApontamento, Integer> contarApontamentosPorTipo(Long idAplicacao) {
        AplicacaoDTO aplicacaoDTO = buscarAplicacaoPorId(idAplicacao);
        return aplicacaoDTO.getApontamentos().stream()
                .collect(Collectors.groupingBy(
                        ApontamentoDTO::getTipo,
                        Collectors.summingInt(ApontamentoDTO::getQuantidade)
                ));
    }


//    @Transactional
//    public void adicionarApontamento(Long idAplicacao, ApontamentoDTO apontamentoDTO) {
//        // Buscar a aplicação pelo ID
//        Aplicacao aplicacao = aplicacaoRepository.findById(idAplicacao)
//                .orElseThrow(() -> new IllegalArgumentException("Aplicação não encontrada com ID: " + idAplicacao));
//
//        // Converte o DTO para a entidade
//        Apontamento novoApontamento = ApontamentoMapper.toEntity(apontamentoDTO);
//
//        // Verifica se já existe um apontamento do mesmo tipo
//        Apontamento apontamentoExistente = aplicacao.getApontamentos().stream()
//                .filter(apontamento -> apontamento.getTipo().equals(novoApontamento.getTipo()))
//                .findFirst()
//                .orElse(null);
//
//        if (apontamentoExistente != null) {
//            // Atualiza a quantidade existente
//            apontamentoExistente.setQuantidade(apontamentoExistente.getQuantidade() + novoApontamento.getQuantidade());
//        } else {
//            // Adiciona um novo apontamento se não existir
//            novoApontamento.setAplicacao(aplicacao); // Relaciona o novo apontamento à aplicação
//            aplicacao.getApontamentos().add(novoApontamento);
//        }
//
//        // Salvar a aplicação com os apontamentos atualizados
//        aplicacaoRepository.save(aplicacao);
//    }

    @Transactional
    public void adicionarApontamento(Long idAplicacao, ApontamentoDTO apontamentoDTO) {
        // Valida se o ID do tipo foi fornecido
        if (apontamentoDTO.getId() == null) {
            throw new IllegalArgumentException("O ID do tipo do apontamento não pode ser nulo.");
        }

        // Valida se a quantidade foi fornecida
        int quantidade = (apontamentoDTO.getQuantidade() != null) ? apontamentoDTO.getQuantidade() : 0;


        // Mapeia o ID para o enum TipoApontamento
        TipoApontamento tipoApontamento = mapIdToTipoApontamento(apontamentoDTO.getId().intValue());
        if (tipoApontamento == null) {
            throw new IllegalArgumentException("ID inválido para tipo de apontamento: " + apontamentoDTO.getId());
        }

        // Buscar a aplicação pelo ID
        Aplicacao aplicacao = aplicacaoRepository.findById(idAplicacao)
                .orElseThrow(() -> new IllegalArgumentException("Aplicação não encontrada com ID: " + idAplicacao));

        // Busca o apontamento existente pelo tipo
        Apontamento apontamentoExistente = aplicacao.getApontamentos().stream()
                .filter(apontamento -> apontamento.getTipo().equals(tipoApontamento))
                .findFirst()
                .orElse(null);

        if (apontamentoExistente != null) {
            // Atualiza a quantidade do apontamento existente
            apontamentoExistente.setQuantidade(apontamentoExistente.getQuantidade() + quantidade);
        } else {
            // Cria um novo apontamento e o adiciona à aplicação
            Apontamento novoApontamento = new Apontamento();
            novoApontamento.setTipo(tipoApontamento);
            novoApontamento.setQuantidade(quantidade);
            novoApontamento.setAplicacao(aplicacao);

            // Adiciona o novo apontamento à lista de apontamentos da aplicação
            aplicacao.getApontamentos().add(novoApontamento);
        }

        // Salva a aplicação com os apontamentos atualizados
        aplicacaoRepository.saveAndFlush(aplicacao);
    }


    @Transactional
    public void removerApontamento(Long idAplicacao, Long idApontamento) {
        // Recupera a entidade Aplicacao
        Aplicacao aplicacao = aplicacaoRepository.findById(idAplicacao)
                .orElseThrow(() -> new IllegalArgumentException("Aplicação não encontrada com ID: " + idAplicacao));

        // Remove o apontamento pelo ID
        boolean removed = aplicacao.getApontamentos().removeIf(apontamento -> apontamento.getId().equals(idApontamento));
        if (!removed) {
            throw new IllegalArgumentException("Apontamento não encontrado com ID: " + idApontamento);
        }

        // Salva a aplicação atualizada
        aplicacaoRepository.save(aplicacao);
    }


    @Transactional
    public Map<String, Integer> atualizarApontamentoPorId(Long idAplicacao, Long idApontamento, Integer novaQuantidade) {
        // Recupera a entidade Aplicacao
        Aplicacao aplicacao = aplicacaoRepository.findById(idAplicacao)
                .orElseThrow(() -> new IllegalArgumentException("Aplicação não encontrada com ID: " + idAplicacao));

        // Busca o apontamento pelo ID
        Apontamento apontamentoExistente = aplicacao.getApontamentos().stream()
                .filter(apontamento -> apontamento.getId().equals(idApontamento))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Apontamento com ID " + idApontamento + " não encontrado."));

        // Atualiza a quantidade do apontamento
        apontamentoExistente.setQuantidade(novaQuantidade);

        // Salva as alterações no banco
        aplicacaoRepository.saveAndFlush(aplicacao);

        // Recalcula os apontamentos agrupados por tipo utilizando DTOs
        AplicacaoDTO aplicacaoDTO = AplicacaoMapper.toDTO(aplicacao);

        Map<TipoApontamento, Integer> apontamentosPorTipo = aplicacaoDTO.getApontamentos().stream()
                .collect(Collectors.groupingBy(
                        ApontamentoDTO::getTipo,
                        Collectors.summingInt(ApontamentoDTO::getQuantidade)
                ));

        // Organiza o resultado na ordem desejada
        Map<String, Integer> apontamentosOrdenados = new LinkedHashMap<>();
        apontamentosOrdenados.put("CRITICO", apontamentosPorTipo.getOrDefault(TipoApontamento.CRITICO, 0));
        apontamentosOrdenados.put("ALTO", apontamentosPorTipo.getOrDefault(TipoApontamento.ALTO, 0));
        apontamentosOrdenados.put("MEDIO", apontamentosPorTipo.getOrDefault(TipoApontamento.MEDIO, 0));
        apontamentosOrdenados.put("BAIXO", apontamentosPorTipo.getOrDefault(TipoApontamento.BAIXO, 0));

        return apontamentosOrdenados;
    }


    public Map<String, Double> relacionarHorasPorApontamentosResolvidos(Long id) {
        // Busca a entidade e converte para DTO
        AplicacaoDTO aplicacaoDTO = buscarAplicacaoPorId(id);

        // Obtém os valores necessários a partir do DTO
        double totalHorasConsumidas = aplicacaoDTO.getHorasTotais();
        int totalApontamentosResolvidos = aplicacaoDTO.getApontamentos().stream()
                .mapToInt(ApontamentoDTO::getQuantidade)
                .sum();

        // Calcula as horas por apontamento
        double horasPorApontamento = totalApontamentosResolvidos > 0
                ? totalHorasConsumidas / totalApontamentosResolvidos
                : 0.0;

        // Cria o mapa de retorno
        Map<String, Double> relacao = new HashMap<>();
        relacao.put("TotalHorasConsumidas", totalHorasConsumidas);
        relacao.put("TotalApontamentosResolvidos", (double) totalApontamentosResolvidos);
        relacao.put("HorasPorApontamento", horasPorApontamento);

        return relacao;
    }


    private AplicacaoDTO buscarAplicacaoPorId(Long id) {
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Aplicação não encontrada!"));
        return AplicacaoMapper.toDTO(aplicacao);
    }


    public static LocalDate populaDate(Aplicacao aplicacao) {
        return Optional.ofNullable(aplicacao.getDataChegada()).orElse(LocalDate.now());
    }

    public static Integer populaStatus(Aplicacao aplicacao) {
        return Optional.ofNullable(aplicacao.getStatusAplicacaoCodigo()).orElse(0);
    }

    private LocalDate validarEConverterData(String dataLancamento) {
        // Lista de formatos aceitos
        List<DateTimeFormatter> formatters = List.of(
                DateTimeFormatter.ofPattern("dd-MM-yyyy"),
                DateTimeFormatter.ofPattern("MM-dd-yyyy"),
                DateTimeFormatter.ofPattern("yyyy-MM-dd"),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"),
                DateTimeFormatter.ofPattern("MM/dd/yyyy")
        );

        // Itera pelos formatos e tenta parsear
        for (DateTimeFormatter formatter : formatters) {
            try {
                return LocalDate.parse(dataLancamento, formatter);
            } catch (DateTimeParseException ignored) {
                // Continua para o próximo formato
            }
        }

        // Se nenhum formato for válido, lança exceção
        throw new IllegalArgumentException("Data inválida fornecida: " + dataLancamento);
    }

    private TipoApontamento mapIdToTipoApontamento(int id) {
        switch (id) {
            case 1:
                return TipoApontamento.CRITICO;
            case 2:
                return TipoApontamento.ALTO;
            case 3:
                return TipoApontamento.MEDIO;
            case 4:
                return TipoApontamento.BAIXO;
            default:
                throw new IllegalArgumentException("ID inválido para TipoApontamento: " + id);
        }


    }
}