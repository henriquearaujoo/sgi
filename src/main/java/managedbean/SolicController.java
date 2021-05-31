package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.PrimeFaces;
import org.primefaces.component.tabview.TabView;

import model.Lancamento;
import service.AutorizacaoService;
import service.SolicService;
import util.ArquivoLancamento;
import util.DownloadUtil;
import util.UsuarioSessao;

@Named(value = "solic_controller")
@ViewScoped
public class SolicController implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private UsuarioSessao usuarioSessao;
	@Inject
	private ArquivoLancamento arqAuxiliar;
	
	private Lancamento lancamento;
	
	private List<Lancamento> lancamentos;
	private List<Lancamento> lancamentosAuxiliar;
	private List<Long> identificacoes;
	private String motivoRecusa;

	@Inject
	private SolicService service;
	
	public SolicController() {
	}

	public void init() {
		carregarLista();
	}
	
	public void imprimir(Lancamento lancamento) {
		service.imprimir(lancamento);
	}
	
	public void imprimirPrestacao(Lancamento lancamento) {
		service.imprimirPrestacao(lancamento.getId());
	}
	
	public void imprimirMapa(Long idCompra) {
		service.imprimirMapaComparativo(idCompra);
	}
	
	public void prepararResumo(Long id) {
		carregarLancamento(id);
	}
	
	public void carregarLancamento(Long id) {
		lancamento = new Lancamento();
		lancamento = service.getLancamentoByIdResumo(id);
	}
	
	public void visualizarArquivo() throws IOException {
		DownloadUtil.downloadFile(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}
	

	private void executeScript(String script) {
		PrimeFaces.current().executeScript(script);
	}

	/*
	 * public void autorizar() { service.autorizar(identificacoes,
	 * usuarioSessao.getIdColadorador()); carregarLista(); }
	 */

	/*
	 * public void prepararDesautorizacao() { motivoRecusa = ""; if (identificacoes
	 * != null && identificacoes.size() >= 1) {
	 * executeScript("PF('dlg_motivo').show();"); } else {
	 * addMessage("Selecione 1 (uma) solicitação ", FacesMessage.SEVERITY_ERROR); }
	 * 
	 * }
	 */
	
	

	/*
	 * public void desautorizar() { service.desautorizar(identificacoes,
	 * usuarioSessao.getIdColadorador(), motivoRecusa); carregarLista();
	 * executeScript("PF('dlg_motivo').hide()"); }
	 */

	/*
	 * public void selecionarAprovacao(Lancamento lancamento) {
	 * 
	 * if (identificacoes == null) identificacoes = new ArrayList<>();
	 * 
	 * if (lancamento.getSelecionado()) { identificacoes.add(lancamento.getId()); }
	 * else { identificacoes.remove(lancamento.getId()); }
	 * 
	 * }
	 */

	public void carregarLista() {
		lancamentos = new ArrayList<Lancamento>();
		lancamentos = service.getSolicitacoes(usuarioSessao.getIdColadorador());
		lancamentosAuxiliar = new ArrayList<>();
	}

	public void addMessage(String detail, Severity severiry) {
		FacesMessage message = new FacesMessage(severiry, "", detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<Lancamento> getLancamentos() {
		return lancamentos;
	}

	public void setLancamentos(List<Lancamento> lancamentos) {
		this.lancamentos = lancamentos;
	}

	public List<Lancamento> getLancamentosAuxiliar() {
		return lancamentosAuxiliar;
	}

	public void setLancamentosAuxiliar(List<Lancamento> lancamentosAuxiliar) {
		this.lancamentosAuxiliar = lancamentosAuxiliar;
	}

	public UsuarioSessao getUsuarioSessao() {
		return usuarioSessao;
	}

	public void setUsuarioSessao(UsuarioSessao usuarioSessao) {
		this.usuarioSessao = usuarioSessao;
	}

	public String getMotivoRecusa() {
		return motivoRecusa;
	}

	public void setMotivoRecusa(String motivoRecusa) {
		this.motivoRecusa = motivoRecusa;
	}

	private TabView tabview;

	public void carregarDadosTab() {
		int index = 0;
		if (tabview != null)
			index = tabview.getActiveIndex();

		if (index == 0) {
			System.out.println(index);
		} else if (index == 1) {
			System.out.println(index);
		} else if (index == 2) {
			System.out.println(index);
		}

	}

	public TabView getTabview() {
		return tabview;
	}

	public void setTabview(TabView tabview) {
		this.tabview = tabview;
	}

	public Lancamento getLancamento() {
		return lancamento;
	}

	public void setLancamento(Lancamento lancamento) {
		this.lancamento = lancamento;
	}

	public ArquivoLancamento getArqAuxiliar() {
		return arqAuxiliar;
	}

	public void setArqAuxiliar(ArquivoLancamento arqAuxiliar) {
		this.arqAuxiliar = arqAuxiliar;
	}

}
