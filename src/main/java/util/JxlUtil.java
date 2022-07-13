package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Paths;
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
import model.ContaBancaria;
import model.RelatorioContasAPagar;
import service.ContaService;

public class JxlUtil {

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private String inputArquivo;

	public void setOutputFile(String inputArquivo) {
		this.inputArquivo = inputArquivo;
	}
	
	

	public void insere(List<ContaBancaria> selectedContas, ContaService service, Filtro filtro)
			throws IOException, WriteException {

		// Cria um novo arquivo
		File arquivo = new File(inputArquivo);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("pt", "BR"));

		WritableWorkbook workbook = Workbook.createWorkbook(arquivo, wbSettings);
		// Define um nome para a planilha
		workbook.createSheet("Jexcel", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		criaTitulo(excelSheet, 0, "Contas a pagar");
		criaColunas(excelSheet, 3);

		int contLinha = 5;

		BigDecimal saldoDiarioTotal = BigDecimal.ZERO;
		BigDecimal saldoTotalPorConta = BigDecimal.ZERO;
		Double saldo = 0.0;
		
		
		Double  entrada = new Double(0);
		Double saida = new Double(0);
		Double prestacao = new Double(0);
		Double adiantamento = new Double(0);
		
		
		Double totalEntrada = new Double(0);
		Double totalSaida = new Double(0);
		Double totalPrestacao = new Double(0);
		Double totalAdiantamento = new Double(0);
		
		String dataAnterior = "";
		String dataAtual = "";

		for (ContaBancaria conta : selectedContas) {
			criaLabelIndependente(excelSheet, contLinha, 0, conta.getNomeConta());
			
			totalEntrada = new Double(0);
			totalSaida = new Double(0);
			totalPrestacao = new Double(0);
			totalAdiantamento = new Double(0);
			
			if(!conta.getTipo().equals("CA")){
			saldo = conta.getSaldoInicial().doubleValue() - conta.getTotalSaida().doubleValue()
					+ conta.getTotalEntrada().doubleValue();

			}else{
				filtro.setIdConta(conta.getId());
				ContaBancaria contaBancaria = service.getContaSaldoCA(filtro);
				saldo = contaBancaria.getSaldoInicial().doubleValue() - contaBancaria.getTotalSaida().doubleValue()
						+ contaBancaria.getTotalEntrada().doubleValue();
			}
			
			addNumero(excelSheet, 17, contLinha, saldo);

			contLinha++;
			contLinha++;

			filtro.setIdConta(conta.getId());

			int i = 0;

			for (RelatorioContasAPagar relatorio : conta.getTipo().equals("CA") ? service.getPagamentosByContaCA(filtro)
					: service.getPagamentosByConta(filtro)) {

				dataAtual = relatorio.getPagamento();

				if (i == 0) {
					dataAnterior = dataAtual;
					i++;
				}

				if (!dataAtual.equals(dataAnterior)) {
					contLinha++;
					criaLabelIndependente(excelSheet, contLinha, 12, "Totais no dia :" + dataAnterior);
					addNumero(excelSheet, 13, contLinha, totalEntrada);
					addNumero(excelSheet, 14, contLinha, totalSaida);
					addNumero(excelSheet, 15, contLinha, totalAdiantamento);
					addNumero(excelSheet, 16, contLinha, totalPrestacao);
					addNumero(excelSheet, 17, contLinha, saldo);
					
					totalEntrada = new Double(0);
					totalSaida = new Double(0);
					totalPrestacao = new Double(0);
					totalAdiantamento = new Double(0);
					
					contLinha++;
					contLinha++;
					
					dataAnterior = dataAtual;
				}

				addLabel(excelSheet, 0, contLinha, relatorio.getPagamento());
				addLabel(excelSheet, 1, contLinha, relatorio.getEmissao());
				addLabel(excelSheet, 2, contLinha, relatorio.getDoc());
				
				addLabel(excelSheet, 3, contLinha, relatorio.getNotaFiscal());
				addLabel(excelSheet, 4, contLinha, relatorio.getCategoriaDespesa());
				
				
				addLabel(excelSheet, 5, contLinha, relatorio.getNumeroLancamento());
				addLabel(excelSheet, 6, contLinha, relatorio.getPagador());
				addLabel(excelSheet, 7, contLinha, relatorio.getRecebedor());
				addLabel(excelSheet, 8, contLinha, relatorio.getDescricao());
				addLabel(excelSheet, 9, contLinha, relatorio.getProjeto());
				addLabel(excelSheet, 10, contLinha, relatorio.getFonte());
				addLabel(excelSheet, 11, contLinha, relatorio.getAcao());
				addLabel(excelSheet, 12, contLinha, relatorio.getStatus());
				
				
				entrada = relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0);
				totalEntrada = totalEntrada + entrada;
				addNumero(excelSheet, 13, contLinha,
						relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0));
				
				saida =  relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0);
				totalSaida = totalSaida + saida;
				addNumero(excelSheet, 14, contLinha,
						relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0));
				
				adiantamento =  relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0);
				totalAdiantamento = adiantamento;
				addNumero(excelSheet, 15, contLinha,
						relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0));
				
				prestacao = relatorio.getPrestacaoConta() != null ? relatorio.getPrestacaoConta() : new Double(0);
				addNumero(excelSheet, 16, contLinha,
						relatorio.getPrestacaoConta() != null ? relatorio.getPrestacaoConta() : new Double(0));

				saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - relatorio.getAdiantamento();

				addNumero(excelSheet, 17, contLinha, saldo);
				contLinha++;
			}

			contLinha++;
			/*criaLabelIndependente(excelSheet, contLinha, 14, "Totais no dia :" + dataAtual);
			addNumero(excelSheet, 15, contLinha, saldo);*/
			
			criaLabelIndependente(excelSheet, contLinha, 12, "Totais no dia :" + dataAnterior);
			addNumero(excelSheet, 13, contLinha, totalEntrada);
			addNumero(excelSheet, 14, contLinha, totalSaida);
			addNumero(excelSheet, 15, contLinha, totalAdiantamento);
			addNumero(excelSheet, 16, contLinha, totalPrestacao);
			addNumero(excelSheet, 17, contLinha, saldo);

			contLinha++;
			contLinha++;

			criaLabelIndependente(excelSheet, contLinha, 14, "Saldo no período:");
			addNumero(excelSheet, 17, contLinha, saldo);

			contLinha++;
			contLinha++;

		}

		workbook.write();
		workbook.close();

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		DownloadFileHandler dlwd = new DownloadFileHandler();

		dlwd.downloadFile("Relatório CP " + data + ".xls", arquivo, "application/vnd.ms-excel",
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
	
	
	public void insereMODE01(List<ContaBancaria> selectedContas, ContaService service, Filtro filtro)
			throws IOException, WriteException {
		// Cria um novo arquivo
		File arquivo = new File(inputArquivo);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("pt", "BR"));

		WritableWorkbook workbook = Workbook.createWorkbook(arquivo, wbSettings);
		// Define um nome para a planilha
		workbook.createSheet("Jexcel", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		criaTitulo(excelSheet, 0, "Contas a pagar");
		criaColunas(excelSheet, 3);

		int contLinha = 5;

		BigDecimal saldoDiarioTotal = BigDecimal.ZERO;
		BigDecimal saldoTotalPorConta = BigDecimal.ZERO;
		Double saldo = 0.0;
		
		
		Double  entrada = new Double(0);
		Double saida = new Double(0);
		Double prestacao = new Double(0);
		Double adiantamento = new Double(0);
		
		
		Double totalEntrada = new Double(0);
		Double totalSaida = new Double(0);
		Double totalPrestacao = new Double(0);
		Double totalAdiantamento = new Double(0);
		
		String dataAnterior = "";
		String dataAtual = "";

		for (ContaBancaria conta : selectedContas) {
			criaLabelIndependente(excelSheet, contLinha, 0, conta.getNomeConta());
			
			totalEntrada = new Double(0);
			totalSaida = new Double(0);
			totalPrestacao = new Double(0);
			totalAdiantamento = new Double(0);
			
			if(!conta.getTipo().equals("CA")){
			saldo = conta.getSaldoInicial().doubleValue() - conta.getTotalSaida().doubleValue()
					+ conta.getTotalEntrada().doubleValue();

			}else{
				filtro.setIdConta(conta.getId());
				ContaBancaria contaBancaria = service.getContaSaldoCA(filtro);
				saldo = contaBancaria.getSaldoInicial().doubleValue() - contaBancaria.getTotalSaida().doubleValue()
						+ contaBancaria.getTotalEntrada().doubleValue();
			}
			
			addNumero(excelSheet, 17, contLinha, saldo);

			contLinha++;
			contLinha++;

			filtro.setIdConta(conta.getId());

			int i = 0;

			for (RelatorioContasAPagar relatorio : conta.getTipo().equals("CA") ? service.getPagamentosByContaCAMODE01(filtro)
					: service.getPagamentosByContaMODE01(filtro)) {

				dataAtual = relatorio.getPagamento();

				if (i == 0) {
					dataAnterior = dataAtual;
					i++;
				}

				if (!dataAtual.equals(dataAnterior)) {
					contLinha++;
					criaLabelIndependente(excelSheet, contLinha, 12, "Totais no dia :" + dataAnterior);
					addNumero(excelSheet, 13, contLinha, totalEntrada);
					addNumero(excelSheet, 14, contLinha, totalSaida);
					addNumero(excelSheet, 15, contLinha, totalAdiantamento);
					addNumero(excelSheet, 16, contLinha, totalPrestacao);
					addNumero(excelSheet, 17, contLinha, saldo);
					
					totalEntrada = new Double(0);
					totalSaida = new Double(0);
					totalPrestacao = new Double(0);
					totalAdiantamento = new Double(0);
					
					contLinha++;
					contLinha++;
					
					dataAnterior = dataAtual;
				}

				addLabel(excelSheet, 0, contLinha, relatorio.getPagamento());
				addLabel(excelSheet, 1, contLinha, relatorio.getEmissao());
				addLabel(excelSheet, 2, contLinha, relatorio.getDoc());
				
				addLabel(excelSheet, 3, contLinha, relatorio.getNotaFiscal());
				addLabel(excelSheet, 4, contLinha, relatorio.getCategoriaDespesa());
				
				
				addLabel(excelSheet, 5, contLinha, relatorio.getNumeroLancamento());
				addLabel(excelSheet, 6, contLinha, relatorio.getPagador());
				addLabel(excelSheet, 7, contLinha, relatorio.getRecebedor());
				addLabel(excelSheet, 8, contLinha, relatorio.getDescricao());
				addLabel(excelSheet, 9, contLinha, relatorio.getProjeto());
				addLabel(excelSheet, 10, contLinha, relatorio.getFonte());
				addLabel(excelSheet, 11, contLinha, relatorio.getAcao());
				addLabel(excelSheet, 12, contLinha, relatorio.getStatus());
				
				
				entrada = relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0);
				totalEntrada = totalEntrada + entrada;
				addNumero(excelSheet, 13, contLinha,
						relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0));
				
				saida =  relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0);
				totalSaida = totalSaida + saida;
				addNumero(excelSheet, 14, contLinha,
						relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0));
				
				adiantamento =  relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0);
				totalAdiantamento = adiantamento;
				addNumero(excelSheet, 15, contLinha,
						relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0));
				
				prestacao = relatorio.getPrestacaoConta() != null ? relatorio.getPrestacaoConta() : new Double(0);
				addNumero(excelSheet, 16, contLinha,
						relatorio.getPrestacaoConta() != null ? relatorio.getPrestacaoConta() : new Double(0));

				saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - relatorio.getAdiantamento();

				addNumero(excelSheet, 17, contLinha, saldo);
				contLinha++;
			}

			contLinha++;
			/*criaLabelIndependente(excelSheet, contLinha, 14, "Totais no dia :" + dataAtual);
			addNumero(excelSheet, 15, contLinha, saldo);*/
			
			criaLabelIndependente(excelSheet, contLinha, 12, "Totais no dia :" + dataAnterior);
			addNumero(excelSheet, 13, contLinha, totalEntrada);
			addNumero(excelSheet, 14, contLinha, totalSaida);
			addNumero(excelSheet, 15, contLinha, totalAdiantamento);
			addNumero(excelSheet, 16, contLinha, totalPrestacao);
			addNumero(excelSheet, 17, contLinha, saldo);

			contLinha++;
			contLinha++;

			criaLabelIndependente(excelSheet, contLinha, 14, "Saldo no período:");
			addNumero(excelSheet, 17, contLinha, saldo);

			contLinha++;
			contLinha++;

		}

		workbook.write();
		workbook.close();

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		DownloadFileHandler dlwd = new DownloadFileHandler();

		dlwd.downloadFile("Relatório CP " + data + ".xls", arquivo, "application/vnd.ms-excel",
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
	
	
	public void insereTESTE(List<ContaBancaria> selectedContas, ContaService service, Filtro filtro)
			throws IOException, WriteException {
		// Cria um novo arquivo
		
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		File arquivo = new File(inputArquivo);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("pt", "BR"));

		WritableWorkbook workbook = Workbook.createWorkbook(arquivo, wbSettings);
		// Define um nome para a planilha
		workbook.createSheet("Jexcel", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		criaTitulo(excelSheet, 0, "Execução "+sdf.format(filtro.getDataInicio())+" a "+sdf.format(filtro.getDataFinal()));
		criaColunasExecuçãoFinanceira(excelSheet, 3);

		int contLinha = 5;

		BigDecimal saldoDiarioTotal = BigDecimal.ZERO;
		BigDecimal saldoTotalPorConta = BigDecimal.ZERO;
		Double saldo = 0.0;

		String dataAnterior = "";
		String dataAtual = "";

		for (ContaBancaria conta : selectedContas) {
			/*criaLabelIndependente(excelSheet, contLinha, 0, conta.getNomeConta());
			
			
			if(!conta.getTipo().equals("CA")){
			saldo = conta.getSaldoInicial().doubleValue() - conta.getTotalSaida().doubleValue()
					+ conta.getTotalEntrada().doubleValue();

			}else{
				filtro.setIdConta(conta.getId());
				ContaBancaria contaBancaria = service.getContaSaldoCA(filtro);
				saldo = contaBancaria.getSaldoInicial().doubleValue() - contaBancaria.getTotalSaida().doubleValue()
						+ contaBancaria.getTotalEntrada().doubleValue();
			}
			
			addNumero(excelSheet, 15, contLinha, saldo);

			contLinha++;
			contLinha++;*/

			filtro.setIdConta(conta.getId());

			int i = 0;

			for (RelatorioContasAPagar relatorio : conta.getTipo().equals("CA") ? service.getPagamentosByContaCAParaOutrosFiltros(filtro)
					: service.getPagamentosByContaParaOutrosFiltros(filtro)) {

				/*dataAtual = relatorio.getPagamento();

				if (i == 0) {
					dataAnterior = dataAtual;
					i++;
				}

				if (!dataAtual.equals(dataAnterior)) {
					contLinha++;
					criaLabelIndependente(excelSheet, contLinha, 13, "Totais no dia :" + dataAnterior);
					addNumero(excelSheet, 14, contLinha, saldo);
					contLinha++;
					contLinha++;
					
					dataAnterior = dataAtual;
				}
*/
				addLabel(excelSheet, 0, contLinha, relatorio.getPagamento());
				addLabel(excelSheet, 1, contLinha, relatorio.getEmissao());
				addLabel(excelSheet, 2, contLinha, relatorio.getDoc());
				
				addLabel(excelSheet, 3, contLinha, relatorio.getNotaFiscal());
				addLabel(excelSheet, 4, contLinha, relatorio.getCategoriaDespesa());
				
				addLabel(excelSheet, 5, contLinha, relatorio.getNumeroLancamento());
				addLabel(excelSheet, 6, contLinha, relatorio.getPagador());
				addLabel(excelSheet, 7, contLinha, relatorio.getRecebedor());
				addLabel(excelSheet, 8, contLinha, relatorio.getDescricao());
				addLabel(excelSheet, 9, contLinha, relatorio.getProjeto());
				addLabel(excelSheet, 10, contLinha, relatorio.getFonte());
				addLabel(excelSheet, 11, contLinha, relatorio.getAcao());
				addLabel(excelSheet, 12, contLinha, relatorio.getStatus());
				addNumero(excelSheet, 13, contLinha,
						relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0));
				addNumero(excelSheet, 14, contLinha,
						relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0));
				addNumero(excelSheet, 15, contLinha,
						relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0));
				addNumero(excelSheet, 16, contLinha,
						relatorio.getPrestacaoConta() != null ? relatorio.getPrestacaoConta() : new Double(0));

				saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - relatorio.getAdiantamento();

				addNumero(excelSheet, 17, contLinha, saldo);
				contLinha++;
			}

			/*contLinha++;
			criaLabelIndependente(excelSheet, contLinha, 14, "Totais no dia :" + dataAtual);
			addNumero(excelSheet, 15, contLinha, saldo);

			contLinha++;
			contLinha++;

			criaLabelIndependente(excelSheet, contLinha, 14, "Saldo no período:");
			addNumero(excelSheet, 15, contLinha, saldo);

			contLinha++;
			contLinha++;
*/
		}

		workbook.write();
		workbook.close();

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		DownloadFileHandler dlwd = new DownloadFileHandler();

		dlwd.downloadFile("Relatório CP " + data + ".xls", arquivo, "application/vnd.ms-excel",
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

		addCaption(sheet, 0, linha, "Pagamento");
		addCaption(sheet, 1, linha, "Emissão");
		addCaption(sheet, 2, linha, "Nº doc");
		
		addCaption(sheet, 3, linha, "Nota fiscal");
		addCaption(sheet, 4, linha, "Categoria");
		
		addCaption(sheet, 5, linha, "Nº Lançamento");
		addCaption(sheet, 6, linha, "Pagador");
		addCaption(sheet, 7, linha, "Recebedor");
		addCaption(sheet, 8, linha, "Descrição");
		addCaption(sheet, 9, linha, "Projeto");
		addCaption(sheet, 10, linha, "Fonte");
		addCaption(sheet, 11, linha, "Ação");
		addCaption(sheet, 12, linha, "Status");
		addCaption(sheet, 13, linha, "Entrada"); // receita
		addCaption(sheet, 14, linha, "Saída"); // despesa
		addCaption(sheet, 15, linha, "Adiantamentos"); // despesa
		addCaption(sheet, 16, linha, "Prestações de conta"); // despesa mas não
																// baixa na
																// conta e sim
																// no
																// adiantamento
		addCaption(sheet, 17, linha, "Saldo");

	}
	
	
	private void criaColunasExecuçãoFinanceira(WritableSheet sheet, int linha) throws WriteException {

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

		addCaption(sheet, 0, linha, "Pagamento");
		addCaption(sheet, 1, linha, "Emissão");
		addCaption(sheet, 2, linha, "Nº doc");
		addCaption(sheet, 3, linha, "Nota fiscal");
		addCaption(sheet, 4, linha, "Categoria");
		addCaption(sheet, 5, linha, "Nº Lançamento");
		addCaption(sheet, 6, linha, "Pagador");
		addCaption(sheet, 7, linha, "Recebedor");
		addCaption(sheet, 8, linha, "Descrição");
		addCaption(sheet, 9, linha, "Projeto");
		addCaption(sheet, 10, linha, "Fonte");
		addCaption(sheet, 11, linha, "Ação");
		addCaption(sheet, 12, linha, "Status");
		addCaption(sheet, 13, linha, "Entrada"); // receita
		addCaption(sheet, 14, linha, "Saída"); // despesa
		addCaption(sheet, 15, linha, "Adiantamentos"); // despesa
		addCaption(sheet, 16, linha, "Prestações de conta"); // despesa mas não
																// baixa na
																// conta e sim
																// no
																// adiantamento
		addCaption(sheet, 17, linha, "Saldo");

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
