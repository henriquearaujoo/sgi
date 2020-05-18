package model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="categoria_despesa")
public class CategoriaDespesaClass implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column
	private String chaveEnum;
	@Column
	private String nome;
	@Column
	private Boolean saneado;
	
	@Column
	private String tipoCategoria; //COMPRA, FINANCEIRO
	
	@ManyToOne
	private CategoriaDespesaClass CategoriaPai;
	
	@Enumerated(EnumType.STRING)
	private TipoDeCategoria tipo;
	
	public CategoriaDespesaClass(){}
	
	public CategoriaDespesaClass(Long id, String nome){
		this.id = id;
		this.nome = nome;
	}
	
	public Long getId() {
		return id;
	}

	
	
	public void setId(Long id) {
		this.id = id;
	}

	public String getChaveEnum() {
		return chaveEnum;
	}

	public void setChaveEnum(String chaveEnum) {
		this.chaveEnum = chaveEnum;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CategoriaDespesaClass other = (CategoriaDespesaClass) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	public CategoriaDespesaClass getCategoriaPai() {
		return CategoriaPai;
	}

	public void setCategoriaPai(CategoriaDespesaClass categoriaPai) {
		CategoriaPai = categoriaPai;
	}

	public TipoDeCategoria getTipo() {
		return tipo;
	}

	public void setTipo(TipoDeCategoria tipo) {
		this.tipo = tipo;
	}

	public Boolean getSaneado() {
		return saneado;
	}

	public void setSaneado(Boolean saneado) {
		this.saneado = saneado;
	}

	public String getTipoCategoria() {
		return tipoCategoria;
	}

	public void setTipoCategoria(String tipoCategoria) {
		this.tipoCategoria = tipoCategoria;
	}
	
	
	
}
