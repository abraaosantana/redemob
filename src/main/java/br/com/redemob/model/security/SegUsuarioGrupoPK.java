package br.com.redemob.model.security;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
@Table(schema = "rocket")
public class SegUsuarioGrupoPK implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "usuario_id", insertable = false, updatable = false)
	private Long usuarioId;

	@Column(name = "grupo_id", insertable = false, updatable = false)
	private Long grupoId;

	public SegUsuarioGrupoPK() {
	}

}