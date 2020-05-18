package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("com")
public class Comunidade extends Localidade{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	
	@ManyToOne
	private Municipio municipio;
	
	public Comunidade(String mascara){
		setMascara(mascara);
	}
	



	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}



	@Column
	private String setor;

	@Column
	private Integer nPessoa;
	
	@Column
	private Integer nfamilia;
	
	




	public Comunidade(){}
	
	public Comunidade(Long id, String nome, Municipio municipio){
		setId(id);
		setNome(nome);
		this.municipio = municipio;
	}
	
	public String getDescricaoComMunicipio(){
		if (municipio != null){
			return getNome() + " - " + municipio.getNome();
		}
		
		return getNome();
	}




	public Integer getnPessoa() {
		return nPessoa;
	}




	public void setnPessoa(Integer nPessoa) {
		this.nPessoa = nPessoa;
	}




	public String getSetor() {
		return setor;
	}




	public void setSetor(String setor) {
		this.setor = setor;
	}




	public Integer getNfamilia() {
		return nfamilia;
	}




	public void setNfamilia(Integer nfamilia) {
		this.nfamilia = nfamilia;
	}
}
