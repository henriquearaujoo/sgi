package service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.http.HttpServletRequest;

import org.bouncycastle.asn1.ocsp.ResponderID;

import anotacoes.Transactional;
import model.Acao;
import model.Aprouve;
import model.CategoriaDespesaClass;
import model.CategoriaFinanceira;
import model.Compra;
import model.ContaBancaria;
import model.DespesaReceita;
import model.Estado;
import model.FontePagadora;
import model.Fornecedor;
import model.Lancamento;
import model.LancamentoAcao;
import model.Localidade;
import model.LogStatus;
import model.ProjetoRubrica;
import model.SolicitacaoPagamento;
import model.StatusAdiantamento;
import model.StatusCompra;
import model.TipoDeDocumentoFiscal;
import model.TipoGestao;
import model.TipoLocalidade;
import model.TipoParcelamento;
import model.UnidadeConservacao;
import model.User;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import operator.Parcela;
import repositorio.AcaoRepositorio;
import repositorio.AprouveRepositorio;
import repositorio.CategoriaRepositorio;
import repositorio.ConfiguracaoRepositorio;
import repositorio.ContaRepository;
import repositorio.CotacaoRepository;
import repositorio.EstadoRepositorio;
import repositorio.FontePagadoraRepositorio;
import repositorio.FornecedorRepositorio;
import repositorio.LancamentoRepository;
import repositorio.LocalRepositorio;
import repositorio.OrcamentoRepositorio;
import repositorio.PagamentoLancamentoRepository;
import repositorio.ProjetoRepositorio;
import util.ArquivoLancamento;
import util.Email;
import util.Filtro;
import util.GeradorConteudoHTML;
import util.ReportUtil;
import util.Util;

public class SolicitacaoPagamentoService implements Serializable {

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
	private AcaoRepositorio acaoRepositorio;
	@Inject
	private FontePagadoraRepositorio fonteRespositorio;

	@Inject
	private FornecedorRepositorio fornecedorRepositorio;

	@Inject
	private CotacaoRepository Cotacaorepositorio;
	@Inject
	private ContaRepository contaRepositorio;

	@Inject
	private PagamentoLancamentoRepository pagtoRepositorio;

	@Inject
	private CategoriaRepositorio categoriaRepositorio;

	@Inject
	private AprouveRepositorio aprouveRepositorio;

	@Inject
	private OrcamentoRepositorio orcamentoRepositorio;

	@Inject
	private ProjetoRepositorio projetoRepositorio;

	// metodo movido para CalculatorRubricaRepositorio
	@Deprecated
	public List<ProjetoRubrica> completeRubricasDeProjetoJOIN(String s) {
		return orcamentoRepositorio.completeRubricasDeProjetoJOIN(s);
	}

	public List<CategoriaFinanceira> buscarCategoriasFin() {
		return categoriaRepositorio.buscarGategoriasFin();
	}

	public SolicitacaoPagamentoService() {
	}

	@Transactional
	public ArquivoLancamento salvarArquivo(ArquivoLancamento arquivo) {
		return repositorio.salvarArquivo(arquivo);
	}

	public Boolean verificarExigencia(SolicitacaoPagamento pagamento) {

		TipoDeDocumentoFiscal tdf = pagamento.getTipoDocumentoFiscal();
		if (tdf == null) {
			return true;
		}
		switch (tdf) {
		case OUTRO:
			return false;
		case RECIBO:
			return false;
		default:
			return true;
		}
	}

	public Boolean verificarSaldo(Long projeto, BigDecimal valor) {
		BigDecimal orcado = Util.getNullValue(projetoRepositorio.getOrcamentoDeProjeto(projeto));
		BigDecimal saida = Util.getNullValue(orcamentoRepositorio.getTotalDespesa(projeto));
		BigDecimal entrada = Util.getNullValue(orcamentoRepositorio.getTotalReceita(projeto));
		BigDecimal saldo = orcado.subtract(saida).add(entrada);
		return (saldo.compareTo(valor) > 0 || saldo.compareTo(valor) >= 0);
	}

	// metodo movido e atualizado para a class CalculatorRubricaRepositorio
	@Deprecated
	public Boolean verificarSaldoRubrica(LancamentoAcao lancamentoAcao, BigDecimal valor) {

		BigDecimal asomar = BigDecimal.ZERO;

		if (lancamentoAcao.getId() != null) {
			asomar = asomar.add(orcamentoRepositorio.findByIdLancamentoAcao(lancamentoAcao.getId()).getValor());
		}

		BigDecimal orcado = Util
				.getNullValue(projetoRepositorio.getOrcamentoDeRubrica(lancamentoAcao.getProjetoRubrica().getId()));
		BigDecimal saida = Util.getNullValue(
				orcamentoRepositorio.getTotalDespesaPorRubrica(lancamentoAcao.getProjetoRubrica().getId()));
		BigDecimal entrada = Util.getNullValue(
				orcamentoRepositorio.getTotalReceitaPorRubrica(lancamentoAcao.getProjetoRubrica().getId()));
		BigDecimal saldo = orcado.subtract(saida).add(entrada).add(asomar);
		return (saldo.compareTo(valor) > 0 || saldo.compareTo(valor) >= 0);
	}

	public Lancamento getLancamentoById(Long id) {
		return repositorio.getLancamentoById(id);
	}

	@Transactional
	public SolicitacaoPagamento salvar(SolicitacaoPagamento pagamento, User usuario, Integer type) {

		try {

			pagamento.setTipoGestao(buscarTipoGestao(pagamento));
			pagamento.setTipoLocalidade(buscarTipoLocalidade(pagamento));
			pagamento.setVersionLancamento("MODE01");

			if (pagamento.getCondicaoPagamentoEnum().toString().equals("DEPOSITO")
					&& pagamento.getContaRecebedor() == null)
				throw new Exception("Selecione uma conta bancária.");

			if (pagamento.getLancamentosAcoes().size() >= 1) {

				if (pagamento.getId() == null) {
					pagamento.setCodigo("");

					pagamento.setDataEmissao(new Date());
					pagamento.setSolicitante(usuario.getColaborador());
					pagamento.setDepesaReceita(DespesaReceita.DESPESA);
				}

				SolicitacaoPagamento p = new SolicitacaoPagamento();

				verificarNF(pagamento, p);

				pagamento.setCategoriaDespesaClass(null);

				if (type == 1) {
					pagamento.setStatusCompra(StatusCompra.PENDENTE_APROVACAO);
					pagamento = repositorio.salvarSolicitacaoPagamento(pagamento, usuario);
					enviarEmailAutorizacaoSP(pagamento, type);
					addMessage("",
							"Salvo com sucesso,  confirme o e-mail na sua caixa de entrada, caso não tenha recebido clique em reenviar",
							FacesMessage.SEVERITY_INFO);
				} else {
					pagamento.setStatusCompra(StatusCompra.PENDENTE_APROVACAO);
					repositorio.salvarSolicitacaoPagamento(pagamento, usuario);
					addMessage("", "Salvo com sucesso.", FacesMessage.SEVERITY_INFO);
				}

			} else {
				throw new Exception(
						"Você deve adicionar ao menos 1(uma) linha Orçamentária/ação ao lançamento para que o armazenamento seja concluído.");
			}
		} catch (Exception e) {
			addMessage("", e.getMessage(), FacesMessage.SEVERITY_WARN);
		}

		return pagamento;

	}

	public TipoGestao buscarTipoGestao(SolicitacaoPagamento pagamento) {
		String type = pagamento.getGestao().getType();
		switch (type) {
		case "coord":
			return TipoGestao.COORD;
		case "reg":
			return TipoGestao.REGIONAL;
		default:
			return TipoGestao.SUP;
		}
	}

	public TipoLocalidade buscarTipoLocalidade(SolicitacaoPagamento pagamento) {

		String type = pagamento.getLocalidade().getType();
		switch (type) {
		case "mun":
			return TipoLocalidade.MUNICIPIO;
		case "uc":
			return TipoLocalidade.UC;
		case "com":
			return TipoLocalidade.COMUNIDADE;
		case "sede":
			return TipoLocalidade.SEDE;
		default:
			return TipoLocalidade.NUCLEO;
		}
	}

	private void verificarNF(SolicitacaoPagamento pagamento, SolicitacaoPagamento p) throws Exception {
		if (pagamento.getNotaFiscal().length() > 1 && pagamento.getContaRecebedor() != null)
			p = verificarNotaFiscal(pagamento.getNotaFiscal(),
					pagamento.getId() != null ? pagamento.getId() : new Long(0), pagamento.getContaRecebedor().getId());

		if (p != null && p.getId() != null) {
			p = null;
			throw new Exception("Nota fiscal já foi paga no lançamento de número: " + p.getId());
		}
	}

	public Boolean verificarPrivilegioENF(Aprouve aprouve) {
		return aprouveRepositorio.verificaPrivilegioNF(aprouve);
	}

	public List<CategoriaDespesaClass> buscarCategorias() {
		return categoriaRepositorio.buscarGategorias();
	}

	public List<CategoriaDespesaClass> buscarCategoriasFinanceira() {
		return categoriaRepositorio.buscarGategoriasFinanceira();
	}

	public SolicitacaoPagamento verificarNotaFiscal(String nf, Long id, Long idConta) {
		return pagtoRepositorio.verificarNotaFiscal(nf, id, idConta);
	}

	public List<ArquivoLancamento> getArquivoByLancamento(Long lancamento) {
		return repositorio.getArquivosByLancamento(lancamento);
	}

	@Transactional
	public void removerArquivo(ArquivoLancamento arquivo) {
		repositorio.removerArquivo(arquivo);
	}

	private TipoParcelamento tipoParcelamento;

	@Transactional
	public void mudarStatus(Long id, StatusCompra statusCompra, LogStatus logStatus) {

		repositorio.mudarStatusPagamento(id, statusCompra);
		repositorio.salvarLog(logStatus);
	}

	public Date setarData(Date dataBase) {
		Parcela parcela = tipoParcelamento.obterParcela();
		Date data = parcela.calculaData(dataBase);
		return data;
	}

	public boolean findAprouve(Aprouve aprouve) {
		return aprouveRepositorio.findAprouvePedido(aprouve);
	}

	public ContaBancaria getContaById(Long id) {
		return contaRepositorio.getContaById(id);
	}

	public ContaBancaria getContaByFornecedor(Long fornecedor) {
		return contaRepositorio.getContaByFornecedor(fornecedor);
	}

	public Localidade getLocalidade(Long id) {
		return localRepositorio.getLocalPorId(id);
	}

	public List<LancamentoAcao> getLancamentosAcoes(Lancamento lancamento) {

		return repositorio.getLancamentosAcoes(lancamento);
	}

	public List<Estado> getEstados(Filtro filtro) {
		return estadoRepositorio.getEatados(filtro);
	}

	public List<Localidade> getMunicipioByEstado(Long id) {
		return localRepositorio.getMunicipioByEstado(id);
	}

	public SolicitacaoPagamento getPagamentoById(Long id) {
		return repositorio.getPagamentoPorId(id);
	}

	public List<Acao> acoesAutoComplete(String acao) {
		return acaoRepositorio.getAcaoAutoComplete(acao);
	}

	public List<FontePagadora> fontesAutoComplete(String fonte) {
		return fonteRespositorio.getFonteAutoComplete(fonte);
	}

	public SolicitacaoPagamentoService(LancamentoRepository repositorio) {
		this.repositorio = repositorio;
	}

	public Fornecedor getFornecedorById(Long id) {
		return fornecedorRepositorio.getFornecedorPorId(id);
	}

	public List<Fornecedor> getFornecedores(String s) {
		return Cotacaorepositorio.getFornecedores(s);
	}

	public List<ContaBancaria> getContasFornecedoresEAdiantamentos(String s) {
		return contaRepositorio.getContasFornecedoresEAdiantamentos(s);
	}

	public List<ContaBancaria> getContasFornecedoresAtivos(String s) {
		return contaRepositorio.getContasFornecedoresAtivos(s);
	}

	public List<ContaBancaria> getContasFornecedor(Fornecedor fornecedor) {
		try {
			return contaRepositorio.getContasFornecedor(fornecedor);
		} catch (NoResultException e) {
			return new ArrayList<ContaBancaria>();
		}

	}

	public List<SolicitacaoPagamento> getSolicitacaoPagamentos(Filtro filtro) {
		if (filtro.getDataInicio() != null && filtro.getDataFinal() != null) {
			if (filtro.getDataInicio().after(filtro.getDataFinal())) {
				addMessage("", "Data inicio maior que data final", FacesMessage.SEVERITY_ERROR);
				return new ArrayList<SolicitacaoPagamento>();
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

		return repositorio.getPagamentos(filtro);
	}

	public List<Localidade> buscaLocalidade(String s) {
		return localRepositorio.buscarLocalidade(s);
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	public void enviarEmailAutorizacaoSP(SolicitacaoPagamento pagamento, Integer type) {
		Email email = new Email();

		if (type == 0) {
			repositorio.mudarStatusPagamento(pagamento.getId(), StatusCompra.PENDENTE_APROVACAO);
		}

		prepararEmailAutorizacaoSP(email, pagamento);

		try {
			try {
				if (email.verificaInternet()) { // verificar internet
					email.enviarSolicitacaoPagamento(pagamento);
					// addMessage("", "Mensagem enviada com sucesso", FacesMessage.SEVERITY_INFO);
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

	public void prepararEmailAutorizacaoSP(Email email, SolicitacaoPagamento pagamento) {

		email.setFromEmail(getFromEmail());
		email.setSubject("AUTORIZACAO DE SP - " + pagamento.getId() + " Data:"
				+ new SimpleDateFormat("dd/MM/yyyy").format(pagamento.getDataEmissao()));
		email.setSolicitação(String.valueOf(pagamento.getId()));
		email.setSenhaEmail("FAS123fas");
		email.setToEmail(getToEmail(pagamento));
		email.setContent(GeradorConteudoHTML.emailAutorizacaoSP("SOLICITAÇÃO DE PAGAMENTO",
				"Solicito autorização para solicitação de pagamento em anexo.", pagamento));

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

	@Inject
	protected ConfiguracaoRepositorio configRepositorio;

	public String getFromEmail() {
//		return configRepositorio.getFromEmail().get(0).getEmail();
		return "comprasgi@fas-amazonas.org";
	}

	public UnidadeConservacao findByIdUnidadeConservacao(Long id) {
		return localRepositorio.findByIdUnidadeDeConservacao(id);
	}

	public void imprimir(Long id, CompraService compraService) {

		// List<ItemCompra> itens =
		// compraService.getItensTransientByCompra(compra);

		NumberFormat z = NumberFormat.getCurrencyInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		SolicitacaoPagamento pagamento = getPagamentoById(id);

		List<LancamentoAcao> acoes = getLancamentosAcoes(pagamento);

		StringBuilder acs = new StringBuilder();
		if (pagamento.getVersionLancamento() != null && pagamento.getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getProjetoRubrica().getComponente().getNome());
				acs.append("| ");
				acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				acs.append("| ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append("| ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				acs.append("| ");
				// Alteração para Adicionar valor do rateio no relatorio 03/09/2018 by
				// christophe
				acs.append("R$: " + lancamentoAcao.getValor());
				// end
				acs.append(" \n ");
			}
		} else {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getAcao().getCodigo() + " \n");
			}
		}

		// for (LancamentoAcao lancamentoAcao : acoes) {
		// acs.append(lancamentoAcao.getAcao().getCodigo() + ": " +
		// z.format(lancamentoAcao.getValor()) + " \n");
		// }

		// Fornecedor fornecedor =
		// pagamentoService.getFornecedorById(pagamento.getFornecedor().getId());

		ContaBancaria contaBanc = getContaById(pagamento.getContaRecebedor().getId());

		Fornecedor fornecedor = new Fornecedor();
		if (contaBanc.getFornecedor() != null) {
			fornecedor = getFornecedorById(contaBanc.getFornecedor().getId());
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("tipo_gestao", pagamento.getTipoGestao().getNome());
		parametros.put("gestao", pagamento.getGestao().getNome());
		parametros.put("tipo_localidade", pagamento.getTipoLocalidade().getNome());
		parametros.put("localidade", pagamento.getLocalidade().getNome());
		parametros.put("usuario", pagamento.getSolicitante().getNome() + " "
				+ new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", pagamento.getId().toString());
		parametros.put("observ", "");
		parametros.put("acao", acs.toString());

		parametros.put("fornecedor", contaBanc.getNomeConta() != null ? contaBanc.getNomeConta() : "");
		parametros.put("banco", contaBanc.getNomeBanco() != null ? contaBanc.getNomeBanco() : "");
		parametros.put("agencia", contaBanc.getNumeroAgencia() != null ? contaBanc.getNumeroAgencia() : "");
		parametros.put("conta", contaBanc.getNumeroConta() != null ? contaBanc.getNumeroConta() : "");
		parametros.put("cnpj_fornecedor", contaBanc.getCnpj() != null ? contaBanc.getCnpj() : "");
		parametros.put("cpf", contaBanc.getCpf() != null ? contaBanc.getCpf() : "");

		if (contaBanc.getFornecedor() != null) {
			parametros.put("contato", fornecedor.getContato() != null ? fornecedor.getContato() : "");
			parametros.put("fone", fornecedor.getTelefone() != null ? fornecedor.getTelefone() : "");
		} else {
			parametros.put("contato", "");
			parametros.put("fone", "");
		}

		parametros.put("data_pagamento", sdf.format(pagamento.getDataPagamento()));
		parametros.put("pis", contaBanc.getPIS());

		parametros.put("sp", pagamento.getId().toString());
		parametros.put("data_emissao", sdf.format(pagamento.getDataEmissao()));
		parametros.put("descricao", pagamento.getDescricao());

		parametros.put("data_solic", sdf.format(pagamento.getDataEmissao()));
		// parametros.put("data_aprovacao",
		// sdf.format(pagamento.getDataAprovacao()));
		parametros.put("data_sup", "");
		parametros.put("solicitante", pagamento.getSolicitante().getNome());

		parametros.put("usuario", pagamento.getSolicitante().getNome());
		parametros.put("nf", pagamento.getNotaFiscal());
		parametros.put("valor_total_c_desconto", z.format(pagamento.getValorTotalComDesconto()));
		parametros.put("data_vencto", sdf.format(pagamento.getDataPagamento()));
		parametros.put("modo_pagamento", pagamento.getCondicaoPagamentoEnum().getNome());

		Util util = new Util();

		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy hh:mm").format(pagamento.getDataEmissao()));

		if (pagamento.getTipoGestao().getNome().equals("Regional")) {
			// setarParamRegional(parametros);
			util.setarParamRegional(parametros, pagamento);
		}

		if (pagamento.getTipoGestao().getNome().equals("Coordenadoria")) {
			// setarParamCoordenadoria(parametros);
			util.setarParamCoordenadoria(parametros, pagamento);
		}

		if (pagamento.getTipoGestao().getNome().equals("Superintendencia")) {
			// setarParamSuperintendencia(parametros);
			util.setarParamSuperintendencia(parametros, pagamento);
		}

		if (pagamento.getTipoLocalidade().getNome().equals("Uc")) {
			// setarDestinoUC(parametros);
			util.setarDestinoUC(parametros, pagamento, compraService);
		} else if (pagamento.getTipoLocalidade().getNome().equals("Comunidade")) {
			// setarDestinoComunidade(parametros);
			util.setarDestinoComunidade(parametros, pagamento);
		} else {
			// setarDestino(parametros);
			util.setarDestino(parametros, pagamento);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList<>());
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/sp.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			ReportUtil.openReport("SC", "" + pagamento.getId().toString(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
