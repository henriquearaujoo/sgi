package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
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

import model.Banco;
import model.ClassificacaoFornecedor;
import model.ContaBancaria;
import model.Estado;
import model.Fornecedor;
import model.LancamentoAuxiliar;
import model.Localidade;
import model.MenuLateral;
import model.TipoPagamento;
import service.FornecedorService;
import service.PagamentoService;
import service.UsuarioService;
import util.Filtro;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "fornecedor_terceiro_controller")
@ViewScoped
public class FornecedorTerceiroController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private FornecedorService fornecedorService;

	@Inject
	private PagamentoService pagamentoService;

	@Inject
	private UsuarioService usuarioService;

	private List<Estado> estados = new ArrayList<Estado>();

	private Boolean editandoConta = false;

	private Filtro filtro = new Filtro();

	private Boolean panelBloqueio = false;

	@Inject
	private UsuarioSessao usuarioSessao;

	private List<Fornecedor> listaFiltroFornecedores;

	private List<Localidade> listaLocalidade = new ArrayList<Localidade>();

	private Fornecedor fornecedor = new Fornecedor();

	private List<Fornecedor> listaFornecedor = new ArrayList<Fornecedor>();

	private ContaBancaria contaBancaria = new ContaBancaria();

	private MenuLateral menu = new MenuLateral();

	private Boolean cadastroConitnuo = false;

	private LancamentoAuxiliar lancamento = new LancamentoAuxiliar();

	private boolean tipo;

	public String mandaPronovo() {

		return "fornecedores_terceiros_cadastro?faces-redirect=true";
	}

	public List<Localidade> completeLocalidade(String s) {
		if (s.length() > 2)
			return fornecedorService.buscaLocalidade(s);

		return new ArrayList<Localidade>();
	}

	public void carregarEstados() {
		estados = fornecedorService.getEstados();
	}

	public void carregarCidades() {
		if (fornecedor.getEstado() != null)
			listaLocalidade = fornecedorService.findCidadeByEstado(fornecedor.getEstado().getId());
	}

	@PostConstruct
	public void init() {
		carregarFornecedores();
		carregarEstados();
		contaBancaria = new ContaBancaria();

	}

	public void carregarConta() {
		contaBancaria = pagamentoService.getContaBancariaByFornecedor(fornecedor);
		if (contaBancaria == null) {
			contaBancaria = new ContaBancaria();
		}
	}

	public void carregarFornecedores() {
		// if(usuarioSessao.getUsuario().getNomeUsuario().equals("admin"))
		// listaFornecedor = fornecedorService.getForcedores();
		// else
		// listaFornecedor = fornecedorService.getForcedoresAtivos();

		listaFornecedor = fornecedorService.getForcedoresTerceiros(filtro);
	}

	public void removerConta(ContaBancaria contaBancaria) {
		if (fornecedor.getContasBancarias() != null)
			fornecedor.getContasBancarias().remove(contaBancaria);
	}

	public void editarConta() {

		if (contaBancaria.getId() != null) {
			contaBancaria = fornecedorService.findById(contaBancaria.getId());
		}

		editandoConta = true;
	}

	public void editarConta(Integer index) {

		indexConta = index;

		if (contaBancaria.getId() != null) {
			contaBancaria = fornecedorService.findById(contaBancaria.getId());
		}

		editandoConta = true;
	}

	public void criarContaFleg() {
		contaBancaria = new ContaBancaria();
		contaBancaria.setNumeroConta("conta_fleg");
		contaBancaria.setNumeroAgencia("agencia_fleg");
		contaBancaria.setClassificacaoConta("FLEG");
		contaBancaria.setCnpj(fornecedor.getCnpj() != null ? fornecedor.getCnpj() : "");
		contaBancaria.setCpf(fornecedor.getCpf() != null ? fornecedor.getCpf() : "");
		contaBancaria.setEmail(fornecedor.getEmail() != null ? fornecedor.getEmail() : "");
		contaBancaria.setPIS(fornecedor.getPis() != null ? fornecedor.getPis() : "");
		contaBancaria.setRazaoSocial(fornecedor.getRazaoSocial() != null ? fornecedor.getRazaoSocial() : "");
		contaBancaria.setNomeConta(fornecedor.getNomeFantasia() != null ? fornecedor.getNomeFantasia() : "");
		contaBancaria.setSaldoInicial(new BigDecimal("0"));
		contaBancaria.setSaldoAtual(new BigDecimal("0"));
		contaBancaria.setTipo("CF");
		contaBancaria.setTipoJuridico(fornecedor.getTipo());
		contaBancaria.setFornecedor(fornecedor);

		if (fornecedor.getContasBancarias() == null)
			fornecedor.setContasBancarias(new ArrayList<>());

		fornecedor.getContasBancarias().add(contaBancaria);
		contaBancaria = new ContaBancaria();

	}

	private Integer indexConta;

	public String salvarConta() {

		try {
			contaBancaria.setCnpj(fornecedor.getCnpj() != null ? fornecedor.getCnpj() : "");
			contaBancaria.setCpf(fornecedor.getCpf() != null ? fornecedor.getCpf() : "");
			contaBancaria.setEmail(fornecedor.getEmail() != null ? fornecedor.getEmail() : "");
			contaBancaria.setPIS(fornecedor.getPis() != null ? fornecedor.getPis() : "");
			contaBancaria.setRazaoSocial(fornecedor.getRazaoSocial() != null ? fornecedor.getRazaoSocial() : "");
			contaBancaria.setNomeConta(fornecedor.getNomeFantasia() != null ? fornecedor.getNomeFantasia() : "");
			contaBancaria.setSaldoInicial(new BigDecimal("0"));
			contaBancaria.setSaldoAtual(new BigDecimal("0"));
			contaBancaria.setTipo("CF");
			contaBancaria.setTipoJuridico(fornecedor.getTipo());
			contaBancaria.setFornecedor(fornecedor);

			String contaSemDigito = contaBancaria.getNumeroConta().substring(0, 12);
			String digitoConta = contaBancaria.getNumeroConta().substring(13);

			String agenciaSemDigito = contaBancaria.getNumeroAgencia().substring(0, 4);
			String digitoAgencia = contaBancaria.getNumeroAgencia().substring(5);

			contaBancaria.setNumeroConta(contaSemDigito);
			contaBancaria.setDigitoConta(digitoConta);

			contaBancaria.setNumeroAgencia(agenciaSemDigito);
			contaBancaria.setDigitoAgencia(digitoAgencia);

			if (fornecedor.getContasBancarias() == null)
				fornecedor.setContasBancarias(new ArrayList<>());

			if (!editandoConta) {
				for (ContaBancaria contaBancaria : fornecedor.getContasBancarias()) {
					if (this.contaBancaria.getClassificacaoConta().equals("PRINCIPAL")
							&& contaBancaria.getClassificacaoConta().equals("PRINCIPAL"))
						throw new Exception("Já existe uma conta principal");
				}
			}

			if (editandoConta) {
				fornecedor.getContasBancarias().remove(contaBancaria);
				fornecedor.getContasBancarias().add(indexConta, contaBancaria);
			} else {
				fornecedor.getContasBancarias().add(contaBancaria);
			}

			contaBancaria = new ContaBancaria();
			editandoConta = false;

			addMessage("", "Conta salva com sucesso", FacesMessage.SEVERITY_INFO);
		} catch (Exception e) {
			e.printStackTrace();
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
		}

		return "";
	}

	public void filtrar() {
		carregarFornecedores();
	}

	public void limparFiltro() {
		filtro = new Filtro();
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuConfigurações();
	}

	public List<Fornecedor> getListaFornecedor() {
		return listaFornecedor;
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public String voltarTelaDeListagem() {
		return "fornecedores_terceiros?faces-redirect=true";
	}

	public void setListaFornecedor(List<Fornecedor> listaFornecedor) {
		this.listaFornecedor = listaFornecedor;
	}

	private List<Banco> bancos = new ArrayList<>();

	public List<Banco> completeBanco(String query) {
		bancos = new ArrayList<>();
		bancos = fornecedorService.getBancoAutoComplete(query);
		return bancos;
	}

//	public void update(Fornecedor fornecedor) {
//
//		Fornecedor fornecedorAux = fornecedorService.getFornecedor(fornecedor.getId());
//		ContaBancaria conta = pagamentoService.getContaBancariaByFornecedor(fornecedor);
//		conta.setCnpj(fornecedor.getCnpj() != null ? fornecedor.getCnpj() : "");
//		conta.setCpf(fornecedor.getCpf() != null ? fornecedor.getCpf() : "");
//		conta.setEmail(fornecedor.getEmail() != null ? fornecedor.getEmail() : "");
//		conta.setRazaoSocial(fornecedor.getRazaoSocial() != null ? fornecedor.getRazaoSocial() : "");
//		conta.setNomeConta(fornecedor.getNomeFantasia() != null ? fornecedor.getNomeFantasia() : "");
//		fornecedor.setTxModify(usuarioSessao.getNomeUsuario());
//		fornecedor.setDtModify(new Date());
//		fornecedorAux.setRazaoSocial(fornecedor.getRazaoSocial());
//		fornecedorAux.setCnpj(fornecedor.getCnpj());
//		fornecedorAux.setNomeFantasia(fornecedor.getNomeFantasia());
//		fornecedorAux.setEmail(fornecedor.getEmail());
//		fornecedorAux.setContato(fornecedor.getContato());
//
//		fornecedorService.insert(fornecedorAux);
//		pagamentoService.updateContaBancaria(conta);
//
//	}

	public String salvarTerceiro() {

		if (fornecedor.getTipoPagamento().compareTo(TipoPagamento.DEP_CONTA) == 0) {
			if (!(fornecedor.getContasBancarias().size() > 0)) {
				addMessage("", "É necessário ao menos 1 conta para cadastros de depósitos em conta",
						FacesMessage.SEVERITY_ERROR);
				return "";
			} else {
				contaBancaria = fornecedorService.buscarContaFleg(fornecedor.getId());
				if (contaBancaria != null) {
					fornecedor.getContasBancarias().add(contaBancaria);
				}

				contaBancaria = new ContaBancaria();
			}
		} else if (fornecedor.getTipoPagamento().compareTo(TipoPagamento.BOLETO) == 0) {

			contaBancaria = fornecedorService.buscarContaFleg(fornecedor.getId());

			if (contaBancaria != null) {
				fornecedor.getContasBancarias().add(contaBancaria);
			} else {
				criarContaFleg();
			}

			contaBancaria = new ContaBancaria();
		}

		if (fornecedor.getTipo().equals("fisica") || fornecedor.getTipo().equals("int")) {
			fornecedor.setNomeFantasia(fornecedor.getRazaoSocial() != null ? fornecedor.getRazaoSocial() : "");
		}

		if (fornecedor.getAtivo() == null) {
			fornecedor.setAtivo(true);
		}

		fornecedor.setClassificacao(ClassificacaoFornecedor.TERCEIRO);
		fornecedorService.insert(fornecedor);
		addMessage("", "Salvo com sucesso!", FacesMessage.SEVERITY_INFO);

		if (cadastroConitnuo) {
			fornecedor = new Fornecedor();
			contaBancaria = new ContaBancaria();
			return "";
		} else {
			return "fornecedores_terceiros?faces-redirect=true";
		}

	}

//	if (!fornecedor.getTipo().equals("int")) {
//		ContaBancaria contaAuxiliar = pagamentoService.getContaBancaria(contaBancaria);
//
//		if (contaAuxiliar != null) {
//			addMessage("", "CNPJ/CPF já cadastrado. Fornecedor: " + contaAuxiliar.getRazaoSocial(),
//					FacesMessage.SEVERITY_ERROR);
//			return "";
//		}
//	}

	public Boolean verificarObrigatoriedade(String field) {

		if (fornecedor.getTipo() == null) {
			return false;
		}

		if (field.equals("nome")) {
			return true;
		}

		if (field.equals("nome_fantasia")) {
			if (fornecedor.getTipo().equals("juridica")) {
				return true;
			} else if (fornecedor.getTipo().equals("fisica")) {
				return false;
			} else {
				return false;
			}
		}

		if (field.equals("telefone")) {
			if (fornecedor.getTipo().equals("juridica")) {
				return true;
			} else if (fornecedor.getTipo().equals("fisica")) {
				return true;
			} else {
				return false;
			}
		}

		if (field.equals("endereco")) {
			return true;
		}

		if (field.equals("numero")) {
			return true;
		}

		if (field.equals("cep")) {
			return true;
		}

		if (field.equals("bairro")) {
			return true;
		}

		if (field.equals("cidade")) {
			return true;
		}

		return false;
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public Fornecedor getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(Fornecedor fornecedor) {
		this.fornecedor = fornecedor;
	}

	// public List<Fornecedor> listaFornecedores(){
	// if(usuarioSessao.getUsuario().getNomeUsuario().equals("admin"))
	// return fornecedorService.getForcedores();
	// else
	// return fornecedorService.getForcedoresAtivos();
	// }

	public List<Fornecedor> getListaFiltroFornecedores() {
		return listaFiltroFornecedores;
	}

	public void setListaFiltroFornecedores(List<Fornecedor> listaFiltroFornecedores) {
		this.listaFiltroFornecedores = listaFiltroFornecedores;
	}

	public boolean isTipo() {
		return tipo;
	}

	public void setTipo(boolean tipo) {
		this.tipo = tipo;
	}

	public ContaBancaria getContaBancaria() {
		return contaBancaria;
	}

	public void setContaBancaria(ContaBancaria contaBancaria) {
		this.contaBancaria = contaBancaria;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public Boolean getCadastroConitnuo() {
		return cadastroConitnuo;
	}

	public void setCadastroConitnuo(Boolean cadastroConitnuo) {
		this.cadastroConitnuo = cadastroConitnuo;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Boolean getPanelBloqueio() {
		return panelBloqueio;
	}

	public void setPanelBloqueio(Boolean panelBloqueio) {
		this.panelBloqueio = panelBloqueio;
	}

	public List<Banco> getBancos() {
		return bancos;
	}

	public void setBancos(List<Banco> bancos) {
		this.bancos = bancos;
	}

	public LancamentoAuxiliar getLancamento() {
		return lancamento;
	}

	public void setLancamento(LancamentoAuxiliar lancamento) {
		this.lancamento = lancamento;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Localidade> getListaLocalidade() {
		return listaLocalidade;
	}

	public void setListaLocalidade(List<Localidade> listaLocalidade) {
		this.listaLocalidade = listaLocalidade;
	}

}
