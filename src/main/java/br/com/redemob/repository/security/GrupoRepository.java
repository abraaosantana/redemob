package br.com.redemob.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.redemob.model.security.SegGrupo;

import java.util.List;


@Repository("GrupoRepository")
public interface GrupoRepository extends JpaRepository<SegGrupo, Long> {

	SegGrupo getByName(String grupo);

	List<SegGrupo> findByName(String grupo);

}
