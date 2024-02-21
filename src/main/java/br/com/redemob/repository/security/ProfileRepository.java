package br.com.redemob.repository.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.model.security.SegUsuarioProfile;


@Repository("profileRepository")
public interface ProfileRepository extends JpaRepository<SegUsuarioProfile, Long> {

    SegUsuarioProfile findBySegUsuario(SegUsuario usuario);

}
