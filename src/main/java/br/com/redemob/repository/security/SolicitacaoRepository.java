package br.com.redemob.repository.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.model.security.Solicitacao;


@Repository("SolicitacaoRepository")
public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {
	
	Optional<List<Solicitacao>> findBySegUsuario(SegUsuario user);

}
