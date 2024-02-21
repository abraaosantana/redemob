package br.com.redemob.service.security;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import br.com.redemob.enums.CidadeEnum;
import br.com.redemob.interfaces.SegUsuarioInterface;
import br.com.redemob.model.auxiliar.AuxEndereco;
import br.com.redemob.model.security.SegGrupo;
import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.model.security.SegUsuarioProfile;
import br.com.redemob.repository.security.EnderecoRepository;
import br.com.redemob.repository.security.GrupoRepository;
import br.com.redemob.repository.security.ProfileRepository;
import br.com.redemob.repository.security.UserRepository;
import br.com.redemob.service.Infra.ViaCepService;

@Transactional
@Service("SegUsuarioService")
public class SegUsuarioService implements SegUsuarioInterface, UserDetailsService {

	@Autowired
	private ViaCepService viaCepService;

	@Autowired
	@Qualifier("userRepository")
	private UserRepository userRepository;

    @Autowired
	@Qualifier("GrupoRepository")
	private GrupoRepository grupoRepository;

    @Qualifier("profileRepository")
    @Autowired
	private ProfileRepository profileRepository;

    @Autowired
	private EnderecoRepository enderecoRepository;

	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public SegUsuario findUserByEmail(String email) {
		return userRepository.findByEmail(email);
	}

	public List<SegUsuario> findByGrupo(SegGrupo grupo) {
		return userRepository.findBySegGrupos(grupo);
	}

	@Override
	public ModelAndView saveUser(ModelAndView modelAndView, SegUsuario user) {
		user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
		user.setActive(true);
		user.setDateCreated(new Date());
        user.setVersion(0);

		List<SegGrupo> userGroup = grupoRepository.findByName("USER");
		if (Objects.nonNull(userGroup)) {
			user.setSegGrupos(userGroup);
			userRepository.save(user);
		} else {
			SegGrupo role = new SegGrupo();
			role.setId(1L);
			role.setName("ADMIN");
			role.setDateCreated(new Date());
			grupoRepository.save(role);
			role.setId(2L);
			role.setName("USER");
			grupoRepository.save(role);

			userGroup = grupoRepository.findByName("USER");
			user.setSegGrupos(userGroup);

			userRepository.save(user);
		}
		
		modelAndView.addObject("successMessage", user.getNomeCompleto() + " cadastrado(a) com sucesso!");
		modelAndView.setViewName("account/login");
		
		return modelAndView;
	}
	
	@Override
	public String resetPassword(SegUsuario user) {
		String newPasswd = String.valueOf(new Random().nextInt(99999999));
		user.setPassword(bCryptPasswordEncoder.encode(newPasswd));
		userRepository.save(user);
		return newPasswd;
	}

    @Override
    public void updateUser(SegUsuario userParams, SegUsuario userInstance) {

	    updateEndereco(userParams.getSegUsuarioProfile().getAuxEndereco()
                     , userInstance.getSegUsuarioProfile().getAuxEndereco());

        updateProfile(userParams.getSegUsuarioProfile()
                    , userInstance.getSegUsuarioProfile());

        userInstance.setNomeCompleto(userParams.getNomeCompleto());
        userInstance.setNomeCompletoMae(userParams.getNomeCompletoMae());
        userInstance.setVersion(userParams.getVersion() + 1);
        userInstance.setLastUpdated(new Date());

        userRepository.saveAndFlush(userInstance);
    }

    @Override
	public void updateProfile(SegUsuarioProfile profileParams, SegUsuarioProfile profileInstance) {

	    profileInstance.setTelefoneFixo(profileParams.getTelefoneFixo());
	    profileInstance.setCelular(profileParams.getCelular());
	    profileInstance.setProfissao(profileParams.getProfissao());

		profileRepository.saveAndFlush(profileInstance);

	}

	@Override
	public void updateEndereco(AuxEndereco enderecoParams, AuxEndereco enderecoInstance) {

	    enderecoInstance.setBairro(enderecoParams.getBairro());
        enderecoInstance.setCep(enderecoParams.getCep());
        enderecoInstance.setComplemento(enderecoParams.getComplemento());
        enderecoInstance.setGia(enderecoParams.getGia());
        enderecoInstance.setIbge(enderecoParams.getIbge());
        enderecoInstance.setNumero(enderecoParams.getNumero());
        enderecoInstance.setLocalidade(enderecoParams.getLocalidade());
        enderecoInstance.setLogradouro(enderecoParams.getLogradouro());
        enderecoInstance.setUf(enderecoParams.getUf());
        enderecoInstance.setUnidade(enderecoParams.getUnidade());

        enderecoRepository.saveAndFlush(enderecoInstance);

	}

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		SegUsuario user = userRepository.findByEmail(userName);
		List<GrantedAuthority> authorities = getUserAuthority(user.getSegGrupos());
		return buildUserForAuthentication(user, authorities);
	}

	private List<GrantedAuthority> getUserAuthority(List<SegGrupo> userRole) {
		Set<GrantedAuthority> roles = new HashSet<GrantedAuthority>();
		for (SegGrupo segGrupo : userRole) {
			roles.add(new SimpleGrantedAuthority(segGrupo.getName()));
		}

		List<GrantedAuthority> grantedAuthorities = new ArrayList<GrantedAuthority>(roles);
		return grantedAuthorities;
	}

	private UserDetails buildUserForAuthentication(SegUsuario user, List<GrantedAuthority> authorities) {
		return new User(user.getEmail(), user.getPassword(), user.isActive(), true, true, true, authorities);
	}

	public ModelAndView validate(ModelAndView modelAndView, SegUsuario user) {
		SegUsuario userExists = findUserByEmail(user.getEmail());
		if (Objects.nonNull(userExists)) {
			modelAndView.addObject("errorValidate",	"Já existe um usuário cadastrado com esse email: " + user.getEmail());
			modelAndView.setViewName("account/register");
			return modelAndView;
		}
		
		if(maiorIdade(user.getDataNascimento()) < 18) {
			modelAndView.addObject("errorValidate",	"Idade mínima elegível de 18 anos. ");
			modelAndView.setViewName("account/register");
			return modelAndView;
		}
		
		String cidade = viaCepService.getCidade(user.getSegUsuarioProfile().getAuxEndereco().getCep());
		if(!CidadeEnum.GOIANIA.getDescricao().equals(cidade)) {
			modelAndView.addObject("errorValidate",	"Cidade informada não é elegível. Apenas região metropolitana de Goiânia.");
			modelAndView.setViewName("account/register");
			return modelAndView;
		}
		
		return null;
	}
	
	public static int maiorIdade(final Date dataAniversario) {
	    return Period.between(LocalDateTime.ofInstant(dataAniversario.toInstant(), ZoneOffset.UTC).toLocalDate(), LocalDate.now()).getYears();
	}
	

}
