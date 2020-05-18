package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.Alignment;
import jxl.format.Colour;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import lombok.val;
import model.Acao;
import model.ContaBancaria;
import model.RelatorioContasAPagar;
import service.ContaService;

public class GeradorOrcamentariaByAcao {

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputArquivo;

	public void setOutputFile(String inputArquivo) {
		this.inputArquivo = inputArquivo;
	}

	public void insere(List<Acao> acoes, ContaService service, Filtro filtro) throws IOException, WriteException {
		// Cria um novo arquivo
		File arquivo = new File(inputArquivo);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("pt", "BR"));

		WritableWorkbook workbook = Workbook.createWorkbook(arquivo, wbSettings);
		// Define um nome para a planilha
		workbook.createSheet("Jexcel", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		criaTitulo(excelSheet, 0, "Orçado x Realizado");
		criaColunas(excelSheet, 3);

		int contLinha = 5;

		BigDecimal saldoDiarioTotal = BigDecimal.ZERO;
		BigDecimal saldoTotalPorConta = BigDecimal.ZERO;
		Double saldo = 0.0;

		String dataAnterior = "";
		String dataAtual = "";

		Double saldoOrcado = 0.0;
		Double saldoFinal = 0.0;
		Double totalSaidaProjeto = 0.0;
		Double totalEntradaProjeto = 0.0;
		Double totalAdiantamentoProjeto = 0.0;
		Double totalPrestacaoProjeto = 0.0;
		Double totalSaldoAdiantamentoProjeto = 0.0;

		Double totalSaida = 0.0;
		Double totalEntrada = 0.0;
		Double totalAdiantamento = 0.0;
		Double totalPrestacao = 0.0;
		Double totalSaldoAdiantamento = 0.0;

		Double entrada = 0.0;
		Double saida = 0.0;
		Double adiantamento = 0.0;
		Double prestacaoDeConta = 0.0;

		for (Acao acao : acoes) {
			criaLabelIndependente(excelSheet, contLinha, 0, acao.getCodigo());

			totalSaida = 0.0;
			totalEntrada = 0.0;
			totalAdiantamento = 0.0;
			totalPrestacao = 0.0;
			totalSaldoAdiantamento = 0.0;

			saldo = acao.getSaldo().doubleValue();

			saldoOrcado = saldoOrcado + saldo;

			addNumero(excelSheet, 16, contLinha, saldo);

			contLinha++;
			contLinha++;

			filtro.setAcaoId(acao.getId());

			int i = 0;

			for (RelatorioContasAPagar relatorio : service.getPagamentosByAcao(filtro)) {

				if (relatorio.getTipoLancamento().equals("dev") || relatorio.getTipoLancamento().equals("reenb"))
					continue;

				/*
				 * dataAtual = relatorio.getPagamento();
				 * 
				 * if (i == 0) { dataAnterior = dataAtual; i++; }
				 * 
				 * if (!dataAtual.equals(dataAnterior)) { contLinha++;
				 * criaLabelIndependente(excelSheet, contLinha, 10,
				 * "Totais no dia :" + dataAnterior); addNumero(excelSheet, 11,
				 * contLinha, totalEntrada); addNumero(excelSheet, 12,
				 * contLinha, totalSaida); addNumero(excelSheet, 13, contLinha,
				 * totalAdiantamento); addNumero(excelSheet, 14, contLinha,
				 * totalPrestacao); addNumero(excelSheet, 15, contLinha, saldo);
				 * 
				 * totalSaida = 0.0; totalEntrada = 0.0; totalAdiantamento =
				 * 0.0; totalPrestacao = 0.0; //addNumero(excelSheet, 15,
				 * contLinha, saldo); contLinha++; contLinha++;
				 * 
				 * dataAnterior = dataAtual; }
				 */

				addLabel(excelSheet, 0, contLinha, relatorio.getEmissao());
				addLabel(excelSheet, 1, contLinha, relatorio.getPagamento());
				addLabel(excelSheet, 2, contLinha, relatorio.getDoc());
				addLabel(excelSheet, 3, contLinha, relatorio.getNumeroLancamento());
				addLabel(excelSheet, 4, contLinha, relatorio.getPagador());
				addLabel(excelSheet, 5, contLinha, relatorio.getRecebedor());
				addLabel(excelSheet, 6, contLinha, relatorio.getDescricao());
				addLabel(excelSheet, 7, contLinha, relatorio.getProjeto());
				addLabel(excelSheet, 8, contLinha, relatorio.getFonte());
				addLabel(excelSheet, 9, contLinha, relatorio.getAcao());
				addLabel(excelSheet, 10, contLinha, relatorio.getRubrica());
				addLabel(excelSheet, 11, contLinha, relatorio.getStatus());
				
				
				String tp = "";
				if (relatorio.getTipoLancamento().equals("ad")) {
					tp =  "Adiantamento";
				}
				
				
				addLabel(excelSheet, 18, contLinha, tp);

				entrada = relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0);
				totalEntrada = totalEntrada + entrada;

				if (entrada != 0)
					addNumero(excelSheet, 12, contLinha, entrada);

				saida = relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0);
				totalSaida = totalSaida + saida;

				if (saida != 0)
					addNumero(excelSheet, 13, contLinha, saida);

				adiantamento = relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0);
				totalAdiantamento = totalAdiantamento + adiantamento;

				if (adiantamento != 0)
					addNumero(excelSheet, 14, contLinha, adiantamento);

				prestacaoDeConta = relatorio.getPrestacaoConta() != null ? relatorio.getPrestacaoConta()
						: new Double(0);
				totalPrestacao = totalPrestacao + prestacaoDeConta;

				if (prestacaoDeConta != 0)
					addNumero(excelSheet, 16, contLinha, prestacaoDeConta);

				if (relatorio.getTipoLancamento().equals("ad")) {
					filtro.setLancamentoID(new Long(relatorio.getNumeroLancamento()));
					filtro.setNumeroDocumento(relatorio.getDoc());
					Double ss = getSaldoAdiantamento(service.getPagamentosByDocumento(filtro), //TODO
							relatorio.getAdiantamento());

					if (ss < new Double(0)) {
						ss = (double) 0;
					}
					saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - ss;
				} else {
					saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - relatorio.getAdiantamento();
				}
				addNumero(excelSheet, 17, contLinha, saldo);

				contLinha++;

				if (relatorio.getTipoLancamento().equals("ad")) {

					filtro.setLancamentoID(new Long(relatorio.getNumeroLancamento()));
					filtro.setNumeroDocumento(relatorio.getDoc());

					List<RelatorioContasAPagar> auxList = service.getPagamentosByDocumento(filtro);

					Double saldoAdiantamento = getSaldoAdiantamento(auxList, relatorio.getAdiantamento());
					totalSaldoAdiantamento = totalSaldoAdiantamento + saldoAdiantamento;

					contLinha--;
					addNumero(excelSheet, 15, contLinha,
							saldoAdiantamento < new Double(0) ? new Double(0) : saldoAdiantamento);
					contLinha++;

					for (RelatorioContasAPagar rel : auxList) {
						if (rel.getTipoLancamento().equals("reenb")) {

							addLabel(excelSheet, 0, contLinha, rel.getEmissao());
							addLabel(excelSheet, 1, contLinha, rel.getPagamento());
							addLabel(excelSheet, 2, contLinha, rel.getDoc());
							addLabel(excelSheet, 3, contLinha, rel.getNumeroLancamento());
							addLabel(excelSheet, 4, contLinha, rel.getPagador());
							addLabel(excelSheet, 5, contLinha, rel.getRecebedor());
							addLabel(excelSheet, 6, contLinha, rel.getDescricao());
							addLabel(excelSheet, 7, contLinha, rel.getProjeto());
							addLabel(excelSheet, 8, contLinha, rel.getFonte());
							addLabel(excelSheet, 9, contLinha, rel.getAcao());
							addLabel(excelSheet, 11, contLinha, rel.getStatus());
							addLabel(excelSheet, 18, contLinha, "Reembolso");

							/*
							 * entrada = relatorio.getEntrada() != null ?
							 * relatorio.getEntrada() : new Double(0);
							 * totalEntrada = totalEntrada + entrada;
							 */
							// addNumero(excelSheet, 11, contLinha, new
							// Double(0));

							/*
							 * saida = relatorio.getSaida() != null ?
							 * relatorio.getSaida() : new Double(0); totalSaida
							 * = totalSaida + saida;
							 */
							// addNumero(excelSheet, 12, contLinha, new
							// Double(0));

							/*
							 * adiantamento = relatorio.getAdiantamento() !=
							 * null ? relatorio.getAdiantamento() : new
							 * Double(0); totalAdiantamento = totalAdiantamento
							 * + adiantamento;
							 */
							// addNumero(excelSheet, 13, contLinha, new
							// Double(0));

							prestacaoDeConta = rel.getSaida() != null ? rel.getSaida() : new Double(0);
							totalPrestacao = totalPrestacao + prestacaoDeConta;
							addNumero(excelSheet, 16, contLinha, prestacaoDeConta);

							// saldo = saldo + relatorio.getEntrada() -
							// relatorio.getSaida() -
							// relatorio.getAdiantamento();

							// addNumero(excelSheet, 15, contLinha, saldo);
							contLinha++;

							// relatorio.setAdiantamento(relatorio.getAdiantamento().doubleValue()
							// + rel.getSaida());

						} else if (rel.getTipoLancamento().equals("dev")) {

							addLabel(excelSheet, 0, contLinha, rel.getEmissao());
							addLabel(excelSheet, 1, contLinha, rel.getPagamento());
							addLabel(excelSheet, 2, contLinha, rel.getDoc());
							addLabel(excelSheet, 3, contLinha, rel.getNumeroLancamento());
							addLabel(excelSheet, 4, contLinha, rel.getPagador());
							addLabel(excelSheet, 5, contLinha, rel.getRecebedor());
							addLabel(excelSheet, 6, contLinha, rel.getDescricao());
							addLabel(excelSheet, 7, contLinha, rel.getProjeto());
							addLabel(excelSheet, 8, contLinha, rel.getFonte());
							addLabel(excelSheet, 9, contLinha, rel.getAcao());
							addLabel(excelSheet, 11, contLinha, rel.getStatus());
							addLabel(excelSheet, 18, contLinha, "Devolução");

							/*
							 * entrada = relatorio.getEntrada() != null ?
							 * relatorio.getEntrada() : new Double(0);
							 * totalEntrada = totalEntrada + entrada;
							 */
							// addNumero(excelSheet, 11, contLinha, new
							// Double(0));

							/*
							 * saida = relatorio.getSaida() != null ?
							 * relatorio.getSaida() : new Double(0); totalSaida
							 * = totalSaida + saida;
							 */
							// addNumero(excelSheet, 12, contLinha, new
							// Double(0));

							/*
							 * adiantamento = relatorio.getAdiantamento() !=
							 * null ? relatorio.getAdiantamento() : new
							 * Double(0); totalAdiantamento = totalAdiantamento
							 * + adiantamento;
							 */
							// addNumero(excelSheet, 13, contLinha, new
							// Double(0));

							prestacaoDeConta = rel.getEntrada() != null ? rel.getEntrada() : new Double(0);
							totalPrestacao = totalPrestacao + prestacaoDeConta;
							addNumero(excelSheet, 16, contLinha, prestacaoDeConta);

							// saldo = saldo + relatorio.getEntrada() -
							// relatorio.getSaida() -
							// relatorio.getAdiantamento();

							// addNumero(excelSheet, 15, contLinha, saldo);
							contLinha++;

							// relatorio.setAdiantamento(relatorio.getAdiantamento().doubleValue()
							// - rel.getSaida());
						} else {

							addLabel(excelSheet, 0, contLinha, rel.getEmissao());
							addLabel(excelSheet, 1, contLinha, rel.getPagamento());
							addLabel(excelSheet, 2, contLinha, rel.getDoc());
							addLabel(excelSheet, 3, contLinha, rel.getNumeroLancamento());
							addLabel(excelSheet, 4, contLinha, rel.getPagador());
							addLabel(excelSheet, 5, contLinha, rel.getRecebedor());
							addLabel(excelSheet, 6, contLinha, rel.getDescricao());
							addLabel(excelSheet, 7, contLinha, rel.getProjeto());
							addLabel(excelSheet, 8, contLinha, rel.getFonte());
							addLabel(excelSheet, 9, contLinha, rel.getAcao());
							addLabel(excelSheet, 11, contLinha, rel.getStatus());
							
						     tp = "";
							if (rel.getTipoLancamento().equals("ad")) {
								tp =  "Adiantamento";
							}
							
							
							addLabel(excelSheet, 18, contLinha, tp);
							/*
							 * entrada = relatorio.getEntrada() != null ?
							 * relatorio.getEntrada() : new Double(0);
							 * totalEntrada = totalEntrada + entrada;
							 */
							// addNumero(excelSheet, 11, contLinha, new
							// Double(0));

							/*
							 * saida = relatorio.getSaida() != null ?
							 * relatorio.getSaida() : new Double(0); totalSaida
							 * = totalSaida + saida;
							 */
							// addNumero(excelSheet, 12, contLinha, new
							// Double(0));

							/*
							 * adiantamento = relatorio.getAdiantamento() !=
							 * null ? relatorio.getAdiantamento() : new
							 * Double(0); totalAdiantamento = totalAdiantamento
							 * + adiantamento;
							 */
							// addNumero(excelSheet, 13, contLinha, new
							// Double(0));

							prestacaoDeConta = rel.getSaida() != null ? rel.getSaida() : new Double(0);
							totalPrestacao = totalPrestacao + prestacaoDeConta;
							addNumero(excelSheet, 16, contLinha, prestacaoDeConta);

							// saldo = saldo + relatorio.getEntrada() -
							// relatorio.getSaida() -
							// relatorio.getAdiantamento();

							// addNumero(excelSheet, 15, contLinha, saldo);
							contLinha++;

						}
					}
				}

			}

			contLinha++;
			/*
			 * criaLabelIndependente(excelSheet, contLinha, 14,
			 * "Totais no dia :" + dataAtual); addNumero(excelSheet, 15,
			 * contLinha, saldo);
			 */

			// criaLabelIndependente(excelSheet, contLinha, 10, "Totais no dia
			// :" + dataAnterior);

			criaLabelIndependente(excelSheet, contLinha, 11, "Totais:");
			addNumero(excelSheet, 14, contLinha, totalAdiantamento);
			addNumero(excelSheet, 16, contLinha, totalPrestacao);
			// contLinha++;
			addNumero(excelSheet, 15, contLinha, totalSaldoAdiantamento);
			addNumero(excelSheet, 12, contLinha, totalEntrada);
			addNumero(excelSheet, 13, contLinha, totalSaida);
			addNumero(excelSheet, 17, contLinha, saldo);
			
			

			contLinha++;
			contLinha++;

			// criaLabelIndependente(excelSheet, contLinha, 14, "Saldo no
			// período:");
			// addNumero(excelSheet, 15, contLinha, saldo);

			saldoFinal = saldoFinal + saldo;
			totalAdiantamentoProjeto = totalAdiantamentoProjeto + totalAdiantamento;
			totalSaldoAdiantamentoProjeto = totalSaldoAdiantamentoProjeto + totalSaldoAdiantamento;
			totalSaidaProjeto = totalSaidaProjeto + totalSaida;
			totalEntradaProjeto = totalEntradaProjeto + totalEntrada;
			totalPrestacaoProjeto = totalPrestacaoProjeto + totalPrestacao;

			contLinha++;
			contLinha++;

		}

		criaLabelIndependente(excelSheet, contLinha, 9, "Totais:");
		criaLabelIndependente(excelSheet, contLinha, 11, "Orçado");
		criaLabelIndependente(excelSheet, contLinha, 12, "Entrada");
		criaLabelIndependente(excelSheet, contLinha, 13, "Saída");
		criaLabelIndependente(excelSheet, contLinha, 14, "Adiantamento");
		criaLabelIndependente(excelSheet, contLinha, 16, "Herança/Prestação");
		criaLabelIndependente(excelSheet, contLinha, 17, "Saldo");

		contLinha++;

		addNumero(excelSheet, 11, contLinha, saldoOrcado);
		addNumero(excelSheet, 12, contLinha, totalEntradaProjeto);
		addNumero(excelSheet, 13, contLinha, totalSaidaProjeto);
		addNumero(excelSheet, 14, contLinha, totalAdiantamentoProjeto);
		addNumero(excelSheet, 15, contLinha, totalSaldoAdiantamentoProjeto);
		addNumero(excelSheet, 16, contLinha, totalPrestacaoProjeto);
		addNumero(excelSheet, 17, contLinha, saldoFinal);

		/*
		 * System.out.println(saldoOrcado); System.out.println(totalEntrada);
		 * System.out.println(totalSaida);
		 * System.out.println(totalAdiantamento);
		 * System.out.println(totalPrestacao); System.out.println(saldoFinal);
		 */

		workbook.write();
		workbook.close();

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		DownloadFileHandler dlwd = new DownloadFileHandler();

		dlwd.downloadFile("Análise orçamentária " + data + ".xls", arquivo, "application/vnd.ms-excel",
				FacesContext.getCurrentInstance());

		/*
		 * FacesContext fctx = FacesContext.getCurrentInstance();
		 * 
		 * FileInputStream fis = new FileInputStream(arquivo);
		 * HttpServletResponse resp = (HttpServletResponse)
		 * fctx.getExternalContext().getResponse();
		 * resp.setContentType("application/vnd.ms-excel");
		 * resp.setHeader("Content-Disposition",
		 * "atachment;filename=Relatótio de lançamentos.xls");
		 * ServletOutputStream out = resp.getOutputStream(); byte[] array =
		 * IOUtils.toByteArray(fis); resp.setContentLength(array.length);
		 * out.write(array, 0, array.length); out.flush(); out.close();
		 */
	}
	
	public void gerarConferenciaLancamentos(List<Acao> acoes, ContaService service, Filtro filtro) throws IOException, WriteException {
		// Cria um novo arquivo
		File arquivo = new File(inputArquivo);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("pt", "BR"));

		WritableWorkbook workbook = Workbook.createWorkbook(arquivo, wbSettings);
		// Define um nome para a planilha
		workbook.createSheet("Jexcel", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		criaTitulo(excelSheet, 0, "Conferência de lançamentos");
		criaColunas(excelSheet, 3);

		int contLinha = 5;

		BigDecimal saldoDiarioTotal = BigDecimal.ZERO;
		BigDecimal saldoTotalPorConta = BigDecimal.ZERO;
		Double saldo = 0.0;

		String dataAnterior = "";
		String dataAtual = "";

		Double saldoOrcado = 0.0;
		Double saldoFinal = 0.0;
		Double totalSaidaProjeto = 0.0;
		Double totalEntradaProjeto = 0.0;
		Double totalAdiantamentoProjeto = 0.0;
		Double totalPrestacaoProjeto = 0.0;
		Double totalSaldoAdiantamentoProjeto = 0.0;

		Double totalSaida = 0.0;
		Double totalEntrada = 0.0;
		Double totalAdiantamento = 0.0;
		Double totalPrestacao = 0.0;
		Double totalSaldoAdiantamento = 0.0;

		Double entrada = 0.0;
		Double saida = 0.0;
		Double adiantamento = 0.0;
		Double prestacaoDeConta = 0.0;

		for (Acao acao : acoes) {
//			criaLabelIndependente(excelSheet, contLinha, 0, acao.getCodigo());
//
//			totalSaida = 0.0;
//			totalEntrada = 0.0;
//			totalAdiantamento = 0.0;
//			totalPrestacao = 0.0;
//			totalSaldoAdiantamento = 0.0;
//
//			saldo = acao.getSaldo().doubleValue();
//
//			saldoOrcado = saldoOrcado + saldo;
//
//			addNumero(excelSheet, 16, contLinha, saldo);
//
//			contLinha++;
//			contLinha++;

			filtro.setAcaoId(acao.getId());

			int i = 0;

			for (RelatorioContasAPagar relatorio : service.getPagamentosByAcao(filtro)) {

				if (relatorio.getTipoLancamento().equals("dev") || relatorio.getTipoLancamento().equals("reenb"))
					continue;

				/*
				 * dataAtual = relatorio.getPagamento();
				 * 
				 * if (i == 0) { dataAnterior = dataAtual; i++; }
				 * 
				 * if (!dataAtual.equals(dataAnterior)) { contLinha++;
				 * criaLabelIndependente(excelSheet, contLinha, 10,
				 * "Totais no dia :" + dataAnterior); addNumero(excelSheet, 11,
				 * contLinha, totalEntrada); addNumero(excelSheet, 12,
				 * contLinha, totalSaida); addNumero(excelSheet, 13, contLinha,
				 * totalAdiantamento); addNumero(excelSheet, 14, contLinha,
				 * totalPrestacao); addNumero(excelSheet, 15, contLinha, saldo);
				 * 
				 * totalSaida = 0.0; totalEntrada = 0.0; totalAdiantamento =
				 * 0.0; totalPrestacao = 0.0; //addNumero(excelSheet, 15,
				 * contLinha, saldo); contLinha++; contLinha++;
				 * 
				 * dataAnterior = dataAtual; }
				 */

				addLabel(excelSheet, 0, contLinha, relatorio.getEmissao());
				addLabel(excelSheet, 1, contLinha, relatorio.getPagamento());
				addLabel(excelSheet, 2, contLinha, relatorio.getDoc());
				addLabel(excelSheet, 3, contLinha, relatorio.getNumeroLancamento());
				addLabel(excelSheet, 4, contLinha, relatorio.getPagador());
				addLabel(excelSheet, 5, contLinha, relatorio.getRecebedor());
				addLabel(excelSheet, 6, contLinha, relatorio.getDescricao());
				addLabel(excelSheet, 7, contLinha, relatorio.getProjeto());
				addLabel(excelSheet, 8, contLinha, relatorio.getFonte());
				addLabel(excelSheet, 9, contLinha, relatorio.getAcao());
				addLabel(excelSheet, 10, contLinha, relatorio.getRubrica());
				addLabel(excelSheet, 11, contLinha, relatorio.getStatus());
				addLabel(excelSheet, 19, contLinha, relatorio.getCategoriaDespesa());
				
				String tp = "";
				if (relatorio.getTipoLancamento().equals("ad")) {
					tp =  "Adiantamento";
				}
				
				
				addLabel(excelSheet, 18, contLinha, tp);

				entrada = relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0);
				totalEntrada = totalEntrada + entrada;

				if (entrada != 0)
					addNumero(excelSheet, 12, contLinha, entrada);

				saida = relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0);
				totalSaida = totalSaida + saida;

				if (saida != 0)
					addNumero(excelSheet, 13, contLinha, saida);

				adiantamento = relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0);
				totalAdiantamento = totalAdiantamento + adiantamento;

				if (adiantamento != 0)
					addNumero(excelSheet, 14, contLinha, adiantamento);

				prestacaoDeConta = relatorio.getPrestacaoConta() != null ? relatorio.getPrestacaoConta()
						: new Double(0);
				totalPrestacao = totalPrestacao + prestacaoDeConta;

				if (prestacaoDeConta != 0)
					addNumero(excelSheet, 16, contLinha, prestacaoDeConta);

				if (relatorio.getTipoLancamento().equals("ad")) {
					filtro.setLancamentoID(new Long(relatorio.getNumeroLancamento()));
					filtro.setNumeroDocumento(relatorio.getDoc());
					Double ss = getSaldoAdiantamento(service.getPagamentosByDocumento(filtro), //TODO
							relatorio.getAdiantamento());

					if (ss < new Double(0)) {
						ss = (double) 0;
					}
					saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - ss;
				} else {
					saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - relatorio.getAdiantamento();
				}
				addNumero(excelSheet, 17, contLinha, saldo);

				contLinha++;

				if (relatorio.getTipoLancamento().equals("ad")) {

					filtro.setLancamentoID(new Long(relatorio.getNumeroLancamento()));
					filtro.setNumeroDocumento(relatorio.getDoc());

					List<RelatorioContasAPagar> auxList = service.getPagamentosByDocumento(filtro);

					Double saldoAdiantamento = getSaldoAdiantamento(auxList, relatorio.getAdiantamento());
					totalSaldoAdiantamento = totalSaldoAdiantamento + saldoAdiantamento;

					contLinha--;
					addNumero(excelSheet, 15, contLinha,
							saldoAdiantamento < new Double(0) ? new Double(0) : saldoAdiantamento);
					contLinha++;

					for (RelatorioContasAPagar rel : auxList) {
						if (rel.getTipoLancamento().equals("reenb")) {

							addLabel(excelSheet, 0, contLinha, rel.getEmissao());
							addLabel(excelSheet, 1, contLinha, rel.getPagamento());
							addLabel(excelSheet, 2, contLinha, rel.getDoc());
							addLabel(excelSheet, 3, contLinha, rel.getNumeroLancamento());
							addLabel(excelSheet, 4, contLinha, rel.getPagador());
							addLabel(excelSheet, 5, contLinha, rel.getRecebedor());
							addLabel(excelSheet, 6, contLinha, rel.getDescricao());
							addLabel(excelSheet, 7, contLinha, rel.getProjeto());
							addLabel(excelSheet, 8, contLinha, rel.getFonte());
							addLabel(excelSheet, 9, contLinha, rel.getAcao());
							addLabel(excelSheet, 10, contLinha, rel.getRubrica());
							addLabel(excelSheet, 11, contLinha, rel.getStatus());
							addLabel(excelSheet, 18, contLinha, "Reembolso");
							addLabel(excelSheet, 19, contLinha, relatorio.getCategoriaDespesa());

							/*
							 * entrada = relatorio.getEntrada() != null ?
							 * relatorio.getEntrada() : new Double(0);
							 * totalEntrada = totalEntrada + entrada;
							 */
							// addNumero(excelSheet, 11, contLinha, new
							// Double(0));

							/*
							 * saida = relatorio.getSaida() != null ?
							 * relatorio.getSaida() : new Double(0); totalSaida
							 * = totalSaida + saida;
							 */
							// addNumero(excelSheet, 12, contLinha, new
							// Double(0));

							/*
							 * adiantamento = relatorio.getAdiantamento() !=
							 * null ? relatorio.getAdiantamento() : new
							 * Double(0); totalAdiantamento = totalAdiantamento
							 * + adiantamento;
							 */
							// addNumero(excelSheet, 13, contLinha, new
							// Double(0));

							prestacaoDeConta = rel.getSaida() != null ? rel.getSaida() : new Double(0);
							totalPrestacao = totalPrestacao + prestacaoDeConta;
							addNumero(excelSheet, 16, contLinha, prestacaoDeConta);

							// saldo = saldo + relatorio.getEntrada() -
							// relatorio.getSaida() -
							// relatorio.getAdiantamento();

							// addNumero(excelSheet, 15, contLinha, saldo);
							contLinha++;

							// relatorio.setAdiantamento(relatorio.getAdiantamento().doubleValue()
							// + rel.getSaida());

						} else if (rel.getTipoLancamento().equals("dev")) {

							addLabel(excelSheet, 0, contLinha, rel.getEmissao());
							addLabel(excelSheet, 1, contLinha, rel.getPagamento());
							addLabel(excelSheet, 2, contLinha, rel.getDoc());
							addLabel(excelSheet, 3, contLinha, rel.getNumeroLancamento());
							addLabel(excelSheet, 4, contLinha, rel.getPagador());
							addLabel(excelSheet, 5, contLinha, rel.getRecebedor());
							addLabel(excelSheet, 6, contLinha, rel.getDescricao());
							addLabel(excelSheet, 7, contLinha, rel.getProjeto());
							addLabel(excelSheet, 8, contLinha, rel.getFonte());
							addLabel(excelSheet, 9, contLinha, rel.getAcao());
							addLabel(excelSheet, 10, contLinha, rel.getRubrica());
							addLabel(excelSheet, 11, contLinha, rel.getStatus());
							addLabel(excelSheet, 18, contLinha, "Devolução");
							addLabel(excelSheet, 19, contLinha, relatorio.getCategoriaDespesa());

							/*
							 * entrada = relatorio.getEntrada() != null ?
							 * relatorio.getEntrada() : new Double(0);
							 * totalEntrada = totalEntrada + entrada;
							 */
							// addNumero(excelSheet, 11, contLinha, new
							// Double(0));

							/*
							 * saida = relatorio.getSaida() != null ?
							 * relatorio.getSaida() : new Double(0); totalSaida
							 * = totalSaida + saida;
							 */
							// addNumero(excelSheet, 12, contLinha, new
							// Double(0));

							/*
							 * adiantamento = relatorio.getAdiantamento() !=
							 * null ? relatorio.getAdiantamento() : new
							 * Double(0); totalAdiantamento = totalAdiantamento
							 * + adiantamento;
							 */
							// addNumero(excelSheet, 13, contLinha, new
							// Double(0));

							prestacaoDeConta = rel.getEntrada() != null ? rel.getEntrada() : new Double(0);
							totalPrestacao = totalPrestacao + prestacaoDeConta;
							addNumero(excelSheet, 16, contLinha, prestacaoDeConta);

							// saldo = saldo + relatorio.getEntrada() -
							// relatorio.getSaida() -
							// relatorio.getAdiantamento();

							// addNumero(excelSheet, 15, contLinha, saldo);
							contLinha++;

							// relatorio.setAdiantamento(relatorio.getAdiantamento().doubleValue()
							// - rel.getSaida());
						} else {

							addLabel(excelSheet, 0, contLinha, rel.getEmissao());
							addLabel(excelSheet, 1, contLinha, rel.getPagamento());
							addLabel(excelSheet, 2, contLinha, rel.getDoc());
							addLabel(excelSheet, 3, contLinha, rel.getNumeroLancamento());
							addLabel(excelSheet, 4, contLinha, rel.getPagador());
							addLabel(excelSheet, 5, contLinha, rel.getRecebedor());
							addLabel(excelSheet, 6, contLinha, rel.getDescricao());
							addLabel(excelSheet, 7, contLinha, rel.getProjeto());
							addLabel(excelSheet, 8, contLinha, rel.getFonte());
							addLabel(excelSheet, 9, contLinha, rel.getAcao());
							addLabel(excelSheet, 10, contLinha, rel.getRubrica());
							addLabel(excelSheet, 11, contLinha, rel.getStatus());
							addLabel(excelSheet, 19, contLinha, relatorio.getCategoriaDespesa());
							
						     tp = "";
							if (rel.getTipoLancamento().equals("ad")) {
								tp =  "Adiantamento";
							}
							
							
							addLabel(excelSheet, 18, contLinha, tp);
							/*
							 * entrada = relatorio.getEntrada() != null ?
							 * relatorio.getEntrada() : new Double(0);
							 * totalEntrada = totalEntrada + entrada;
							 */
							// addNumero(excelSheet, 11, contLinha, new
							// Double(0));

							/*
							 * saida = relatorio.getSaida() != null ?
							 * relatorio.getSaida() : new Double(0); totalSaida
							 * = totalSaida + saida;
							 */
							// addNumero(excelSheet, 12, contLinha, new
							// Double(0));

							/*
							 * adiantamento = relatorio.getAdiantamento() !=
							 * null ? relatorio.getAdiantamento() : new
							 * Double(0); totalAdiantamento = totalAdiantamento
							 * + adiantamento;
							 */
							// addNumero(excelSheet, 13, contLinha, new
							// Double(0));

							prestacaoDeConta = rel.getSaida() != null ? rel.getSaida() : new Double(0);
							totalPrestacao = totalPrestacao + prestacaoDeConta;
							addNumero(excelSheet, 16, contLinha, prestacaoDeConta);

							// saldo = saldo + relatorio.getEntrada() -
							// relatorio.getSaida() -
							// relatorio.getAdiantamento();

							// addNumero(excelSheet, 15, contLinha, saldo);
							contLinha++;

						}
					}
				}

			}

//			contLinha++;
//			/*
//			 * criaLabelIndependente(excelSheet, contLinha, 14,
//			 * "Totais no dia :" + dataAtual); addNumero(excelSheet, 15,
//			 * contLinha, saldo);
//			 */
//
//			// criaLabelIndependente(excelSheet, contLinha, 10, "Totais no dia
//			// :" + dataAnterior);
//
//			criaLabelIndependente(excelSheet, contLinha, 10, "Totais:");
//			addNumero(excelSheet, 13, contLinha, totalAdiantamento);
//			addNumero(excelSheet, 15, contLinha, totalPrestacao);
//			// contLinha++;
//			addNumero(excelSheet, 14, contLinha, totalSaldoAdiantamento);
//			addNumero(excelSheet, 11, contLinha, totalEntrada);
//			addNumero(excelSheet, 12, contLinha, totalSaida);
//			addNumero(excelSheet, 16, contLinha, saldo);
//			
//			
//
//			contLinha++;
//			contLinha++;
//
//			// criaLabelIndependente(excelSheet, contLinha, 14, "Saldo no
//			// período:");
//			// addNumero(excelSheet, 15, contLinha, saldo);
//
//			saldoFinal = saldoFinal + saldo;
//			totalAdiantamentoProjeto = totalAdiantamentoProjeto + totalAdiantamento;
//			totalSaldoAdiantamentoProjeto = totalSaldoAdiantamentoProjeto + totalSaldoAdiantamento;
//			totalSaidaProjeto = totalSaidaProjeto + totalSaida;
//			totalEntradaProjeto = totalEntradaProjeto + totalEntrada;
//			totalPrestacaoProjeto = totalPrestacaoProjeto + totalPrestacao;
//
//			contLinha++;
//			contLinha++;

		}

//		criaLabelIndependente(excelSheet, contLinha, 9, "Totais:");
//		criaLabelIndependente(excelSheet, contLinha, 10, "Orçado");
//		criaLabelIndependente(excelSheet, contLinha, 11, "Entrada");
//		criaLabelIndependente(excelSheet, contLinha, 12, "Saída");
//		criaLabelIndependente(excelSheet, contLinha, 13, "Adiantamento");
//		criaLabelIndependente(excelSheet, contLinha, 15, "Herança/Prestação");
//		criaLabelIndependente(excelSheet, contLinha, 16, "Saldo");
//
//		contLinha++;
//
//		addNumero(excelSheet, 10, contLinha, saldoOrcado);
//		addNumero(excelSheet, 11, contLinha, totalEntradaProjeto);
//		addNumero(excelSheet, 12, contLinha, totalSaidaProjeto);
//		addNumero(excelSheet, 13, contLinha, totalAdiantamentoProjeto);
//		addNumero(excelSheet, 14, contLinha, totalSaldoAdiantamentoProjeto);
//		addNumero(excelSheet, 15, contLinha, totalPrestacaoProjeto);
//		addNumero(excelSheet, 16, contLinha, saldoFinal);

		/*
		 * System.out.println(saldoOrcado); System.out.println(totalEntrada);
		 * System.out.println(totalSaida);
		 * System.out.println(totalAdiantamento);
		 * System.out.println(totalPrestacao); System.out.println(saldoFinal);
		 */

		workbook.write();
		workbook.close();

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		DownloadFileHandler dlwd = new DownloadFileHandler();

		dlwd.downloadFile("Conferência de lançamentos " + data + ".xls", arquivo, "application/vnd.ms-excel",
				FacesContext.getCurrentInstance());

		/*
		 * FacesContext fctx = FacesContext.getCurrentInstance();
		 * 
		 * FileInputStream fis = new FileInputStream(arquivo);
		 * HttpServletResponse resp = (HttpServletResponse)
		 * fctx.getExternalContext().getResponse();
		 * resp.setContentType("application/vnd.ms-excel");
		 * resp.setHeader("Content-Disposition",
		 * "atachment;filename=Relatótio de lançamentos.xls");
		 * ServletOutputStream out = resp.getOutputStream(); byte[] array =
		 * IOUtils.toByteArray(fis); resp.setContentLength(array.length);
		 * out.write(array, 0, array.length); out.flush(); out.close();
		 */
	}


	public Double getSaldoAdiantamento(List<RelatorioContasAPagar> relatorio, Double valor) {
		Double saldo = valor;

		for (RelatorioContasAPagar rel : relatorio) {
			if (rel.getTipoLancamento().equals("reenb") || rel.getTipoLancamento().equals("ad")) {
				valor = valor + rel.getSaida();
			} else if (rel.getTipoLancamento().equals("dev")) {
				valor = valor - rel.getEntrada();
			} else {
				valor = valor - rel.getSaida();
			}
		}

		return valor;
	}

	private void criaLabelIndependente(WritableSheet sheet, int linha, int coluna, String nome) throws WriteException {

		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);

		times = new WritableCellFormat(times10pt);
		times.setWrap(false);
		WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.ARIAL, 8, WritableFont.BOLD, false);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		timesBoldUnderline.setWrap(false);
		timesBoldUnderline.setAlignment(Alignment.RIGHT);
		if (nome.equals("Ação")) {
			timesBoldUnderline.setBackground(Colour.GREY_25_PERCENT);
		}
		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		addCaption(sheet, coluna, linha, nome);

	}

	private void criaColunas(WritableSheet sheet, int linha) throws WriteException {

		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);

		times = new WritableCellFormat(times10pt);
		times.setWrap(false);
		WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		timesBoldUnderline.setWrap(false);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		addCaption(sheet, 0, linha, "Emissão");
		addCaption(sheet, 1, linha, "Pagamento");
		addCaption(sheet, 2, linha, "Nº doc");
		addCaption(sheet, 3, linha, "Nº Lançamento");
		addCaption(sheet, 4, linha, "Pagador");
		addCaption(sheet, 5, linha, "Recebedor");
		addCaption(sheet, 6, linha, "Descrição");
		addCaption(sheet, 7, linha, "Projeto");
		addCaption(sheet, 8, linha, "Fonte");
		addCaption(sheet, 9, linha, "Ação");
		addCaption(sheet, 10, linha, "Rubrica");
		addCaption(sheet, 11, linha, "Status");
		addCaption(sheet, 12, linha, "Entrada"); // receita
		addCaption(sheet, 13, linha, "Saída"); // despesa
		addCaption(sheet, 14, linha, "Adiantamento"); // reembolso não deve
														// somar
		addCaption(sheet, 15, linha, "Saldo - Adiantamento");
		addCaption(sheet, 16, linha, "Prestação de conta");
		addCaption(sheet, 17, linha, "Saldo");
		addCaption(sheet, 18, linha, "Reemb/Dev/Adiantamento");
		addCaption(sheet, 19, linha, "Categoria");
		

	}

	private void criaTitulo(WritableSheet sheet, int linha, String titulo) throws WriteException {

		WritableFont times10pt = new WritableFont(WritableFont.TIMES, 10);

		times = new WritableCellFormat(times10pt);
		times.setWrap(false);
		WritableFont times10ptBoldUnderline = new WritableFont(WritableFont.ARIAL, 14, WritableFont.BOLD, false);
		timesBoldUnderline = new WritableCellFormat(times10ptBoldUnderline);
		timesBoldUnderline.setWrap(false);

		CellView cv = new CellView();
		cv.setFormat(times);
		cv.setFormat(timesBoldUnderline);
		cv.setAutosize(true);

		addCaption(sheet, 0, 0, titulo);

		Label label;
		label = new Label(0, linha, titulo, timesBoldUnderline);
		sheet.mergeCells(0, (short) 0, (short) 3, 0);
		sheet.addCell(label);

	}

	private void addCaption(WritableSheet planilha, int coluna, int linha, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(coluna, linha, s, timesBoldUnderline);
		planilha.addCell(label);
	}

	private void addNumero(WritableSheet planilha, int coluna, int linha, Double d)
			throws WriteException, RowsExceededException {
		jxl.write.Number numero;
		numero = new jxl.write.Number(coluna, linha, d, times);
		planilha.addCell(numero);
	}

	private void addLabel(WritableSheet planilha, int coluna, int linha, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(coluna, linha, s, times);
		planilha.addCell(label);
	}

}
