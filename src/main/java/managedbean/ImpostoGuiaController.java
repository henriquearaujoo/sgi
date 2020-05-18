
package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.context.RequestContext;
import org.primefaces.PrimeFaces;
import org.primefaces.model.DualListModel;

import model.AtividadeProjeto;
import model.ContaBancaria;
import model.GuiaImposto;
import model.Imposto;
import model.LancamentoAcao;
import model.Projeto;
import model.ProjetoRubrica;
import model.TipoImposto;
import repositorio.CalculatorRubricaRepositorio;
import service.ImpostoService;
import service.ProjetoService;
import util.FilterImposto;
import util.Filtro;
import util.UsuarioSessao;

@Named(value = "imposto_guia")
@ViewScoped
public class ImpostoGuiaController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private DualListModel<Imposto> impostos;

	@Inject
	private UsuarioSessao usuarioSessao;

	@Inject
	private GuiaImposto guia;

	@Inject
	private ImpostoService service;

	private Projeto projetoAux;

	private Long idConta;
	private TipoImposto tipoImposto;
	
	private FilterImposto filter = new FilterImposto();

	private List<Projeto> listaProjeto;

	private List<ContaBancaria> contaFas = new ArrayList<>();
	private List<ContaBancaria> contaForaFas = new ArrayList<>();
	
	private List<GuiaImposto> guias = new ArrayList<>();

	@PostConstruct
	public void init() {
		carregarListPickList();
		carregarContas();
		carregarGuias();
	}
	
	public void carregarGuias() {
		guias = service.buscarGuias();
	}

	public void carregarListPickList() {
		List<Imposto> impostoSource = getListaImpostos();
		List<Imposto> impostoTarget = new ArrayList<>();
		impostos = new DualListModel<Imposto>(impostoSource, impostoTarget);
	}
	
	public void prepararEdicaoGuia() {
		//guia = new GuiaImposto();
		guia = service.buscarGuiaPorId(guia.getId());
		carregarPickListEditor();
	}
	
	public void carregarPickListEditor() {
		
		filter = new FilterImposto();
		List<Imposto> impostoSource = getListaImpostos(); 
		
		filter.setIdGuia(guia.getId());
		List<Imposto> impostoTarget = getListaImpostos();
		
		impostos = new DualListModel<Imposto>(impostoSource, impostoTarget);
		
		limpar();
	}
	
	public void limpar() {
		filter = new FilterImposto();
	}
	
	public void filtrar() {
		carregarListPickList();
	}

	public List<Imposto> getListaImpostos() {
		return service.buscarImpostos(filter);
	}

	public TipoImposto[] valuesImposto() {
		return TipoImposto.values();
	}

	public void carregarContas() {
		contaFas = service.getContasFas();
		contaForaFas = service.getContasForaFas();
	}

	public void salvarGuia() {

		// lancamentoAcao.setLancamento(guia);
		// guia.setLancamentosAcoes(new ArrayList<>());
		// guia.getLancamentosAcoes().add(lancamentoAcao);
		service.salvarGuia(guia, usuarioSessao.getUsuario());
		carregarGuias();
		carregarListPickList();
	}
	
	

	private LancamentoAcao lancamentoAcao = new LancamentoAcao();
	private List<ProjetoRubrica> listaDeRubricasProjeto = new ArrayList<>();
	private List<AtividadeProjeto> listaAtividades;

	@Inject
	private CalculatorRubricaRepositorio calculatorRubricaRepositorio;

	public void listaRubricasAndAtividades() {

		if (projetoAux == null) {
			lancamentoAcao = new LancamentoAcao();
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Você deve selecionar um projeto antes de buscar rubrica! ");
			FacesContext.getCurrentInstance().addMessage(null, message);
			listaDeRubricasProjeto = new ArrayList<ProjetoRubrica>();
			setListaAtividades(new ArrayList<AtividadeProjeto>());
		} else {
			listaDeRubricasProjeto = calculatorRubricaRepositorio.getProjetoRubricaByProjeto(projetoAux.getId());
			// findAtividadesByProjetoWithSaldo();
		}
	}

	private @Inject ProjetoService projetoService;

	private Filtro filtro = new Filtro();

	public void carregarProjetoByUsuario() {
		// listaProjeto =
		// projetoService.getProjetosByUsuario(usuarioSessao.getUsuario());

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			listaProjeto = projetoService.getProjetosbyUsuarioProjeto(null, filtro);
			return;
		}

		listaProjeto = projetoService.getProjetosbyUsuarioProjeto(usuarioSessao.getUsuario(), filtro);
	}

	public void gerarGuia() {

		List<Imposto> list = this.impostos.getTarget();
		guia = service.gerarGuia(guia, list);
		;
		carregarContas();
		carregarProjetoByUsuario();

		if (list.size() > 0) {
			openDialog("PF('guia-dlg').show();");
		} else {
			addMessage("", "Adicione um imposto para gerar guia", FacesMessage.SEVERITY_WARN);
		}

	}

	public void openDialog(String nomeDialog) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript(nomeDialog);
	}

	public DualListModel<Imposto> getImpostos() {
		return impostos;
	}

	public void setImpostos(DualListModel<Imposto> impostos) {
		this.impostos = impostos;
	}

	public GuiaImposto getGuia() {
		return guia;
	}

	public void setGuia(GuiaImposto guia) {
		this.guia = guia;
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<ContaBancaria> getContaFas() {
		return contaFas;
	}

	public void setContaFas(List<ContaBancaria> contaFas) {
		this.contaFas = contaFas;
	}

	public List<ContaBancaria> getContaForaFas() {
		return contaForaFas;
	}

	public void setContaForaFas(List<ContaBancaria> contaForaFas) {
		this.contaForaFas = contaForaFas;
	}

	public Projeto getProjetoAux() {
		return projetoAux;
	}

	public void setProjetoAux(Projeto projetoAux) {
		this.projetoAux = projetoAux;
	}

	public List<Projeto> getListaProjeto() {
		return listaProjeto;
	}

	public void setListaProjeto(List<Projeto> listaProjeto) {
		this.listaProjeto = listaProjeto;
	}

	public LancamentoAcao getLancamentoAcao() {
		return lancamentoAcao;
	}

	public void setLancamentoAcao(LancamentoAcao lancamentoAcao) {
		this.lancamentoAcao = lancamentoAcao;
	}

	public List<ProjetoRubrica> getListaDeRubricasProjeto() {
		return listaDeRubricasProjeto;
	}

	public void setListaDeRubricasProjeto(List<ProjetoRubrica> listaDeRubricasProjeto) {
		this.listaDeRubricasProjeto = listaDeRubricasProjeto;
	}

	public List<AtividadeProjeto> getListaAtividades() {
		return listaAtividades;
	}

	public void setListaAtividades(List<AtividadeProjeto> listaAtividades) {
		this.listaAtividades = listaAtividades;
	}

	public Long getIdConta() {
		return idConta;
	}

	public void setIdConta(Long idConta) {
		filter.setIdConta(idConta);
		this.idConta = idConta;
	}

	public TipoImposto getTipoImposto() {
		return tipoImposto;
	}

	public void setTipoImposto(TipoImposto tipoImposto) {
		filter.setTipo(tipoImposto);
		this.tipoImposto = tipoImposto;
	}

	public List<GuiaImposto> getGuias() {
		return guias;
	}

	public void setGuias(List<GuiaImposto> guias) {
		this.guias = guias;
	}

}
