package br.com.redemob.controller.tools;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.redemob.infra.GetSqlError;
import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.model.security.Solicitacao;
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
    public ModelAndView listarGrupo() {
        ModelAndView modelAndView = new ModelAndView("security/solicitacao/list-solicitacao");
        SegUsuario user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        modelAndView.addObject("segUsuario", user);
        List<Solicitacao> solicitacaoList = solicitacaoService.listarSolicitacoes(user);
        modelAndView.addObject("solicitacoes", solicitacaoList);
        return modelAndView;
    }


    @GetMapping("/create-solicitacao")
    public ModelAndView cadastroGrupo(Solicitacao solicitacao) {
        ModelAndView modelAndView = new ModelAndView("security/solicitacao/create-solicitacao");
        modelAndView.addObject("segUsuario", userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        modelAndView.addObject("solicitacao", solicitacao);
        return modelAndView;
    }


    @PostMapping("/create-solicitacao")
    public ModelAndView cadastroGrupo(@Valid @ModelAttribute("solicitacao") Solicitacao solicitacao, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("security/solicitacao/create-solicitacao");
        SegUsuario user = userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        modelAndView.addObject("segUsuario", user);

        try{
            solicitacaoService.saveSolicitacao(solicitacao, user);
        } catch (Exception e){
            modelAndView.addObject("errorMessage", "Erro ao salvar " + solicitacao.getFoto() + GetSqlError.getErro(e));
            return modelAndView;
        }

        modelAndView.addObject("successMessage", solicitacao.getFoto() + " cadastrado(a) com sucesso!");
        modelAndView.addObject("solicitacoes", solicitacaoService.listarSolicitacoes(user));
        modelAndView.setViewName("security/solicitacao/list-solicitacao");


        return modelAndView;
    }

}
