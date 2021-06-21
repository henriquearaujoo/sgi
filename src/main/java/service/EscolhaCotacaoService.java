package service;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.inject.Inject;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import model.Compra;
import model.Cotacao;
import model.Lancamento;
import model.LancamentoAcao;
import model.Pedido;
import repositorio.ConfiguracaoRepositorio;
import repositorio.CotacaoRepository;
import repositorio.LancamentoRepository;
import util.Email;

public class EscolhaCotacaoService implements Serializable {

	private static final long serialVersionUID = 1L;

	public EscolhaCotacaoService() {
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

	public void enviarEmailAutorizacaoPedido(Pedido pedido) throws IOException, AddressException, MessagingException {
		Email email = new Email();
		if (email.verificaInternet()) {
			setarParametrosEmail(email, pedido);
			email.EnviarEmailAprovacaoHTML(pedido.getCompra());
		} else {
			FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_WARN, "Atenção",
					"Problema de conexão com a internet. O pedido foi gerado no entanto o e-mail de aviso não foi enviado, tente novamente mais tarde!");
		}

	}

	public void setarParametrosEmail(Email email, Pedido pedido) {
		email.setFromEmail(getFromEmail());
		email.setSenhaEmail("FAS123fas");

		email.setSubject("SOLICITACAO DE APROVACAO DE PEDIDO - " + pedido.getId() + " Fornecedor: " + pedido.getFornecedor().getNomeFantasia()
				+ " Data:" + new SimpleDateFormat("dd/MM/yyyy").format(pedido.getDataEmissao()));
		email.setSolicitação(String.valueOf(pedido.getCompra().getId()));
		
		email.setToEmail(getToEmail(pedido));
		email.setContent(gerarConteudoHTML("SOLICITAÇÃO DE APROVAÇÃO DE MAPA COMPARATIVO.", "Mapa em anexo!", pedido));

	}
	
	public String gerarConteudoHTML(String titulo, String texto, Pedido pedido) {
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

		html.append("<b>Solicitação:</b>" + pedido.getCompra().getId() + "<br>"); // busca
																		// numero
																		// da
																		// solicitação

		html.append("<b>Solicitante:</b>" + pedido.getCompra().getSolicitante().getNome() + "<br>");

		html.append("<b>Tipo gestão:</b>" + pedido.getCompra().getTipoGestao().getNome() + "<br>");

		html.append("<b>Gestão:</b> " + pedido.getCompra().getGestao().getNome() + "<br><br>");

		// preparar o for para iterar sobre a quantidade de ações

		carregarLancamentos(pedido.getFornecedor().getId(), pedido.getCompra());

		// for (LancamentoAcao l : lancamentos) {
		// // html.append("<b>" + l.getFontePagadora().getNome() + "</b>" +
		// // l.getAcao().getCodigo() + "<br>");
		//
		// html.append(
		// "<b> Recurso: </b> " + l.getFontePagadora().getNome() + " - " +
		// l.getAcao().getCodigo() + "<br>");
		// }

		StringBuilder acs = new StringBuilder();

		if (pedido.getCompra().getVersionLancamento() != null && pedido.getCompra().getVersionLancamento().equals("MODE01")) {
			for (LancamentoAcao l : lancamentos) {

				acs = new StringBuilder();
				acs.append(l.getProjetoRubrica().getProjeto().getNome());
				acs.append("//");
				acs.append(l.getProjetoRubrica().getRubricaOrcamento().getOrcamento().getTitulo());
				acs.append("//");
				acs.append(l.getProjetoRubrica().getRubricaOrcamento().getRubrica().getNome());
				html.append("<b> Recurso: </b> " + acs.toString() + "<br>");
			}
		} else {
			for (LancamentoAcao l : lancamentos) {
				html.append("<b> Recurso: </b> " + l.getFontePagadora().getNome() + " - " + l.getAcao().getCodigo()
						+ "<br>");
			}
		}

		html.append("<br> <b>Fornecedor:</b>" + pedido.getFornecedor().getNomeFantasia() + "<br> <br>");
		html.append("<table id=\"t01\" style=\"width:100%\"> ");
		html.append(
				"<tr>  <th>Produto</th>  <th>Qtd</th>  <th>Valor unitário</th> <th>Valor unitário c/ desconto</th> <th>Total</th> </tr>");
		// preparar o for para iterar sobre os itens do pedido

		List<Cotacao> itens = getCotacoesHouveEscolha(pedido.getFornecedor().getId(), pedido.getCompra());
		DecimalFormat formatador = new DecimalFormat("#,##0.00");
		for (Cotacao cotacao : itens) {

			String valorDescontoS = formatador.format(cotacao.getValorDesconto()).toString();
			String valorTotalS = formatador
					.format(cotacao.getValorDesconto().multiply(new BigDecimal(cotacao.getQuantidade()))).toString();

			html.append("<tr>  <td>" + cotacao.getItemCompra().getProduto().getDescricao() + "</td> <td>"
					+ cotacao.getQuantidade() + "</td> <td>R$" + formatador.format(cotacao.getValor()).toString()
					+ "</td> <td>R$" + valorDescontoS + "</td> <td>R$" + valorTotalS + "</td> </tr>");
		}

		String valorTotalPedidoSemDescontoS = formatador.format(pedido.getValorTotalSemDesconto()).toString();
		String valorTotalDescontoS = formatador
				.format(pedido.getValorTotalSemDesconto().subtract(pedido.getValorTotalComDesconto())).toString();
		String valorTotalPedidoComDescontoS = formatador.format(pedido.getValorTotalComDesconto()).toString();

		html.append("<tr> <td colspan=\"5\"> <div align=\"center\"><b>Totalizadores</b></div> </td> </tr>");
		html.append("<tr> <td colspan=\"4\"> <label style=\"float:right\">Subtotal:</label></td> <td>R$"
				+ valorTotalPedidoSemDescontoS + "</td> </tr>");
		html.append("<tr> <td colspan=\"4\"> <label style=\"float:right\">Desconto:</label></td> <td>R$"
				+ valorTotalDescontoS + "</td> </tr> ");
		html.append("<tr> <td colspan=\"4\"> <label style=\"float:right\">Total:</label></td> <td>R$"
				+ valorTotalPedidoComDescontoS + "</td> </tr>");
		html.append("</table> </body> </html>");
		return html.toString();
	}
	
	private List<LancamentoAcao> lancamentos = new ArrayList<>();
	private BigDecimal valorTotalAliberar = BigDecimal.ZERO;
	private BigDecimal valorTotalLiberado = BigDecimal.ZERO;
	private BigDecimal valorTotalFaltaLiberar = BigDecimal.ZERO;
	@Inject
	private LancamentoRepository lancamentoRepositorio;
	
	private BigDecimal valorTotalPedidoSemDesconto;
	private BigDecimal valorTotalPedidoComDesconto;
	
	public void carregarLancamentos(Long fornecedor, Compra compra) {
		lancamentos = new ArrayList<>();
		lancamentos = getListaDeLancamentoPorFornecedorECompra(fornecedor, compra);
		valorTotalLiberado = BigDecimal.ZERO;
		if (lancamentos.size() > 0) {
			for (LancamentoAcao lanc : lancamentos) {
				valorTotalLiberado = valorTotalLiberado.add(lanc.getValor());
				valorTotalFaltaLiberar = valorTotalAliberar.subtract(valorTotalLiberado);
			}
		} else {
			this.valorTotalFaltaLiberar = valorTotalAliberar.subtract(valorTotalLiberado);
		}
	}
	
	@Inject
	private CotacaoRepository cotacaoRepositorio;
	
	public List<Cotacao> getCotacoesHouveEscolha(Long fornecedor, Compra compra) {
		return cotacaoRepositorio.getCotacoesHouveEscolha(fornecedor, compra);
	}
	
	
	public List<LancamentoAcao> getListaDeLancamentoPorFornecedorECompra(Long fornecedor, Compra compra) {
		return lancamentoRepositorio.getLancamentoPorFornecedorECompra(fornecedor, compra);
	}
	
	

}
