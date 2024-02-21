package br.com.redemob.service.Infra;


import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.redemob.model.security.SegUsuario;

@Service
public class NotificacaoService {

	private Logger LOG = LoggerFactory.getLogger(NotificacaoService.class);
	
	private JavaMailSender javaMailSender;
	
	public NotificacaoService(JavaMailSender javaMailSender){
		this.javaMailSender = javaMailSender;
	}
	
	public void enviarNotificacao(SegUsuario usuario, String passwd) throws MailException{
		
		//Lógica de envio de notificacao por email
		
		SimpleMailMessage email = new SimpleMailMessage();
		
		email.setTo(usuario.getEmail());
		email.setFrom("contato@devweb.tec.br");

		if(StringUtils.isNotBlank(passwd)) {
			email.setSubject("Reset de senha");
			email.setText(String.join(": ", "Nova senha", passwd));
		} else {
			email.setSubject("Cadastro");
			email.setText("Cadastro Realizado com sucesso: ");
		}
		

		LOG.info("Enviando e-mail!");
		javaMailSender.send(email);
		
		LOG.info("Email enviado!");
	}
	
}
