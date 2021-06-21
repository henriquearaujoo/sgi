package service;

import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;

import anotacoes.Transactional;
import model.Acao;
import model.Aprouve;
import model.CategoriaDespesaClass;
import model.CategoriaProjeto;
import model.Compra;
import model.CompraMunicipio;
import model.Estado;
import model.FontePagadora;
import model.ItemCompra;
import model.ItemPedido;
import model.Lancamento;
import model.LancamentoAcao;
import model.Localidade;
import model.LogStatus;
import model.Municipio;
import model.OrcamentoProjeto;
import model.Pedido;
import model.Produto;
import model.Projeto;
import model.ProjetoRubrica;
import model.StatusCompra;
import model.UnidadeConservacao;
import model.UnidadeDeCompra;
import model.User;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import repositorio.AcaoRepositorio;
import repositorio.AprouveRepositorio;
import repositorio.ConfiguracaoRepositorio;
import repositorio.CotacaoRepository;
import repositorio.EstadoRepositorio;
import repositorio.FontePagadoraRepositorio;
import repositorio.LancamentoRepository;
import repositorio.LocalRepositorio;
import repositorio.OrcamentoRepositorio;
import repositorio.PedidoRepositorio;
import repositorio.ProdutoRepositorio;
import repositorio.ProjetoRepositorio;
import repositorio.UsuarioRepository;
import util.CompraUtil;
import util.Email;
import util.Filtro;
import util.ReportUtil;
import util.UsuarioSessao;
import util.Util;

/**
 * @author: Italo Almeida
 * @version 3.0
 * 
 *          Classe: Gestao de Projeto Objetivo: Gerir a regra de negocio
 *          referente a instancia do objeto
 *
 *
 */

public class CompraService implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private LancamentoRepository repositorio;
	@Inject
	private EstadoRepositorio estadoRepositorio;
	@Inject
	private LocalRepositorio localRepositorio;
	@Inject
	private ProdutoRepositorio produtoRepositorio;
	@Inject
	private AcaoRepositorio acaoRepositorio;
	@Inject
	private FontePagadoraRepositorio fonteRespositorio;
	@Inject
	private AprouveRepositorio aprouveRepositorio;
	@Inject
	private PedidoRepositorio pedidoRepositorio;

	@Inject
	private CotacaoRepository cotacaoRepositorio;

	@Inject
	private UsuarioRepository usuarioRepositorio;

	@Inject
	private ProjetoRepositorio projetoRepositorio;

	@Inject
	private OrcamentoRepositorio orcamentoRepositorio;

	@Inject
	private UsuarioSessao usuarioSessao;

	@Transactional
	public Boolean excluirCotacaoByItem(ItemCompra item) {
		return cotacaoRepositorio.excluirCotacaoByItem(item);
	}

	public List<UnidadeDeCompra> buscarUnidades() {
		return produtoRepositorio.buscarUnidades();
	}

	public List<CategoriaProjeto> getCategoriasDeProjeto(Filtro filtro) {
		return projetoRepositorio.getListaDeCategoriasDeProjeto(filtro);
	}

	public Projeto findProjetoById(Long id) {
		return projetoRepositorio.getProjetoPorId(id);
	}

	public List<CategoriaDespesaClass> buscarCategoriasDeCompras() {
		return produtoRepositorio.buscarGategorias();
	}

	public List<Municipio> getMunicipiosDeCompra() {
		return localRepositorio.getMunicipiosDeCompra();
	}

	public List<Projeto> getProjetosFiltroPorUsuarioMODE01(Filtro filtro, User usuario) {
		return projetoRepositorio.getProjetosFiltroPorUsuarioMODE01(filtro, usuario);
	}

	public List<ProjetoRubrica> getRubricasDeProjeto(Long idProjeto, Long idOrcamento) {
		return orcamentoRepositorio.getRubricasDeProjeto(idProjeto, idOrcamento);
	}

	public List<ProjetoRubrica> getRubricasDeProjetoJOIN(Long idProjeto, Long idOrcamento) {
		return orcamentoRepositorio.getRubricasDeProjetoJOIN(idProjeto, idOrcamento);
	}

	public StringBuilder buscarMunicipiosDeCompra(List<Municipio> municipiosDeCompraSelected, Compra compra) {
		List<CompraMunicipio> list = getLocaisDeCompra(compra.getId());
		StringBuilder localDeCompra = new StringBuilder();

		for (CompraMunicipio compraMunicipio : list) {
			municipiosDeCompraSelected.add(compraMunicipio.getMunicipio());
			localDeCompra.append("/");
			localDeCompra.append(compraMunicipio.getNomeMunicipio());
		}
		return localDeCompra;

	}

	public void imprimirCompra(Compra compra) {
		compra = getCompraById(compra.getId());
		StringBuilder localDeCompra = buscarMunicipiosDeCompra(new ArrayList<Municipio>(), compra);
		List<ItemCompra> itens = getItensTransientByCompra(compra);
		List<LancamentoAcao> acoes = getLancamentosAcoes(compra);

		StringBuilder acs = new StringBuilder();

		if (compra.getVersionLancamento() != null && compra.getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getProjetoRubrica().getComponente().getNome());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				acs.append("\n");
			}
		} else {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getAcao().getCodigo() + " \n");
			}
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		HashMap<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("logo", logo);
		parametros.put("tipo_gestao", compra.getTipoGestao().getNome());
		parametros.put("gestao", compra.getGestao().getNome());
		parametros.put("tipo_localidade", compra.getTipoLocalidade().getNome());
		parametros.put("localidade", compra.getLocalidade().getNome());
		parametros.put("usuario",
				usuarioSessao.getNomeUsuario() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", compra.getId().toString());
		parametros.put("observ", compra.getObservacao());
		parametros.put("acao", acs.toString());
		parametros.put("locais_compra", localDeCompra.toString());

		Util util = new Util();

		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy hh:mm").format(compra.getDataEmissao()));

		if (compra.getTipoGestao().getNome().equals("Regional")) {
			// setarParamRegional(parametros);
			util.setarParamRegional(parametros, compra);
		}

		if (compra.getTipoGestao().getNome().equals("Coordenadoria")) {
			// setarParamCoordenadoria(parametros);
			util.setarParamCoordenadoria(parametros, compra);
		}

		if (compra.getTipoGestao().getNome().equals("Superintendencia")) {
			// setarParamSuperintendencia(parametros);
			util.setarParamSuperintendencia(parametros, compra);
		}

		if (compra.getTipoLocalidade().getNome().equals("Uc")) {
			// setarDestinoUC(parametros);
			util.setarDestinoUC(parametros, compra, this);
		} else if (compra.getTipoLocalidade().getNome().equals("Comunidade")) {
			// setarDestinoComunidade(parametros);
			util.setarDestinoComunidade(parametros, compra);
		} else {
			// setarDestino(parametros);
			util.setarDestino(parametros, compra);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(itens);

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/scnew.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("SC", "" + compra.getId(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// metodo movido para calculatorRubricaRepositorio
	@Deprecated
	public List<ProjetoRubrica> completeRubricasDeProjetoJOIN(String s) {
		return orcamentoRepositorio.completeRubricasDeProjetoJOIN(s);
	}

	public List<OrcamentoProjeto> getOrcamentoByProjeto(Long id) {
		return orcamentoRepositorio.getOrcamentosProjeto(id);
	}

	public void enviarEmailCompra(Compra compra) throws EmailException {
		Email email = new Email();
		email.EnviarSolicitacaoCompra(compra);
	}

	@Transactional
	public boolean salvar(Compra compra, User usuario, List<Municipio> municipios, Integer type) {
		try {

			compra.setListCompraMunicipio(new ArrayList<>());
			for (Municipio municipio : municipios) {
				CompraMunicipio compraMunicipio = new CompraMunicipio(municipio, compra);
				compra.getListCompraMunicipio().add(compraMunicipio);
			}

			compra.setCategoriaDespesaClass(compra.getItens().get(0).getProduto().getCategoriaDespesa());
			if (compra.getCategoriaDespesaClass().getNome().equals("FRETE SEDEX")
					|| compra.getCategoriaDespesaClass().getNome().equals("FRETE PAC")) {
				if (compra.getItens().size() > 1)
					compra.setCategoriaDespesaClass(compra.getItens().get(1).getProduto().getCategoriaDespesa());
			}

			if (compra.getId() == null) {
				compra.setDataEmissao(new Date());
			}

			if (type == 1) {
				compra.setStatusCompra(StatusCompra.PENDENTE_APROVACAO);
				compra = repositorio.salvarCompra(compra, usuario);
				enviarEmailAutorizacaoSC(compra, type);
				addMessage("", "Salvo com sucesso, confirme o e-mail na sua caixa de entrada, caso não tenha recebido clique em reenviar", FacesMessage.SEVERITY_INFO);
			} else {
				compra.setStatusCompra(StatusCompra.PENDENTE_APROVACAO);
				compra = repositorio.salvarCompra(compra, usuario);
				addMessage("", "Salvo com sucesso.", FacesMessage.SEVERITY_INFO);
			}


			return true;

		} catch (Exception e) {
			addMessage("", "Erro ao salvar!!!", FacesMessage.SEVERITY_ERROR);
			return false;
		}

	}

	@Inject
	protected ConfiguracaoRepositorio configRepositorio;

	public String getFromEmail() {
		return configRepositorio.getFromEmail().get(0).getEmail();
	}

	public String getToEmail(Lancamento lancamento) {
		List<String> listEmail = configRepositorio.getToEmail(lancamento);
		StringBuilder stb = new StringBuilder();

		// GESTOR DA COMPRA
		stb.append(lancamento.getGestao().getColaborador().getEmail());
		stb.append(",");
		// SOLICITANTE DA COMPRA
		stb.append(lancamento.getSolicitante().getEmail());

		// LISTA DE APROVADORES E EMAILS PADRÕES DE CONDIGURAÇÃO
		// (COMPRAS/FINANCEIRO/LOGÍSTICA)
		for (String email : listEmail) {
			stb.append(",");
			stb.append(email);
		}


		return stb.toString();
	}

	public void prepararEmailAutorizacaoSC(Email email, Compra compra) {

		email.setFromEmail(getFromEmail());
		email.setSubject("AUTORIZACAO DE SC-" + compra.getId() + " Data:"
				+ new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()));
		email.setSolicitação(String.valueOf(compra.getId()));
		email.setSenhaEmail("FAS123fas");
		email.setToEmail(getToEmail(compra));
		email.setContent(gerarConteudoHTMLSC("SOLICITAÇÃO DE COMPRA",
				"Solicito autorização para abertura do processo de compras, para o documento em anexo.", compra));

	}

	public String gerarConteudoHTMLSC(String titulo, String texto, Compra compra) {
		StringBuilder html = new StringBuilder("<html> <head> <style>");
		html.append("table {width:100%;} table, th, td { border: 1px solid black; border-collapse: collapse;}");
		html.append("th, td { padding: 5px; text-align: left;} table#t01 tr:nth-child(even) {");
		html.append("background-color: #eee;} table#t01 tr:nth-child(odd) { background-color:#fff;}");
		html.append("table#t01 th { background-color: #849A39; color: white; }");
		html.append("</style>");
		html.append("</head>");

		html.append("<body>");

		if (compra.getBootUrgencia()) {
			html.append("<h3 align=\"center\" style=\"color:red\" > " + titulo + " URGENTE</h3>");
		} else {
			html.append("<h3 align=\"center\" >" + titulo + "</h3>");
		}

		if (compra.getBootUrgencia()) {
			html.append(
					" <b>Justificativa:</b> <p style=\"color:red\"> " + compra.getJustificativa() + " </p> <br><br>");
		}

		html.append("<b>Solicitação:</b> " + compra.getId() + "<br>");

		html.append("<b>Solicitante:</b> " + compra.getSolicitante().getNome() + "<br>");

		html.append("<b>Data:</b> " + new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()) + "<br>");

		html.append("<b>Tipo gestão:</b> " + compra.getTipoGestao().getNome() + "<br>");

		html.append("<b>Gestão:</b> " + compra.getGestao().getNome() + "<br><br>");

		html.append("</body> </html>");

		html.append("<p>");
		html.append(texto);
		html.append("</p>");

		return html.toString();
	}
	
	
	@Transactional
	public void atualizarStatus(Long id) {
		repositorio.mudarStatus(id, StatusCompra.PENDENTE_APROVACAO);
	}

	public void enviarEmailAutorizacaoSC(Compra compra, Integer type) {
		Email email = new Email();
		
		if (type == 0) {
			atualizarStatus(compra.getId());
		}
		
		prepararEmailAutorizacaoSC(email, compra);

		try {
			try {
				if (email.verificaInternet()) { // verificar internet
					email.EnviarSolicitacaoCompra(compra);
					//addMessage("", "Mensagem enviada com sucesso", FacesMessage.SEVERITY_INFO);
				}
			} catch (IOException e) {

				addMessage("",
						"Problema de conexão com a internet, por favor contate o administrador do sistema, a sua solicitação não foi enviada para os e-mails designados, aguarde e envie mais tarde.",
						FacesMessage.SEVERITY_WARN);
			}
		} catch (Exception e) {

			addMessage("",
					"Problema de conexão com a internet, por favor contate o administrador do sistema, a sua solicitação não foi enviada para os e-mails designados, aguarde e envie mais tarde.",
					FacesMessage.SEVERITY_WARN);

		}
	}

	public List<CompraMunicipio> getLocaisDeCompra(Long id) {
		return repositorio.getLocaisDeCompra(id);
	}

	@Transactional
	public Boolean excluirItemPedidoByItemCompra(ItemCompra item) {
		return repositorio.excluirItemPedidoByItemCompra(item);
	}

	public Date getDataAprovacao(Long compra) {
		return repositorio.getDataDeAprovacao(compra);
	}

	public UnidadeConservacao findByIdUnidadeConservacao(Long id) {
		return localRepositorio.findByIdUnidadeDeConservacao(id);
	}

	public User getUsuarioByColaborador(Long colaborador) {
		return usuarioRepositorio.getUsuarioByColaborador(colaborador);
	}

	public Boolean verificarAprovacao(Aprouve aprouve) {
		return aprouveRepositorio.findAprouve(aprouve);
	}

	public List<Projeto> getProjetoAutoCompleteMODE01(String s) {
		return projetoRepositorio.getProjetoAutocompleteMODE01(s);
	}

	@Transactional
	public boolean cancelarCompra(LogStatus log) {

		// excluir pedidos

		Compra compra = (Compra) log.getLancamento();
		List<Pedido> pedidos = pedidoRepositorio.getPedidos(compra);

		for (Pedido pedido : pedidos) {
			pedido.setStatusCompra(StatusCompra.CANCELADO);
			pedidoRepositorio.salvar(pedido);
			repositorio.deleteFromPagamentoByPedido(pedido);
		}

		compra.setStatusCompra(StatusCompra.CANCELADO);
		repositorio.salvar(compra);

		return true;
	}

	public Localidade getLocalidadeById(Long id) {
		return localRepositorio.getLocalPorId(id);
	}

	public List<LancamentoAcao> getLancamentosAcoes(Lancamento lancamento) {
		return repositorio.getLancamentosAcoes(lancamento);
	}

	@Transactional
	public Compra mudarStatus(Long id, StatusCompra statusCompra, LogStatus logStatus) {
		repositorio.salvarLog(logStatus);
		return repositorio.mudarStatusCompra(id, statusCompra);

	}

	@Transactional
	public Compra mudarStatusParaSuspensao(Long id, StatusCompra statusCompra, LogStatus logStatus) {
		Compra compra = (Compra) logStatus.getLancamento();
		List<Pedido> pedidos = pedidoRepositorio.getPedidos(compra);

		for (Pedido pedido : pedidos) {
			pedido.setStatusCompra(StatusCompra.CANCELADO);
			pedidoRepositorio.salvar(pedido);
		}

		return repositorio.mudarStatusCompra(id, statusCompra);
	}

	public boolean findAprouve(Aprouve aprouve) {
		return aprouveRepositorio.findAprouvePedido(aprouve);
	}

	public List<ItemCompra> getItensByCompra(Compra compra) {
		return repositorio.getItensByCompra(compra);
	}

	public List<ItemCompra> getItensByCompraAllProducts(Compra compra) {
		return repositorio.getItensByCompraAllProduct(compra);
	}

	public List<ItemCompra> getItensTransientByCompra(Compra compra) {
		return repositorio.getItensTransientByCompra(compra);
	}

	public List<ItemCompra> getItensTransientByCompraNovo(Compra compra) {
		return repositorio.getItensTransientByCompra(compra);
	}

	public Compra getCompraById(Long id) {
		return repositorio.getCompraPorId(id);
	}

	public List<Estado> getEstados(Filtro filtro) {
		return estadoRepositorio.getEatados(filtro);
	}

	public List<Produto> getProdutos(String s) {
		return produtoRepositorio.getProduto(s);
	}

	public List<Localidade> getMunicipioByEstado(Long id) {
		return localRepositorio.getMunicipioByEstado(id);
	}

	public CompraService() {

	}

	public List<Acao> acoesAutoComplete(String acao) {
		return acaoRepositorio.getAcaoAutoComplete(acao);
	}

	public List<FontePagadora> fontesAutoComplete(String fonte) {
		List<FontePagadora> fontesPagadora = new ArrayList<>();
		fontesPagadora = fonteRespositorio.getFonteAutoComplete(fonte);
		
		return fontesPagadora;
	}

	public CompraService(LancamentoRepository repositorio) {
		this.repositorio = repositorio;
	}

	public List<Compra> getCompras(Filtro filtro) {
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<Compra>();
			}

			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());

			Calendar calFinal = Calendar.getInstance();
			calFinal.setTime(filtro.getDataFinal());

			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);

			calFinal.set(Calendar.HOUR, 24);
			calFinal.set(Calendar.MINUTE, 0);
			calFinal.set(Calendar.SECOND, 0);
			calFinal.set(Calendar.MILLISECOND, 0);

			filtro.setDataInicio(calInicio.getTime());
			filtro.setDataFinal(calFinal.getTime());


		} else if (filtro.getDataInicio() != null) {
			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());
			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);
			filtro.setDataInicio(calInicio.getTime());
		}

		return repositorio.getCompras(filtro);
	}

	public Boolean verificaItemPedido(Long id) {
		return repositorio.verificaItemPedido(id);
	}

	@Transactional
	public void verificaStatusExclusaoItemCompra(Compra compra) {
		if (repositorio.getQtdItemPedido(compra) > 0) {
			repositorio.mudarStatus(compra.getId(), StatusCompra.PARCIAL_CONCLUIDO);
		}

		if (CompraUtil.ComparaQuantidadeItens(compra, repositorio)) {
			repositorio.mudarStatus(compra.getId(), StatusCompra.CONCLUIDO);
			compra.setStatusCompra(StatusCompra.PARCIAL_CONCLUIDO);
		}
	}

	@Transactional
	public Compra verificaStatusSC(Compra compra) {
		if (repositorio.getQtdItemPedido(compra) > 0) {
			repositorio.mudarStatus(compra.getId(), StatusCompra.PARCIAL_CONCLUIDO);
			compra.setStatusCompra(StatusCompra.PARCIAL_CONCLUIDO);
		}

		if (CompraUtil.ComparaQuantidadeItens(compra, repositorio)) {
			repositorio.mudarStatus(compra.getId(), StatusCompra.CONCLUIDO);
			compra.setStatusCompra(StatusCompra.CONCLUIDO);

		}

		return compra;
	}

	@Transactional
	public StatusCompra atualizarStatus(Compra compra) {
		if (CompraUtil.ComparaQuantidadeItens(compra, repositorio)) {
			repositorio.mudarStatus(compra.getId(), StatusCompra.CONCLUIDO);
		} else {
			repositorio.mudarStatus(compra.getId(), StatusCompra.PARCIAL_CONCLUIDO);
		}
		return compra.getStatusCompra();
	}

	// TODO: Relatório referente a compras detalhadas por item.
	public List<ItemCompra> getItensCompra(Filtro filtro) {
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<ItemCompra>();
			}

			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());

			Calendar calFinal = Calendar.getInstance();
			calFinal.setTime(filtro.getDataFinal());

			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);

			calFinal.set(Calendar.HOUR, 24);
			calFinal.set(Calendar.MINUTE, 0);
			calFinal.set(Calendar.SECOND, 0);
			calFinal.set(Calendar.MILLISECOND, 0);

			filtro.setDataInicio(calInicio.getTime());
			filtro.setDataFinal(calFinal.getTime());


		} else if (filtro.getDataInicio() != null) {
			Calendar calInicio = Calendar.getInstance();
			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);
			filtro.setDataInicio(calInicio.getTime());
		}

		return repositorio.getItensCompraDetalhados(filtro);
	}

	public List<ItemPedido> getItensPedidos(Filtro filtro) {
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<ItemPedido>();
			}

			Calendar calInicio = Calendar.getInstance();
			calInicio.setTime(filtro.getDataInicio());

			Calendar calFinal = Calendar.getInstance();
			calFinal.setTime(filtro.getDataFinal());

			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);

			calFinal.set(Calendar.HOUR, 24);
			calFinal.set(Calendar.MINUTE, 0);
			calFinal.set(Calendar.SECOND, 0);
			calFinal.set(Calendar.MILLISECOND, 0);

			filtro.setDataInicio(calInicio.getTime());
			filtro.setDataFinal(calFinal.getTime());


		} else if (filtro.getDataInicio() != null) {
			Calendar calInicio = Calendar.getInstance();
			calInicio.set(Calendar.HOUR, 0);
			calInicio.set(Calendar.MINUTE, 0);
			calInicio.set(Calendar.SECOND, 0);
			calInicio.set(Calendar.MILLISECOND, 0);
			filtro.setDataInicio(calInicio.getTime());
		}

		return repositorio.getItensPedidosDetalhados(filtro);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

}
