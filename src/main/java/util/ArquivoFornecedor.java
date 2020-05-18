package util;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import model.Fornecedor;
import model.TipoArquivo;
import model.User;

@Entity
@Table(name = "arquivo_fornecedor")
public class ArquivoFornecedor implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "nome")
	private String nome;
	
	@Enumerated(EnumType.STRING)
	private TipoArquivo tipo;
	

	@Column(name = "descricao")
	private String descricao;
	
	@Column(name = "conteudo")
	private byte[] conteudo;
	
	@ManyToOne
	private Fornecedor fornecedor;
	
	@Column
	private String path;
	
	@Temporal(TemporalType.DATE)
	private Date data;
	
	
	@ManyToOne
	private User usuario;
	

	public ArquivoFornecedor(){}
	
	public ArquivoFornecedor(Long id, String nome, Date data, String path){
		this.id =  id;
		this.nome = nome;
		this.data = data;
		this.path = path;
	}
	


	public Long getId() {
		return id;
	}
	
	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public byte[] getConteudo() {
		return conteudo;
	}

	public void setConteudo(byte[] conteudo) {
		this.conteudo = conteudo;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public TipoArquivo getTipo() {
		return tipo;
	}

	public void setTipo(TipoArquivo tipo) {
		this.tipo = tipo;
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

}