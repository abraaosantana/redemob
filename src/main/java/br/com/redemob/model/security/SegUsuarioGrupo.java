package br.com.redemob.model.security;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SEG_USUARIO_GRUPO", schema = "rocket")
@NamedNativeQuery(name = "SegUsuarioGrupo.findAll", query = "SELECT s FROM SegUsuarioGrupo s")
public class SegUsuarioGrupo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id
	@EmbeddedId
	private SegUsuarioGrupoPK id;

	public SegUsuarioGrupo() {
	}

	@Override
	public String toString() {
		return "SegUsuarioGrupo{" +	"id=" + id +'}';
	}
}