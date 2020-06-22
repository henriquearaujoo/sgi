package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.model.StreamedContent;

import jxl.write.WriteException;
import model.Acao;
import model.Componente;
import model.ContaBancaria;
import model.FontePagadora;
import model.LancamentoAuxiliar;
import model.MenuLateral;
import model.Orcamento;
import model.PlanoDeTrabalho;
import model.Projeto;
import model.TipoRelatorioProjeto;
import service.ContaService;
import service.OrcamentoService;
import service.ProjetoService;
import util.Coluna;
import util.DataUtil;
import util.Filtro;
import util.GeradorRelatorioLinhaOrcamentaria;
import util.GeradorRelatorioUtilXLS;
import util.MakeMenu;
import util.Util;

@Named(value = "report_financ_controller")
@ViewScoped
public class ExecucaoFinanceiraController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private MenuLateral menu = new MenuLateral();
	private List<LancamentoAuxiliar> lancamentos;

	private String textoInformativo;
	
	private List<Coluna> listaColunas;

	private Filtro filtro = new Filtro();

	public List<Orcamento> getOrcamentos() {
		return orcamentos;
	}

	public void setOrcamentos(List<Orcamento> orcamentos) {
		this.orcamentos = orcamentos;
	}

	private @Inject ContaService contaService;
	private @Inject OrcamentoService orcService;
	private @Inject ProjetoService projetoService;

	private Boolean displayColunas = true;

	private Workbook wb = new HSSFWorkbook();

	private List<ContaBancaria> contas = new ArrayList<>();
	private List<FontePagadora> fontes = new ArrayList<>();
	private List<Projeto> projetos = new ArrayList<>();
	private List<Acao> acoes = new ArrayList<>();

	private StreamedContent file;

	private TipoRelatorioProjeto tipoRelatorio;

	private List<Orcamento> orcamentos = new ArrayList<>();

	private List<PlanoDeTrabalho> listPlanoDeTrabalho = new ArrayList<>();

	public ExecucaoFinanceiraController() {

	}

	@PostConstruct
	public void init() {
		limpar();
		// carregarContasBancaria();
		prepararFiltrosParaConferenciaDeLancamentos();
		carregarColunas();
		filtro.setTipoRelatorio("PADRAO");
	}
	
	public void modificarTexto(String field) {
		
		textoInformativo = "Em desenvolvimento. As informações de filtros estão sendo normalizadas para que o usuário tenha a melhor experiência possível na geração de relatórios. Obrigado! ^>.<^";
		
		
		//TODO: Elizabeth desenvolver os textos explicativos
//		switch (field) {
//		case "tipo":
//			textoInformativo = "Tipo";
//			break;
//		case "data":
//			textoInformativo = "Período";
//			break;
//		case "fonte":
//			textoInformativo = "Fonte";
//			break;
//		case "doacao":
//			textoInformativo = "Doação";
//			break;
//		case "plano":
//			textoInformativo = "Plano";
//			break;
//		case "projeto":
//			textoInformativo = "Projeto";
//			break;
//		default:
//			textoInformativo = "Coluna";
//			break;
//		}
		
		
	}

	public void prepararFiltrosParaConferenciaDeLancamentos() {
		carregarContasBancaria();
		carregarFontes();
		carregarProjetos();
		carregarAções();
		carregarDoacoes();
		carregarPlanos();
	}

	public void carregarColunas() {
		listaColunas = new ArrayList<>();
		listaColunas = Util.getColunasPadraoReportOR();
	}

	public void atualizarColunas() {

		switch (filtro.getTipoRelatorio()) {
		case "LINHA":
			displayColunas = true;
			listaColunas = Util.getColunasReportLINHA();
			break;
		case "CONSOLIDADO":
			displayColunas = false;
			break;
		default:
			listaColunas = Util.getColunasPadraoReportOR();
			displayColunas = true;
			break;
		}
	}

	public void carregarPlanos() {
		listPlanoDeTrabalho = orcService.getPlanos();
	}

	// Os orçamentos foram renomeados para doação depois de já termos criado a
	// classe// REFATORAR ASSIM QUE POSSÍVEL, OU SEJA, NUNCA MAIS NA VIDA
	public void carregarDoacoes() {
		orcamentos = orcService.getOrcamentosFilter(new Filtro());
	}

	public void gerarConferenciaDeLancamentos() throws WriteException, IOException {
		GeradorRelatorioUtilXLS gerador = new GeradorRelatorioUtilXLS();
		gerador.setOutputFile("/home/developer/Documents/import/conferencia_lancamento.xls");
		// gerador.setOutputFile("C:/temp/relatorios/conferencia.xls");
		// gerador.gerarConferenciaDeLancamentos(filtro, contaService);
		gerador.gerarXLSbyPOI(filtro, contaService);
	}

	// Esse relatório se chama execução financeira, no entanto no começo da análise
	// o cliente chamava de Conferência de lançamentos, que não tem tanto conceito
	// quanto execução
	public void gerarConferenciaDeLancamentosMODE01() throws WriteException, IOException {
		GeradorRelatorioUtilXLS gerador = new GeradorRelatorioUtilXLS();
		// gerador.setOutputFile("/home/developer/Documents/import/conferencia_lancamento.xls");
		gerador.setOutputFile("C:/temp/relatorios/conferencia.xls");
		// gerador.gerarConferenciaDeLancamentos(filtro, contaService);
		gerador.gerarXLSdeConferencia(filtro, orcService);
	}

	// TODO: ORÇADO X EXECUTADO
	public void gerarOrcamentoExecutado() throws WriteException, IOException, NumberFormatException, ParseException {
		
				
		if (filtro.getTipoRelatorio() == null || filtro.getTipoRelatorio().equals("")) {
			filtro.setTipoRelatorio("default");
		}

		GeradorRelatorioLinhaOrcamentaria geradorLinha; 
		String path = "C:/temp/relatorios/orcamentoExecutado.xls";
		switch (filtro.getTipoRelatorio()) {
		case "LINHA":
			geradorLinha = new GeradorRelatorioLinhaOrcamentaria();
			geradorLinha.setOutputFile(path);
			geradorLinha.gerarRelatorioPORL(filtro, projetoService);
			break;
		case "CONSOLIDADO":
			GeradorRelatorioUtilXLS gerador = new GeradorRelatorioUtilXLS();
			gerador.setOutputFile(path);
			gerador.gerarXLSdeOrcamentoExecutadoConsolidado(filtro, projetoService);
			break;
		default:
			geradorLinha = new GeradorRelatorioLinhaOrcamentaria();
			geradorLinha.setOutputFile(path);
			geradorLinha.gerarXLSdeOrcamentoExecutadoLinhaOrcamentaria(filtro, projetoService);
			break;
		}
	}
	
	public void gerarRelatorioDeExecucao() throws WriteException, IOException, NumberFormatException, ParseException {
		
		
		GeradorRelatorioLinhaOrcamentaria geradorLinha; 
		String path = "C:/temp/relatorios/orcamentoExecutado.xls";
		switch (filtro.getTipoRelatorio()) {
		case "LINHA":
			geradorLinha = new GeradorRelatorioLinhaOrcamentaria();
			geradorLinha.setOutputFile(path);
			geradorLinha.gerarRelatorioPORL(filtro, projetoService);
			break;
		case "CONSOLIDADO":
			GeradorRelatorioUtilXLS gerador = new GeradorRelatorioUtilXLS();
			gerador.setOutputFile(path);
			gerador.gerarXLSdeOrcamentoExecutadoConsolidado(filtro, projetoService);
			break;
		default:
			geradorLinha = new GeradorRelatorioLinhaOrcamentaria();
			geradorLinha.setOutputFile(path);
			geradorLinha.gerarXLSdeOrcamentoExecutadoLinhaOrcamentaria(filtro, projetoService);
			break;
		}
	}

	// TODO ATUALIZAR DOAÇÕES
	public void atualizarDoacoes() {
		orcamentos = orcService.getOrcamentosFilter(filtro);
	}

	public void filtrar() throws WriteException, IOException {
		lancamentos = new ArrayList<>();
		List<Orcamento> orcamentosAux = orcService.getOrcamentosFilter(filtro);
		for (Orcamento orcamento : orcamentosAux) {
			filtro.setIdDoacao(orcamento.getId());
			lancamentos.addAll(orcService.getExtratoCtrlDoacaoParaConferencia(filtro));
		}
		// filtro.setIdDoacao(new Long(1));
		// lancamentos.addAll(orcService.getExtratoCtrlDoacaoParaConferencia(filtro));
	}

	public void filtrarByProject() throws WriteException, IOException, NumberFormatException, ParseException {
		lancamentos = new ArrayList<>();
		List<Projeto> projetosAux = orcService.getProjetosFilter(filtro);
		for (Projeto projeto : projetosAux) {
			filtro.setProjetoId(projeto.getId());
			lancamentos.addAll(projetoService.getExtratoCtrlProjetoOrcamentoExecutado(filtro));
		}
		// filtro.setIdDoacao(new Long(1));
		// lancamentos.addAll(orcService.getExtratoCtrlDoacaoParaConferencia(filtro));
	}

	public List<LancamentoAuxiliar> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoAuxiliar> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public void carregarContasBancaria() {
		contas = contaService.getContas(filtro);
	}

	public void carregarFontes() {
		fontes = contaService.buscarFontes();
	}

	public void carregarProjetos() {
		projetos = contaService.getProjetosMODE01(filtro);
	}

	public void carregarProjetosMODE01() {
		projetos = contaService.getProjetosMODE01();
	}

	public void carregarAções() {
		acoes = contaService.getListAcao(filtro);
	}

	public StreamedContent getFile() {
		return file;
	}

	public TipoRelatorioProjeto[] getTipoRelatorios() {
		return TipoRelatorioProjeto.values();
	}

	public void limpar() {
		filtro = new Filtro();
		// filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		// filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
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

	public Componente[] getComponentes() {
		return Componente.values();
	}

	public TipoRelatorioProjeto getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(TipoRelatorioProjeto tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public List<FontePagadora> getFontes() {
		return fontes;
	}

	public void setFontes(List<FontePagadora> fontes) {
		this.fontes = fontes;
	}

	public List<Projeto> getProjetos() {
		return projetos;
	}

	public void setProjetos(List<Projeto> projetos) {
		this.projetos = projetos;
	}

	public List<Acao> getAcoes() {
		return acoes;
	}

	public void setAcoes(List<Acao> acoes) {
		this.acoes = acoes;
	}

	public List<PlanoDeTrabalho> getListPlanoDeTrabalho() {
		return listPlanoDeTrabalho;
	}

	public void setListPlanoDeTrabalho(List<PlanoDeTrabalho> listPlanoDeTrabalho) {
		this.listPlanoDeTrabalho = listPlanoDeTrabalho;
	}

	public List<Coluna> getListaColunas() {
		return listaColunas;
	}

	public void setListaColunas(List<Coluna> listaColunas) {
		this.listaColunas = listaColunas;
	}

	public Boolean getDisplayColunas() {
		return displayColunas;
	}

	public void setDisplayColunas(Boolean displayColunas) {
		this.displayColunas = displayColunas;
	}

	public String getTextoInformativo() {
		return textoInformativo;
	}

	public void setTextoInformativo(String textoInformativo) {
		this.textoInformativo = textoInformativo;
	}

}
