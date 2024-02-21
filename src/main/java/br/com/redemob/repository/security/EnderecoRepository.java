package br.com.redemob.repository.security;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.redemob.model.auxiliar.AuxEndereco;

@Repository
public interface EnderecoRepository extends JpaRepository<AuxEndereco, Long> {

}
