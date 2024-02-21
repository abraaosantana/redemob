package br.com.redemob.model.security;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedNativeQueries;
import javax.persistence.NamedNativeQuery;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "SEG_USUARIO", schema = "rocket", uniqueConstraints = { @UniqueConstraint(name = "UK_SEG_USUARIO_CPF", columnNames = "cpf"),
		                                                                 @UniqueConstraint(name = "UK_SEG_USUARIO_EMAIL", columnNames = "email") })
@NamedNativeQueries( { @NamedNativeQuery(name = "SegUsuario.findAll", query = "SELECT u FROM SegUsuario u"),
                       @NamedNativeQuery(name = "SegUsuario.findPorId", query = "SELECT u FROM SegUsuario u where u.id = :id"),
		               @NamedNativeQuery(name = "SegUsuario.findPorEmail", query = "SELECT u FROM SegUsuario u where u.email = :email"),
		               @NamedNativeQuery(name = "SegUsuario.findPorToken", query = "SELECT u FROM SegUsuario u where u.token = :token") })
public class SegUsuario implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEG_USUARIO_GEN", schema = "rocket", sequenceName = "SEG_USUARIO_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEG_USUARIO_GEN")
	private Long id;

    private long version;

    private boolean active;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastUpdated;

	@CPF(message = "*Informe um CPF válido!")
	private String cpf;

	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dataNascimento;

	@NotEmpty(message = "*Informe seu nome completo!")
	@Pattern(message = "*Informe apenas letras no campo nome completo!", regexp = "^[A-Za-z áàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]*$")
	private String nomeCompleto;

	@NotEmpty(message = "*Informe o nome completo da sua mãe!")
	@Pattern(message = "*Informe apenas letras no campo Nome completo Mãe!", regexp = "^[A-Za-z áàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]*$")
	private String nomeCompletoMae;
	
	@NotEmpty(message = "*Informe um email!")
	@Email(message = "*Iforme um email válido!")
	private String email;

	@NotEmpty(message = "*Informe sua senha!")
	private String password;

	private String token;

	@Temporal(TemporalType.DATE)
	private Date validadeToken;

    public SegUsuarioProfile getSegUsuarioProfile() {
        return segUsuarioProfile;
    }

    public void setSegUsuarioProfile(SegUsuarioProfile segUsuarioProfile) {
        this.segUsuarioProfile = segUsuarioProfile;
    }

    @OneToOne(mappedBy = "segUsuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private SegUsuarioProfile segUsuarioProfile;
    
	// bi-directional many-to-many association to SegGrupo
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(schema = "rocket", name = "SEG_USUARIO_GRUPO"
                                   , joinColumns = { @JoinColumn(name = "usuario_id") }
	                               , foreignKey = @ForeignKey(name = "FK_SEG_USUARIO_USUARIO_ID")
	                               , inverseJoinColumns = { @JoinColumn(name = "grupo_id") }
	                               , inverseForeignKey = @ForeignKey(name = "FK_SEG_USUARIO_GRUPO_ID"))
	private List<SegGrupo> segGrupos;

	public SegUsuario() {
	}

	public boolean isUser() {
		return segGrupos.stream().map(g -> g.getName()).anyMatch(u -> u.equals("USER"));
	}
	
	public boolean isAdmin() {
		return segGrupos.stream().map(g -> g.getName()).anyMatch(u -> u.equals("ADMIN"));
	}
	
}
