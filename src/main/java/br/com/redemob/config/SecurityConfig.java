package br.com.redemob.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import br.com.redemob.service.security.SegUsuarioService;

@SuppressWarnings("deprecation")
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	@Qualifier("SegUsuarioService")
	private SegUsuarioService segUsuarioService;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(segUsuarioService).passwordEncoder(passwordEncoder);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests(requests -> requests
				.antMatchers("/", "/login", "/registro", "/reset", "/static/**", "/resources/**", "/admin/**",
						"/css/**", "/fonts/**", "/img/**", "/js/**", "/vendor/**", "/less/**").permitAll()
				.antMatchers("**/sistema/**").hasAnyRole("ADMIN", "USER")
				.antMatchers("**/profile/**").hasAuthority("ADMIN")
				.antMatchers("**/grupo/**").hasAuthority("USER")
				.antMatchers("**/solicitacao/**").hasAnyRole("ADMIN", "USER")
				.anyRequest().authenticated())
				.csrf(csrf -> csrf.disable())
				.formLogin(login -> login.loginPage("/login").failureUrl("/login?error=true").permitAll()
						.defaultSuccessUrl("/sistema").usernameParameter("email").passwordParameter("password"))
				.logout(logout -> logout.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/"))
				.exceptionHandling(handling -> handling.accessDeniedPage("/403"));

	}

	@Override
	public void configure(WebSecurity web) throws Exception {

	}

}
