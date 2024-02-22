package br.com.redemob.controller.tools;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.model.security.Solicitacao;
import br.com.redemob.service.dto.FilesDto;
import br.com.redemob.service.security.SegUsuarioService;
import br.com.redemob.service.security.SolicitacaoService;

@RestController
@RequestMapping("/solicitacao")
public class SolicitacaoController {

    @Autowired
    SolicitacaoService solicitacaoService;

    @Autowired
    SegUsuarioService userService;

    @GetMapping("/list-solicitacao")
    public ModelAndView listarSolicitacoes() {
        ModelAndView modelAndView = new ModelAndView("security/solicitacao/list-solicitacao");
        SegUsuario user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        modelAndView.addObject("segUsuario", user);
        List<Solicitacao> solicitacaoList = solicitacaoService.listarSolicitacoes(user);
        modelAndView.addObject("solicitacoes", solicitacaoList);
        return modelAndView;
    }
    
    @GetMapping("/list-deliberacao")
    public ModelAndView listarSolicitacoesDeliberacao() {
        ModelAndView modelAndView = new ModelAndView("security/solicitacao/list-deliberacao");
        modelAndView.addObject("segUsuario", userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        List<Solicitacao> solicitacaoList = solicitacaoService.listarSolicitacoesDeliberacao();
        modelAndView.addObject("solicitacoes", solicitacaoList);
        return modelAndView;
    }


    @GetMapping("/create-solicitacao")
    public ModelAndView cadastroSolicitacao(Solicitacao solicitacao) {
        ModelAndView modelAndView = new ModelAndView("security/solicitacao/create-solicitacao");
        modelAndView.addObject("segUsuario", userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        modelAndView.addObject("solicitacao", solicitacao);
        return modelAndView;
    }


    @PostMapping("/create-solicitacao")
    public ModelAndView cadastroSolicitacao(@ModelAttribute("solicitacao") Solicitacao solicitacao, BindingResult bindingResult, 
    		@RequestPart("biometria") MultipartFile biometria,
    		@RequestPart("identidade") MultipartFile identidade,
    		@RequestPart("residencia") MultipartFile residencia) {
        ModelAndView modelAndView = new ModelAndView("security/solicitacao/create-solicitacao");
        SegUsuario user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        modelAndView.addObject("segUsuario", user);

        try{
        	FilesDto files = FilesDto.builder()
        			.biometria(biometria)
        			.identidade(identidade)
        			.residencia(residencia)
        			.build();
            solicitacaoService.saveSolicitacao(solicitacao, user, files);
        } catch (Exception e){
            modelAndView.addObject("errorMessage", "Erro ao salvar solicitação ");
            return modelAndView;
        }

        modelAndView.addObject("successMessage", "Solicitação cadastrado(a) com sucesso!");
        modelAndView.addObject("solicitacoes", solicitacaoService.listarSolicitacoes(user));
        modelAndView.setViewName("security/solicitacao/list-solicitacao");


        return modelAndView;
    }

    @GetMapping("/downloadfile")
    public void downloadFile(@RequestParam("id") Long id, @RequestParam("type") String type, HttpServletResponse response) throws IOException {
     Solicitacao solicitacao = solicitacaoService.findById(id);
     response.setContentType("application/octet-stream");
     String headerKey = "Content-Disposition";
     String headerValue = "attachment; filename = "+ type + ".jpg";
     response.setHeader(headerKey, headerValue);
     ServletOutputStream outputStream = response.getOutputStream();
     if(type.toLowerCase().equals("biometria")) {
    	 outputStream.write(solicitacao.getDocBiometria());
     }
     
     if(type.toLowerCase().equals("identidade")) {
    	 outputStream.write(solicitacao.getDocIdentidade());
     }
     
     if(type.toLowerCase().equals("residencia")) {
    	 outputStream.write(solicitacao.getDocComprovanteResidencia());
     }
     
     outputStream.close();
     
    }
    
    @GetMapping("/approve/{id}")
    public ModelAndView aprovarSolicitacao(@PathVariable Long id) {
    	solicitacaoService.aprovar(id);
        return listarSolicitacoesDeliberacao();
    }
    
    @GetMapping("/disapprove/{id}")
    public ModelAndView reprovarSolicitacao(@PathVariable Long id) {
    	solicitacaoService.reprovar(id);
        return listarSolicitacoesDeliberacao();
    }
}
