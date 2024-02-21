package br.com.redemob.controller.tools;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.redemob.enums.StatusEnum;
import br.com.redemob.infra.GetSqlError;
import br.com.redemob.model.security.SegGrupo;
import br.com.redemob.service.security.SegGrupoService;
import br.com.redemob.service.security.SegUsuarioService;

@RestController
@RequestMapping("/grupo")
public class GrupoController {

    @Autowired
    SegGrupoService segGrupoService;

    @Autowired
    SegUsuarioService userService;

    @GetMapping("/list-grupo")
    public ModelAndView listarGrupo() {
        ModelAndView modelAndView = new ModelAndView("security/grupo/list-grupo");
        modelAndView.addObject("segUsuario", userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        List<SegGrupo> grupoList = segGrupoService.findAll();
        modelAndView.addObject("grupos", grupoList);
        return modelAndView;
    }


    @GetMapping("/create-grupo")
    public ModelAndView cadastroGrupo(SegGrupo grupo) {
        ModelAndView modelAndView = new ModelAndView("security/grupo/create-grupo");
        modelAndView.addObject("segUsuario", userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        modelAndView.addObject("grupo", grupo);

        return modelAndView;
    }


    @PostMapping("/create-grupo")
    public ModelAndView cadastroGrupo(@Valid @ModelAttribute("grupo") SegGrupo grupo, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("security/grupo/create-grupo");
        modelAndView.addObject("segUsuario", userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));

        SegGrupo grupoExist = segGrupoService.findGrupoByName(grupo.getName());
        if (grupoExist != null) {
            modelAndView.addObject("grupo", grupo);
            modelAndView.addObject("statusList", StatusEnum.values());
            modelAndView.addObject("errorMessage", grupoExist.getName() +" já existe, se deseja alterar navegue em 'Manutenção de cadastros/Controle de Acesso/Grupos/Pesquisa' e escolha o grupo a ser editado!");
            return modelAndView;
        }else {
            if (bindingResult.hasErrors()) {
                modelAndView.addObject("statusList", StatusEnum.values());
                modelAndView.addObject("grupo", grupo);
                return modelAndView;
            }

            try{
                segGrupoService.saveAndFlush(grupo);
            } catch (Exception e){
                modelAndView.addObject("errorMessage", "Erro ao salvar " + grupo.getName() + GetSqlError.getErro(e));
                return modelAndView;
            }

            List<SegGrupo> listaGrupos = segGrupoService.findAll();
            modelAndView.addObject("successMessage", grupo.getName() + " cadastrado(a) com sucesso!");
            modelAndView.addObject("grupos", listaGrupos);
            modelAndView.setViewName("security/grupo/list-grupo");

        }

        return modelAndView;
    }


    @PutMapping("/edit-grupo/{id}")
    public ModelAndView editGrupo(@PathVariable Long id) {
        SegGrupo grupo = segGrupoService.getOne(id);
        return editGrupo(grupo);
    }


    @GetMapping("/edit-grupo")
    public ModelAndView editGrupo(SegGrupo grupo) {
        ModelAndView modelAndView = new ModelAndView("security/grupo/edit-grupo");
        modelAndView.addObject("segUsuario", userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
        modelAndView.addObject("statusList", StatusEnum.values());
        modelAndView.addObject("grupo", grupo);
        return modelAndView;
    }


    @PostMapping("/edit-grupo")
    public ModelAndView editGrupo(@Valid @ModelAttribute("grupo") SegGrupo grupo, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("security/grupo/list-grupo");
        modelAndView.addObject("segUsuario", userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));

        if (bindingResult.hasErrors()) {

            modelAndView.setViewName("security/grupo/edit-grupo");
            modelAndView.addObject("statusList", StatusEnum.values());
            modelAndView.addObject("grupo", grupo);
            modelAndView.addObject("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return modelAndView;
        }

        try{
            segGrupoService.editAndFlush(grupo, modelAndView);
        } catch (Exception e){
            modelAndView.addObject("grupos", segGrupoService.findAll());
            modelAndView.addObject("errorMessage", "Erro ao salvar " + grupo.getName() + GetSqlError.getErro(e));
            return modelAndView;
        }

        modelAndView.addObject("grupos", segGrupoService.findAll());

        return modelAndView;
    }


    @DeleteMapping("/delete-grupo/{id}")
    public String apagar(@PathVariable Long id, RedirectAttributes attributes) {

        try {
            segGrupoService.deleteGrupo(id, attributes);
        } catch (Exception e){
            attributes.addFlashAttribute("errorMessage", "Erro ao salvar " + GetSqlError.getErro(e));
        }

        attributes.addFlashAttribute("grupos", segGrupoService.findAll());

        return "redirect:/grupo/list-grupo";
    }

}
