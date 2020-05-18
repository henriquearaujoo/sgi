package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.Acao;
import model.Aprouve;
import model.Compra;
import model.Diaria;
import model.Estado;
import model.FontePagadora;
import model.ItemCompra;
import model.Lancamento;
import model.LancamentoAcao;
import model.Localidade;
import model.LogStatus;
import model.Pedido;
import model.Produto;
import model.SolicitacaoViagem;
import model.StatusCompra;
import model.User;
import repositorio.AcaoRepositorio;
import repositorio.AprouveRepositorio;
import repositorio.EstadoRepositorio;
import repositorio.FontePagadoraRepositorio;
import repositorio.LancamentoRepository;
import repositorio.LocalRepositorio;
import repositorio.PedidoRepositorio;
import repositorio.ProdutoRepositorio;
import repositorio.UsuarioRepository;
import util.Filtro;

/**
 * Autor: Italo Almeida Classe: Gestao de Projeto Objetivo: Gerir a regra de
 * negocio referente a instancia do objeto
 */

public class DiariaServiceNEW implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoRepository repositorio;
	
	public DiariaServiceNEW(){}
	
	
	public List<Diaria> getDiarias(SolicitacaoViagem viagem){
		return repositorio.findDiariasbyViagem(viagem); 
	}
	
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
