package br.com.redemob.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.redemob.model.security.SegGrupo;
import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.model.security.SegUsuarioProfile;

import java.util.List;


@Repository("userRepository")
public interface UserRepository extends JpaRepository<SegUsuario, Long> {
	
	SegUsuario findByEmail(String email);

    SegUsuario findBySegUsuarioProfile(SegUsuarioProfile profile);

    List<SegUsuario> findBySegGrupos(SegGrupo grupo);

}
