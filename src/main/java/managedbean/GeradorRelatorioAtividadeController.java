package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.util.ArrayList;
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
import model.Gestao;
import model.LancamentoAuxiliar;
import model.MenuLateral;
import model.Orcamento;
import model.PlanoDeTrabalho;
import model.Projeto;
import model.TipoRelatorioProjeto;
import service.ColaboradorService;
import service.ContaService;
import service.OrcamentoService;
import service.ProjetoService;
import util.Coluna;
import util.Filtro;
import util.GeradorRelatorioAtividade;
import util.GeradorRelatorioGeralDeProjeto;
import util.GeradorRelatorioLinhaOrcamentaria;
import util.GeradorRelatorioUtilXLS;
import util.MakeMenu;
import util.Util;

@Named(value = "report_atividade")
@ViewScoped
public class GeradorRelatorioAtividadeController implements Serializable {

	private static final long serialVersionUID = 1L;

	private MenuLateral menu = new MenuLateral();
	private List<LancamentoAuxiliar> lancamentos;

	private List<Gestao> gestoes = new ArrayList<>();

	private String textoInformativo;

	private @Inject ColaboradorService colaboradorService;

	private List<Coluna> listaColunas;

	private Filtro filtro = new Filtro();

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

	public GeradorRelatorioAtividadeController() {

	}

	@PostConstruct
	public void init() {
		limpar();
		prepararFiltro();
		carregarColunas();
		filtro.setTipoRelatorio("ATIVIDADE");
	}

	public void modificarTexto(String field) {

		textoInformativo = "Em desenvolvimento. As informações de filtros estão sendo normalizadas para que o usuário tenha a melhor experiência possível na geração de relatórios. Obrigado! ^>.<^";

		// TODO: Elizabeth desenvolver os textos explicativos
		// switch (field) {
		// case "tipo":
		// textoInformativo = "Tipo";
		// break;
		// case "data":
		// textoInformativo = "Período";
		// break;
		// case "fonte":
		// textoInformativo = "Fonte";
		// break;
		// case "doacao":
		// textoInformativo = "Doação";
		// break;
		// case "plano":
		// textoInformativo = "Plano";
		// break;
		// case "projeto":
		// textoInformativo = "Projeto";
		// break;
		// default:
		// textoInformativo = "Coluna";
		// break;
		// }

	}

	public void prepararFiltro() {
		carregarProjetos();
		carregarListaGestao();
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

	public void gerarRelatorioAtividade() throws WriteException, IOException, NumberFormatException, ParseException {

		if (filtro.getTipoRelatorio() == null || filtro.getTipoRelatorio().equals("")) {
			filtro.setTipoRelatorio("ATIVIDADE");
		}

		GeradorRelatorioLinhaOrcamentaria geradorLinha;
		String path = "C:/temp/relatorios/orcamentoExecutado.xls";
		switch (filtro.getTipoRelatorio()) {
		case "PROJETO":
			GeradorRelatorioGeralDeProjeto geradorProjeto = new GeradorRelatorioGeralDeProjeto();
			geradorProjeto.setOutputFile(path);
			geradorProjeto.gerarRelatorioGeralDeProjetos(filtro, projetoService);
			break;
		case "ATIVIDADE":
			GeradorRelatorioAtividade gerador = new GeradorRelatorioAtividade();
			gerador.setOutputFile(path);
			// filtro.setProjetoId(137l);
			gerador.gerarRelatorioDeAtividade(filtro, projetoService);
			break;
		}
	}
	
	private List<PlanoDeTrabalho> planos = new ArrayList<>();
	
	public void carregarPlanos() {
		planos = projetoService.getPlanos();
	}

	public void carregarListaGestao() {
		gestoes = colaboradorService.findGestao();
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

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
	}

	public List<PlanoDeTrabalho> getPlanos() {
		return planos;
	}

	public void setPlanos(List<PlanoDeTrabalho> planos) {
		this.planos = planos;
	}

}
