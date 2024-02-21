package br.com.redemob.model.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import br.com.redemob.model.auxiliar.AuxEndereco;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "SEG_USUARIO_PROFILE", schema = "rocket")
@NamedNativeQuery(name = "UsuarioProfile.findAll", query = "SELECT u FROM SegUsuarioProfile u")
public class  SegUsuarioProfile implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEG_USUARIO_PROFILE_GEN", schema="rocket", sequenceName = "SEG_USUARIO_PROFILE_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEG_USUARIO_PROFILE_GEN")
	private Long id;

    private long version = 0;

    private boolean active = true;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

	private String celular;

	@Column(name = "telefone_fixo")
	private String telefoneFixo;

	private String profissao;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "FK_SEG_USUARIO_PROFILE_USUARIO_ID"))
	private SegUsuario segUsuario;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_endereco_id", foreignKey = @ForeignKey(name = "FK_SEG_USUARIO_PROFILE_ENDERECO_ID"))
	private AuxEndereco auxEndereco;

	public SegUsuarioProfile() {
	}

	@Override
	public String toString() {
		return "SegUsuarioProfile{" +
				"id=" + id +
				", version=" + version +
				", active=" + active +
				", dateCreated=" + dateCreated +
				", lastUpdated=" + lastUpdated +
				", celular='" + celular + '\'' +
				", telefoneFixo='" + telefoneFixo + '\'' +
				", profissao='" + profissao + '\'' +
				", segUsuario=" + segUsuario +
				", auxEndereco=" + auxEndereco +
				'}';
	}
}