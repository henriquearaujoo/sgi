package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Acao;
import model.ContaBancaria;
import model.FontePagadora;
import model.Fornecedor;
import model.LancamentoAuxiliar;
import model.LancamentoAvulso;
import model.MenuLateral;
import model.Orcamento;
import model.OrcamentoProjeto;
import model.PagamentoLancamento;
import model.PagamentoPE;
import model.Projeto;
import model.StatusPagamentoLancamento;
import model.TipoParcelamento;
import service.PagamentoService;
import util.ArquivoLancamento;
import util.DataUtil;
import util.DownloadUtil;
import util.Filtro;
import util.MakeMenu;

@Named(value="lancamento_controller")
@ViewScoped
public class LancamentoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/***
	 * Classe controladora dos pagamentos
	 */
	
	//Variáveis de classe
	private @Inject PagamentoLancamento pagamento;
	private PagamentoLancamento pagamentoLancAuxiliar = new PagamentoLancamento();
	private @Inject ContaBancaria conta;
	private @Inject PagamentoService service;
	private MenuLateral menu = new MenuLateral();
	private LancamentoAuxiliar lancAuxiliar;
	private Filtro filtro = new Filtro(); 
	
	//Variáveis primitivas		
	private BigDecimal totalApagar = BigDecimal.ZERO;
	private BigDecimal totalFaltaPagar = BigDecimal.ZERO;
	private BigDecimal totalPago = BigDecimal.ZERO;
	//Listas
	
	private List<PagamentoLancamento> pagamentos;
	private List<ContaBancaria> contas;
	private List<LancamentoAuxiliar> lancamentos;
	private List<FontePagadora> fontes;
	private List<Projeto> projetos;
	private List<Acao> acoes;
	private List<Fornecedor> fornecedores;
	private List<PagamentoPE> pagamentosPE;
	private List<Acao> allAcoes = new ArrayList<Acao>();
	private List<PagamentoPE> selectedpagamentosPE;
	
	private LancamentoAvulso lancamentoAvulso;
	
	private List<ContaBancaria> allContas = new  ArrayList<>(); 
	
	private List<LancamentoAvulso> avulsos = new ArrayList<>();
	
	private Boolean prepararDialog = false;
	
	
	private List<LancamentoAvulso> lancamentosAvulsos = new ArrayList<>();
	
	public LancamentoController(){
		
	}
	

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public List<ContaBancaria> completeConta(String query) {
		allContas = new ArrayList<>();;
		allContas = service.getAllConta(query);
		return allContas;
	}
	
	
	public List<Acao> completeAcoes(String query) {
		allAcoes = new ArrayList<Acao>();
		allAcoes = service.acoesAutoComplete(query);
		return allAcoes;
	}
	
	private List<Projeto> listProject = new ArrayList<>(); 
	
	public List<Projeto> completeProjetos(String query) {
		listProject = new ArrayList<Projeto>();
		listProject = service.getProjetoAutoCompleteMODE01(query);
		return listProject;
	}
	
	private List<Acao> allAcoesInPagto = new ArrayList<>();
	
	public List<Acao> completeAcoesInPagto(String query) {
		allAcoesInPagto = new ArrayList<Acao>();
		allAcoesInPagto = service.acoesAutoComplete(query);
		return allAcoesInPagto;
	}
	
	public void visualizarArquivo(String id) throws IOException {
		
		
		ArquivoLancamento arqAuxiliar = new ArquivoLancamento(); 
		
		DownloadUtil.downloadFile(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}
	
	public List<MenuLateral> getMenus() {

		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Solicitaçao de Pagamento", "pagamento", "fa fa-shopping-cart");
		lista.add(menu);
		menu = new MenuLateral("Solicitacao de Viagem", "SolicitacaoViagem", "fa fa-fighter-jet");
		lista.add(menu);
		menu = new MenuLateral("Solicitação de Diaria", "SolicitacaoDiaria", "fa fa-user");
		lista.add(menu);
		menu = new MenuLateral("Lançamentos", "lancamentos", "fa fa-money");
		lista.add(menu);
		menu = new MenuLateral("Lançamentos avulsos", "lancamento_avulso", "fa fa-money");
		lista.add(menu);

		return MakeMenu.getMenuFinanceiro();
	}
	
	
	@PostConstruct
	public void init(){
		limpar();
		//carregarLancamentosNãoProvisionados();
		carregarPagamentosPE();
		//carregarProjetos();
		//carregarAcoes();
		//carregarContasFiltro();
		//carregarFornecedores();
	}
	

	public void prepararLancamentoAvulso(){
		lancamentoAvulso = new LancamentoAvulso();
	}
	
	public void salvarPagamento(){
		pagamento.setStt(StatusPagamentoLancamento.PROVISIONADO);
		pagamento.setDataEmissao(new Date());
		service.salvar(pagamento);
		pagamento =  new PagamentoLancamento();
		prepararDialogPagamento();
		carregarPagamentos();
		carregarPagamentosPE();
	}
	
	public void prepararLancamentoAvulsso(){}
	
	public void editarPagamento(){
		service.editarPagamento(pagamentoLancAuxiliar);
		pagamentoLancAuxiliar = new PagamentoLancamento();
		prepararDialogPagamento();
	}
	
	public void editarPagamentoTab2(){
		service.editarPagamento(pagamentoLancAuxiliar);
		pagamentoLancAuxiliar = new PagamentoLancamento();
		carregarPagamentosPE();
	}
	
	public StatusPagamentoLancamento[] getStatusPagamento(){
		return StatusPagamentoLancamento.values();
	}
	
	private List<OrcamentoProjeto> orcamentosProjetos = new ArrayList<>();
	private List<Orcamento> orcamentos = new ArrayList<>();
	
	public void prepararEdicao(Long id){
		
		
		pagamentoLancAuxiliar = new PagamentoLancamento();
		pagamentoLancAuxiliar = service.findPagamentoById(id);
		orcamentosProjetos = service.getOrcamentoByProjeto(pagamentoLancAuxiliar.getLancamentoAcao().getProjetoRubrica().getProjeto().getId());
		
//		for (OrcamentoProjeto orcProjeto : orcamentosProjetos) {
//			orcamentos.add(orcProjeto.getOrcamento());
//		}
	}
	
	public void removerPagamento(){
		service.removerPagamento(pagamento);
		pagamento = new PagamentoLancamento();
		prepararDialogPagamento();
		
	}
	
	private List<FontePagadora> allFontes = new ArrayList<>();
	
	public List<FontePagadora> completeFonte(String query) {
		allFontes = new ArrayList<FontePagadora>();
		allFontes = service.fontesAutoComplete(query);
		return allFontes;
	}
	
	public void efetivar(Long id){
		service.efetivar(id);	
		carregarPagamentosPE();
	}
	
	public void efetivarMultiplos(){
		service.efetivar_02(selectedpagamentosPE);	
		carregarPagamentosPE();
		selectedpagamentosPE = new ArrayList<>();
	}
	
	public void provisionarMultiplos(){
		service.provisionar_02(selectedpagamentosPE);	
		carregarPagamentosPE();
		selectedpagamentosPE = new ArrayList<>();
	}
	
	public void prepararDialogPagamento(){
		carregarContas();
		carregarPagamentos();
		pagamento = new PagamentoLancamento();
		pagamento.setLancamentoAcao(service.buscarLancamentoAcao(lancAuxiliar.getIdAcaoLancamentoId()));
		pagamento.setQuantidadeParcela(lancAuxiliar.getQuantidadeParcela());
		pagamento.setTipoParcelamento(lancAuxiliar.getTipoParcelamento());
	}
	
	 public void postProcessXLS(Object document) {
//	        HSSFWorkbook wb = (HSSFWorkbook) document;
//	        HSSFSheet sheet = wb.getSheetAt(0);
//	        HSSFRow header = sheet.getRow(0);
//	         
//	        HSSFCellStyle cellStyle = wb.createCellStyle();  
//	        cellStyle.setFillForegroundColor(HSSFColor.YELLOW.index);
//	        cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
//	        short d  = 1;
//	        cellStyle.setBorderBottom(d);
//	        cellStyle.setBorderLeft(d);
//	        cellStyle.setBorderRight(d);
//	        cellStyle.setBorderTop(d);
//	        
//	        
//	        for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
//	            HSSFCell cell = header.getCell(i);
//	            cell.setCellStyle(cellStyle);
//	        }
//	        
	        
	    }
	
	public void carregarContas(){
		contas = new ArrayList<>();
		contas = service.getContas();
		totalApagar = lancAuxiliar.getValorPagoAcao();
	}
	
	public void carregarContasFiltro(){
		contas = new ArrayList<>();
		contas = service.getContasFiltro();
	}
	
	public void carregarFornecedores(){
		fornecedores = new ArrayList<>();
		fornecedores = service.getFornecedores();
	}
	
	public void carregarPagamentos(){
		pagamentos = service.getPagamentos(lancAuxiliar.getIdAcaoLancamentoId());
		totalFaltaPagar = totalApagar;
		totalPago = BigDecimal.ZERO;
		
		for (PagamentoLancamento pagto : pagamentos) {
			totalPago = totalPago.add(pagto.getValor());
			totalFaltaPagar = totalApagar.subtract(totalPago);
		}
	}
	
	public void carregarLancamentosNãoProvisionados(){
		lancamentos = new ArrayList<>();
		lancamentos = service.getLancamentosNãoProvisionadosMODE01(filtro);	
		//service.getPagamentosPE(filtro);
	}
	
	public void carregarPagamentosPE(){
		pagamentosPE = new ArrayList<>();
		pagamentosPE = service.getPagamentosPEMODE01(filtro);
	}
	
	public void limpar(){
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
	}

	public void carregarProjetos(){
		projetos = new ArrayList<>();
		projetos = service.getProjetos();
	}
	
	public void carregarAcoes(){
		acoes = new ArrayList<>();
		acoes = service.getAcoes();
	}
	
	public void carregarFontes(){
		fontes = new ArrayList<>();
		fontes = service.getFontePagadora();
	}
	
	public TipoParcelamento[] getParcelamentos(){
		return TipoParcelamento.values();
	}
	
	public PagamentoLancamento getPagamento() {
		return pagamento;
	}

	public void setPagamento(PagamentoLancamento pagamento) {
		this.pagamento = pagamento;
	}

	public List<PagamentoLancamento> getPagamentos() {
		return pagamentos;
	}

	public void setPagamentos(List<PagamentoLancamento> pagamentos) {
		this.pagamentos = pagamentos;
	}

	public ContaBancaria getConta() {
		return conta;
	}

	public void setConta(ContaBancaria conta) {
		this.conta = conta;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public List<ContaBancaria> getContas() {
		return contas;
	}

	public void setContas(List<ContaBancaria> contas) {
		this.contas = contas;
	}

	public List<LancamentoAuxiliar> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<LancamentoAuxiliar> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
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


	public BigDecimal getTotalApagar() {
		return totalApagar;
	}


	public void setTotalApagar(BigDecimal totalApagar) {
		this.totalApagar = totalApagar;
	}


	public BigDecimal getTotalFaltaPagar() {
		return totalFaltaPagar;
	}


	public void setTotalFaltaPagar(BigDecimal totalFaltaPagar) {
		this.totalFaltaPagar = totalFaltaPagar;
	}


	public BigDecimal getTotalPago() {
		return totalPago;
	}


	public void setTotalPago(BigDecimal totalPago) {
		this.totalPago = totalPago;
	}


	public LancamentoAuxiliar getLancAuxiliar() {
		return lancAuxiliar;
	}


	public void setLancAuxiliar(LancamentoAuxiliar lancAuxiliar) {
		this.lancAuxiliar = lancAuxiliar;
	}


	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}


	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}


	public List<PagamentoPE> getPagamentosPE() {
		return pagamentosPE;
	}


	public void setPagamentosPE(List<PagamentoPE> pagamentosPE) {
		this.pagamentosPE = pagamentosPE;
	}


	public PagamentoLancamento getPagamentoLancAuxiliar() {
		return pagamentoLancAuxiliar;
	}


	public void setPagamentoLancAuxiliar(PagamentoLancamento pagamentoLancAuxiliar) {
		this.pagamentoLancAuxiliar = pagamentoLancAuxiliar;
	}


	public List<LancamentoAvulso> getAvulsos() {
		avulsos = new ArrayList<>();
		avulsos.add(new LancamentoAvulso());
		avulsos.add(new LancamentoAvulso());
		return avulsos;
	}


	public void setAvulsos(List<LancamentoAvulso> avulsos) {
		this.avulsos = avulsos;
	}


	public LancamentoAvulso getLancamentoAvulso() {
		return lancamentoAvulso;
	}


	public void setLancamentoAvulso(LancamentoAvulso lancamentoAvulso) {
		this.lancamentoAvulso = lancamentoAvulso;
	}


	public List<ContaBancaria> getAllContas() {
		return allContas;
	}


	public void setAllContas(List<ContaBancaria> allContas) {
		this.allContas = allContas;
	}


	public List<PagamentoPE> getSelectedpagamentosPE() {
		return selectedpagamentosPE;
	}


	public void setSelectedpagamentosPE(List<PagamentoPE> selectedpagamentosPE) {
		this.selectedpagamentosPE = selectedpagamentosPE;
	}


	public List<Acao> getAllAcoesInPagto() {
		return allAcoesInPagto;
	}


	public void setAllAcoesInPagto(List<Acao> allAcoesInPagto) {
		this.allAcoesInPagto = allAcoesInPagto;
	}


	public List<OrcamentoProjeto> getOrcamentosProjetos() {
		return orcamentosProjetos;
	}


	public void setOrcamentosProjetos(List<OrcamentoProjeto> orcamentosProjetos) {
		this.orcamentosProjetos = orcamentosProjetos;
	}

}
