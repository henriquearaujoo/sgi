package util;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;

import model.ContaBancaria;
import model.Fornecedor;
import model.LancamentoAcao;
import model.SolicitacaoPagamento;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import service.AdiantamentoService;
import service.SolicitacaoPagamentoService;

public class DocumentUtil {
	
	private static SolicitacaoPagamentoService pagamentoService;
	private static SolicitacaoPagamentoService compraService;
	private static AdiantamentoService adiantamentoService;
	
	public static void instanciarPagamentoService() {
		pagamentoService = CDILocator.getBean(SolicitacaoPagamentoService.class);
	}
	
	
	public static void instanciarAdiantamentoService() {
		adiantamentoService = CDILocator.getBean(AdiantamentoService.class);
	}
	
	public static void instanciarCompraService() {
		compraService = CDILocator.getBean(SolicitacaoPagamentoService.class);
	}

	
	
	public static byte[] criarSAparaAnexo(SolicitacaoPagamento adiantamento, String usuario) {
	
		instanciarAdiantamentoService();
		
		NumberFormat z = NumberFormat.getCurrencyInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		adiantamento = adiantamentoService.getPagamentoById(adiantamento.getId());

		List<LancamentoAcao> acoes = adiantamentoService.getLancamentosAcoes(adiantamento);

		StringBuilder acs = new StringBuilder();
		if (adiantamento.getVersionLancamento() != null && adiantamento.getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getProjetoRubrica().getComponente().getNome());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getProjeto().getNome());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append(" | ");
				acs.append(lancamentoAcao.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				acs.append(" | ");
				acs.append("R$: " + lancamentoAcao.getValor());
				acs.append("\n");
			}
		} else {
			for (LancamentoAcao lancamentoAcao : acoes) {
				acs.append(lancamentoAcao.getAcao().getCodigo() + " \n");
			}
		}

		ContaBancaria contaBanc = adiantamentoService.getContaById(adiantamento.getContaRecebedor().getId());

		Fornecedor fornecedor = new Fornecedor();
		if (contaBanc.getFornecedor() != null) {
			fornecedor = adiantamentoService.getFornecedorById(contaBanc.getFornecedor().getId());
		}

		ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
		HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext()
				.getRequest();
		String path = request.getSession().getServletContext().getRealPath("/");
		String logo = path + "resources/image/logoFas.gif";

		Map parametros = new HashMap();
		parametros.put("logo", logo);
		parametros.put("tipo_gestao", adiantamento.getTipoGestao().getNome());
		parametros.put("gestao", adiantamento.getGestao().getNome());
		parametros.put("tipo_localidade", adiantamento.getTipoLocalidade().getNome());
		parametros.put("localidade", adiantamento.getLocalidade().getNome());
		parametros.put("usuario", " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", adiantamento.getId().toString());
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

		parametros.put("data_pagamento", sdf.format(adiantamento.getDataPagamento()));
		parametros.put("pis", contaBanc.getPIS());

		parametros.put("sp", adiantamento.getId().toString());
		parametros.put("data_emissao", sdf.format(adiantamento.getDataEmissao()));
		parametros.put("descricao", adiantamento.getDescricao());

		parametros.put("data_solic", sdf.format(adiantamento.getDataEmissao()));
		// parametros.put("data_aprovacao",
		// sdf.format(pagamento.getDataAprovacao()));
		parametros.put("data_sup", "");
		parametros.put("solicitante", adiantamento.getSolicitante().getNome());

		parametros.put("usuario", adiantamento.getSolicitante().getNome());
		parametros.put("nf", adiantamento.getNotaFiscal());
		parametros.put("valor_total_c_desconto", z.format(adiantamento.getValorTotalComDesconto()));
		parametros.put("data_vencto", sdf.format(adiantamento.getDataPagamento()));
		parametros.put("modo_pagamento", adiantamento.getCondicaoPagamentoEnum().getNome());

		Util util = new Util();

		parametros.put("data_emissao", new SimpleDateFormat("dd/MM/yyyy hh:mm").format(adiantamento.getDataEmissao()));

		if (adiantamento.getTipoGestao().getNome().equals("Regional")) {
			// setarParamRegional(parametros);
			util.setarParamRegional(parametros, adiantamento);
		}

		if (adiantamento.getTipoGestao().getNome().equals("Coordenadoria")) {
			// setarParamCoordenadoria(parametros);
			util.setarParamCoordenadoria(parametros, adiantamento);
		}

		if (adiantamento.getTipoGestao().getNome().equals("Superintendencia")) {
			// setarParamSuperintendencia(parametros);
			util.setarParamSuperintendencia(parametros, adiantamento);
		}

		if (adiantamento.getTipoLocalidade().getNome().equals("Uc")) {
			// setarDestinoUC(parametros);
			util.setarDestinoUC(parametros, adiantamento, pagamentoService);
		} else if (adiantamento.getTipoLocalidade().getNome().equals("Comunidade")) {
			// setarDestinoComunidade(parametros);
			util.setarDestinoComunidade(parametros, adiantamento);
		} else {
			// setarDestino(parametros);
			util.setarDestino(parametros, adiantamento);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(new ArrayList<>());
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/sa.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			return ReportUtil.openReportParaAnexo("AD", "" + adiantamento.getId().toString(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	
		
	}
	
	
	public static byte[] criarSPparaAnexo(SolicitacaoPagamento pagamento, String usuario) {
		
		instanciarPagamentoService();
		NumberFormat z = NumberFormat.getCurrencyInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		pagamento = pagamentoService.getPagamentoById(pagamento.getId());

		List<LancamentoAcao> acoes = pagamentoService.getLancamentosAcoes(pagamento);

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

		ContaBancaria contaBanc = pagamentoService.getContaById(pagamento.getContaRecebedor().getId());

		Fornecedor fornecedor = new Fornecedor();
		if (contaBanc.getFornecedor() != null) {
			fornecedor = pagamentoService.getFornecedorById(contaBanc.getFornecedor().getId());
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
		parametros.put("usuario",
				"" + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
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
			util.setarDestinoUC(parametros, pagamento, pagamentoService);
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
			return ReportUtil.openReportParaAnexo("SP", "" + pagamento.getId().toString(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		return null;

	

	}
	
	
	

}
