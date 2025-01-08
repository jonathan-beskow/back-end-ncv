package br.sistran.ncv.service;

import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.Apontamento;
import br.sistran.ncv.model.HistoricoDeMudanca;
import br.sistran.ncv.model.LancamentoHoras;
import br.sistran.ncv.model.enums.TipoApontamento;
import br.sistran.ncv.repository.AplicacaoRepository;
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
                "http://ci.lexweb.com",
                0,
                1,
                historicoDeMudanca
        );

        Aplicacao aplicacao2 = new Aplicacao(
                null,
                "VDOL-VendaOnline",
                now,
                "http://svn.vdol.com",
                "http://ci.vdol.com",
                1,
                3,
                historicoDeMudanca
        );

        aplicacaoRepository.saveAndFlush(aplicacao1);
        aplicacaoRepository.saveAndFlush(aplicacao2);

        // Adicionar apontamentos às aplicações
        aplicacao1.adicionarOuAtualizarApontamento(TipoApontamento.CRITICO, 1500);
        aplicacao1.adicionarOuAtualizarApontamento(TipoApontamento.ALTO, 600);
        aplicacao1.adicionarOuAtualizarApontamento(TipoApontamento.MEDIO, 150);
        aplicacao1.adicionarOuAtualizarApontamento(TipoApontamento.BAIXO, 2000);

        aplicacao2.adicionarOuAtualizarApontamento(TipoApontamento.CRITICO, 700);
        aplicacao2.adicionarOuAtualizarApontamento(TipoApontamento.ALTO, 350);
        aplicacao2.adicionarOuAtualizarApontamento(TipoApontamento.MEDIO, 16);
        aplicacao2.adicionarOuAtualizarApontamento(TipoApontamento.BAIXO, 847);

        aplicacao1.setLancamentosHoras(new ArrayList<>(lancamentosHoras));
        aplicacao2.setLancamentosHoras(new ArrayList<>(lancamentosHoras));

        // Persistir as aplicações
        aplicacaoRepository.saveAndFlush(aplicacao1);
        aplicacaoRepository.saveAndFlush(aplicacao2);
    }



}
