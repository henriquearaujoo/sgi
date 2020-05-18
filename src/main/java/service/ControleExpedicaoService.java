package service;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;

import anotacoes.Transactional;
import model.Colaborador;
import model.ControleExpedicao;
import model.Estado;
import model.Fornecedor;
import model.Gestao;
import model.ItemPedido;
import model.LancamentoAcao;
import model.Localidade;
import model.Pedido;
import model.TermoExpedicao;
import repositorio.ControleExpedicaoRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import repositorio.TermoExpedicaoPedidoRepositorio;
import util.Filtro;

public class ControleExpedicaoService implements Serializable{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject ControleExpedicaoRepositorio repositorio;
	private @Inject TermoExpedicaoPedidoRepositorio termoRepositorio;
	private @Inject GestaoRepositorio gestaoRepositorio;
	
	@Inject
	private LocalRepositorio localRepositorio;
	
	public ControleExpedicaoService(){}
	
	public List<ControleExpedicao> getExpedicoes(Filtro filtro){
		return repositorio.getExpedicoes(filtro);
	}
	
	public List<Gestao> getGestaoAutoComplete(String query) {
		return gestaoRepositorio.buscarGestaoAutocomplete(query);
	}
	
	public Long getNumeroTermo(Long id) {
		return repositorio.getNumeroTermo(id);
	}
	
	@Transactional
	public ControleExpedicao salvar(ControleExpedicao controleExpedicao){
		return repositorio.salvar(controleExpedicao);
	}
	
	@Transactional
	public TermoExpedicao salvarTermo(TermoExpedicao termo){
		return termoRepositorio.salvarTermo(termo);
	}
	
	public ControleExpedicao findControleById(Long id){
		return repositorio.findByid(id);
	}
	
	public String buscarUC(Long id){
		return repositorio.buscarUC(id);
	}
	
	public List<ItemPedido> buscarItens(Long id){
		return repositorio.buscarItens(id);
	}
	
	public List<Pedido> buscarPedidos(Filtro filtro){
		return repositorio.buscarPedidos(filtro);
	}
	
	public Pedido getPedido(Long id){
		return repositorio.getPedido(id);
	}
	
	public List<ItemPedido> getItensPedidosByPedido(Pedido pedido){
		return repositorio.getItensPedidosByPedido(pedido);
	}
	
	public List<LancamentoAcao> getLancamentosDoPedido(Pedido pedido){
		return repositorio.getLancamentosDoPedido(pedido);
	}
	
	
	public List<Estado> getEstados(Filtro filtro) {
		return repositorio.getEatados(filtro);
	}
	
	public TermoExpedicao getTermoById(Long id){
		return termoRepositorio.getTermoById(id);
	}
	
	public List<ItemPedido> itensPedido(TermoExpedicao expedicao) {
		return termoRepositorio.getItensPedidoByTermo(expedicao);
	}
	
	public List<Localidade> getMunicipioByEstado(Long id) {
		return repositorio.getMunicipioByEstado(id);
	}
	
	public List<Fornecedor> getFornecedores(String s) {
		return repositorio.getFornecedores(s);
	}
	
	public List<Colaborador> getSolicitantes(String s) {
		return repositorio.getSolicitantes(s);
	}
	
	@Transactional
	public void salvaLocalidade(Localidade localidade) {
		localRepositorio.salvar(localidade);
		
	}

}
