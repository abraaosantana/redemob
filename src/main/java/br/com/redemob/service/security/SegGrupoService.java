package br.com.redemob.service.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import br.com.redemob.model.security.SegGrupo;
import br.com.redemob.model.security.SegUsuario;
import br.com.redemob.repository.security.GrupoRepository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Service
public class SegGrupoService {

    @Autowired
    @Qualifier("GrupoRepository")
    GrupoRepository grupoRepository;

    @Autowired
    @Qualifier("SegUsuarioService")
    SegUsuarioService userService;


    public void saveAndFlush(SegGrupo grupo) {
        Date dataAtual = new Date();
        grupo.setDateCreated(dataAtual);
        grupo.setLastUpdated(dataAtual);
        grupo.setActive(true);
        if (grupo.getVersion() > 0) grupo.setVersion(grupo.getVersion() + 1);
        grupo.setName(grupo.getName().toUpperCase());
        grupoRepository.saveAndFlush(grupo);
    }

    public ModelAndView editAndFlush(SegGrupo grupo, ModelAndView modelAndView) {

        List<SegUsuario> userList = userService.findByGrupo(grupo);
        SegGrupo grupoInstance = grupoRepository.findById(grupo.getId()).get();

        if(!userList.isEmpty()) {
            modelAndView.addObject("errorMessage", "O grupo não pode ser editado pois existem usuários vinculados.");
        } else {
            Date dataAtual = new Date();
            grupo.setDateCreated(grupoInstance.getDateCreated());
            grupo.setLastUpdated(dataAtual);
            if (grupo.getVersion() >= 0) grupo.setVersion(grupo.getVersion() + 1);
            grupo.setName(grupo.getName().toUpperCase());
            grupoRepository.saveAndFlush(grupo);
            modelAndView.addObject("successMessage", "O grupo " + grupo.getName() + " foi salvo com sucesso.");
        }
        return modelAndView;
    }

    public RedirectAttributes deleteGrupo(Long id, RedirectAttributes attributes) {

        SegGrupo grupo = getOne(id);
        List<SegUsuario> userList = userService.findByGrupo(grupo);

        if(!userList.isEmpty()) {
            attributes.addFlashAttribute("errorMessage", "O grupo " + grupo.getName() +" não pode ser excluído pois existem usuários vinculados.");
        } else {
            grupoRepository.deleteById(id);
            attributes.addFlashAttribute("successMessage", "O grupo " + grupo.getName() + " foi excluído com sucesso.");
        }

        return attributes;
    }
    
    public List<SegGrupo> findAll() {
        return grupoRepository.findAll();
    }

    public SegGrupo findGrupoByName(String name) {
        return grupoRepository.getByName(name);
    }

    public List<SegGrupo> findGrupoListByName(String name) {
        return grupoRepository.findByName(name);
    }

    public SegGrupo getOne(Long id) {
        return grupoRepository.findById(id).get();
    }
}
