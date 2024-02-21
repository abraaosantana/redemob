package br.com.redemob.model.security;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import br.com.redemob.enums.SituacaoEnum;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "SOLICITACAO", schema = "rocket")
@NamedNativeQuery(name = "Solicitacao.findAll", query = "SELECT s FROM Solicitacao s")
public class Solicitacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "SOLICITACAO_GEN", schema = "rocket", sequenceName = "SOLICITACAO_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SOLICITACAO_GEN")
	private Long id;

    private long version;

    private boolean active;

    @Temporal(TemporalType.TIMESTAMP)
    private Date dateCreated;

    @Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;

    @NotBlank(message = "*Não pode estar em branco!")
    private String foto;
    
    private SituacaoEnum situacao = SituacaoEnum.ANALISE;
    
	@ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	@JoinColumn(name = "usuario_id", foreignKey = @ForeignKey(name = "FK_SOLICITACAO_USUARIO_ID"))
	private SegUsuario segUsuario;
    
    public Solicitacao() {
    }
    
    public String getSituacaoDescricao() {
    	return situacao.getDescricao();
    }

}