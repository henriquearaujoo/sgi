package util;

import java.text.SimpleDateFormat;

import model.Compra;
import model.SolicitacaoPagamento;

public class GeradorConteudoHTML {
	
	
	
	
	 public static String emailAutorizacaoSA(String titulo, String texto, SolicitacaoPagamento adiantamento) {

		
			StringBuilder html = new StringBuilder("<html> <head> <style>");
			html.append("table {width:100%;} table, th, td { border: 1px solid black; border-collapse: collapse;}");
			html.append("th, td { padding: 5px; text-align: left;} table#t01 tr:nth-child(even) {");
			html.append("background-color: #eee;} table#t01 tr:nth-child(odd) { background-color:#fff;}");
			html.append("table#t01 th { background-color: #849A39; color: white; }");
			html.append("</style>");
			html.append("</head>");

			html.append("<body>");

			if (adiantamento.getBootUrgencia() != null && adiantamento.getBootUrgencia()) {
				html.append("<h3 align=\"center\" style=\"color:red\" > " + titulo + " URGENTE</h3>");
			} else {
				html.append("<h3 align=\"center\" >" + titulo + "</h3>");
			}

			if (adiantamento.getBootUrgencia() != null && adiantamento.getBootUrgencia()) {
				html.append(
						" <b>Justificativa:</b> <p style=\"color:red\"> " + adiantamento.getJustificativa() + " </p> <br><br>");
			}

			html.append("<b>Solicitação:</b> " + adiantamento.getId() + "<br>");

			html.append("<b>Solicitante:</b> " + adiantamento.getSolicitante().getNome() + "<br>");

			html.append("<b>Data:</b> " + new SimpleDateFormat("dd/MM/yyyy").format(adiantamento.getDataEmissao()) + "<br>");

			html.append("<b>Tipo gestão:</b> " + adiantamento.getTipoGestao().getNome() + "<br>");

			html.append("<b>Gestão:</b> " + adiantamento.getGestao().getNome() + "<br><br>");

			html.append("</body> </html>");

			html.append("<p>");
			html.append(texto);
			html.append("</p>");

			return html.toString();

	 
	 }
	 
	 public static String emailAutorizacaoSP(String titulo, String texto, SolicitacaoPagamento pagamento) {
		 

			StringBuilder html = new StringBuilder("<html> <head> <style>");
			html.append("table {width:100%;} table, th, td { border: 1px solid black; border-collapse: collapse;}");
			html.append("th, td { padding: 5px; text-align: left;} table#t01 tr:nth-child(even) {");
			html.append("background-color: #eee;} table#t01 tr:nth-child(odd) { background-color:#fff;}");
			html.append("table#t01 th { background-color: #849A39; color: white; }");
			html.append("</style>");
			html.append("</head>");

			html.append("<body>");

			if (pagamento.getBootUrgencia() != null && pagamento.getBootUrgencia()) {
				html.append("<h3 align=\"center\" style=\"color:red\" > " + titulo + " URGENTE</h3>");
			} else {
				html.append("<h3 align=\"center\" >" + titulo + "</h3>");
			}

			if (pagamento.getBootUrgencia() != null && pagamento.getBootUrgencia()) {
				html.append(
						" <b>Justificativa:</b> <p style=\"color:red\"> " + pagamento.getJustificativa() + " </p> <br><br>");
			}

			html.append("<b>Solicitação:</b> " + pagamento.getId() + "<br>");

			html.append("<b>Solicitante:</b> " + pagamento.getSolicitante().getNome() + "<br>");

			html.append("<b>Data:</b> " + new SimpleDateFormat("dd/MM/yyyy").format(pagamento.getDataEmissao()) + "<br>");

			html.append("<b>Tipo gestão:</b> " + pagamento.getTipoGestao().getNome() + "<br>");

			html.append("<b>Gestão:</b> " + pagamento.getGestao().getNome() + "<br><br>");

			html.append("</body> </html>");

			html.append("<p>");
			html.append(texto);
			html.append("</p>");

			return html.toString();
 
	 }

}
