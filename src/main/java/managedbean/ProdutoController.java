package managedbean;

import java.io.File;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.servlet.http.HttpServletRequest;

import anotacoes.Transactional;
import model.CategoriaDespesaClass;
import model.MenuLateral;
import model.ObservacaoProduto;
import model.Produto;
import model.ProdutoHistorico;
import model.UnidadeDeCompra;
import repositorio.ProdutoRepositorio;
import service.CompraService;
import util.DiretorioUtil;
import util.DownloadFileHandler;
import util.Email;
import util.Filtro;
import util.MakeMenu;
import util.UsuarioSessao;

@Named(value = "produtoController")
@ViewScoped
public class ProdutoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> images = new ArrayList<>();
	private @Inject Produto produto;
	private List<Produto> produtos = new ArrayList<Produto>();
	private @Inject ProdutoRepositorio repositorio;
	private MenuLateral menu = new MenuLateral();
	private List<UnidadeDeCompra> unidades = new ArrayList<>();
	private List<CategoriaDespesaClass> categorias = new ArrayList<>();
	private List<Produto> produtosSelected = new ArrayList<>();
	private Boolean panelBloqueio = false;
	private Filtro filtro = new Filtro();
	@Inject
	private UsuarioSessao usuarioSessao;

	@Inject
	private ObservacaoProduto observacaoProduto;

	private List<ObservacaoProduto> listObseravaoProduto = new ArrayList<>();

	private List<ProdutoHistorico> listHistorico = new ArrayList<>();

	private String ultimaManutencao;

	private Boolean novoProduto = false;
	
	public ProdutoController() {
	}

	public void iniciarCadastro(){
		novoProduto = true;
	}
	
	public void carregarAjuda() {
		// DownloadUtil.downloadFileExcel("help", DiretorioUtil.DIRECTORY_UPLOAD
		// + "help.xlsx" , "application/pdf",
		// FacesContext.getCurrentInstance());

		DownloadFileHandler dlwd = new DownloadFileHandler();

		dlwd.downloadFile("help.xls", new File(DiretorioUtil.DIRECTORY_UPLOAD + "help.xlsx"),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	@PostConstruct
	public void init() {
		listarProdutos();
		carregarCategorias();
		carregarUnidades();

		// if
		// (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
		// ||
		// usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra"))
		// {
		// panelBloqueio = false;
		// }else{
		// panelBloqueio = true;
		// }
	}

	public void filtrar() {
		listarProdutos();
	}

	private @Inject CompraService compraService;

	public List<String> completeProduto(String query) {
		List<Produto> allProdutos = new ArrayList<Produto>();
		List<String> produtos = new ArrayList<>();

		if (query.length() > 2) {
			allProdutos = repositorio.getProduto(query);

			for (Produto product : allProdutos) {
				produtos.add(product.getDescricao());
			}
		}

		return produtos;
	}

	public Boolean verificarCampos(Produto produto) {

		Boolean retorno = false;

		if (produto.getUnidadeDeCompra() == null || produto.getUnidadeDeCompra().getId().longValue() == new Long(0)) {
			addMessage("", "Preencha o campo Unidade de medida.", FacesMessage.SEVERITY_ERROR);
			retorno = true;
		}

		if (produto.getCategoriaDespesa() == null || produto.getCategoriaDespesa().getId().longValue() == new Long(0)) {
			addMessage("", "Preencha o campo 'Categoria'.", FacesMessage.SEVERITY_ERROR);
			retorno = true;
		}

		if (produto.getTipo() == null || produto.getTipo().equals("")) {
			addMessage("", "Preencha o campo 'Tipo'.", FacesMessage.SEVERITY_ERROR);
			retorno = true;
		}

		return retorno;
	}

	private Email email = new Email();

	public void enviarAvisoAP() throws AddressException, MessagingException {

		if (produtosSelected.size() > 30) {
			addMessage("", "Selecione no máximo 30 produtos por solicitação.", FacesMessage.SEVERITY_INFO);
		}

		if (produtosSelected.size() > 0) {
			prepararEmailAvisoPreCadastro();
			email.EnviarEmailPreCadastroProduto();
			email = new Email();
			addMessage("", "Mensagem enviada.", FacesMessage.SEVERITY_INFO);
		} else {
			addMessage("", "Selecione no mínimo um produto para reativar", FacesMessage.SEVERITY_INFO);
		}

	}

	public void enviarEmailObservacao() throws AddressException, MessagingException {
		prepararEmailObservacao();
		email.EnviarEmailHTML();
		email = new Email();
		addMessage("", "Mensagem enviada.", FacesMessage.SEVERITY_INFO);
	}

	public void prepararEmailObservacao() {

		String emailPrecadastro = repositorio.getEmailUsuarioById(produto.getIdUsuario());

		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSenhaEmail("FAS123fas");
		email.setSubject("PS: PRÉ-CADASTRO DE PRODUTOS");
		StringBuilder toEmailAux = new StringBuilder("compra@fas-amazonas.org");
		toEmailAux.append(",");
		toEmailAux.append(usuarioSessao.getUsuario().getEmail());

		if (!emailPrecadastro.equals("")) {
			toEmailAux.append(",");
			toEmailAux.append(emailPrecadastro);
		}

		email.setToEmail(toEmailAux.toString());
		email.setContent(gerarConteudoHTMLOBS("Pendências.", "Existem observações feitas sobre o pré-cadastro."));

	}

	public void prepararEmailAvisoPreCadastro() {

		email.setFromEmail("comprasgi@fas-amazonas.org");
		email.setSenhaEmail("FAS123fas");
		email.setSubject("PRÉ-CADASTRO DE PRODUTOS");
		StringBuilder toEmailAux = new StringBuilder("suprimentos@fas-amazonas.org");
		toEmailAux.append(",");
		toEmailAux.append(usuarioSessao.getUsuario().getEmail());
		// StringBuilder toEmailAux = new
		// StringBuilder(usuarioSessao.getUsuario().getEmail());
		email.setToEmail(toEmailAux.toString());
		email.setContent(
				gerarConteudoHTMLAP("Pré-Cadastro.", "Solicito a revisão do produto pré-cadastrado no sistema."));

	}

	public String gerarConteudoHTMLOBS(String titulo, String texto) {

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logofas.png";

		StringBuilder html = new StringBuilder("<html> <head> <style>");
		html.append("table {width:100%;} table, th, td { border: 1px solid black; border-collapse: collapse;}");
		html.append("th, td { padding: 5px; text-align: left;} table#t01 tr:nth-child(even) {");
		html.append("background-color: #eee;} table#t01 tr:nth-child(odd) { background-color:#fff;}");
		html.append("table#t01 th { background-color: #849A39; color: white; }");
		html.append(".separator_vi{background-color: rgba(54, 63, 46, 0.15);width: 100%;height: 3px;} ");

		html.append("</style>");
		html.append("</head>");
		html.append("<body>");
		html.append("<h3 align=\"center\" >" + titulo + "</h3>");
		html.append("<p>");
		html.append(texto);
		html.append("</p>");

		html.append("<DIV class=\"separator_vi\"></DIV>");
		html.append("<b>Produto:</b> ");
		html.append(produto.getId());
		html.append("<br> <br>");
		html.append(produto.getDescricao());
		html.append("<br><br>");
		html.append("<DIV class=\"separator_vi\"></DIV>");
		html.append("<br> <b>Observação:</b> <br><br> ");
		html.append(observacaoProduto.getMensagem());
		html.append(" <br>");
		html.append("<DIV class=\"separator_vi\"></DIV>");
		html.append(" <br><br> ");

		html.append(
				"<hr size=\"1\" /> <table> <tr> <td style=\"font-family:tahoma,arial,verdana; font-size:11px; text-align:center\" valign=\"top\">");
		html.append("<a href=\"https://www.fas-amazonas.org\" target=\"_blank\"><img src=\"" + logo
				+ "\" border=\"0\" /></a>");
		html.append("</td> <td style=\"font-family:tahoma,arial,verdana; font-size:12px; padding-left:10px\">");
		html.append("<strong>SISTEMA DE GESTÃO INTEGRADO</strong><br />		");
		html.append("<strong>Fone:</strong> (92) 4009-8900<br />");
		html.append(
				"<strong>Site:</strong> <a href=\"https://www.fas-amazonas.org\" target=\"_blank\">www.fas-amazonas.org</a><br />");
		html.append("<strong>SKYPE:</strong> sgifas@gmail.com");
		html.append("</td> </tr> </table> <hr size=\"1\" />");

		html.append(" </body> </html>");

		return html.toString();

	}

	public String gerarConteudoHTMLAP(String titulo, String texto) {

		StringBuilder html = new StringBuilder("<html> <head> <style>");
		html.append("table {width:100%;} table, th, td { border: 1px solid black; border-collapse: collapse;}");
		html.append("th, td { padding: 5px; text-align: left;} table#t01 tr:nth-child(even) {");
		html.append("background-color: #eee;} table#t01 tr:nth-child(odd) { background-color:#fff;}");
		html.append("table#t01 th { background-color: #849A39; color: white; }");
		html.append("</style>");
		html.append("</head>");
		html.append("<body>");
		html.append("<h3 align=\"center\" >" + titulo + "</h3>");
		html.append("<p>");
		html.append(texto);
		html.append("</p>");
		html.append("<b>Solicitante:</b>" + usuarioSessao.getUsuario().getColaborador().getNome() + "<br><br>"); // busca

		html.append("<table id=\"t01\" style=\"width:100%\"> ");
		html.append("<tr>  <th>#</th>  <th>Código</th>  <th>Descrição do produto</th> <th>Data de criação</th>  </tr>");
		// preparar o for para iterar sobre os itens do pedido

		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		int count = 1;
		for (Produto product : produtosSelected) {

			html.append("<tr> ");
			html.append("<td>");
			html.append(count);
			html.append("</td>");

			html.append("<td>");
			html.append(product.getId());
			html.append("</td>");

			html.append("<td> ");
			html.append(product.getDescricao());
			html.append("</td>");

			if (product.getDataPrecadasto() != null) {
				html.append("<td> ");
				html.append(format.format(product.getDataPrecadasto()));
				html.append("</td>");
			} else {
				html.append("<td> ");
				html.append("</td>");
			}

			html.append("</tr> ");

			count++;
		}

		html.append("</table> </body> </html>");

		return html.toString();

	}

	public void listarProdutos() {
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra") || usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			produtos = repositorio.listarTodos(filtro);
		} else {
			produtos = repositorio.listarTodosSemPrivi(filtro, usuarioSessao.getUsuario());
		}
		carregarUnidades();
	}

	public void cancelar() {
		produto = new Produto();
		limpar();
	}

	public void carregarHistoricos() {
		listHistorico = repositorio.getHistoricosByProduto(produto.getId());
	}

	public void carregarListaDeObservacao() {
		if (produtosSelected.size() > 0) {
			produto = produtosSelected.get(0);
			produto = repositorio.getProdutoPorId(produto.getId());
			listObseravaoProduto = repositorio.buscarObservacao(produto);
			observacaoProduto = new ObservacaoProduto();
		} else {
			addMessage("", "Selecione somente 1(um) produto para alterar.", FacesMessage.SEVERITY_WARN);
		}
	}

	public void salvarObservacao() {
		observacaoProduto.setProduto(produto);
		observacaoProduto.setUsuario(usuarioSessao.getUsuario());
		observacaoProduto.setData(new Date());
		repositorio.salvarObservacao(observacaoProduto);
		carregarListaDeObservacao();
		observacaoProduto = new ObservacaoProduto();
	}

	public boolean filterByPrice(Object value, Object filter, Locale locale) {
		String filterText = (filter == null) ? null : filter.toString().trim();
		if (filterText == null || filterText.equals("")) {
			return true;
		}

		if (value == null) {
			return false;
		}

		return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
	}

	@Transactional
	public void inativar() {
		if (produtosSelected.size() > 0) {
			for (Produto prod : produtosSelected) {
				prod.setInativo(true);
				repositorio.salvar(prod, usuarioSessao.getUsuario());
			}
			listarProdutos();
			addMessage("", "Produto(s) inativo(s).", FacesMessage.SEVERITY_INFO);

		} else {
			addMessage("", "Selecione no mínimo um produto para inativar", FacesMessage.SEVERITY_ERROR);
		}
	}

	@Transactional
	public void reativar() {
		if (produtosSelected.size() > 0) {
			for (Produto prod : produtosSelected) {
				prod.setInativo(false);
				repositorio.salvar(prod, usuarioSessao.getUsuario());
			}
			listarProdutos();
			addMessage("", "Produto(s) ativo(s).", FacesMessage.SEVERITY_INFO);
		} else {
			addMessage("", "Selecione no mínimo um produto para reativar", FacesMessage.SEVERITY_INFO);
		}
	}

	
	public void remover() {
		
		String perfil = usuarioSessao.getUsuario().getPerfil().getDescricao();
		
		
		
		if (produtosSelected.size() > 0) {
			
			if (perfil.equals("admin") ||perfil.equals("compra") ||perfil.equals("financeiro")) {
				removeProductForAdmin();;
			}else{
				removeProductForUser();
			}
			
			
//			for (Produto prod : produtosSelected) {
//				if (perfil.equals("admin")
//						|| perfil.equals("compra")) {
//					repositorio.remover(prod);
//				} else {
//					if (!prod.getPreCadastro()) {
//						addMessage("",
//								"Produto " + prod.getDescricao()
//										+ " não pode ser removido, você não tem privilégios para tal ação",
//								FacesMessage.SEVERITY_ERROR);
//						return;
//					} else {
//						repositorio.remover(prod);
//					}
//				}
//			}

			listarProdutos();
			
		} else {
			addMessage("", "Selecione no mínimo um produto para remover", FacesMessage.SEVERITY_ERROR);
		}

	}
	
	@Transactional
	public void removeProductForAdmin(){
		for (Produto prod : produtosSelected) {
			if (repositorio.verificarProdutoEmPedido(prod.getId())) {
				addMessage("", "Produto: "+prod.getDescricao()+" não pode ser removido.(COD1101)", FacesMessage.SEVERITY_ERROR);
				return;
			}else{
				repositorio.remover(prod);
			}
			
		}
		addMessage("", "Produto(s) removido(s).", FacesMessage.SEVERITY_INFO);
	}
	
	@Transactional
	public void removeProductForUser(){
		for (Produto prod : produtosSelected) {
			if (!prod.getPreCadastro()) {
				addMessage("",
						"Produto " + prod.getDescricao()
								+ " não pode ser removido. (COD1102)",
						FacesMessage.SEVERITY_ERROR);
				return;
			} else {
				if (repositorio.verificarProdutoEmPedido(prod.getId())) {
					addMessage("", "Produto: "+prod.getDescricao()+" não pode ser removido.(COD1101)", FacesMessage.SEVERITY_ERROR);
					return;
				}else{
					repositorio.remover(prod);
				}
			}
		
		}
		
		addMessage("", "Produto(s) removido(s).", FacesMessage.SEVERITY_INFO);
	}

	private String descricaoProduto;

	public String descricaoMaiusculo(String text) {
		descricaoProduto = text.toUpperCase();
		return descricaoProduto;
	}

	public void alterar() {

		boolean adm = usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra") || usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro");

		if (produtosSelected.size() > 0) {
			produto = produtosSelected.get(0);

			if (repositorio.verificarProdutoEmPedido(produto.getId())) {
				produto = new Produto();
				addMessage("", "Produto já foi usado em uma solicitação e não pode ser alterado.",
						FacesMessage.SEVERITY_WARN);
				return;
			}

			if (adm) {
				produto = repositorio.getProdutoPorId(produto.getId());
				ultimaManutencao = repositorio.getHistoricoProduto(produto.getId());
				novoProduto = true;
			} else {
				if (produto.getPreCadastro() == null || !produto.getPreCadastro()) {
					addMessage("",
							"Olá " + usuarioSessao.getUsuario().getColaborador().getNome()
									+ " você não tem autorização para alterar um produto revisado pela equipe de cadastro",
							FacesMessage.SEVERITY_WARN);
					produto = new Produto();
					novoProduto = false;
					return;
				}
				produto = repositorio.getProdutoPorId(produto.getId());
				ultimaManutencao = repositorio.getHistoricoProduto(produto.getId());
				novoProduto = true;
			}
		} else {
			novoProduto = false;
			addMessage("", "Selecione somente 1(um) produto para alterar.", FacesMessage.SEVERITY_WARN);
		}
	}

	public void iniciarProduto() {
		iniciarCadastro();
		produto = new Produto();
		limpar();
	}
	
	public void cancelarCadastro() {
		novoProduto = false;
		produto = new Produto();
		limpar();
	}

	public void filtrarProduto() {
		produtos = repositorio.getProduto(filtro);
	}

	// TODO: métodos (salvar,remover,listar,editar) são correspondentes ao
	// controller da entidade (PRODUTO)
	// Os demais métodos que também são relacionados a CRUD mas de outras
	// entidades devem ser assinados
	// com o nome do método concatenado com o nome da endidade Ex:
	// salvarEntidade();

	public void salvarEditCell(Produto produto) {
		// repositorio.salvar(produto);
		if (produto.getId() == null)
			listarProdutos();
	}

	public void carregarUnidades() {
		unidades = repositorio.buscarUnidades();
	}

	public void addProduto() {
		Produto p = new Produto();
		p.setDescricao(" ");
		p.setMarca(" ");
		p.setNcm(" ");
		p.setUnidadeDeMedida(" ");
		p.setCategoria("");
		p.setTipo("");
		produtos.add(0, p);

	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public List<CategoriaDespesaClass> listaCategoria() {
		return repositorio.getCategoria();
	}

	public void carregarCategorias() {
		categorias = repositorio.buscarGategorias();
	}

	public CategoriaDespesaClass getCategoriaZERO() {
		CategoriaDespesaClass cat = new CategoriaDespesaClass();
		cat.setId(new Long(0));
		cat.setNome("Categorias");
		return cat;
	}

	public UnidadeDeCompra getUnidadeZERO() {
		UnidadeDeCompra unid = new UnidadeDeCompra();
		unid.setId(new Long(0));
		unid.setDescricao("Unidades");
		return unid;
	}

	public void salvar() {

		if (produto.getInativo() == null) {
			produto.setInativo(false);
		}

		if (verificarCampos(produto)) {
			return;
		}

		if (produto.getDescricao() == null || produto.getDescricao().equals("")) {
			addMessage("", "Preencha o campo 'Descrição'.", FacesMessage.SEVERITY_ERROR);
			return;
		} else {
			produto.setDescricao(produto.getDescricao().toUpperCase());
		}

		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra") || usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {
			produto.setSaneado(true);
			repositorio.salvar(produto, usuarioSessao.getUsuario());

		} else {
			produto.setSaneado(true);
			repositorio.salvarPrecadastro(produto, usuarioSessao.getUsuario());
		}
		
		novoProduto = false;
		produto = new Produto();
		listarProdutos();
		limpar();
	}

	public void limpar() {
		ultimaManutencao = "";
	}

	public boolean poderEditar() {
		if (usuarioSessao.getUsuario().getPerfil().getDescricao().equals("admin")
				|| usuarioSessao.getUsuario().getPerfil().getDescricao().equals("compra") || usuarioSessao.getUsuario().getPerfil().getDescricao().equals("financeiro")) {

			return true;
		}
		return false;
	}

	public void remover(Produto produto) {
		if (produto.getId() != null) {
			repositorio.remover(produto);
		}
		produtos.remove(produto);
		listarProdutos();
	}

	public void preparaProduto() {
		produto = new Produto();
	}

	public void editar() {
		produto = repositorio.getProdutoPorId(produto.getId());
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuConfigurações();
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public Produto getProduto() {
		return produto;
	}

	public void setProduto(Produto produto) {
		this.produto = produto;
	}

	public List<Produto> getProdutos() {
		return produtos;
	}

	public void setProdutos(List<Produto> produtos) {
		this.produtos = produtos;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public List<UnidadeDeCompra> getUnidades() {
		return unidades;
	}

	public void setUnidades(List<UnidadeDeCompra> unidades) {
		this.unidades = unidades;
	}

	public List<CategoriaDespesaClass> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<CategoriaDespesaClass> categorias) {
		this.categorias = categorias;
	}

	public Boolean getPanelBloqueio() {
		return panelBloqueio;
	}

	public void setPanelBloqueio(Boolean panelBloqueio) {
		this.panelBloqueio = panelBloqueio;
	}

	public List<Produto> getProdutosSelected() {
		return produtosSelected;
	}

	public void setProdutosSelected(List<Produto> produtosSelected) {
		this.produtosSelected = produtosSelected;
	}

	public String getUltimaManutencao() {
		return ultimaManutencao;
	}

	public void setUltimaManutencao(String ultimaManutencao) {
		this.ultimaManutencao = ultimaManutencao;
	}

	public List<ProdutoHistorico> getListHistorico() {
		return listHistorico;
	}

	public void setListHistorico(List<ProdutoHistorico> listHistorico) {
		this.listHistorico = listHistorico;
	}

	public ObservacaoProduto getObservacaoProduto() {
		return observacaoProduto;
	}

	public void setObservacaoProduto(ObservacaoProduto observacaoProduto) {
		this.observacaoProduto = observacaoProduto;
	}

	public List<ObservacaoProduto> getListObseravaoProduto() {
		return listObseravaoProduto;
	}

	public void setListObseravaoProduto(List<ObservacaoProduto> listObseravaoProduto) {
		this.listObseravaoProduto = listObseravaoProduto;
	}

	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public Boolean getNovoProduto() {
		return novoProduto;
	}

	public void setNovoProduto(Boolean novoProduto) {
		this.novoProduto = novoProduto;
	}

}
