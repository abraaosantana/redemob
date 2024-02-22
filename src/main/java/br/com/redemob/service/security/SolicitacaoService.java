package br.com.redemob.service.security;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.redemob.enums.SituacaoEnum;
import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.model.security.Solicitacao;
import br.com.redemob.repository.security.SolicitacaoRepository;
import br.com.redemob.service.dto.FilesDto;

@Service
public class SolicitacaoService {

    @Autowired
    SolicitacaoRepository solicitacaoRepository;

    @Autowired
    SegUsuarioService userService;


    public void saveSolicitacao(Solicitacao solicitacao, SegUsuario user, FilesDto files) throws IOException {
        Date dataAtual = new Date();
        solicitacao.setDateCreated(dataAtual);
        solicitacao.setLastUpdated(dataAtual);
        solicitacao.setActive(true);
        solicitacao.setSegUsuario(user);
        if (solicitacao.getVersion() > 0) solicitacao.setVersion(solicitacao.getVersion() + 1);
        solicitacao.setDocBiometria(files.getBiometria().getBytes());
        solicitacao.setDocIdentidade(files.getIdentidade().getBytes());
        solicitacao.setDocComprovanteResidencia(files.getResidencia().getBytes());
        solicitacaoRepository.save(solicitacao);
    }


	public List<Solicitacao> listarSolicitacoes(SegUsuario user) {
		return solicitacaoRepository.findBySegUsuario(user).get();
	}
	
	public List<Solicitacao> listarSolicitacoesDeliberacao() {
		return solicitacaoRepository.findAll(); 
	}


	public void aprovar(Long id) {
		Solicitacao solicitacao = solicitacaoRepository.findById(id).get();
		solicitacao.setSituacao(SituacaoEnum.APROVADO);
		solicitacaoRepository.save(solicitacao);
	}
	
	public void reprovar(Long id) {
		Solicitacao solicitacao = solicitacaoRepository.findById(id).get();
		solicitacao.setSituacao(SituacaoEnum.REPROVADO);
		solicitacaoRepository.save(solicitacao);
	}


	public Solicitacao findById(Long id) {
		return solicitacaoRepository.findById(id).get();
	}
  
  
}
