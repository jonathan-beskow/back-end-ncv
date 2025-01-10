package br.sistran.ncv.repository;

import br.sistran.ncv.model.HistoricoApontamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HistoricoApontamentoRepository extends JpaRepository<HistoricoApontamento, Long> {
    List<HistoricoApontamento> findByData(LocalDate data);
}
