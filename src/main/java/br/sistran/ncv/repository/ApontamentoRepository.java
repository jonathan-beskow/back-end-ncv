package br.sistran.ncv.repository;

import br.sistran.ncv.model.Apontamento;
import br.sistran.ncv.model.enums.TipoApontamento;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ApontamentoRepository extends JpaRepository<Apontamento, Long> {
    List<Apontamento> findByTipoAndAplicacaoId(TipoApontamento tipo, Long aplicacaoId);
}
