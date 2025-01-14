package br.sistran.ncv.service;

import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.HistoricoApontamento;
import br.sistran.ncv.model.HistoricoDeMudanca;
import br.sistran.ncv.model.LancamentoHoras;
import br.sistran.ncv.model.enums.TipoApontamento;
import br.sistran.ncv.repository.AplicacaoRepository;
import br.sistran.ncv.repository.ApontamentoRepository;
import br.sistran.ncv.repository.HistoricoApontamentoRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class DBService {

    @Autowired
    private AplicacaoRepository aplicacaoRepository;

    @Autowired
    private AplicacaoService aplicacaoService;

    @Autowired
    private ApontamentoRepository apontamentoRepository;

    @Autowired
    HistoricoApontamentoRepository historicoApontamentoRepository;

    @PostConstruct
    public void instanciaDB() {
        LocalDate now = LocalDate.now();

        // Criar histórico de mudanças
        List<HistoricoDeMudanca> historicoDeMudanca = List.of(
                new HistoricoDeMudanca(0, now.minusYears(1)),
                new HistoricoDeMudanca(1, now.minusMonths(6))
        );

        // Criar lançamentos de horas
        List<LancamentoHoras> lancamentosHoras = new ArrayList<>();
        lancamentosHoras.add(new LancamentoHoras("João", 8.0, now.minusDays(2)));
        lancamentosHoras.add(new LancamentoHoras("Maria", 6.0, now.minusDays(1)));

        // Criar aplicações
        Aplicacao aplicacao1 = new Aplicacao(
                null,
                "LEXW-BsLexWeb",
                now,
                "http://svn.lexweb.com",
                "https://ic.dsv.bradseg.com.br/build/job/LEXW-BsLexWeb-branch-2.1.85.1/",
                0,
                1,
                historicoDeMudanca
        );

        Aplicacao aplicacao2 = new Aplicacao(
                null,
                "VDOL-VendaOnline",
                now,
                "http://svn.vdol.com",
                "https://ic.dsv.bradseg.com.br/build/job/VDOL-VendaOnline-branch-1.15.20.2/",
                1,
                3,
                historicoDeMudanca
        );

        aplicacaoRepository.saveAndFlush(aplicacao1);
        aplicacaoRepository.saveAndFlush(aplicacao2);

        // Adicionar apontamentos às aplicações
        aplicacao1.adicionarOuAtualizarApontamento(TipoApontamento.CRITICO, 0);
        aplicacao1.adicionarOuAtualizarApontamento(TipoApontamento.ALTO, 0);
        aplicacao1.adicionarOuAtualizarApontamento(TipoApontamento.MEDIO, 0);
        aplicacao1.adicionarOuAtualizarApontamento(TipoApontamento.BAIXO, 0);

        aplicacao2.adicionarOuAtualizarApontamento(TipoApontamento.CRITICO, 700);
        aplicacao2.adicionarOuAtualizarApontamento(TipoApontamento.ALTO, 350);
        aplicacao2.adicionarOuAtualizarApontamento(TipoApontamento.MEDIO, 16);
        aplicacao2.adicionarOuAtualizarApontamento(TipoApontamento.BAIXO, 847);

        aplicacao1.setLancamentosHoras(new ArrayList<>(lancamentosHoras));
        aplicacao2.setLancamentosHoras(new ArrayList<>(lancamentosHoras));

        // Persistir as aplicações
        aplicacaoRepository.saveAndFlush(aplicacao1);
        aplicacaoRepository.saveAndFlush(aplicacao2);

        // Agendador Service
        LocalDate segunda = now.with(java.time.DayOfWeek.MONDAY);
        LocalDate sexta = now.with(java.time.DayOfWeek.FRIDAY);

        List<HistoricoApontamento> historicoSegunda = List.of(
                new HistoricoApontamento(TipoApontamento.CRITICO, 0, aplicacao1, segunda),
                new HistoricoApontamento(TipoApontamento.ALTO, 0, aplicacao1, segunda),
                new HistoricoApontamento(TipoApontamento.MEDIO, 0, aplicacao1, segunda),
                new HistoricoApontamento(TipoApontamento.BAIXO, 0, aplicacao1, segunda),
                new HistoricoApontamento(TipoApontamento.CRITICO, 700, aplicacao2, segunda),
                new HistoricoApontamento(TipoApontamento.ALTO, 350, aplicacao2, segunda),
                new HistoricoApontamento(TipoApontamento.MEDIO, 16, aplicacao2, segunda),
                new HistoricoApontamento(TipoApontamento.BAIXO, 847, aplicacao2, segunda)
        );

        List<HistoricoApontamento> historicoSexta = List.of(
                new HistoricoApontamento(TipoApontamento.CRITICO, 10, aplicacao1, sexta),
                new HistoricoApontamento(TipoApontamento.ALTO, 5, aplicacao1, sexta),
                new HistoricoApontamento(TipoApontamento.MEDIO, 3, aplicacao1, sexta),
                new HistoricoApontamento(TipoApontamento.BAIXO, 1, aplicacao1, sexta),
                new HistoricoApontamento(TipoApontamento.CRITICO, 750, aplicacao2, sexta),
                new HistoricoApontamento(TipoApontamento.ALTO, 370, aplicacao2, sexta),
                new HistoricoApontamento(TipoApontamento.MEDIO, 20, aplicacao2, sexta),
                new HistoricoApontamento(TipoApontamento.BAIXO, 860, aplicacao2, sexta)
        );

        historicoApontamentoRepository.saveAll(historicoSegunda);
        historicoApontamentoRepository.saveAll(historicoSexta);


    }


}
