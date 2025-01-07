package br.sistran.ncv.service;

import br.sistran.ncv.model.Aplicacao;
import br.sistran.ncv.model.HistoricoDeMudanca;
import br.sistran.ncv.model.enums.BSResponsavel;
import br.sistran.ncv.model.enums.StatusAplicacao;
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


    @PostConstruct
    public void instanciaDB() {
        LocalDate now = LocalDate.now();
        LocalDate oneYearAgo = now.minusYears(1);
        LocalDate sixMonthsAgo = now.minusMonths(6);
        LocalDate oneDayAgo = now.minusDays(1);
        HistoricoDeMudanca umAno = new HistoricoDeMudanca(0, oneYearAgo);
        HistoricoDeMudanca seisMeses = new HistoricoDeMudanca(1, sixMonthsAgo);
        HistoricoDeMudanca umDia = new HistoricoDeMudanca(2, oneDayAgo);
        List<HistoricoDeMudanca> listaDeMudanca = new ArrayList<>(List.of(umAno, seisMeses, umDia));
        List<HistoricoDeMudanca> listaVazia = new ArrayList<>();
        Aplicacao lexw = new Aplicacao(
                null,
                "LEXW-BsLexWeb",
                now,
                "http://svn.dsv.bradseg.com.br/svn/lexw_bslexweb/branches/lexw_bslexweb_branch_2.1.85.1",
                "https://ic.dsv.bradseg.com.br/build/job/LEXW-BsLexWeb-branch-2.1.85.1/",
                0,
                0,
                listaVazia
        );

        Aplicacao gcti = new Aplicacao(
                null,
                "GCTI-Acompanhamento",
                now,
                "SVN GCTI",
                "IC GCTI",
                3,
                4,
                listaDeMudanca
        );

        Aplicacao vdol = new Aplicacao(
                null,
                "VDOL-VendaOnline",
                now,
                "http://svn.dsv.bradseg.com.br/VDOL-VendaOnline",
                "https://ic.dsv.bradseg.com.br/build/job/VDOL-VendaOnline/",
                0,
                0,
                listaVazia
        );

        Aplicacao vprs = new Aplicacao(
                null,
                "VPRS-AcompCarteiras",
                now,
                "http://svn.dsv.bradseg.com.br/VPRS-AcompCarteiras",
                "https://ic.dsv.bradseg.com.br/build/job/VPRS-AcompCarteiras/",
                2,
                2,
                listaVazia
        );

        Aplicacao sares = new Aplicacao(
                null,
                "SARE-GestaoSinitro",
                now,
                "http://svn.dsv.bradseg.com.br/SARE-GestaoSinitro",
                "https://ic.dsv.bradseg.com.br/build/job/SARE-GestaoSinitro/",
                1,
                1,
                listaVazia
        );

        Aplicacao sareg = new Aplicacao(
                null,
                "SARE-GestaoRegulacao",
                now,
                "http://svn.dsv.bradseg.com.br/SARE-GestaoRegulacao",
                "https://ic.dsv.bradseg.com.br/build/job/SARE-GestaoRegulacao/",
                1,
                5,
                listaVazia
        );



        aplicacaoRepository.save(lexw);
        aplicacaoRepository.save(gcti);
        aplicacaoRepository.save(vdol);
        aplicacaoRepository.save(vprs);
        aplicacaoRepository.save(sares);
        aplicacaoRepository.save(sareg);

    }

}
