package br.com.redemob.controller.tools;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.service.security.SegUsuarioService;


@RestController
public class AccountController {

	private final Logger LOG = LoggerFactory.getLogger(AccountController.class);

	@Autowired
	@Qualifier("SegUsuarioService")
	private SegUsuarioService userService;

	@GetMapping(value = { "/login" })
	public ModelAndView login(@AuthenticationPrincipal SegUsuario user) {

		ModelAndView modelAndView = new ModelAndView("account/login");
		LOG.info("Realizando login.");
		modelAndView.addObject("errorMessage", "Se é o primeiro acesso, realize o cadastro!");
		return modelAndView;

	}

	@GetMapping("/403")
	public String error403() {
		return "/error/403";
	}

	@GetMapping("/profile")
	public ModelAndView profile(SegUsuario user) {
		ModelAndView modelAndView = new ModelAndView("account/profile");
		modelAndView.addObject("segUsuario", userService.findUserByEmail(SecurityContextHolder.getContext().getAuthentication().getName()));
		return modelAndView;
	}

	@PostMapping("/profile")
	public ModelAndView createNewUser(@Valid SegUsuario userParams, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView("admin/layout/_dashboard-blank");

		LOG.info("Iniciando cadastro do perfil de usuario");

		SegUsuario userExists = userService.findUserByEmail(userParams.getEmail());
		modelAndView.addObject("segUsuario", userExists);
				
		if (userExists != null) {
			if (userExists.getSegUsuarioProfile() != null) {

				userService.updateUser(userParams, userExists );
				
				modelAndView.addObject("successMessage", "*  " + userParams.getNomeCompleto() + " seu cadastro foi atualizado com sucesso! ");
				modelAndView.setViewName("account/profile");
				
				return modelAndView;
						    
		    } else {
		    	userParams.getSegUsuarioProfile().setSegUsuario(userExists);
		    	userExists.setSegUsuarioProfile(userParams.getSegUsuarioProfile());
				userService.updateUser(userParams, userExists);
				modelAndView.addObject("successMessage", "*  " + userParams.getNomeCompleto() + " seu cadastro foi atualizado com sucesso! ");
				modelAndView.setViewName("account/profile");
				return modelAndView;
			
		    }
		}
		
		if (bindingResult.hasErrors()) {
			List<String> lista2 = new ArrayList<String>();
			for (FieldError lista : bindingResult.getFieldErrors()) {
				lista.getDefaultMessage();
				lista2.add(new String(lista.getDefaultMessage()));
			}

			modelAndView.addObject("errorMessage", lista2);
			modelAndView.addObject("segUsuario", userParams);

		}
		return modelAndView;
	}

}
