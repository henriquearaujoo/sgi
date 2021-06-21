package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.EmailException;
import org.primefaces.event.FileUploadEvent;

import model.Compra;
import model.CotacaoAuxiliar;
import model.EspelhoPedido;
import model.Fornecedor;
import model.ItemCompra;
import model.LancamentoAcao;
import model.MenuLateral;
import model.Observacao;
import model.StatusCompra;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import service.CotacaoService;
import util.Email;
import util.MakeMenu;
import util.ReportUtil;
import util.UsuarioSessao;

@Named(value = "cotacaoController")
@ViewScoped
public class CotacaoController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject Compra compra;
	private @Inject CotacaoService cotacaoService;

	private @Inject UsuarioSessao usuarioSessao;
	private MenuLateral menu = new MenuLateral();
	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<>();
	private List<CotacaoAuxiliar> cotacoesSelected = new ArrayList<>();
	private Long idFornecedor;
	private Map<String, Integer> mapIndiceDeAnexos = new HashMap<>();
	private List<String> listAnexo = new ArrayList<>();
	private byte[] anexo;
	private Email email = new Email();
	private Integer indexAnexo = 0;
	private String pathAnexo = "";

	private String path = "";

	public CotacaoController() {
	}

	public void carregarItens() throws IOException {

		// compra = (Compra)
		// FacesContext.getCurrentInstance().getExternalContext().getFlash().get("compra"+usuarioSessao.getNomeUsuario());

		if (compra == null || compra.getId() == null) {
			// FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra"+usuarioSessao.getNomeUsuario(),
			// cotacoes.get(0).getCompra());
			HttpServletResponse resp = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext()
					.getResponse();
			HttpServletRequest req = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
					.getRequest();
			resp.sendRedirect(req.getContextPath() + "/main/compras.xhtml");
			return;
		}
		cotacoesAuxiliares = cotacaoService.getCotacaoAuxiliar(compra.getId());
	}

	public void excluirCotacao(Long idCompra, Long idFornecedor) throws IOException {
		cotacaoService.excluirCotacao(idCompra, idFornecedor);
		carregarItens();
	}

	public void imprimirMapaComparativo() {

		StringBuilder resumo = new StringBuilder();
		// compra = cotacaoService.getCompraById(compra.getId());
		for (CotacaoAuxiliar cot : cotacoesSelected) {
			resumo.append(cot.getFornecedor() + ": " + getTotais(cot.getTotalDesconto()) + "\n");
		}

		List<Observacao> listObs = cotacaoService.getObservacoes(compra);
		StringBuilder notaTecnica = new StringBuilder();

		for (Observacao observacao : listObs) {
			notaTecnica.append(observacao.getTexto() + "\n");
		}

		Long f1 = 0l;
		Long f2 = 0l;
		Long f3 = 0l;
		Long f4 = 0l;
		Long f5 = 0l;
		Long f6 = 0l;
		String n1 = "";
		String n2 = "";
		String n3 = "";
		String n4 = "";
		String n5 = "";
		String n6 = "";

		BigDecimal total1 = BigDecimal.ZERO, desc1 = BigDecimal.ZERO;
		BigDecimal total2 = BigDecimal.ZERO, desc2 = BigDecimal.ZERO;
		BigDecimal total3 = BigDecimal.ZERO, desc3 = BigDecimal.ZERO;
		BigDecimal total4 = BigDecimal.ZERO, desc4 = BigDecimal.ZERO;
		BigDecimal total5 = BigDecimal.ZERO, desc5 = BigDecimal.ZERO;
		BigDecimal total6 = BigDecimal.ZERO, desc6 = BigDecimal.ZERO;

		List<ItemCompra> itens = cotacaoService.getItensTransientByCompra(compra);
		// cotacoesAuxiliares = cotacaoService.getCotacaoAuxiliar(compra);
		List<EspelhoPedido> espelhos = cotacaoService.getEspelhoPedido(compra);
		List<LancamentoAcao> acoes = cotacaoService.getLancamentosAcoes(compra);

		StringBuilder acs = new StringBuilder();

		if (compra.getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				acs.append("//");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append("//");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				acs.append("\n");
			}
		} else {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getAcao().getCodigo() + " \n");
			}
		}

		// StringBuilder resumo = new StringBuilder();

		for (EspelhoPedido espelho : espelhos) {

			String linha = "Fornecedor: " + espelho.getFornecedor() + " valor: " + getTotais(espelho.getTotalValor());

		}

		int cont = 0;

		for (CotacaoAuxiliar cotAuxiliar : cotacoesSelected) {

			if (cont == 0) {
				f1 = cotAuxiliar.getFornecedorId();
				n1 = cotAuxiliar.getFornecedor().toLowerCase();

			}

			if (cont == 1) {
				f2 = cotAuxiliar.getFornecedorId();
				n2 = cotAuxiliar.getFornecedor().toLowerCase();
			}

			if (cont == 2) {
				f3 = cotAuxiliar.getFornecedorId();
				n3 = cotAuxiliar.getFornecedor().toLowerCase();
			}

			if (cont == 3) {
				f4 = cotAuxiliar.getFornecedorId();
				n4 = cotAuxiliar.getFornecedor().toLowerCase();
			}

			if (cont == 4) {
				f5 = cotAuxiliar.getFornecedorId();
				n5 = cotAuxiliar.getFornecedor().toLowerCase();
			}

			if (cont == 5) {
				f6 = cotAuxiliar.getFornecedorId();
				n6 = cotAuxiliar.getFornecedor().toLowerCase();
			}

			cont++;
		}

		BigDecimal totalDesconto = BigDecimal.ZERO;

		for (ItemCompra itemCompra : itens) {

			if (!n1.equals("")) {
				ItemCompra item = cotacaoService.getValoresDeItem(f1, itemCompra.getId());
				itemCompra.setP1(item.getValorDesconto());
				itemCompra.setT1(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValorDesconto()));
				total1 = total1.add(itemCompra.getT1());
				desc1 = desc1.add(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValor())
						.subtract(itemCompra.getT1()));
			}

			if (!n2.equals("")) {
				ItemCompra item = cotacaoService.getValoresDeItem(f2, itemCompra.getId());
				itemCompra.setP2(item.getValorDesconto());
				itemCompra.setT2(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValorDesconto()));
				total2 = total2.add(itemCompra.getT2());
				desc2 = desc2.add(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValor())
						.subtract(itemCompra.getT2()));
			}

			if (!n3.equals("")) {
				ItemCompra item = cotacaoService.getValoresDeItem(f3, itemCompra.getId());
				itemCompra.setP3(item.getValorDesconto());
				itemCompra.setT3(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValorDesconto()));
				total3 = total3.add(itemCompra.getT3());
				desc3 = desc3.add(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValor())
						.subtract(itemCompra.getT3()));
			}

			if (!n4.equals("")) {
				ItemCompra item = cotacaoService.getValoresDeItem(f4, itemCompra.getId());
				itemCompra.setP4(item.getValorDesconto());
				itemCompra.setT4(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValorDesconto()));
				total4 = total4.add(itemCompra.getT4());
				desc4 = desc4.add(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValor())
						.subtract(itemCompra.getT4()));
			}

			if (!n5.equals("")) {
				ItemCompra item = cotacaoService.getValoresDeItem(f5, itemCompra.getId());
				itemCompra.setP5(item.getValorDesconto());
				itemCompra.setT5(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValorDesconto()));
				total5 = total5.add(itemCompra.getT5());
				desc5 = desc5.add(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValor())
						.subtract(itemCompra.getT5()));
			}

			if (!n6.equals("")) {
				ItemCompra item = cotacaoService.getValoresDeItem(f6, itemCompra.getId());
				itemCompra.setP6(item.getValorDesconto());
				itemCompra.setT6(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValorDesconto()));
				total6 = total6.add(itemCompra.getT6());
				desc6 = desc6.add(BigDecimal.valueOf(item.getQuantidade()).multiply(item.getValor())
						.subtract(itemCompra.getT6()));

			}
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();

		parametros.put("total1", total1);
		parametros.put("total2", total2);
		parametros.put("total3", total3);
		parametros.put("total4", total4);
		parametros.put("total5", total5);
		parametros.put("total6", total6);

		parametros.put("desc1", desc1);
		parametros.put("desc2", desc2);
		parametros.put("desc3", desc3);
		parametros.put("desc4", desc4);
		parametros.put("desc5", desc5);
		parametros.put("desc6", desc6);

		parametros.put("f1", n1);
		parametros.put("f2", n2);
		parametros.put("f3", n3);
		parametros.put("f4", n4);
		parametros.put("f5", n5);
		parametros.put("f6", n6);

		parametros.put("nota_tec", notaTecnica.toString());

		parametros.put("resumo", resumo.toString());

		parametros.put("data", new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()));

		parametros.put("logo", logo);
		parametros.put("acao", acs.toString());
		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", "SC" + compra.getId().toString());

		JRDataSource dataSource = new JRBeanCollectionDataSource(itens);
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/Mapa.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("MC", "Mapa " + compra.getId(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private String toEmailArray[]; 
	
	public void prepararEmailTeste() throws UnknownHostException {
		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSubject("APROVAÇÃO MC-" + compra.getId() + " Data:"
				+ new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()));
		email.setSolicitação(String.valueOf(compra.getId()));
		email.setSenhaEmail("FAS123fas");
		StringBuilder toEmailAux = new StringBuilder("italo.almeida@fas-amazonas.org");
		email.setToEmail(toEmailAux.toString());
	}

	public void prepararEmail() {

		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSubject("APROVAÇÃO MC-" + compra.getId() + " Data:"
				+ new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()));
		email.setSolicitação(String.valueOf(compra.getId()));
		email.setSenhaEmail("FAS123fas");
		// TODO DESCOMENTAR
		// StringBuilder toEmailAux = new
		// StringBuilder("italo.almeida@fas-amazonas.org");

		StringBuilder toEmailAux = new StringBuilder(compra.getSolicitante().getEmail());
		toEmailAux.append(",");
		toEmailAux.append(compra.getGestao().getColaborador().getEmail());
		toEmailAux.append(",");
		toEmailAux.append("admsgi@fas-amazonas.org");
		toEmailAux.append(",");
		toEmailAux.append("compra@fas-amazonas.org");

		if (compra.getGestao().getId().longValue() == 10) {
			toEmailAux.append(",");
			toEmailAux.append("raquel.caldas@fas-amazonas.org");
		}

		email.setToEmail(toEmailAux.toString());
	}

	public void enviarCotacaoEmail() {
		try {

			if (email.verificaInternet()) {
				email.EnviarMapaComparativo(compra, cotacoesSelected);
				addMessage("", "Mensagem enviada com sucesso", FacesMessage.SEVERITY_INFO);
				limparEmail();
			}

		} catch (EmailException e) {
			addMessage("", "Problema de conexão com a internet, "
					+ "por favor contate o administrador do sistema, o seu envio não foi possível, tente mais tarde.",
					FacesMessage.SEVERITY_WARN);
		} catch (IOException e) {
			addMessage("", "Problema de conexão com a internet, "
					+ "por favor contate o administrador do sistema, o seu envio não foi possível, tente mais tarde.",
					FacesMessage.SEVERITY_WARN);
		}
	}

	public void limparEmail() {
		email = new Email();
		mapIndiceDeAnexos = new HashMap<>();
		listAnexo = new ArrayList<>();
		pathAnexo = "";
		path = "";

	}

	public void handleFileUpload(FileUploadEvent event) throws IOException {
		path = event.getFile().getFileName();
		anexo = event.getFile().getContents();
		anexarEmail();
	}

	public void anexarEmail() {
		try {
			email.getPartes().addBodyPart(criarAnexoMimeBodyPart());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void removerAnexoEmail(int index) throws MessagingException {
		MimeMultipart mime = (MimeMultipart) email.getPartes();
		listAnexo.remove(index);
		Integer ii = mapIndiceDeAnexos.get(this.pathAnexo);

		MimeBodyPart body = (MimeBodyPart) mime.getBodyPart(mapIndiceDeAnexos.get(pathAnexo).intValue());
		mime.removeBodyPart(body);
	}

	public MimeBodyPart criarAnexoMimeBodyPart() {
		MimeBodyPart mime = new MimeBodyPart();
		// FileDataSource fds = new FileDataSource("");

		try {

			mime.setDataHandler(new DataHandler(new ByteArrayDataSource(anexo, "application/pdf")));
			mime.setFileName(this.path);

			mapIndiceDeAnexos.put(this.path, indexAnexo);
			listAnexo.add(this.path);
			indexAnexo++;
			anexo = null;
			return mime;

		} catch (MessagingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public String redirecionarPaginaDeEscolha() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra" + usuarioSessao.getNomeUsuario(),
				compra);
		return "escolhaCotacaoBackup?faces-redirect=true";
	}

	public void imprimirFormulario() {

		List<ItemCompra> itens = cotacaoService.getItensTransientByCompra(compra);

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("tipo_gestao", compra.getTipoGestao().getNome());
		parametros.put("gestao", compra.getGestao().getNome());
		parametros.put("tipo_localidade", compra.getTipoLocalidade().getNome());
		parametros.put("localidade", compra.getLocalidade().getNome());
		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", "" + compra.getId());
		parametros.put("observ", "");
		JRDataSource dataSource = new JRBeanCollectionDataSource(itens);
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/coleta_preco.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("Formulário", "Formulário" + compra.getId(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public String redirecionarPageCadastroCotacao() {
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra" + usuarioSessao.getNomeUsuario(),
				compra);
		return "cotacaoCadastro?faces-redirect=true";
	}

	public String redirecionarPageCompras() {
		compra = null;
		return "compras?faces-redirect=true";
	}

	public boolean verificarAcessoACotacao() {
		if (compra.getStatusCompra().equals(StatusCompra.N_INCIADO))
			return false;
		if (compra.getStatusCompra().equals(StatusCompra.CANCELADO))
			return false;
		if (compra.getStatusCompra().equals(StatusCompra.CONCLUIDO))
			return false;

		return true;
	}

	public boolean verificaUsuario() {
		// Verifica o usuario de acordo com a restrição de dados
		return true;
	}

	public String editarCotacao() {
		Fornecedor fornecedor = cotacaoService.getFornecedorById(idFornecedor);
		FacesContext.getCurrentInstance().getExternalContext().getFlash().put("compra" + usuarioSessao.getNomeUsuario(),
				compra);
		FacesContext.getCurrentInstance().getExternalContext().getFlash()
				.put("fornecedor" + usuarioSessao.getNomeUsuario(), fornecedor);
		return "cotacaoCadastro?faces-redirect=true";
	}

	public Compra getCompra() {
		return compra;
	}

	public void setCompra(Compra compra) {
		this.compra = compra;
	}

	public List<Fornecedor> getFornecedor() {
		return null;
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public List<MenuLateral> getMenus() {

		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Compras", "compras", "fa fa-shopping-cart");
		lista.add(menu);
		menu = new MenuLateral("Produtos", "produtos", "fa fa-database");
		lista.add(menu);
		menu = new MenuLateral("Fornecedores", "fornecedores", "fa fa-user");
		lista.add(menu);

		return MakeMenu.getMenuCompra();
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public List<CotacaoAuxiliar> getCotacoesAuxiliares() {
		return cotacoesAuxiliares;
	}

	public void setCotacoesAuxiliares(List<CotacaoAuxiliar> cotacoesAuxiliares) {
		this.cotacoesAuxiliares = cotacoesAuxiliares;
	}

	public Long getIdFornecedor() {
		return idFornecedor;
	}

	public void setIdFornecedor(Long idFornecedor) {
		this.idFornecedor = idFornecedor;
	}

	public Email getEmail() {
		return email;
	}

	public void setEmail(Email email) {
		this.email = email;
	}

	public String getTotais(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}

	public Map<String, Integer> getMapIndiceDeAnexos() {
		return mapIndiceDeAnexos;
	}

	public void setMapIndiceDeAnexos(Map<String, Integer> mapIndiceDeAnexos) {
		this.mapIndiceDeAnexos = mapIndiceDeAnexos;
	}

	public List<String> getListAnexo() {
		return listAnexo;
	}

	public void setListAnexo(List<String> listAnexo) {
		this.listAnexo = listAnexo;
	}

	public String getPathAnexo() {
		return pathAnexo;
	}

	public void setPathAnexo(String pathAnexo) {
		this.pathAnexo = pathAnexo;
	}

	public List<CotacaoAuxiliar> getCotacoesSelected() {
		return cotacoesSelected;
	}

	public void setCotacoesSelected(List<CotacaoAuxiliar> cotacoesSelected) {
		this.cotacoesSelected = cotacoesSelected;
	}

	public String[] getToEmailArray() {
		return toEmailArray;
	}

	public void setToEmailArray(String[] toEmailArray) {
		this.toEmailArray = toEmailArray;
	}

}
