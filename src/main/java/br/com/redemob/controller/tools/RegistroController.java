package br.com.redemob.controller.tools;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.service.Infra.NotificacaoService;
import br.com.redemob.service.security.SegUsuarioService;


@RestController
public class RegistroController {


	@Autowired
	private NotificacaoService notificacaoService;

	@Autowired
	private SegUsuarioService segUsuarioService;

	@GetMapping("/registro")
	public ModelAndView registration(SegUsuario user) {
		 ModelAndView modelAndView = new ModelAndView("account/register");
		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@PostMapping("/registro")
	public ModelAndView createNewUser(@Valid @ModelAttribute("user") SegUsuario user, BindingResult bindingResult) {
		 ModelAndView modelAndView = new ModelAndView("account/register");

		if (bindingResult.hasErrors()) {
			List<String> errors = new ArrayList<String>();
			for (FieldError error : bindingResult.getFieldErrors()) {
				error.getDefaultMessage();
				errors.add(new String(error.getDefaultMessage()));
			}

			modelAndView.addObject("errorMessage", errors);
			modelAndView.addObject("user", user);
			return modelAndView;
		}
		
		if(Objects.isNull(segUsuarioService.validate(modelAndView, user))) {
			modelAndView = segUsuarioService.saveUser(modelAndView, user);
			notificacaoService.enviarNotificacao(user, null);
		}
		
		return modelAndView;
	}

	@GetMapping("/reset")
	public ModelAndView esqueceuSenha(SegUsuario user) {
		ModelAndView modelAndView = new ModelAndView("account/reset");
		modelAndView.addObject("user", user);
		return modelAndView;
	}

	@PostMapping("/reset")
	public ModelAndView resetSenha(@Valid @ModelAttribute("user") SegUsuario user, BindingResult bindingResult) {
		ModelAndView modelAndView = new ModelAndView();

		/**
		 * Verifica usuário por email informado, caso não exista exibe mensagem de erro
		 * e solicita registro.
		 */

		SegUsuario userExists = segUsuarioService.findUserByEmail(user.getEmail());

		if (userExists == null) {
			modelAndView.addObject("errorValidate",
					"Não existe nenhum usuário cadastrado com esse email! Realizar cadastro para " + user.getEmail());
			modelAndView.addObject("user", user);
			modelAndView.setViewName("account/reset");
			return modelAndView;
		} else {
			String newPasswd = segUsuarioService.resetPassword(userExists);
			notificacaoService.enviarNotificacao(userExists, newPasswd);
			modelAndView.addObject("successMessage", "Nova senha enviada para o e-mail: " + user.getEmail());
			modelAndView.setViewName("account/login");
			return modelAndView;
		}
	}

}