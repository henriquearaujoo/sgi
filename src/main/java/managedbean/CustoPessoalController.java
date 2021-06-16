package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.Column;

import model.CategoriaDespesaClass;
import model.ContaBancaria;
import model.CustoPessoal;
import model.DespesaReceita;
import model.LancamentoAcao;
import model.ProjetoRubrica;
import model.Reembolso;
import model.RubricaOrcamento;
import model.TipoGestao;
import model.TipoLocalidade;
import model.TipoParcelamento;
import service.CustoPessoalService;
import util.Filtro;
import util.UsuarioSessao;

@Named(value = "custo_pessoal_controller")
@ViewScoped
public class CustoPessoalController implements Serializable {

	private static final long serialVersionUID = 1L;

	private Filtro filtro = new Filtro();

	@Inject
	private CustoPessoal custoPessoal;

	@Inject
	private CustoPessoalService service;

	@Inject
	private LancamentoAcao lancamentoAcao;

	@Inject
	private Reembolso reembolso;

	@Inject
	private UsuarioSessao usuarioSessao;

	private List<String> competencias = new ArrayList<>();

	private List<CategoriaDespesaClass> categorias = new ArrayList<>();

	private List<CustoPessoal> lista = new ArrayList<>();

	private List<CustoPessoal> listaSelected = new ArrayList<>();

	private List<Reembolso> listaReembolsoSelected = new ArrayList<>();

	private List<Reembolso> reembolsos = new ArrayList<>();

	private List<LancamentoAcao> listaResumo;

	private BigDecimal valorTotalResumo;
	
	@Inject
	private ContaBancaria contaRecebedor;

	private Long idDoacaoReembolsada;
	
	private Long idRubricaOrcamento;
	
	private Long idFonteReembolsada;

	private String competencia;

	public CustoPessoalController() {
	}

	public void initCadastro() {
		carregarCategorias();
		carregarCompetencias();
	}

	public void initListagem() {
		listar();
	}

	public void initListagemReembolso() {
		listarReembolso();
	}

	public void mostrarResumo() {
		limparListaResumo();
		carregarListaResumo();
		calcularValoTotalResumo();
	}

	public void calcularValoTotalResumo() {
		valorTotalResumo = BigDecimal.ZERO;
		for (LancamentoAcao resumo : listaResumo) {
			valorTotalResumo = valorTotalResumo.add(resumo.getValor());
		}
	}

	public String buscarValorTotal(BigDecimal valor) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		if (valor == null) {
			return "";
		} else {
			return format.format(valor);
		}
	}

	public void carregarListaResumo() {
		listaResumo = service.carregarListaResumo(listaReembolsoSelected);
	}

	public void limparListaResumo() {
		listaResumo = new ArrayList<>();
	}

	public void filtrar() {
		listar();
	}

	public void filtrarReembolso() {
		listarReembolso();
	}

	public void prepararReembolso() {
		calcularTotalReembolso();
		carregarIdDoacaoRecebedor();
		carregarIdLinhaOrcamentaria();
		carregarIdFonteRecebedor();
		carregarIdContaRecebedor();
		if (reembolso.getLancamentosAcoes() == null) {
			reembolso.setLancamentosAcoes(new ArrayList<>());
		}
	}

	private BigDecimal totalReembolso;

	public void calcularTotalReembolso() {
		totalReembolso = BigDecimal.ZERO;
		for (CustoPessoal custo : listaSelected) {
			totalReembolso = totalReembolso.add(custo.getValorTotalComDesconto());

		}


	}

	public void carregarIdContaRecebedor() {
		if (!listaSelected.isEmpty()) {
			CustoPessoal aux = service.findById(listaSelected.get(0).getId());
			contaRecebedor = aux.getLancamentosAcoes().get(0).getRubricaOrcamento().getOrcamento().getContaBancaria();
		}
	}

	public void carregarIdDoacaoRecebedor() {
		if (!listaSelected.isEmpty()) {
			CustoPessoal aux = service.findById(listaSelected.get(0).getId());
			idDoacaoReembolsada = aux.getLancamentosAcoes().get(0).getRubricaOrcamento().getOrcamento().getId();
			competencia = aux.getCompetencia();
		}
	}
	
	public void carregarIdLinhaOrcamentaria() {
		if (!listaSelected.isEmpty()) {
			CustoPessoal aux = service.findById(listaSelected.get(0).getId());
			idRubricaOrcamento = aux.getLancamentosAcoes().get(0).getRubricaOrcamento().getId();
			competencia = aux.getCompetencia();
		}
	}
	
	
	public void carregarIdFonteRecebedor() {
		if (!listaSelected.isEmpty()) {
			CustoPessoal aux = service.findById(listaSelected.get(0).getId());
			idFonteReembolsada = aux.getLancamentosAcoes().get(0).getRubricaOrcamento().getOrcamento().getFonte().getId();
			competencia = aux.getCompetencia();
		}
	}

	public void carregarCompetencias() {
		Calendar calendar = Calendar.getInstance();
		String ano1 = String.valueOf(calendar.get(Calendar.YEAR) - 1);
		String anoAtual = String.valueOf(calendar.get(Calendar.YEAR));
		int contAno1 = 12;

		for (int i = 11; i <= contAno1; i++) {
			competencias.add(i + "/" + ano1);
		}

		int contAnoAtual = 12;

		for (int i = 1; i <= contAnoAtual; i++) {
			if (i < 10) {
				competencias.add("0" + i + "/" + anoAtual);
			} else {
				competencias.add(i + "/" + anoAtual);
			}
		}
	}

	public String salvar() {

		if (custoPessoal.getId() == null) {
			custoPessoal.setCodigo(gerarCodigo());
			custoPessoal.setSolicitante(usuarioSessao.getUsuario().getColaborador());
			custoPessoal.setDepesaReceita(DespesaReceita.DESPESA);
			custoPessoal.setLocalidade(service.findLocalidadeByIdDefault());
			custoPessoal.setGestao(service.findGestaoByIdDefault());
			custoPessoal.setQuantidadeParcela(1);
			

		}

		custoPessoal.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		custoPessoal.setTipoGestao(TipoGestao.COORD);
		custoPessoal.setTipoLocalidade(TipoLocalidade.SEDE);
		custoPessoal.setVersionLancamento("MODE01");

		custoPessoal = service.salvar(custoPessoal, usuarioSessao.getUsuario());
		if (custoPessoal.getId() == null) {
			limpar();
			return "";
		}

		return "custo_pessoal.xhtml?faces-redirect=true";

	}

	public String salvarReembolso() {

		if (reembolso.getId() == null) {
			reembolso.setCodigo(gerarCodigo());
			reembolso.setSolicitante(usuarioSessao.getUsuario().getColaborador());
			reembolso.setDepesaReceita(DespesaReceita.DESPESA);
			reembolso.setLocalidade(service.findLocalidadeByIdDefault());
			reembolso.setGestao(service.findGestaoByIdDefault());
			reembolso.setQuantidadeParcela(1);
		}

		reembolso.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
		reembolso.setTipoGestao(TipoGestao.COORD);
		reembolso.setTipoLocalidade(TipoLocalidade.SEDE);
		reembolso.setVersionLancamento("MODE01");
		reembolso.setDescricao("Reembolso de custo de pessoal competência: " + competencia);
		reembolso.setCompetencia(competencia);

		reembolso = service.salvarReembolso(reembolso, usuarioSessao.getUsuario(), contaRecebedor, idDoacaoReembolsada,
				listaSelected, idFonteReembolsada, idRubricaOrcamento);
		if (reembolso.getId() == null) {
			limpar();
			return "";
		}
		return "custo_pessoal.xhtml?faces-redirect=true";
	}

	

	public void carregarCategorias() {
		categorias = service.buscarGategoriasCusteioPessoal();
	}

	public void limpar() {
		custoPessoal = new CustoPessoal();
	}

	public void remover() {	 
		service.remover(custoPessoal);
		listar();
	}

	public void removerReembolso() {
		if (!listaReembolsoSelected.isEmpty()) {
			service.removerReembolso(listaReembolsoSelected);
			addMessage("", "Removido!", FacesMessage.SEVERITY_INFO);
		} else {
			addMessage("", "Não existem itens selecionados", FacesMessage.SEVERITY_WARN);
		}

		listarReembolso();
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String gerarCodigo() {

		StringBuilder s = new StringBuilder("SP");

		Calendar calendar = Calendar.getInstance();
		int ano = calendar.get(Calendar.YEAR);
		int mes = calendar.get(Calendar.MONTH) + 1;
		int dia = calendar.get(Calendar.DAY_OF_MONTH);
		int minuto = calendar.get(Calendar.MINUTE);
		int segundo = calendar.get(Calendar.SECOND);
		int milisegundo = calendar.get(Calendar.MILLISECOND);
		
		s.append(String.valueOf(ano).substring(2));
		s.append(String.valueOf(mes));
		s.append(String.valueOf(dia));
		s.append(String.valueOf(minuto));
		s.append(String.valueOf(segundo));
		// s.append(String.valueOf(milisegundo).substring(0, 2));

		return s.toString();
	}

	public void listar() {
		lista = service.getTodos(filtro);
	}

	public void listarReembolso() {
		reembolsos = service.getTodosReembolsos(filtro);
	}

	public void adicionarNovaAcao() {
		if (custoPessoal.getValorTotalComDesconto() == null) {
			custoPessoal.setValorTotalComDesconto(BigDecimal.ZERO);
		}

		lancamentoAcao.setDespesaReceita(DespesaReceita.DESPESA);
		
		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		//lancamentoAcao.setOrcamento(lancamentoAcao.getRubricaOrcamento().getOrcamento());
		
		custoPessoal.setValorTotalComDesconto(custoPessoal.getValorTotalComDesconto().add(lancamentoAcao.getValor()));
		lancamentoAcao.setLancamento(custoPessoal);
		custoPessoal.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();
	}

	public void adicionarRecursoDeReembolso() {
		if (reembolso.getValorTotalComDesconto() == null) {
			reembolso.setValorTotalComDesconto(BigDecimal.ZERO);
		}

		lancamentoAcao.setDespesaReceita(DespesaReceita.DESPESA);
		// lancamentoAcao.setProjeto(lancamentoAcao.getProjetoRubrica().getProjeto());
		// lancamentoAcao.setOrcamento(lancamentoAcao.getRubricaOrcamento().getOrcamento());
		reembolso.setValorTotalComDesconto(reembolso.getValorTotalComDesconto().add(lancamentoAcao.getValor()));
		lancamentoAcao.setLancamento(reembolso);
		reembolso.getLancamentosAcoes().add(lancamentoAcao);
		lancamentoAcao = new LancamentoAcao();
	}

	public void removerAcao(int index) {
		BigDecimal valor = custoPessoal.getLancamentosAcoes().get(index).getValor();
		LancamentoAcao la = custoPessoal.getLancamentosAcoes().get(index);

		custoPessoal.setValorTotalComDesconto(custoPessoal.getValorTotalComDesconto().subtract(valor));
		custoPessoal.getLancamentosAcoes().remove(index);
		lancamentoAcao = new LancamentoAcao();

	}

	public void removerRecursoDeReembolso(int index) {
		BigDecimal valor = reembolso.getLancamentosAcoes().get(index).getValor();
		LancamentoAcao la = reembolso.getLancamentosAcoes().get(index);

		reembolso.setValorTotalComDesconto(custoPessoal.getValorTotalComDesconto().subtract(valor));
		custoPessoal.getLancamentosAcoes().remove(index);
		lancamentoAcao = new LancamentoAcao();

	}

	public List<RubricaOrcamento> completeRubricasDeOrcamento(String s) {
		return service.completetRubricasDeOrcamento(s);
	}

	public List<ContaBancaria> completeConta(String query) {
		return service.getAllConta(query);

	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public CustoPessoal getCustoPessoal() {
		return custoPessoal;
	}

	public void setCustoPessoal(CustoPessoal custoPessoal) {
		this.custoPessoal = custoPessoal;
	}

	public List<CustoPessoal> getLista() {
		return lista;
	}

	public void setLista(List<CustoPessoal> lista) {
		this.lista = lista;
	}

	public LancamentoAcao getLancamentoAcao() {
		return lancamentoAcao;
	}

	public void setLancamentoAcao(LancamentoAcao lancamentoAcao) {
		this.lancamentoAcao = lancamentoAcao;
	}

	public List<CategoriaDespesaClass> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaDespesaClass> categorias) {
		this.categorias = categorias;
	}

	public List<String> getCompetencias() {
		return competencias;
	}

	public void setCompetencias(List<String> competencias) {
		this.competencias = competencias;
	}

	public List<CustoPessoal> getListaSelected() {
		return listaSelected;
	}

	public void setListaSelected(List<CustoPessoal> listaSelected) {
		this.listaSelected = listaSelected;
	}

	public BigDecimal getTotalReembolso() {
		return totalReembolso;
	}

	public void setTotalReembolso(BigDecimal totalReembolso) {
		this.totalReembolso = totalReembolso;
	}

	public Reembolso getReembolso() {
		return reembolso;
	}

	public void setReembolso(Reembolso reembolso) {
		this.reembolso = reembolso;
	}

	public List<Reembolso> getListaReembolsoSelected() {
		return listaReembolsoSelected;
	}

	public List<Reembolso> getReembolsos() {
		return reembolsos;
	}

	public void setReembolsos(List<Reembolso> reembolsos) {
		this.reembolsos = reembolsos;
	}

	public void setListaReembolsoSelected(List<Reembolso> listaReembolsoSelected) {
		this.listaReembolsoSelected = listaReembolsoSelected;
	}

	public List<LancamentoAcao> getListaResumo() {
		return listaResumo;
	}

	public void setListaResumo(List<LancamentoAcao> listaResumo) {
		this.listaResumo = listaResumo;
	}

	public BigDecimal getValorTotalResumo() {
		return valorTotalResumo;
	}

	public void setValorTotalResumo(BigDecimal valorTotalResumo) {
		this.valorTotalResumo = valorTotalResumo;
	}

}
