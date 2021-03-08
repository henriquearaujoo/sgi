package managedbean;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
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
import javax.servlet.http.HttpServletRequest;

import org.primefaces.event.FileUploadEvent;

import model.Banco;
import model.CategoriaDespesaClass;
import model.ClassificacaoConta;
import model.ClassificacaoFornecedor;
import model.ContaBancaria;
import model.Estado;
import model.Fornecedor;
import model.LancamentoAuxiliar;
import model.Localidade;
import model.MenuLateral;
import model.TipoArquivo;
import model.TipoPagamento;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import service.BancoService;
import service.FornecedorService;
import service.PagamentoService;
import service.UsuarioService;
import util.ArquivoFornecedor;
import util.DiretorioUtil;
import util.DownloadUtil;
import util.Filtro;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;

@Named(value = "fornecedor_controller")
@ViewScoped
public class FornecedorController implements Serializable {

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

    private ArquivoFornecedor arquivo = new ArquivoFornecedor();

    private @Inject ArquivoFornecedor arqAuxiliar;

    private Filtro filtro = new Filtro();

    private Boolean panelBloqueio = false;

    @Inject
    private UsuarioSessao usuarioSessao;

    private List<Fornecedor> listaFiltroFornecedores;

    private List<Localidade> listaLocalidade = new ArrayList<Localidade>();

    private List<Estado> estados = new ArrayList<Estado>();

    private Fornecedor fornecedor = new Fornecedor();

    private List<Fornecedor> listaFornecedor = new ArrayList<Fornecedor>();

    private ContaBancaria contaBancaria = new ContaBancaria();

    private MenuLateral menu = new MenuLateral();

    private Boolean cadastroConitnuo = false;

    private boolean tipo;

    private Integer indexConta;

    private Boolean editandoConta = false;

    private LancamentoAuxiliar lancamento = new LancamentoAuxiliar();

    private Boolean existeFornecimento = false;

    private List<CategoriaDespesaClass> categorias = new ArrayList<>();

    @Inject
    private Banco banco = new Banco();

    @Inject
    private BancoService bancoService;

    public String mandaPronovo() {

	return "fornecedores_cadastro?faces-redirect=true";
    }

    public List<Localidade> completeLocalidade(String s) {
	if (s.length() > 2)
	    return fornecedorService.buscaLocalidade(s);

	return new ArrayList<Localidade>();
    }

    @PostConstruct
    public void init() {

	filtro.setAtivo(true);
	carregarFornecedores();
	carregarCategorias();
	contaBancaria = new ContaBancaria();
	carregarEstados();

	if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
		|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra")
		|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
	    panelBloqueio = false;
	} else {
	    panelBloqueio = true;
	}
    }

    public String voltarTelaDeListagem() {
	return "fornecedores?faces-redirect=true";
    }

    public void carregarCidades() {
	if (fornecedor.getEstado() != null)
	    listaLocalidade = fornecedorService.findCidadeByEstado(fornecedor.getEstado().getId());
    }

    public void carregarEstados() {
	estados = fornecedorService.getEstados();
    }

    private List<Banco> bancos = new ArrayList<>();

    public List<Banco> completeBanco(String query) {
	bancos = new ArrayList<>();
	bancos = fornecedorService.getBancoAutoComplete(query);
	return bancos;
    }

    public void carregarConta() {
	// contaBancaria = pagamentoService.getContaBancariaByFornecedor(fornecedor);
	if (contaBancaria == null) {
	    contaBancaria = new ContaBancaria();
	}
    }

    public void carregarUltimoFornecimento() {
	if (fornecedor.getId() != null) {
	    carregarCidades();
	    existeFornecimento = fornecedorService.verificarExisteFornecimento(fornecedor.getId());
	    if (existeFornecimento) {
		lancamento = fornecedorService.buscarUltimoFornecimento(fornecedor.getId());
	    }
	} else {
	    existeFornecimento = false;
	    fornecedor.setAtivo(true);
	}

    }

    public void removerFornecedor() {

	if (!fornecedorService.verificarExisteFornecimento(fornecedor.getId())) {
	    fornecedorService.removerFornecedor(fornecedor);
	    addMessage("Sucesso", "Fornecedor removido com sucesso!", FacesMessage.SEVERITY_INFO);
	    carregarFornecedores();
	} else {
	    addMessage("Erro", "Fornecedor não pode ser removido, existem fornecimentos vinculados ao cadastro",
		    FacesMessage.SEVERITY_ERROR);
	}
    }

    public void carregarFornecedores() {

	listaFornecedor = fornecedorService.getForcedores(filtro);
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

    public void setListaFornecedor(List<Fornecedor> listaFornecedor) {
	this.listaFornecedor = listaFornecedor;
    }

    public void update(Fornecedor fornecedor) {

	Fornecedor fornecedorAux = fornecedorService.getFornecedor(fornecedor.getId());
	ContaBancaria conta = pagamentoService.getContaBancariaByFornecedor(fornecedor);
	conta.setCnpj(fornecedor.getCnpj() != null ? fornecedor.getCnpj() : "");
	conta.setCpf(fornecedor.getCpf() != null ? fornecedor.getCpf() : "");
	conta.setEmail(fornecedor.getEmail() != null ? fornecedor.getEmail() : "");
	conta.setRazaoSocial(fornecedor.getRazaoSocial() != null ? fornecedor.getRazaoSocial() : "");
	conta.setNomeConta(fornecedor.getNomeFantasia() != null ? fornecedor.getNomeFantasia() : "");
	fornecedor.setTxModify(usuarioSessao.getNomeUsuario());
	fornecedor.setDtModify(new Date());
	fornecedorAux.setRazaoSocial(fornecedor.getRazaoSocial());
	fornecedorAux.setCnpj(fornecedor.getCnpj());
	fornecedorAux.setNomeFantasia(fornecedor.getNomeFantasia());
	fornecedorAux.setEmail(fornecedor.getEmail());
	fornecedorAux.setContato(fornecedor.getContato());

	fornecedorService.insert(fornecedorAux);
	// pagamentoService.updateContaBancaria(conta);

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
	    contaBancaria.setDigitoAgencia(contaBancaria.getDigitoAgencia().toUpperCase());
	    contaBancaria.setDigitoConta(contaBancaria.getDigitoConta().toUpperCase());

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

    public Boolean existeConta(ContaBancaria conta) {
	for (ContaBancaria contaBancaria : fornecedor.getContasBancarias()) {
	    if (contaBancaria.getNomeBanco().toLowerCase().equals(conta.getNomeBanco().toLowerCase())
		    && contaBancaria.getNumeroAgencia().toLowerCase().equals(conta.getNumeroAgencia().toLowerCase())
		    && contaBancaria.getNumeroConta().toLowerCase().equals(conta.getNumeroConta().toLowerCase()))
		return true;
	}

	return false;
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

    public String insert() {

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

	if (fornecedor.getCategoriaFornecedor1() == null
		|| fornecedor.getCategoriaFornecedor1().getId().longValue() == 0) {
	    addMessage("", "É necessário ao menos selecionar a primeira categoria do fornecedor.",
		    FacesMessage.SEVERITY_ERROR);
	    return "";
	}

	if (fornecedor.getCategoriaFornecedor1() != null) {
	    fornecedor.setCategoria1(fornecedor.getCategoriaFornecedor1().getNome());
	}

//		if (fornecedor.getCategoriaFornecedor2() != null) {
//			fornecedor.setCategoria2(fornecedor.getCategoriaFornecedor2().getNome());
//		}
//
//		if (fornecedor.getCategoriaFornecedor3() != null) {
//			fornecedor.setCategoria2(fornecedor.getCategoriaFornecedor2().getNome());
//		}

	fornecedor.setClassificacao(ClassificacaoFornecedor.FORNECEDOR);

	if (fornecedor.getNomeFantasia() != null)
	    fornecedor.setNomeFantasia(fornecedor.getNomeFantasia().toUpperCase());

	if (fornecedor.getRazaoSocial() != null)
	    fornecedor.setRazaoSocial(fornecedor.getRazaoSocial().toUpperCase());

	if (fornecedor.getEndereco() != null)
	    fornecedor.setEndereco(fornecedor.getEndereco().toUpperCase());

	if (fornecedor.getBanco() != null)
	    fornecedor.setBairro(fornecedor.getBairro().toUpperCase());

	if (fornecedor.getContato() != null)
	    fornecedor.setContato(fornecedor.getContato().toUpperCase());

	if (fornecedor.getEmail() != null)
	    fornecedor.setEmail(fornecedor.getEmail().toUpperCase());

	if (fornecedor.getEmailSecundario() != null)
	    fornecedor.setEmailSecundario(fornecedor.getEmailSecundario().toUpperCase());

	fornecedorService.insert(fornecedor);
	addMessage("", "Salvo com sucesso!", FacesMessage.SEVERITY_INFO);

	if (cadastroConitnuo) {
	    fornecedor = new Fornecedor();
	    contaBancaria = new ContaBancaria();
	    return "";
	} else {
	    return "fornecedores?faces-redirect=true";
	}

    }

    public String salvarBanco() {
	try {
	    if (banco.getNomeBanco() == null || banco.getNomeBanco().equals(""))
		throw new Exception("Nome do banco é obrigatório");

	    if (banco.getNumeroBanco() == null || banco.getNumeroBanco().equals(""))
		throw new Exception("Número do banco é obrigatório");

	    bancoService.salvarBanco(banco);
	    banco = new Banco();

	    addMessage("", "Banco salvo com sucesso", FacesMessage.SEVERITY_INFO);
	} catch (Exception e) {
	    e.printStackTrace();
	    addMessage("", e.getMessage(), FacesMessage.SEVERITY_ERROR);
	}
	return "";
    }

    public ClassificacaoConta[] listClassificacoes() {
	return ClassificacaoConta.values();
    }

    public CategoriaDespesaClass getCategoriaZERO() {
	CategoriaDespesaClass cat = new CategoriaDespesaClass();
	cat.setId((long) 0);
	cat.setNome("Categorias");
	return cat;
    }

    public void carregarCategorias() {
	categorias = fornecedorService.getCategorias();
    }

    public void exportarPDF() {

	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
		.getRequest();
	String path = request.getSession().getServletContext().getRealPath("/");
	String logo = path + "resources/image/logoFas.gif";
	Map parametros = new HashMap();
	parametros.put("logo", logo);
	List<Fornecedor> fornecedores = fornecedorService.getFornecedoresRelatorio(filtro);
	JRDataSource dataSource = new JRBeanCollectionDataSource(fornecedores);

	try {
	    JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/fornecedores.jrxml");
	    JasperReport report = JasperCompileManager.compileReport(jd);
	    ReportUtil.openReport("Fornecedores",
		    "fornecedores_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()), report, parametros,
		    dataSource);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    public String exportarExcel() {
	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
		.getRequest();
	String path = request.getSession().getServletContext().getRealPath("/");

	List<Fornecedor> fornecedores = fornecedorService.getFornecedoresRelatorio(filtro);
	JRDataSource dataSource = new JRBeanCollectionDataSource(fornecedores);

	try {
	    JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/fornecedores_xls.jrxml");
	    JasperReport report = JasperCompileManager.compileReport(jd);
	    ReportUtil.openReportXls("Fornecedores",
		    "fornecedores_" + new SimpleDateFormat("ddMMyyyyHHmmss").format(new Date()), report, null,
		    dataSource);
	} catch (Exception e) {
	    e.printStackTrace();
	}

	return "";
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

    public TipoArquivo[] getTipos() {
	return TipoArquivo.values();
    }

    public void handleFileUpload(FileUploadEvent event) throws IOException {
	String pathFinal = DiretorioUtil.DIRECTORY_UPLOAD + File.separator + fornecedor.getId() + "_"
		+ event.getFile().getFileName();
	arquivo.setConteudo(event.getFile().getContents());
	arquivo.setPath(pathFinal);
	arquivo.setNome(event.getFile().getFileName());
	arquivo.setTipo(TipoArquivo.PROCESSO);
	arquivo.setData(new Date());
    }

    public void adicionarArquivo() {

	try {
	    arquivo.setUsuario(usuarioSessao.getUsuario());

	    File file = new File(arquivo.getNome());

	    if (!file.exists()) {
		file.createNewFile();
	    }

	    FileOutputStream fot = new FileOutputStream(file);

	    fot.write(arquivo.getConteudo());
	    fot.close();
	} catch (FileNotFoundException e) {
	    System.out.println(e.getMessage());
	} catch (IOException e) {
	    e.printStackTrace();
	}

	arquivo.setFornecedor(fornecedor);
	if (fornecedor.getArquivos() == null) {
	    fornecedor.setArquivos(new ArrayList<>());
	}

	fornecedor.getArquivos().add(arquivo);

	arquivo = new ArquivoFornecedor();

    }

    public void visualizarArquivo() throws IOException {
	DownloadUtil.downloadFile(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
		FacesContext.getCurrentInstance());
	arqAuxiliar = new ArquivoFornecedor();
    }

    public void removerArquivo(ArquivoFornecedor arquivo) {
	fornecedor.getArquivos().remove(arquivo);
    }

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

	if (field.equals("ie")) {
	    if (fornecedor.getTipo().equals("juridica")) {
		return true;
	    } else {
		return false;
	    }
	}

	if (field.equals("data_nasc")) {
	    if (fornecedor.getTipo().equals("fisica")) {
		return true;
	    } else {
		return false;
	    }
	}

	if (field.equals("pis")) {
	    if (fornecedor.getTipo().equals("fisica")) {
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

    public Integer getIndexConta() {
	return indexConta;
    }

    public void setIndexConta(Integer indexConta) {
	this.indexConta = indexConta;
    }

    public LancamentoAuxiliar getLancamento() {
	return lancamento;
    }

    public void setLancamento(LancamentoAuxiliar lancamento) {
	this.lancamento = lancamento;
    }

    public Boolean getExisteFornecimento() {
	return existeFornecimento;
    }

    public void setExisteFornecimento(Boolean existeFornecimento) {
	this.existeFornecimento = existeFornecimento;
    }

    public List<CategoriaDespesaClass> getCategorias() {
	return categorias;
    }

    public void setCategorias(List<CategoriaDespesaClass> categorias) {
	this.categorias = categorias;
    }

    public ArquivoFornecedor getArquivo() {
	return arquivo;
    }

    public void setArquivo(ArquivoFornecedor arquivo) {
	this.arquivo = arquivo;
    }

    public ArquivoFornecedor getArqAuxiliar() {
	return arqAuxiliar;
    }

    public void setArqAuxiliar(ArquivoFornecedor arqAuxiliar) {
	this.arqAuxiliar = arqAuxiliar;
    }

    public Banco getBanco() {
	return banco;
    }

    public void setBanco(Banco banco) {
	this.banco = banco;
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