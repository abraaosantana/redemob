package br.com.redemob.model.security;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "SEG_GRUPO", schema = "rocket", uniqueConstraints = { @UniqueConstraint(name = "UK_SEG_GRUPO_NAME", columnNames = "name")})
@NamedNativeQuery(name = "SegGrupo.findAll", query = "SELECT s FROM SegGrupo s")
public class SegGrupo implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SEG_GRUPO_GEN", schema = "rocket", sequenceName = "SEG_GRUPO_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEG_GRUPO_GEN")
	private Long id;

    private long version;

    private boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;

    @NotBlank(message = "*Não pode estar em branco!")
    @NotEmpty(message = "*Não pode estar vazio!")
    @Pattern(message = "*Informe apenas letras!", regexp = "^[A-Za-z áàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ]*$")
    private String name;

    @ManyToMany(mappedBy="segGrupos", cascade = CascadeType.ALL)
    private List<SegUsuario> segUsuarios;

    public SegGrupo() {
    }

    @Override
    public String toString() {
        return "SegGrupo{" +
                "id=" + id +
                ", version=" + version +
                ", active=" + active +
                ", dateCreated=" + dateCreated +
                ", lastUpdated=" + lastUpdated +
                ", name='" + name + '\'' +
                '}';
    }
}