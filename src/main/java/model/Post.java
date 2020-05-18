package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "post")
public class Post implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String titulo;
	
	@Column(name = "link_reuniao")
	private String linkReuniao;
	
	@Column(name = "link_arquivo")
	private String linkArquivo;
	
	@Column
	private String texto;
	
	@Column(name = "data_publicacao")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataPublicacao;
	
	@Column(name = "data_inicio")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataInicio;
	
	@Column(name = "data_fim")
	@Temporal(TemporalType.TIMESTAMP)
	private Date dataFim;
	
	@ManyToOne
	private Gestao gestao;
	
	@ManyToOne
	private User usuario;
	
	
	public Post() {
		
	}


	public Long getId() {
		return id;
	}


	public void setId(Long id) {
		this.id = id;
	}


	public String getTitulo() {
		return titulo;
	}


	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}


	public String getLinkReuniao() {
		return linkReuniao;
	}


	public void setLinkReuniao(String linkReuniao) {
		this.linkReuniao = linkReuniao;
	}


	public String getLinkArquivo() {
		return linkArquivo;
	}


	public void setLinkArquivo(String linkArquivo) {
		this.linkArquivo = linkArquivo;
	}


	public String getTexto() {
		return texto;
	}


	public void setTexto(String texto) {
		this.texto = texto;
	}


	public Date getDataPublicacao() {
		return dataPublicacao;
	}


	public void setDataPublicacao(Date dataPublicacao) {
		this.dataPublicacao = dataPublicacao;
	}


	public Date getDataInicio() {
		return dataInicio;
	}


	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}


	public Date getDataFim() {
		return dataFim;
	}


	public void setDataFim(Date dataFim) {
		this.dataFim = dataFim;
	}


	public Gestao getGestao() {
		return gestao;
	}


	public void setGestao(Gestao gestao) {
		this.gestao = gestao;
	}


	public User getUsuario() {
		return usuario;
	}


	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}
	
	
	
}
