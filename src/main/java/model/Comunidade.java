package model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("com")
public class Comunidade extends Localidade {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Column
	private String setor;

	@Column
	private Integer nPessoa;

	@Column
	private Integer nfamilia;

	@ManyToOne
	private Municipio municipio;

	public Comunidade() {
	}

	public Comunidade(String mascara) {
		setMascara(mascara);
	}
	
	public Comunidade(Long id, String nome) {
		setId(id);
		setNome(nome);
	}

	public Comunidade(Long id, String nome, Municipio municipio) {
		setId(id);
		setNome(nome);
		this.municipio = municipio;
	}

	public Comunidade(Long id, String nome, String mascara, Long ucId, String ucNome, String ucMascara, String setor, Long munId,
			String munNome, Integer nPessoa, Integer nfamilia) {
		this.setId(id);
		this.setNome(nome);
		this.setMascara(mascara);
		this.setUnidadeConservacao(new UnidadeConservacao(ucId, ucNome, ucMascara));
		this.setSetor(setor);
		this.setMunicipio(new Municipio(munId, munNome));
		this.setnPessoa(nPessoa);
		this.setNfamilia(nfamilia);
	}

	public Municipio getMunicipio() {
		return municipio;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
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

	public String getDescricaoComMunicipio() {
		if (municipio != null) {
			return getNome() + " - " + municipio.getNome();
		}

		return getNome();
	}
}
