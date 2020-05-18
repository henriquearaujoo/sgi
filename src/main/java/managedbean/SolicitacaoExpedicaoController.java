package managedbean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import anotacoes.Transactional;
import model.MenuLateral;
import model.Pedido;
import model.SolicitacaoExpedicao;
import model.StatusSolicitacaoExpedicao;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import service.SolicitacaoExpedicaoPedidoService;
import util.DataUtil;
import util.Filtro;
import util.MakeMenu;
import util.UsuarioSessao;


@ViewScoped
@Named(value = "solicitacao_expedicao")
public class SolicitacaoExpedicaoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private @Inject SolicitacaoExpedicao solicitacao;
	private List<SolicitacaoExpedicao> solicitacoes = new ArrayList<>();
	private MenuLateral menu = new MenuLateral();
	private @Inject SolicitacaoExpedicaoPedidoService solicitacaoExpedicaoService;
	private Filtro filtro = new Filtro();
	private Long idPedido;
	private @Inject UsuarioSessao usuarioSessao;
	
	public SolicitacaoExpedicaoController(){}
	
	
	@PostConstruct
	public void init(){
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(new Date());
		carregarSolicitacoes();	
	}
	
	
	public void limparFiltro(){
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(new Date());
	}
	
	public void carregarSolicitacoes(){
		solicitacoes = solicitacaoExpedicaoService.getSolicitacoes(filtro);
	}
	
	public void iniciarNovaSolicitacao(){
		solicitacao = new SolicitacaoExpedicao();
		solicitacao.setPedidos(new ArrayList<>());
	}
	
	public void adicionarNovoPedido(){
		
		// Busca pedido pelo id
		Pedido pedido = solicitacaoExpedicaoService.getPedido(idPedido);
		// Se não existe exibe mensagem
		if (pedido == null) {
			addMessage("", "Pedido não encontrado, por favor verifique se o código está correto.", FacesMessage.SEVERITY_ERROR);
			return;
		}
		
		// Verifica se ja tem o pedido na lista
		// Se ja houver exibe mensagem
		if (solicitacao.getPedidos().stream().filter(ped -> ped.getId().longValue() == pedido.getId().longValue()).findAny().isPresent()) {
			addMessage("", "O Pedido já se encontra na lista.", FacesMessage.SEVERITY_WARN);
			return;
		}
		
		if(pedido.getSolicitacaoExpedicao() != null){
			addMessage("", "O Pedido já se encontra em outra solicitação de expedição com p número: "+pedido.getSolicitacaoExpedicao().getId().toString()+".", FacesMessage.SEVERITY_ERROR);
			return;
		}
		
		//Adicionar o pedido
		pedido.setSolicitacaoExpedicao(solicitacao);
		solicitacao.getPedidos().add(pedido);
		
		addMessage("", "Pedido salvo na lista.", FacesMessage.SEVERITY_INFO);
		
		
	}
	
	public void prepararEdicao(){
		solicitacao = solicitacaoExpedicaoService.getSolicitacaoById(solicitacao.getId());
		solicitacao.setPedidos(new ArrayList<>());
		solicitacao.setPedidos(solicitacaoExpedicaoService.getPedidosBySolicitacao(solicitacao.getId()));
	}
	
	public void removerSolicitacao(){
		solicitacaoExpedicaoService.removerPedidosBySolicitacao(solicitacao.getId());
		solicitacaoExpedicaoService.removerSolicitacao(solicitacao);
		init();
	}
	
	public void salvarSolicitacao(){
		
		if (solicitacao.getPedidos().isEmpty()) {
			addMessage("", "Por favor adicione pedidos a sua solicitação.",FacesMessage.SEVERITY_ERROR);
			return ;
		}
		
		solicitacao.setDataEmissao(new Date());
		solicitacao.setUsuario(usuarioSessao.getUsuario());
		solicitacao.setStatusSolicitacao(StatusSolicitacaoExpedicao.N_INICIADO);
		
		solicitacao = solicitacaoExpedicaoService.salvar(solicitacao);
		solicitacao = new SolicitacaoExpedicao();
		solicitacao.setPedidos(new ArrayList<>());
		addMessage("", "Solicitação salva com sucesso!",FacesMessage.SEVERITY_INFO);
		
		init();
		
		//return "solicitacao_expedicao?faces-redirect=true";
	}
	
	@Transactional
	public void aceitarSolicitacao(){
	   solicitacao.setStatusSolicitacao(StatusSolicitacaoExpedicao.ACOLHIDO);
	   solicitacaoExpedicaoService.aceitarSolicitacao(solicitacao);
	   solicitacaoExpedicaoService.salvarPedidosEControlesExpedicao(solicitacao);
	   init(); 
	}
	
	public void removerPedidoDaSolicitacao(int index){
		Pedido pedido = solicitacao.getPedidos().get(index);
		solicitacao.getPedidos().remove(index);
		if (pedido.getId() != null) {
			solicitacaoExpedicaoService.ajustaDependendiasDoPedido(pedido);
		}

	}
 
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuLogistica();
	}
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}


	public SolicitacaoExpedicao getSolicitacao() {
		return solicitacao;
	}


	public void setSolicitacao(SolicitacaoExpedicao solicitacao) {
		this.solicitacao = solicitacao;
	}


	public List<SolicitacaoExpedicao> getSolicitacoes() {
		return solicitacoes;
	}


	public void setSolicitacoes(List<SolicitacaoExpedicao> solicitacoes) {
		this.solicitacoes = solicitacoes;
	}
	
	


	public MenuLateral getMenu() {
		return menu;
	}



	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}


	public Filtro getFiltro() {
		return filtro;
	}


	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Long getIdPedido() {
		return idPedido;
	}


	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}
	
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	
}
