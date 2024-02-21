package br.com.redemob.model.auxiliar;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedNativeQuery;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

/**
 * Entidade baseada nos dados do WS do viacep.com
 */
@Entity
@Getter
@Setter
@EqualsAndHashCode
@Table(name = "AUX_ENDERECO", schema = "rocket")
@NamedNativeQuery(name = "AuxEndereco.findAll", query = "SELECT u FROM AuxEndereco u")
public class AuxEndereco implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@SequenceGenerator(name = "AUX_ENDERECO_GEN", schema = "rocket", sequenceName = "AUX_ENDERECO_SEQ", initialValue = 1, allocationSize = 1)
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "AUX_ENDERECO_GEN")
	private Long id;

	private long version = 0;

	private boolean active = true;

	@Temporal(TemporalType.TIMESTAMP)
	private Date dateCreated;

	@Temporal(TemporalType.TIMESTAMP)
	private Date lastUpdated;
	private String cep;
	private String logradouro;
	private String complemento;
	private String bairro;
	private String localidade;
	private String numero;
	private String uf;
	private String unidade;
	private String ibge;
	private String gia;

	public AuxEndereco() {
	}

	@Override
	public String toString() {
		return "AuxEndereco{" +
				"id=" + id +
				", version=" + version +
				", active=" + active +
				", dateCreated=" + dateCreated +
				", lastUpdated=" + lastUpdated +
				", cep='" + cep + '\'' +
				", logradouro='" + logradouro + '\'' +
				", complemento='" + complemento + '\'' +
				", bairro='" + bairro + '\'' +
				", localidade='" + localidade + '\'' +
				", numero='" + numero + '\'' +
				", uf='" + uf + '\'' +
				", unidade='" + unidade + '\'' +
				", ibge='" + ibge + '\'' +
				", gia='" + gia + '\'' +
				'}';
	}
}
