package service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;

import anotacoes.Transactional;
import model.Compra;
import model.CotacaoAuxiliar;
import model.EspelhoPedido;
import model.ItemCompra;
import model.Lancamento;
import model.LancamentoAcao;
import model.LancamentoAvulso;
import model.Observacao;
import model.Pedido;
import model.TipoLancamentoV4;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import operator.LancamentoIterface;
import repositorio.AutorizacaoRepositorio;
import repositorio.ConfiguracaoRepositorio;
import repositorio.CotacaoRepository;
import repositorio.LancamentoRepository;
import util.ArquivoLancamento;
import util.Email;
import util.ReportUtil;

public class SolicService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private AutorizacaoRepositorio autorizacaoRepositorio;

	@Inject
	private CotacaoService cotacaoService;

	public SolicService() {
	}

	public List<Lancamento> getSolicitacoes(Long id) {
		List<Long> listIdAusentes;
		List<Lancamento> list = new ArrayList<Lancamento>();
		list.addAll(autorizacaoRepositorio.getSolicitacoes(id));
		return list;
	}

	public void imprimir(Lancamento lancamento) {

		if (lancamento.getTipov4().equals("PCA")) {
			LancamentoIterface lancamentoInterface = TipoLancamentoV4.valueOf("SA").obterInstancia();
			lancamentoInterface.imprimir(lancamento);

		} else {
			LancamentoIterface lancamentoInterface = TipoLancamentoV4.valueOf(lancamento.getTipov4()).obterInstancia();
			lancamentoInterface.imprimir(lancamento);

		}

	}

	// public void desautorizar(List<Lancamento> lancamentos, String args) {
	//
	// if (lancamentos.size() >= 1) {
	// LancamentoIterface lancamentoInterface =
	// TipoLancamentoV4.valueOf(lancamentos.get(0).getTipov4())
	// .obterInstancia();
	// lancamentoInterface.desautorizar(lancamentos.get(0), "");
	// } else {
	// addMessageError("Selecione ao menos 1 lançamento para autorizar");
	// }
	// }

	@Transactional
	public Boolean autorizar(List<Long> identificacoes, Long idCol) {

		if (identificacoes == null || (!(identificacoes.size() > 0) && (idCol != null && idCol != 0l))) {
			addMessageError("Selecione no mínimo uma(1) solicitação para autorizar.");
			return false;
		}

		boolean retorno = autorizacaoRepositorio.autorizar(identificacoes, idCol);
		if (retorno) {
			addMessage("As solicitações foram autorizadas e podem ser acompanhadas na outra aba",
					FacesMessage.SEVERITY_INFO);
		} else {
			addMessage(
					"Houve algum problema ao gerar sua autorização, aguarde alguns minutos e tente novamente, caso o problema persista, contate time de desenvolvimento através do email pgt@fas-amazonas.org",
					FacesMessage.SEVERITY_ERROR);

		}

		return retorno;
	}

	@Transactional
	public Boolean desautorizar(List<Long> identificacoes, Long idCol, String motivo) {

		if (identificacoes == null || (!(identificacoes.size() >= 1))) {
			addMessageError("Selecione somente 1(uma) solicitação para recusar.");
			return false;
		}

		if (idCol == null || idCol == 0l) {
			addMessageError(
					"Usuário sem colaborador associado, por favor contate o time de desenvolvimento no e-mail pgt@fas-amazonas.org");
			return false;
		}

		boolean retorno = autorizacaoRepositorio.desautorizar(identificacoes, idCol);
		Lancamento lancamento = autorizacaoRepositorio.getLancamentoById(identificacoes.get(0));
		enviarEmailDeRecusa(lancamento, motivo);

		if (retorno) {
			addMessage("As solicitações foram recusadas", FacesMessage.SEVERITY_INFO);
		} else {
			addMessage(
					"Houve algum problema ao gerar sua recusa de solicitações, aguarde alguns minutos e tente novamente, caso o problema persista, contate time de desenvolvimento através do email pgt@fas-amazonas.org",
					FacesMessage.SEVERITY_ERROR);

		}

		return retorno;
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

		// System.out.println(stb.toString());

		return stb.toString();
	}

	public void prepararEmailResuca(Email email, Lancamento lancamento, String motivo) {

		email.setFromEmail(getFromEmail());
		email.setSubject("SOLICITAÇÃO NÃO AUTORIZADA - " + lancamento.getTipov4() + ": " + lancamento.getId() + " Data:"
				+ new SimpleDateFormat("dd/MM/yyyy").format(lancamento.getDataEmissao()));
		email.setSolicitação(String.valueOf(lancamento.getId()));
		email.setSenhaEmail("FAS123fas");
		email.setToEmail(getToEmail(lancamento));
		email.setContent(gerarConteudoHTMLRecusaDeSolicitacao("SOLICITAÇÃO NÃO AUTORIZADA", motivo, lancamento));

	}

	public String gerarConteudoHTMLRecusaDeSolicitacao(String titulo, String texto, Lancamento lancamento) {
		StringBuilder html = new StringBuilder("<html> <head> <style>");
		html.append("table {width:100%;} table, th, td { border: 1px solid black; border-collapse: collapse;}");
		html.append("th, td { padding: 5px; text-align: left;} table#t01 tr:nth-child(even) {");
		html.append("background-color: #eee;} table#t01 tr:nth-child(odd) { background-color:#fff;}");
		html.append("table#t01 th { background-color: #849A39; color: white; }");
		html.append("</style>");
		html.append("</head>");

		html.append("<body>");

		html.append("<h3 align=\"center\" >" + titulo + "</h3>");

		// if (lancamento.getBootUrgencia()) {
		// html.append(" <b>Justificativa:</b> <p style=\"color:red\"> " +
		// lancamento.getJustificativa()
		// + " </p> <br><br>");
		// }

		html.append("<b>Solicitação:</b> " + lancamento.getId() + "<br>");

		html.append("<b>Solicitante:</b> " + lancamento.getSolicitante().getNome() + "<br>");

		html.append("<b>Data:</b> " + new SimpleDateFormat("dd/MM/yyyy").format(lancamento.getDataEmissao()) + "<br>");

		html.append("<b>Gestão:</b> " + lancamento.getGestao().getNome() + "<br><br>");

		html.append("</body> </html>");

		html.append("<p>");
		html.append(texto);
		html.append("</p>");

		return html.toString();
	}

	public void enviarEmailDeRecusa(Lancamento lancamento, String motivo) {
		Email email = new Email();
		prepararEmailResuca(email, lancamento, motivo);

		try {
			try {
				if (email.verificaInternet()) { // verificar internet
					email.EnviarEmailDeRecusa(lancamento);
					// addMessage("", "Mensagem enviada com sucesso", FacesMessage.SEVERITY_INFO);
				}
			} catch (IOException e) {

				addMessage(
						"Problema de conexão com a internet, por favor contate o administrador do sistema, a sua solicitação não foi enviada para os e-mails designados, aguarde e envie mais tarde.",
						FacesMessage.SEVERITY_WARN);
			}
		} catch (Exception e) {

			addMessage(
					"Problema de conexão com a internet, por favor contate o administrador do sistema, a sua solicitação não foi enviada para os e-mails designados, aguarde e envie mais tarde.",
					FacesMessage.SEVERITY_WARN);

		}
	}

	public Lancamento getLancamentoByIdResumo(Long id) {
		Lancamento l = autorizacaoRepositorio.getLancamentoByIdResumo(id);

		l.setArquivos(new ArrayList<ArquivoLancamento>());
		l.setLancamentosAcoes(new ArrayList<>());
		l.setLancamentosAcoes(getRecursos(id));

		if (l.getIdCompra() != null) {
			l.setArquivos(getArquivosByLancamento(id, l.getIdCompra()));
		} else {
			l.setArquivos(getArquivosByLancamento(id));
		}

		return l;
	}

	// ARQUIVOS DE LANÇAMENTO - ANEXOS
	public List<ArquivoLancamento> getArquivosByLancamento(Long id) {
		return autorizacaoRepositorio.getArquivosByLancamento(id);
	}

	// ARQUIVOS DE LANÇAMENTO - ANEXOS
	public List<ArquivoLancamento> getArquivosByLancamento(Long id, Long idCompra) {
		return autorizacaoRepositorio.getArquivosByLancamento(id, idCompra);
	}

	// RECURSOS QUE CUSTEIAM O LANÇAMENTO
	public List<LancamentoAcao> getRecursos(Long id) {
		return autorizacaoRepositorio.getRecursos(id);
	}

	@Inject
	private CotacaoRepository cotRepositorio;
	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<>();

	public List<CotacaoAuxiliar> getCotacaoAuxiliar(Long id) {
		return cotRepositorio.getCotacaoAuxiliar(id);
	}

	public void addMessage(String detail, Severity severiry) {
		FacesMessage message = new FacesMessage(severiry, "", detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void addMessageError(String detail) {
		FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public AutorizacaoRepositorio getAutorizacaoRepositorio() {
		return autorizacaoRepositorio;
	}

	public void setAutorizacaoRepositorio(AutorizacaoRepositorio autorizacaoRepositorio) {
		this.autorizacaoRepositorio = autorizacaoRepositorio;
	}

	public String getTotais(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}

	public void imprimirMapaComparativo(Long id) {

		StringBuilder resumo = new StringBuilder();
		Compra compra = cotacaoService.getCompraById(id);

		cotacoesAuxiliares = cotacaoService.getCotacaoAuxiliar(id);
		for (CotacaoAuxiliar cot : cotacoesAuxiliares) {
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

		for (CotacaoAuxiliar cotAuxiliar : cotacoesAuxiliares) {

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
				compra.getSolicitante().getNome() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
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

	@Inject
	private LancamentoRepository lancamentoRepositorio;

	public List<LancamentoAvulso> getPrestacoes(Long id) {
		return lancamentoRepositorio.getPrestacoes(id);
	}

	public LancamentoAcao buscarLancamentoAcao(Long id) {
		return lancamentoRepositorio.getLancamentoAcaoPorLancamento(id);
	}

	public void imprimirPrestacao(Long idAdiantamento) {

		BigDecimal totalEntrada = BigDecimal.ZERO;
		BigDecimal totalSaida = BigDecimal.ZERO;
		String conta = "";

		// filtro.setNumeroDocumento(idAdiantamento.toString());

		LancamentoAcao lancamentoAcao = buscarLancamentoAcao(idAdiantamento);

		List<LancamentoAvulso> mListAux = getPrestacoes(idAdiantamento);

		List<LancamentoAvulso> mList = new ArrayList<>();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		BigDecimal saldo = BigDecimal.ZERO;

		for (LancamentoAvulso lancamentoAvulso : mListAux) {
			LancamentoAvulso l = new LancamentoAvulso();

			System.out.println(l.getNumeroDocumento());

			l.setDtEmissao(sdf.format(lancamentoAvulso.getDataEmissao()));
			l.setDtPagto(sdf.format(lancamentoAvulso.getDataPagamento()));
			l.setDescricao(lancamentoAvulso.getDescricao());
			l.setNumeroDocumento(lancamentoAvulso.getNumeroDocumento());
			l.setPagador(lancamentoAvulso.getPagador());
			l.setRecebedor(lancamentoAvulso.getRecebedor());
			l.setAcao(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());

			if (lancamentoAvulso.getTipoLancamento() == null || lancamentoAvulso.getTipoLancamento().equals("")) {
				l.setEntrada(BigDecimal.ZERO);
				l.setSaida(lancamentoAvulso.getValorTotalComDesconto());
			}

			if (lancamentoAvulso.getTipoLancamento().equals("ad")) {
				conta = l.getRecebedor();
				l.setEntrada(lancamentoAvulso.getValorTotalComDesconto());
				l.setSaida(BigDecimal.ZERO);
			}

			if (lancamentoAvulso.getTipoLancamento().equals("reenb")) {
				conta = l.getRecebedor();
				l.setEntrada(lancamentoAvulso.getValorTotalComDesconto());
				l.setSaida(BigDecimal.ZERO);
			}

			if (lancamentoAvulso.getTipoLancamento().equals("dev")) {
				l.setEntrada(BigDecimal.ZERO);
				l.setSaida(lancamentoAvulso.getValorTotalComDesconto());
			}

			totalEntrada = totalEntrada.add(l.getEntrada());
			totalSaida = totalSaida.add(l.getSaida());

			saldo = saldo.add(l.getEntrada()).subtract(l.getSaida());
			l.setSaldo(saldo);
			mList.add(l);
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("contabancaria", conta);
		parametros.put("TOTAL_ENTRADA", totalEntrada);
		parametros.put("TOTAL_SAIDA", totalSaida);
		parametros.put("saldoFinal", totalEntrada.subtract(totalSaida));
		parametros.put("usuario", "" + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));

		JRDataSource dataSource = new JRBeanCollectionDataSource(mList, false);

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/PrestacaoContas.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("Adiantamento", "Adiantamento" + idAdiantamento, report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
