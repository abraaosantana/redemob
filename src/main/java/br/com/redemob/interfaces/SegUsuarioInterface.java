package br.com.redemob.interfaces;

import org.springframework.web.servlet.ModelAndView;

import br.com.redemob.model.auxiliar.AuxEndereco;
import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.model.security.SegUsuarioProfile;

public interface SegUsuarioInterface {
	SegUsuario findUserByEmail(String email);
	ModelAndView saveUser(ModelAndView modelAndView, SegUsuario user);
	String resetPassword(SegUsuario user);
    void updateUser(SegUsuario userParams, SegUsuario userInstance);
	void updateProfile(SegUsuarioProfile profileParams, SegUsuarioProfile profileInstance);
	void updateEndereco(AuxEndereco enderecoParams, AuxEndereco enderecoInstance);

}
