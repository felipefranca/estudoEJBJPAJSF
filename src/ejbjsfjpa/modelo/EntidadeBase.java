package ejbjsfjpa.modelo;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public abstract class EntidadeBase implements Serializable {

	private static final long serialVersionUID = -3091092131669273433L;
	
	public abstract Long getId();
	
	@Temporal(TemporalType.DATE)
	@Column(name="DATA_CADASTRO", nullable = false)
	protected Date dataCadastro;

	public Date getDataCadastro() {
		return dataCadastro;
	}

	public void setDataCadastro(Date dataCadastro) {
		this.dataCadastro = dataCadastro;
	}
	
}
