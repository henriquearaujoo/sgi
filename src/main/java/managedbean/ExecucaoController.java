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
import org.primefaces.model.StreamedContent;
import jxl.write.WriteException;
import model.Componente;
import model.FontePagadora;
import model.LancamentoAuxiliar;
import model.MenuLateral;
import model.Projeto;
import model.RelatorioContasAPagar;
import model.TipoRelatorioProjeto;
import service.ExecucaoFinanceiraService;
import util.Coluna;
import util.Filtro;
import util.GeradorExecucaoFinanceira;
import util.GeradorRelatorioLinhaOrcamentaria;
import util.GeradorRelatorioUtilXLS;
import util.MakeMenu;
import util.Util;

@Named(value = "execucaoController")
@ViewScoped
public class ExecucaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject ExecucaoFinanceiraService service;

	private List<LancamentoAuxiliar> lancamentos;
	private List<Coluna> listaColunas;
	private Filtro filtro = new Filtro();
	private Boolean displayColunas = true;
	private List<FontePagadora> fontes = new ArrayList<>();
	private List<Projeto> projetos = new ArrayList<>();

	private StreamedContent file;

	private TipoRelatorioProjeto tipoRelatorio;

	public ExecucaoController() {

	}

	@PostConstruct
	public void init() {
		limpar();
		prepararFiltros();
		carregarColunas();
		filtro.setTipoRelatorio("PADRAO");
	}

	public void gerarOrcamentoExecutado() throws WriteException, IOException, NumberFormatException, ParseException {

		if (filtro.getFontes() != null && filtro.getFontes().length > 0) {
			GeradorExecucaoFinanceira gerador = new GeradorExecucaoFinanceira();
			String path = "C:/temp/relatorios/executado.xls";
			gerador = new GeradorExecucaoFinanceira();
			gerador.setOutputFile(path);
			gerador.gerarExecucao(service.getLancamentos(filtro));
			// geradorLinha.gerarRelatorioPORL(filtro, projetoService);
		}else {
			
			addMessage("", "Preencha no mínimo uma fonte de recurso", FacesMessage.SEVERITY_ERROR);
		}

	}

	public void carregarFontes() {
		fontes = service.buscarFontes();
	}

	public void carregarProjetos() {
		if (filtro.getFontes().length > 0) {
			projetos = service.getGroupProjectsByFonte(filtro);
		} else {

			projetos = new ArrayList<>();
		}

	}

	public void prepararFiltros() {
		carregarFontes();
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

	public List<LancamentoAuxiliar> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoAuxiliar> lancamentos) {
		this.lancamentos = lancamentos;
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

}
