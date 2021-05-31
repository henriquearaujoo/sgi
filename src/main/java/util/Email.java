package util;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;

import model.Compra;
import model.CotacaoAuxiliar;
import model.EspelhoPedido;
import model.ItemCompra;
import model.Lancamento;
import model.LancamentoAcao;
import model.Observacao;
import model.SolicitacaoPagamento;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import service.CompraService;
import service.CotacaoService;

public class Email {

	private String toEmail;
	private String fromEmail;
	private String solicitação;
	private String subject;
	private String cc;
	private String content;

	private String senhaEmail;

	private Message message; // = new MimeMessage(criarSessionMail());
	private Multipart partes = new MimeMultipart();

	private List<CotacaoAuxiliar> cotacoesAuxiliares = new ArrayList<>();

	private CotacaoService cotacaoService = CDILocator.getBean(CotacaoService.class);
	private CompraService compraService = CDILocator.getBean(CompraService.class);

	public Email() {
	}

	public Email(String toEmail, String fromEmail, String solicitação, String subject, String cc, String content) {
		this.toEmail = toEmail;
		this.fromEmail = fromEmail;
		this.solicitação = solicitação;
		this.subject = subject;
		this.cc = cc;
		this.content = content;
	}

	public Email(String toEmail, String fromEmail, String solicitação, String subject) {
		this.toEmail = toEmail;
		this.fromEmail = fromEmail;
		this.solicitação = solicitação;
		this.subject = subject;
	}

	public void sendEmailAprovacao() throws EmailException {

		StringBuilder content = new StringBuilder(
				"As cotações foram concluída, estamos aguardando suas escolhas.\n\n  Solicitação nº:" + solicitação
						+ "\n\n");
		content.append(this.content);

		SimpleEmail email = new SimpleEmail();
		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(587);
		email.addTo(toEmail);
		email.setFrom(fromEmail);
		email.setSubject(subject);
		email.setMsg(content.toString());
		email.setSSLOnConnect(true);
		email.setAuthentication("it4lo.almeida@gmail.com", "s3nh40utr4s3nh4");
		email.send();

	}

	public void EnviarEmailSimples() throws AddressException, MessagingException {
		criarMessagem();
		message.setSentDate(new Date());
		message.setContent(partes);
		Transport.send(message);
	}

	public void EnviarEmailSimplesHTML() throws AddressException, MessagingException {
		criarMessagemHTML();
		message.setSentDate(new Date());
		message.setContent(partes);
		Transport.send(message);
	}

	public void EnviarEmailAprovacaoHTML(Compra compra) throws AddressException, MessagingException {
		criarMessagemHTML();
		// message.setSentDate(new Date());
		// message.setContent(partes);
		// Transport.send(message);

		MimeBodyPart part2 = new MimeBodyPart();
		part2.setDataHandler(
				new DataHandler(new ByteArrayDataSource(criarCotacaoParaAnexo(compra), "application/pdf")));
		part2.setFileName("Cotação.pdf");
		partes.addBodyPart(part2);
		message.setContent(partes);
		message.setSentDate(new Date());
		Transport.send(message);
	}

	public void EnviarEmailPreCadastroProduto() throws AddressException, MessagingException {
		criarMessagemHTML();
		message.setContent(partes);
		message.setSentDate(new Date());
		Transport.send(message);
	}

	public void EnviarEmailHTML() throws AddressException, MessagingException {
		criarMessagemHTML();
		message.setContent(partes);
		message.setSentDate(new Date());
		Transport.send(message);
	}

	private Session criarSessionMail(String email, String senha) {
		
		Properties props = new Properties();
		
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", 465);
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", true);
		props.put("mail.smtp.port", 465);
		//props.put("mail.smtp.starttls.enable", "true");

		Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {

			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(email, senha);
			}
		});

		session.setDebug(true);

		return session;
	}

	// TODO: Envio do mapa comparativo em anexo nas partes do e-mail

	public void EnviarMapaComparativo(Compra compra, List<CotacaoAuxiliar> cotacaoSelected) throws EmailException {

		try {

			criarMessagem();
			MimeBodyPart part2 = new MimeBodyPart();
			part2.setDataHandler(new DataHandler(
					new ByteArrayDataSource(criarCotacaoParaAnexo(compra, cotacaoSelected), "application/pdf")));
			part2.setFileName("Cotação.pdf");
			partes.addBodyPart(part2);
			message.setContent(partes);
			message.setSentDate(new Date());
			Transport.send(message);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	// TODO: Envio de SC primeiro estágio.

	public void EnviarSolicitacaoCompra(Compra compra) throws EmailException {
		try {

			criarMessagemHTML();
			MimeBodyPart part2 = new MimeBodyPart();
			part2.setDataHandler(
					new DataHandler(new ByteArrayDataSource(criarSCparaAnexo(compra, ""), "application/pdf")));
			part2.setFileName("SC" + compra.getId() + ".pdf");
			partes.addBodyPart(part2);
			message.setContent(partes);
			message.setSentDate(new Date());
		
			Transport.send(message);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	
	public void EnviarEmailDeRecusa(Lancamento lancamento) throws EmailException {
		try {

			criarMessagemHTML();
//			MimeBodyPart part2 = new MimeBodyPart();
//			part2.setDataHandler(
//					new DataHandler(new ByteArrayDataSource(criarSCparaAnexo(lancamento, ""), "application/pdf")));
//			part2.setFileName("SC" + lancamento.getId() + ".pdf");
//			partes.addBodyPart(part2);
			message.setContent(partes);
			message.setSentDate(new Date());
		
			Transport.send(message);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public void enviarSolicitacaoAdiantamento(SolicitacaoPagamento pagamento) throws EmailException {
		
		try {
			criarMessagemHTML();
			MimeBodyPart part2 = new MimeBodyPart();
			part2.setDataHandler(
					new DataHandler(new ByteArrayDataSource(DocumentUtil.criarSAparaAnexo(pagamento, ""), "application/pdf")));
			part2.setFileName("SC" + pagamento.getId() + ".pdf");
			partes.addBodyPart(part2);
			message.setContent(partes);
			message.setSentDate(new Date());
		
			Transport.send(message);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		
	}
	
	public void enviarSolicitacaoPagamento(SolicitacaoPagamento pagamento) throws EmailException {
		try {

			criarMessagemHTML();
			MimeBodyPart part2 = new MimeBodyPart();
			part2.setDataHandler(
					new DataHandler(new ByteArrayDataSource(DocumentUtil.criarSPparaAnexo(pagamento, ""), "application/pdf")));
			part2.setFileName("SP" + pagamento.getId() + ".pdf");
			partes.addBodyPart(part2);
			message.setContent(partes);
			message.setSentDate(new Date());
		
			Transport.send(message);

		} catch (AddressException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	public void criarMessagem() throws AddressException, MessagingException {
		message = new MimeMessage(criarSessionMail(fromEmail, senhaEmail));
		message.setFrom(new InternetAddress(fromEmail));
		Address[] to = InternetAddress.parse(toEmail);
		message.setRecipients(Message.RecipientType.TO, to);
		message.setSubject(subject);

		MimeBodyPart part1 = new MimeBodyPart();
		part1.setText(content);
		partes.addBodyPart(part1);
	}

	public void criarMessagemHTML() throws AddressException, MessagingException {
		message = new MimeMessage(criarSessionMail(fromEmail, senhaEmail));
		message.setFrom(new InternetAddress(fromEmail));
		Address[] to = InternetAddress.parse(toEmail);
		message.setRecipients(Message.RecipientType.TO, to);
		message.setSubject(subject);

		MimeBodyPart part1 = new MimeBodyPart();
		// part1.setText(content);
		part1.setContent(content, "text/html; charset=UTF-8");
		partes.addBodyPart(part1);
	}

	public void sendEmailAprovacaoDePedido() throws EmailException {

		StringBuilder content = new StringBuilder(
				"\n\nPedidos gerados aguardando aprovação para solicitação nº :" + solicitação + "\n\n ");
		content.append(this.content);

		SimpleEmail email = new SimpleEmail();

		email.setHostName("smtp.gmail.com");
		email.setSmtpPort(587);
		email.addTo(toEmail);
		email.setFrom(fromEmail);
		email.setSubject(subject);
		email.setMsg(content.toString());
		email.setSSLOnConnect(true);
		email.setAuthentication("italo.almeida@fas-amazonas.org", "s3nh@fas");
		email.send();

	}

	public void instanciarServicoCompra() {
		compraService = CDILocator.getBean(CompraService.class);
	}

	public void instanciarServicoCotacao() {
		cotacaoService = CDILocator.getBean(CotacaoService.class);
	}

	public byte[] criarSCparaAnexo(Compra compra, String usuario) {
		instanciarServicoCompra();
		List<ItemCompra> itens = compraService.getItensTransientByCompra(compra);
		List<LancamentoAcao> acoes = compraService.getLancamentosAcoes(compra);

		StringBuilder acs = new StringBuilder();
		
		if (compra.getVersionLancamento() != null && compra.getVersionLancamento().equals("MODE01")) {
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
		parametros.put("usuario", usuario + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", compra.getId().toString());
		parametros.put("observ", "");
		parametros.put("acao", acs.toString());

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
			util.setarDestinoUC(parametros, compra, compraService);
		} else if (compra.getTipoLocalidade().getNome().equals("Comunidade")) {
			// setarDestinoComunidade(parametros);
			util.setarDestinoComunidade(parametros, compra);
		} else {
			// setarDestino(parametros);
			util.setarDestino(parametros, compra);
		}

		JRDataSource dataSource = new JRBeanCollectionDataSource(itens);
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/scnew.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			return ReportUtil.openReportParaAnexo("SC", "" + compra.getCodigo(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public Boolean verificaInternet() throws IOException {
		InetAddress endereco = InetAddress.getByName("www.google.com");

		if (endereco.isReachable(30000)) {
			return true;
		}

		return false;
	}

	public byte[] criarCotacaoParaAnexo(Compra compra, List<CotacaoAuxiliar> cotacaoSelected) {

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

		List<Observacao> listObs = cotacaoService.getObservacoes(compra);
		StringBuilder notaTecnica = new StringBuilder();

		for (Observacao observacao : listObs) {
			notaTecnica.append(observacao.getTexto() + "\n");
		}

		StringBuilder resumo = new StringBuilder();

		for (CotacaoAuxiliar cot : cotacaoSelected) {
			resumo.append(cot.getFornecedor() + ": " + getTotais(cot.getTotalDesconto()) + "\n");
		}

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

		// for (LancamentoAcao lancamentoAcao : acoes) {
		// acs.append(lancamentoAcao.getAcao().getCodigo()+" \n");
		// }

		for (EspelhoPedido espelho : espelhos) {

			String linha = "Fornecedor: " + espelho.getFornecedor() + " valor: " + getTotais(espelho.getTotalValor());

		}

		int cont = 0;

		for (CotacaoAuxiliar cotAuxiliar : cotacaoSelected) {

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

		parametros.put("data", new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()));

		parametros.put("logo", logo);
		parametros.put("acao", acs.toString());
		parametros.put("usuario",
				compra.getSolicitante().getNome() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", compra.getCodigo());

		parametros.put("resumo", resumo.toString());

		JRDataSource dataSource = new JRBeanCollectionDataSource(itens);
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/Mapa.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			return ReportUtil.openReportParaAnexo("MC", "Mapa " + compra.getCodigo(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public byte[] criarCotacaoParaAnexo(Compra compra) {

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
		cotacoesAuxiliares = cotacaoService.getCotacaoAuxiliar(compra.getId());
		List<EspelhoPedido> espelhos = cotacaoService.getEspelhoPedido(compra);
		List<LancamentoAcao> acoes = cotacaoService.getLancamentosAcoes(compra);

		List<Observacao> listObs = cotacaoService.getObservacoes(compra);
		StringBuilder notaTecnica = new StringBuilder();

		for (Observacao observacao : listObs) {
			notaTecnica.append(observacao.getTexto() + "\n");
		}

		StringBuilder resumo = new StringBuilder();

		for (CotacaoAuxiliar cot : cotacoesAuxiliares) {
			resumo.append(cot.getFornecedor() + ": " + getTotais(cot.getTotalDesconto()) + "\n");
		}

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
		
//		for (LancamentoAcao lancamentoAcao : acoes) {
//			acs.append(lancamentoAcao.getAcao().getCodigo() + " \n");
//		}

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

		parametros.put("data", new SimpleDateFormat("dd/MM/yyyy").format(compra.getDataEmissao()));

		parametros.put("logo", logo);
		parametros.put("acao", acs.toString());
		parametros.put("usuario",
				compra.getSolicitante().getNome() + " " + new SimpleDateFormat("dd/MM/yyyy hh:mm").format(new Date()));
		parametros.put("solicitacao", compra.getId().toString());

		parametros.put("resumo", resumo.toString());

		JRDataSource dataSource = new JRBeanCollectionDataSource(itens);
		;

		try {
			JasperDesign jd = JRXmlLoader.load(path + "resources/relatorio/Mapa.jrxml");
			JasperReport report = JasperCompileManager.compileReport(jd);
			return ReportUtil.openReportParaAnexo("MC", "Mapa " + compra.getCodigo(), report, parametros, dataSource);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}

	}

	public String getTotais(BigDecimal total) {
		NumberFormat format = NumberFormat.getCurrencyInstance();
		return format.format(total);
		// return new DecimalFormat("###,###.###").format(total);
	}

	public String getToEmail() {
		return toEmail;
	}

	public void setToEmail(String toEmail) {
		this.toEmail = toEmail;
	}

	public String getFromEmail() {
		return fromEmail;
	}

	public void setFromEmail(String fromEmail) {
		this.fromEmail = fromEmail;
	}

	public String getSolicitação() {
		return solicitação;
	}

	public void setSolicitação(String solicitação) {
		this.solicitação = solicitação;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getCc() {
		return cc;
	}

	public void setCc(String cc) {
		this.cc = cc;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Message getMessage() {
		return message;
	}

	public void setMessage(Message message) {
		this.message = message;
	}

	public Multipart getPartes() {
		return partes;
	}

	public void setPartes(Multipart partes) {
		this.partes = partes;
	}

	public String getSenhaEmail() {
		return senhaEmail;
	}

	public void setSenhaEmail(String senhaEmail) {
		this.senhaEmail = senhaEmail;
	}

}
