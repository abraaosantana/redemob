package br.com.redemob.controller.tools;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.service.security.SegUsuarioService;


@Controller
@RequestMapping("/sistema")
public class AdminController {

	@Autowired
	private SegUsuarioService userService;

	@GetMapping
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("admin/layout/_dashboard-blank");
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		SegUsuario user = userService.findUserByEmail(auth.getName());
		modelAndView.addObject("segUsuario", user);
		modelAndView.setViewName("admin/index");
		modelAndView.addObject("segUsuario", user);

		return modelAndView;
	}

}
