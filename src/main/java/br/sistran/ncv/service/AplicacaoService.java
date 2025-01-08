package br.sistran.ncv.service;

import br.sistran.ncv.exception.ObjectNotFoundException;
import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.HistoricoDeMudanca;
import br.sistran.ncv.model.LancamentoHoras;
import br.sistran.ncv.repository.AplicacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class AplicacaoService {

    @Autowired
    AplicacaoRepository aplicacaoRepository;

    public void adicionarHoras(Long id, LancamentoHoras lancamentoHoras) {
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Aplicação não encontrada"));
        aplicacao.adicionarHoras(
                lancamentoHoras.getDesenvolvedor(),
                lancamentoHoras.getHoras(),
                lancamentoHoras.getDataLancamento()
        );
        aplicacaoRepository.save(aplicacao);
    }

    public Double getHorasTotais(Long id) {
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Aplicação não encontrada"));
        return aplicacao.getHorasTotais();
    }

    public Map<String, Double> getHorasPorDesenvolvedor(Long id) {
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Aplicação não encontrada"));
        return aplicacao.getHorasPorDesenvolvedor();
    }

    public Map<String, Map<Double, List<LocalDate>>> getHorasDetalhadasPorDesenvolvedor(Long id) {
        Aplicacao aplicacao = aplicacaoRepository.findById(id)
                .orElseThrow(() -> new ObjectNotFoundException("Aplicação não encontrada"));
        return aplicacao.getLancamentosHoras().stream()
                .collect(Collectors.groupingBy(
                        LancamentoHoras::getDesenvolvedor,
                        Collectors.groupingBy(
                                LancamentoHoras::getHoras,
                                Collectors.mapping(LancamentoHoras::getDataLancamento, Collectors.toList())
                        )
                ));
    }

    public Aplicacao findById(Long id) {
        return aplicacaoRepository.findById(id).orElseThrow(
                () -> new ObjectNotFoundException("Aplicação não encontrada!")
        );
    }

    public List<Aplicacao> findAll() {
        return aplicacaoRepository.findAll();
    }

    public Aplicacao create(Aplicacao aplicacao) {
        HistoricoDeMudanca emDesenvolvimento = new HistoricoDeMudanca(0, LocalDate.now());
        List<HistoricoDeMudanca> novoHistorico = new ArrayList<>();
        novoHistorico.add(emDesenvolvimento);
        Aplicacao novaAplicacao = new Aplicacao();
        novaAplicacao.setId(null);
        novaAplicacao.setNomeAplicacao(aplicacao.getNomeAplicacao());
        novaAplicacao.setIc(aplicacao.getIc());
        novaAplicacao.setRepositorio(aplicacao.getRepositorio());
        novaAplicacao.setDataChegada(populaDate(aplicacao));
        novaAplicacao.setStatusAplicacaoCodigo(populaStatus(aplicacao));
        novaAplicacao.setBsResponsavelCodigo(aplicacao.getBsResponsavelCodigo());
        novaAplicacao.setHistoricoDeMudanca(novoHistorico);
        return aplicacaoRepository.save(novaAplicacao);
    }

    public Aplicacao update(Long id, Aplicacao aplicacao) {
        Aplicacao newObj = findById(id);

        // Verifica se houve mudança no status
        if (!Objects.equals(newObj.getStatusAplicacaoCodigo(), aplicacao.getStatusAplicacaoCodigo())) {
            newObj.adicionarHistorico(aplicacao.getStatusAplicacaoCodigo(), LocalDate.now());
        }

        newObj.setNomeAplicacao(aplicacao.getNomeAplicacao());
        newObj.setIc(aplicacao.getIc());
        newObj.setRepositorio(aplicacao.getRepositorio());
        newObj.setDataChegada(populaDate(aplicacao));
        newObj.setStatusAplicacaoCodigo(aplicacao.getStatusAplicacaoCodigo());
        newObj.setBsResponsavelCodigo(aplicacao.getBsResponsavelCodigo());

        // Salva o objeto atualizado no banco
        return aplicacaoRepository.saveAndFlush(newObj);
    }


    public void delete(Long id) {
        findById(id);
        aplicacaoRepository.deleteById(id);
    }

    public static LocalDate populaDate(Aplicacao aplicacao) {
        if (aplicacao.getDataChegada() == null) {
            return LocalDate.now();
        }
        return aplicacao.getDataChegada();
    }

    public static Integer populaStatus(Aplicacao aplicacao) {
        if (aplicacao.getStatusAplicacaoCodigo() == null) {
            return 0;
        }
        return aplicacao.getStatusAplicacaoCodigo();
    }
}
