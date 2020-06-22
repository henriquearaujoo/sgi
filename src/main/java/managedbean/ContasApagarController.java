package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.jboss.weld.context.RequestContext;
import org.primefaces.model.StreamedContent;

import jxl.write.WriteException;
import model.Acao;
import model.Componente;
import model.ContaBancaria;
import model.Fornecedor;
import model.Gestao;
import model.LinkRelatorio;
import model.MenuLateral;
import model.Projeto;
import model.TipoRelatorioProjeto;
import service.ContaService;
import util.DataUtil;
import util.Filtro;
import util.GeradorOrcamentariaByAcao;
import util.JxlUtil;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "cp_controler")
@ViewScoped
public class ContasApagarController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MenuLateral menu = new MenuLateral();

	private Filtro filtro = new Filtro();

	private List<ContaBancaria> contas = new ArrayList<>();

	private List<ContaBancaria> selectedContas = new ArrayList<>();

	private List<Projeto> listProjetos = new ArrayList<>();

	private List<Gestao> gestoes = new ArrayList<>();

	private List<Acao> acoes = new ArrayList<>();

	private List<LinkRelatorio> listaDeRelatorios = new ArrayList<>();

	private LinkRelatorio link = new LinkRelatorio();

	@Inject
	private UsuarioSessao usuario;

	private @Inject ContaService contaService;

	private Long idProjeto;

	private Workbook wb = new HSSFWorkbook();

	private StreamedContent file;

	private TipoRelatorioProjeto tipoRelatorio;

	public ContasApagarController() {
	}

	@PostConstruct
	public void init() {
		limpar();
		carregarListaDeRelatorios();
		carregarContasBancaria();
		carregarProjetos();
		carregarGestoes();
	}

	public void carregarGestoes() {
		gestoes = contaService.getGestao();
	}

	public List<Projeto> getListProjetos() {

		return listProjetos;
	}

	public void setListProjetos(List<Projeto> listProjetos) {
		this.listProjetos = listProjetos;
	}

	public void carregarProjetos() {
		listProjetos = contaService.getProjetos();
	}

	public void carregarContasBancaria() {
		contas = contaService.getContas(filtro);
		carregarAções();
	}

	public void carregarAções() {
		acoes = contaService.getListAcao(filtro);
	}

	public void gerarContasApagar() throws WriteException, IOException {
		selectedContas = contaService.getContaSaldo(filtro);
		JxlUtil jxl = new JxlUtil();
		// jxl.setOutputFile("/home/developer/Documents/import/relatorio_cp.xls");
		jxl.setOutputFile("C:/temp/relatorios/relatorio.xls");
		jxl.insere(selectedContas, contaService, filtro);
	}

	public void carregarFiltroRelatorio() {

		switch (tipoRelatorio) {
		// case ORCADO_REALIZADO:
		// carregarDialogOrcadoRealizado();
		// break;
		case CONFERENCIA_LANCAMENTO:
			//carregarDialogConferenciaLancamentos();
			break;
		default:
			break;
		}
	}

//	public void carregarDialogOrcadoRealizado() {
//		Map<String, Object> option = new HashMap<>();
//		option.put("modal", true);
//		option.put("resizable", false);
//		option.put("contentHeight", 470);
//		RequestContext.getCurrentInstance().openDialog("filterOrcadoRealizado", option, null);
//	}

//	public void carregarDialogContasAPagar() {
//		Map<String, Object> option = new HashMap<>();
//		option.put("modal", true);
//		option.put("resizable", false);
//		option.put("contentHeight", 470);
//		RequestContext.getCurrentInstance().openDialog("contaApagar", option, null);
//	}

//	public void carregarDialogConferenciaLancamentos() {
//		Map<String, Object> option = new HashMap<>();
//		option.put("modal", true);
//		option.put("resizable", false);
//		option.put("contentHeight", 470);
//		RequestContext.getCurrentInstance().openDialog("filterConferenciaLancamento", option, null);
//	}

	public void gerarOrcamentariaDeAcoes() throws WriteException, IOException {
		acoes = contaService.getListAcoes(filtro);
		GeradorOrcamentariaByAcao gerador = new GeradorOrcamentariaByAcao();
		// gerador.setOutputFile("/home/developer/Documents/import/relatorio.xls");
		gerador.setOutputFile("C:/temp/relatorios/relatorio_acao.xls");
		gerador.insere(acoes, contaService, filtro);
	}

	public void gerarConferenciaDeLancamentos() throws WriteException, IOException {
		acoes = contaService.getListAcoes(filtro);
		GeradorOrcamentariaByAcao gerador = new GeradorOrcamentariaByAcao();
		// gerador.setOutputFile("/home/developer/Documentos/doc/bd/relatorio_acao.xls");
		gerador.setOutputFile("C:/temp/relatorios/conferencia.xls");
		// gerador.insere(acoes, contaService, filtro);
		gerador.gerarConferenciaLancamentos(acoes, contaService, filtro);
	}

	private List<Fornecedor> fornecedores = new ArrayList<>();

	public List<Fornecedor> completeFornecedor(String query) {
		fornecedores = new ArrayList<Fornecedor>();
		if (query.length() > 2)
			fornecedores = contaService.getFornecedores(query);
		return fornecedores;
	}

	// TODO: Relatório de conferência de lançamentos.
	public void gerarConferenciaDeLancamentosNOVO() {

	}

	/*
	 * public void FileDownloadView() throws WriteException, IOException { JxlUtil
	 * jxl = new JxlUtil();
	 * jxl.setOutputFile("/home/developer/Documentos/doc/bd/relatorio.xls"); // nome
	 * do arquivo; jxl.insere(selectedContas, contaService, filtro); InputStream
	 * stream = new
	 * FileInputStream("/home/developer/Documentos/doc/bd/relatorio.xls"); file =
	 * new DefaultStreamedContent(stream, "application/vnd.ms-excel",
	 * "relatorio.xls"); }
	 */

	public StreamedContent getFile() {
		return file;
	}

	public TipoRelatorioProjeto[] getTipoRelatorios() {
		return TipoRelatorioProjeto.values();
	}

	public void preparaPlanilha() throws WriteException, IOException {
		JxlUtil jxl = new JxlUtil();
		// jxl.setOutputFile("/home/developer/Documentos/doc/bd/relatorio.xls");
		// // nome do arquivo;
		jxl.setOutputFile("C:/temp/relatorios/relatorio.xls");
		jxl.insere(selectedContas, contaService, filtro);
		// jxl.insereTESTE(selectedContas, contaService, filtro);
	}

	public void limpar() {
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		filtro.setDataInicioEmissao(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinalEmissao(DataUtil.getDataInicio(new Date()));
	}

	public void carregarListaDeRelatorios() {
		/*if (usuario.getUsuario().getPerfil().getDescricao().equals("admin")) {
			ListarRelatoriosFinanceiro();
		}else {
			listarRelatorios();
		}*/
		
		ListarRelatoriosFinanceiro();
	}

	public void listarRelatorios() {
		LinkRelatorio link = new LinkRelatorio();

		link = new LinkRelatorio();
		link.setLabel("Orçado e Executado");
		link.setLink("orcado_realizadoMODE01");
		listaDeRelatorios.add(link);

		link = new LinkRelatorio();
		link.setLabel("Contas a pagar/receber");
		link.setLink("conta_pagarMODE01");
		listaDeRelatorios.add(link);

		link = new LinkRelatorio();
		link.setLabel("Execução financeira");
		link.setLink("conferencia_lancamentos_MODE01");
		listaDeRelatorios.add(link);
		
		link = new LinkRelatorio();
		link.setLabel("Saving");
		link.setLink("saving");
		listaDeRelatorios.add(link);
		
		link = new LinkRelatorio();
		link.setLabel("Relatório de diárias");
		link.setLink("relatorio_diaria");
		listaDeRelatorios.add(link);

		link = new LinkRelatorio();
		link.setLabel("Relatório de lançamentos de contas");
		link.setLink("relatorio_lancamentos_contas");
		listaDeRelatorios.add(link);
	}

	public void ListarRelatoriosFinanceiro() {
		LinkRelatorio link = new LinkRelatorio();

/*		link = new LinkRelatorio();
		link.setLabel("Orçado e Realizado");
		link.setLink("orcado_realizadoMODE01");
		listaDeRelatorios.add(link);
		
		
		link = new LinkRelatorio();
		link.setLabel("Relatório de atividades");
		link.setLink("relatorio_atividade");
		listaDeRelatorios.add(link);

		link = new LinkRelatorio();
		link.setLabel("Contas a pagar");
		link.setLink("conta_pagarMODE01");
		listaDeRelatorios.add(link);*/

		link = new LinkRelatorio();
		link.setLabel("Execução Financeira");
		link.setLink("rel_exec_financeiro");
		listaDeRelatorios.add(link);
		
		link = new LinkRelatorio();
		link.setLabel("Compras detalhadas");
		link.setLink("rel_compra_detail");
		listaDeRelatorios.add(link);

		
		/*link = new LinkRelatorio();
		link.setLabel("Saving");
		link.setLink("saving");
		listaDeRelatorios.add(link);
		
		
		link = new LinkRelatorio();
		link.setLabel("Relatório de diárias");
		link.setLink("relatorio_diaria");
		listaDeRelatorios.add(link);

		link = new LinkRelatorio();
		link.setLabel("Relatório de lançamentos de contas");
		link.setLink("relatorio_lancamentos_contas");*/
		
		//listaDeRelatorios.add(link);
	}

	public String redirecionarRelatorio() {
		return link.getLink() + "?faces-redirect=true";
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuFinanceiro();
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public List<ContaBancaria> getContas() {
		return contas;
	}

	public void setContas(List<ContaBancaria> contas) {
		this.contas = contas;
	}

	public List<ContaBancaria> getSelectedContas() {
		return selectedContas;
	}

	public void setSelectedContas(List<ContaBancaria> selectedContas) {
		this.selectedContas = selectedContas;
	}

	public List<Acao> getAcoes() {
		return acoes;
	}

	public Componente[] getComponentes() {
		return Componente.values();
	}

	public void setAcoes(List<Acao> acoes) {
		this.acoes = acoes;
	}

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
	}

	public TipoRelatorioProjeto getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorioProjeto tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public List<LinkRelatorio> getListaDeRelatorios() {
		return listaDeRelatorios;
	}

	public void setListaDeRelatorios(List<LinkRelatorio> listaDeRelatorios) {
		this.listaDeRelatorios = listaDeRelatorios;
	}

	public LinkRelatorio getLink() {
		return link;
	}

	public void setLink(LinkRelatorio link) {
		this.link = link;
	}

}
