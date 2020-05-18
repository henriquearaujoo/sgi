package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.write.Label;
import jxl.write.NumberFormat;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;
import model.LancamentoAuxiliar;
import model.Orcamento;
import model.Projeto;
import model.RelatorioFinanceiro;
import service.ContaService;
import service.OrcamentoService;
import service.ProjetoService;

public class GeradorRelatorioUtilXLS implements Serializable {

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private WritableCellFormat numberFormat;
	private String inputArquivo;
	private @Inject OrcamentoService orcService;

	public void setOutputFile(String inputArquivo) {
		this.inputArquivo = inputArquivo;
	}

	public void gerarConferenciaDeLancamentos(Filtro filtro, ContaService service) throws IOException, WriteException {

		File arquivo = new File(inputArquivo);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("pt", "BR"));
		WritableWorkbook workbook = Workbook.createWorkbook(arquivo, wbSettings);
		workbook.createSheet("Jexcel", 0);

		WritableSheet excelSheet = workbook.getSheet(0);
		criaTitulo(excelSheet, 0, "Conferência de lançamentos");
		criaColunas(excelSheet, 3);

		int contLinha = 4;

		List<RelatorioFinanceiro> relatorios = service.buscarPagamentos(filtro);

		for (RelatorioFinanceiro relatorio : relatorios) {

			addLabel(excelSheet, 0, contLinha, relatorio.getPagamento());
			addLabel(excelSheet, 1, contLinha, relatorio.getEmissao());
			addLabel(excelSheet, 2, contLinha, relatorio.getDoc());
			addLabel(excelSheet, 3, contLinha, relatorio.getNumeroLancamento());

			addLabel(excelSheet, 4, contLinha, relatorio.getNotaFiscal());
			addLabel(excelSheet, 5, contLinha, relatorio.getCategoriaDespesa());
			addLabel(excelSheet, 6, contLinha, relatorio.getRubrica());
			addLabel(excelSheet, 7, contLinha, relatorio.getPagador());
			addLabel(excelSheet, 8, contLinha, relatorio.getRecebedor());
			addLabel(excelSheet, 9, contLinha, relatorio.getDescricao());
			addLabel(excelSheet, 10, contLinha, relatorio.getProjeto());
			addLabel(excelSheet, 11, contLinha, relatorio.getFonte());
			addLabel(excelSheet, 12, contLinha, relatorio.getAcao());
			addLabel(excelSheet, 13, contLinha, relatorio.getStatus());
			addNumero(excelSheet, 14, contLinha,
					relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0));
			addNumero(excelSheet, 15, contLinha, relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0));
			addNumero(excelSheet, 16, contLinha,
					relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0));

			contLinha++;

		}

		workbook.write();
		workbook.close();

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		DownloadFileHandler dlwd = new DownloadFileHandler();

		dlwd.downloadFile("Conferência de lançamentos" + data + ".xls", arquivo, "application/vnd.ms-excel",
				FacesContext.getCurrentInstance());

	}

	public void gerarContasApagar(Filtro filtro, ContaService service) throws IOException, WriteException {

		File arquivo = new File(inputArquivo);
		WorkbookSettings wbSettings = new WorkbookSettings();
		wbSettings.setLocale(new Locale("pt", "BR"));
		WritableWorkbook workbook = Workbook.createWorkbook(arquivo, wbSettings);
		workbook.createSheet("Jexcel", 0);

		WritableSheet excelSheet = workbook.getSheet(0);
		criaTitulo(excelSheet, 0, "Conferência de lançamentos");
		criaColunas(excelSheet, 3);

		int contLinha = 4;

		List<RelatorioFinanceiro> relatorios = service.buscarPagamentos(filtro);

		for (RelatorioFinanceiro relatorio : relatorios) {

			addLabel(excelSheet, 0, contLinha, relatorio.getPagamento());
			addLabel(excelSheet, 1, contLinha, relatorio.getEmissao());
			addLabel(excelSheet, 2, contLinha, relatorio.getDoc());
			addLabel(excelSheet, 3, contLinha, relatorio.getNumeroLancamento());

			addLabel(excelSheet, 4, contLinha, relatorio.getNotaFiscal());
			addLabel(excelSheet, 5, contLinha, relatorio.getCategoriaDespesa());
			addLabel(excelSheet, 6, contLinha, relatorio.getRubrica());
			addLabel(excelSheet, 7, contLinha, relatorio.getPagador());
			addLabel(excelSheet, 8, contLinha, relatorio.getRecebedor());
			addLabel(excelSheet, 9, contLinha, relatorio.getDescricao());
			addLabel(excelSheet, 10, contLinha, relatorio.getProjeto());
			addLabel(excelSheet, 11, contLinha, relatorio.getFonte());
			addLabel(excelSheet, 12, contLinha, relatorio.getAcao());
			addLabel(excelSheet, 13, contLinha, relatorio.getStatus());
			addNumero(excelSheet, 14, contLinha,
					relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0));
			addNumero(excelSheet, 15, contLinha, relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0));
			addNumero(excelSheet, 16, contLinha,
					relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0));

			contLinha++;

		}

		workbook.write();
		workbook.close();

		String data = new SimpleDateFormat("dd/MM/yyyy").format(new Date());

		DownloadFileHandler dlwd = new DownloadFileHandler();

		dlwd.downloadFile("Conferência de lançamentos" + data + ".xls", arquivo, "application/vnd.ms-excel",
				FacesContext.getCurrentInstance());

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
		addCaption(sheet, 2, linha, "Nº documento");
		addCaption(sheet, 3, linha, "Nº Lançamento");
		addCaption(sheet, 4, linha, "Nota fiscal");
		addCaption(sheet, 5, linha, "Categoria");
		addCaption(sheet, 6, linha, "Rubrica");
		addCaption(sheet, 7, linha, "Pagador");
		addCaption(sheet, 8, linha, "Recebedor");
		addCaption(sheet, 9, linha, "Descrição");
		addCaption(sheet, 10, linha, "Projeto");
		addCaption(sheet, 11, linha, "Fonte");
		addCaption(sheet, 12, linha, "Ação");
		addCaption(sheet, 13, linha, "Status");
		addCaption(sheet, 14, linha, "Entrada"); // receita
		addCaption(sheet, 15, linha, "Saída"); // despesa
		addCaption(sheet, 16, linha, "Adiantamentos"); // despesa
		addCaption(sheet, 17, linha, "Prestações de conta"); // despesa mas não
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

		if (d == 0.0) {
			return;
		}

		NumberFormat decimalNo = new NumberFormat("R$ #,#.00");
		WritableCellFormat numberFormat = new WritableCellFormat(decimalNo);
		numero = new jxl.write.Number(coluna, linha, d, numberFormat);
		planilha.addCell(numero);
	}

	private void addLabel(WritableSheet planilha, int coluna, int linha, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(coluna, linha, s, times);
		planilha.addCell(label);
	}

	// ********************************* API MAIS INTUITIVA DO EXCEL

	private HSSFWorkbook workbook;
	private CellStyle styleNumber; // = workbook.createCellStyle();
	private CellStyle styleHeader;
	private CellStyle styleTituloGeral;
	private DataFormat format;// = workbook.createDataFormat();
	private CellStyle styleNegrito;

	public void gerarXLSdeOrcamentoExecutadoConsolidado(Filtro filtro, ProjetoService service)
			throws IOException, NumberFormatException, ParseException {

		int rownum = 3;

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetOrcamento = workbook.createSheet("Orçado x Realizado Consolidado");

		styleNumber = workbook.createCellStyle();
		styleHeader = workbook.createCellStyle();
		styleTituloGeral = workbook.createCellStyle();
		styleNegrito = workbook.createCellStyle();

		createStyleHeader(sheetOrcamento);
		createStyleNumber(sheetOrcamento, workbook);

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellStyle(styleHeader);
		cell.setCellValue("Orçado x Realizado Consolidado");

		BigDecimal totalEntrada = BigDecimal.ZERO;
		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal saldo = BigDecimal.ZERO;

		BigDecimal orcadoPlano = BigDecimal.ZERO;
		BigDecimal totalEntradaPlano = BigDecimal.ZERO;
		BigDecimal totalSaidaPlano = BigDecimal.ZERO;
		BigDecimal saldoPlano = BigDecimal.ZERO;

		Row rowCabecalho = sheetOrcamento.createRow(rownum++);
		createTitulosdeOrcamentoExectuadoConsolidado(rowCabecalho, filtro);

		List<Projeto> projetosAux = service.getProjetosFilter(filtro);
		for (Projeto projeto : projetosAux) {

			filtro.setProjetoId(projeto.getId());

			filtro.setProjetoId(projeto.getId());
			if (filtro.getDataInicio() == null)
				filtro.setDataInicio(projeto.getDataInicio());
			if (filtro.getDataFinal() == null)
				filtro.setDataFinal(projeto.getDataFinal());

			filtro.setTarifado(projeto.getTarifado());

			Row linha = sheetOrcamento.createRow(rownum++);
			totalEntrada = BigDecimal.ZERO;
			totalSaida = BigDecimal.ZERO;
			saldo = BigDecimal.ZERO;

//			List<LancamentoAuxiliar> lancamentos = service.getExtratoCtrlProjetoOrcamentoExecutado(filtro);
			List<LancamentoAuxiliar> lancamentos = service.getRelacaoDespesasLinhaOrcamentaria(filtro);
		
			for (LancamentoAuxiliar relatorio : lancamentos) {
				totalEntrada = totalEntrada.add(relatorio.getEntrada());
				totalSaida = totalSaida.add(relatorio.getSaida());

			}

			saldo = projeto.getValor().add(totalEntrada).subtract(totalSaida);

			createConteudoOrcamentoExecutadoConsolidado(projeto, linha, filtro, projeto.getValor(), totalEntrada,
					totalSaida, saldo);

			orcadoPlano = orcadoPlano.add(projeto.getValor());
			totalSaidaPlano = totalSaidaPlano.add(totalSaida);
			totalEntradaPlano = totalEntradaPlano.add(totalEntrada);
			saldoPlano = saldoPlano.add(saldo);

		}

		Row rowSheet = sheetOrcamento.createRow(rownum++);

		createCellColNegrito("Totais:", rowSheet, 2);
		createCellColTotais(orcadoPlano.doubleValue(), rowSheet, 3);
		createCellColTotais(totalEntradaPlano.doubleValue(), rowSheet, 4);
		createCellColTotais(totalSaidaPlano.doubleValue(), rowSheet, 5);
		createCellColTotais(saldoPlano.doubleValue(), rowSheet, 6);

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		dlwd.downloadFile("Orçado x Executado Consolidado " + sdf.format(new Date()) + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void gerarXLSdeOrcamentoExecutado(Filtro filtro, ProjetoService service)
			throws IOException, NumberFormatException, ParseException {

		int rownum = 3;

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetOrcamento = workbook.createSheet("Orçado x Realizado");

		styleNumber = workbook.createCellStyle();
		styleHeader = workbook.createCellStyle();
		styleTituloGeral = workbook.createCellStyle();
		styleNegrito = workbook.createCellStyle();

		createStyleHeader(sheetOrcamento);
		createStyleNumber(sheetOrcamento, workbook);

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellStyle(styleHeader);
		cell.setCellValue("Orçado x Realizado");

		BigDecimal totalEntrada = BigDecimal.ZERO;
		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal saldo = BigDecimal.ZERO;

		BigDecimal orcadoPlano = BigDecimal.ZERO;
		BigDecimal totalEntradaPlano = BigDecimal.ZERO;
		BigDecimal totalSaidaPlano = BigDecimal.ZERO;
		BigDecimal saldoPlano = BigDecimal.ZERO;

		List<Projeto> projetosAux = service.getProjetosFilter(filtro);
		for (Projeto projeto : projetosAux) {

			filtro.setProjetoId(projeto.getId());
			filtro.setProjetoId(projeto.getId());
			if (filtro.getDataInicio() == null)
				filtro.setDataInicio(projeto.getDataInicio());
			if (filtro.getDataFinal() == null)
				filtro.setDataFinal(projeto.getDataFinal());

			filtro.setTarifado(projeto.getTarifado());

			rownum++;
			Row linha = sheetOrcamento.createRow(rownum++);
			createCellColNegrito("Código:", linha, 0);
			createCellCol(projeto.getCodigo(), linha, 1);

			linha = sheetOrcamento.createRow(rownum++);
			createCellColNegrito("Projeto:", linha, 0);
			createCellCol(projeto.getNome(), linha, 1);

			linha = sheetOrcamento.createRow(rownum++);
			createCellColNegrito("Orçamento:", linha, 0);
			createCellColTotais(projeto.getValor().doubleValue(), linha, 1);

			int linhaAuxProjeto = rownum;

			rownum++;
			rownum++;
			rownum++;
			rownum++;

			totalEntrada = BigDecimal.ZERO;
			totalSaida = BigDecimal.ZERO;
			saldo = BigDecimal.ZERO;

			Row rowCabecalho = sheetOrcamento.createRow(rownum++);
			createTitulosdeOrcamentoExectuado(rowCabecalho, filtro);

			List<LancamentoAuxiliar> lancamentos = service.getExtratoCtrlProjetoOrcamentoExecutado(filtro);
			for (LancamentoAuxiliar relatorio : lancamentos) {
				Row row = sheetOrcamento.createRow(rownum++);
				totalEntrada = totalEntrada.add(relatorio.getEntrada());
				totalSaida = totalSaida.add(relatorio.getSaida());
				createConteudoOrcamentoExecutado(relatorio, row, filtro);
			}

			saldo = projeto.getValor().add(totalEntrada).subtract(totalSaida);
			rownum++;

			Row rowAuxProjeto = sheetOrcamento.createRow(linhaAuxProjeto++);

			createCellColNegrito("Saídas:", rowAuxProjeto, 0);
			createCellColTotais(totalSaida.doubleValue(), rowAuxProjeto, 1);

			rowAuxProjeto = sheetOrcamento.createRow(linhaAuxProjeto++);

			createCellColNegrito("Entradas:", rowAuxProjeto, 0);
			createCellColTotais(totalEntrada.doubleValue(), rowAuxProjeto, 1);

			rowAuxProjeto = sheetOrcamento.createRow(linhaAuxProjeto++);

			createCellColNegrito("Saldo:", rowAuxProjeto, 0);
			createCellColTotais(saldo.doubleValue(), rowAuxProjeto, 1);

			orcadoPlano = orcadoPlano.add(projeto.getValor());
			totalSaida = totalSaida.add(totalSaida);
			totalEntradaPlano = totalEntradaPlano.add(totalEntrada);
			saldoPlano = saldoPlano.add(saldo);

			// Row row = sheetOrcamento.createRow(rownum++);
			// createCellColNegrito("Totais:", row, 16);
			// createCellCol(totalEntrada.doubleValue(), row, 17);
			// createCellCol(totalSaida.doubleValue(), row, 18);
			// createCellCol(saldo.doubleValue(), row, 19);

			rownum++;
			rownum++;
		}

		Row rowSheet = sheetOrcamento.createRow(rownum++);

		createCellColNegrito("Orçamento:", rowSheet, 0);
		createCellColNegrito("Entradas:", rowSheet, 1);
		createCellColNegrito("Saídas:", rowSheet, 2);
		createCellColNegrito("Saldo:", rowSheet, 3);

		rowSheet = sheetOrcamento.createRow(rownum++);

		createCellColTotais(orcadoPlano.doubleValue(), rowSheet, 0);
		createCellColTotais(totalEntradaPlano.doubleValue(), rowSheet, 1);
		createCellColTotais(totalSaidaPlano.doubleValue(), rowSheet, 2);
		createCellColTotais(saldoPlano.doubleValue(), rowSheet, 3);

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		dlwd.downloadFile("Orçado x Executado " + sdf.format(new Date()) + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void createCellColTotais(Double valor, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(styleNumber);
		cell.setCellValue(valor);
	}

	public void createCellColNegrito(String texto, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(styleHeader);
		cell.setCellValue(texto);
	}

	public void gerarXLSdeConferencia(Filtro filtro, OrcamentoService service) throws IOException {

		// List<RelatorioFinanceiro> relatorios = service.buscarPagamentos(filtro);

		int rownum = 3;

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetOrcamento = workbook.createSheet("Lançamentos");

		styleNumber = workbook.createCellStyle();
		styleHeader = workbook.createCellStyle();
		styleTituloGeral = workbook.createCellStyle();

		createStyleHeader(sheetOrcamento);
		createStyleNumber(sheetOrcamento, workbook);

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellValue("Conferência de lançamentos");
		cell.setCellStyle(styleTituloGeral);

		// sheetOrcamento.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
		// 0, // last row (0-based)
		// 1, // first column (0-based)
		// 4 // last column (0-based)
		// ));

		Row rowCabecalho = sheetOrcamento.createRow(rownum++);
		createTitulosdeConferencia(rowCabecalho);

		List<Orcamento> orcamentos = service.getOrcamentosFilter(filtro);
		for (Orcamento orcamento : orcamentos) {

			filtro.setIdDoacao(orcamento.getId());
			List<LancamentoAuxiliar> lancamentos = service.getExtratoCtrlDoacaoParaConferencia(filtro);

			for (LancamentoAuxiliar relatorio : lancamentos) {
				Row row = sheetOrcamento.createRow(rownum++);
				createConteudoConferencia(relatorio, row);
			}

		}

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		dlwd.downloadFile("Conferência de lançamentos" + new Date() + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void gerarXLSbyPOI(Filtro filtro, ContaService service) throws IOException {

		List<RelatorioFinanceiro> relatorios = service.buscarPagamentos(filtro);

		int rownum = 3;

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetOrcamento = workbook.createSheet("Lançamentos");

		styleNumber = workbook.createCellStyle();
		styleHeader = workbook.createCellStyle();
		styleTituloGeral = workbook.createCellStyle();

		createStyleHeader(sheetOrcamento);
		createStyleNumber(sheetOrcamento, workbook);

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellValue("conferência de lançamentos");
		cell.setCellStyle(styleTituloGeral);

		// sheetOrcamento.addMergedRegion(new CellRangeAddress(0, // first row (0-based)
		// 0, // last row (0-based)
		// 1, // first column (0-based)
		// 4 // last column (0-based)
		// ));

		Row rowCabecalho = sheetOrcamento.createRow(rownum++);
		createTitulos(rowCabecalho);

		for (RelatorioFinanceiro relatorio : relatorios) {
			Row row = sheetOrcamento.createRow(rownum++);
			createConteudo(relatorio, row);
		}

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		dlwd.downloadFile("Conferência de lançamentos" + new Date() + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void createStyleNumber(HSSFSheet sheetOrcamento, HSSFWorkbook workbook) {
		format = workbook.createDataFormat();
		styleNumber.setDataFormat(format.getFormat("R$ #,#.00"));
	}

	public void createStyleTitulo(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);

		// styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
		// styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		// styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		// styleHeader.setBorderTop(CellStyle.BORDER_THIN);
		// styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeader.setFont(f);

	}

	public void createStyleHeader(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);
		// styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
		// styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		// styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		// styleHeader.setBorderTop(CellStyle.BORDER_THIN);
		// styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeader.setFont(f);

	}

	public void createStyleNegrito(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);
		// styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
		// styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		// styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		// styleHeader.setBorderTop(CellStyle.BORDER_THIN);
		// styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleNegrito.setFont(f);

	}

	public void createTitulos(Row rowCabecalho) {
		Integer collNumHeader = 0;
		createCellColHeader("Pagamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Emissão", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº Documento", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº Lançamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Nota fiscal", rowCabecalho, collNumHeader++);
		createCellColHeader("Categoria", rowCabecalho, collNumHeader++);
		createCellColHeader("Rubrica", rowCabecalho, collNumHeader++);
		createCellColHeader("Pagador", rowCabecalho, collNumHeader++);
		createCellColHeader("Recebedor", rowCabecalho, collNumHeader++);
		createCellColHeader("Descrição", rowCabecalho, collNumHeader++);
		createCellColHeader("Projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("Fonte", rowCabecalho, collNumHeader++);
		createCellColHeader("Ação", rowCabecalho, collNumHeader++);
		createCellColHeader("Status", rowCabecalho, collNumHeader++);
		createCellColHeader("Entrada", rowCabecalho, collNumHeader++);
		createCellColHeader("Saída", rowCabecalho, collNumHeader++);
	}

	public void createTitulosdeConferencia(Row rowCabecalho) {
		Integer collNumHeader = 0;
		createCellColHeader("Tipo", rowCabecalho, collNumHeader++);
		createCellColHeader("Pagamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Emissão", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº Documento", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº Lançamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Nota fiscal", rowCabecalho, collNumHeader++);
		createCellColHeader("Pagador", rowCabecalho, collNumHeader++);
		createCellColHeader("Recebedor", rowCabecalho, collNumHeader++);
		createCellColHeader("Descrição", rowCabecalho, collNumHeader++);
		createCellColHeader("Gestão", rowCabecalho, collNumHeader++);
		createCellColHeader("Fonte", rowCabecalho, collNumHeader++);
		createCellColHeader("Doação", rowCabecalho, collNumHeader++);
		createCellColHeader("Código", rowCabecalho, collNumHeader++);
		createCellColHeader("Projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("Componente", rowCabecalho, collNumHeader++);
		createCellColHeader("SubComponente", rowCabecalho, collNumHeader++);
		createCellColHeader("Requisito de doação", rowCabecalho, collNumHeader++);
		createCellColHeader("Status", rowCabecalho, collNumHeader++);
		createCellColHeader("Entrada", rowCabecalho, collNumHeader++);
		createCellColHeader("Saída", rowCabecalho, collNumHeader++);
	}

	public void createConteudoConferencia(LancamentoAuxiliar relatorio, Row row) {

		int cellnum = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		createCellCol(relatorio.getTipo(), row, cellnum++);
		createCellCol(sdf.format(relatorio.getDataPagamento()), row, cellnum++);
		createCellCol(sdf.format(relatorio.getDataEmissao()), row, cellnum++);
		createCellCol(relatorio.getNumeroDocumento(), row, cellnum++);
		createCellCol(relatorio.getId().toString(), row, cellnum++);
		createCellCol(relatorio.getNotaFiscal(), row, cellnum++);
		createCellCol(relatorio.getContaPagadorLbl(), row, cellnum++);
		createCellCol(relatorio.getContaRecebedorLbl(), row, cellnum++);
		createCellCol(relatorio.getDescricao(), row, cellnum++);
		createCellCol(relatorio.getNomeGestao(), row, cellnum++);
		createCellCol(relatorio.getFonte(), row, cellnum++);
		createCellCol(relatorio.getDoacao(), row, cellnum++);
		createCellCol(relatorio.getCodigo(), row, cellnum++);
		createCellCol(relatorio.getNomeProjeto(), row, cellnum++);
		createCellCol(relatorio.getComponente(), row, cellnum++);
		createCellCol(relatorio.getSubComponente(), row, cellnum++);
		createCellCol(relatorio.getRequisitoDeDoador(), row, cellnum++);
		createCellCol(relatorio.getStatus(), row, cellnum++);
		createCellCol(relatorio.getEntrada().doubleValue(), row, cellnum++);
		createCellCol(relatorio.getSaida().doubleValue(), row, cellnum++);

	}

	public void createTitulosdeOrcamentoExectuadoConsolidado(Row rowCabecalho, Filtro filtro) {

		Integer collNumHeader = 0;
		createCellColHeader("Código", rowCabecalho, collNumHeader++);
		createCellColHeader("Projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("Cadeia", rowCabecalho, collNumHeader++);
		createCellColHeader("Orçamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Entrada", rowCabecalho, collNumHeader++);
		createCellColHeader("Saída", rowCabecalho, collNumHeader++);
		createCellColHeader("Saldo", rowCabecalho, collNumHeader++);
	}

	public void createTitulosdeOrcamentoExectuado(Row rowCabecalho, Filtro filtro) {
		Integer collNumHeader = 0;

		if (contains("tipo", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Tipo", rowCabecalho, collNumHeader++);
		if (contains("dt_pagamento", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Pagamento", rowCabecalho, collNumHeader++);
		if (contains("dt_emissao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Emissão", rowCabecalho, collNumHeader++);
		if (contains("doc", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Nº Documento", rowCabecalho, collNumHeader++);
		if (contains("n_lanc", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Nº Lançamento", rowCabecalho, collNumHeader++);
		if (contains("nf", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Nota fiscal", rowCabecalho, collNumHeader++);
		if (contains("pagador", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Pagador", rowCabecalho, collNumHeader++);
		if (contains("recebedor", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Recebedor", rowCabecalho, collNumHeader++);
		if (contains("desc", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Descrição", rowCabecalho, collNumHeader++);
		if (contains("fonte", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Fonte", rowCabecalho, collNumHeader++);
		if (contains("doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Doação", rowCabecalho, collNumHeader++);
		if (contains("codigo", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Código", rowCabecalho, collNumHeader++);
		if (contains("projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Projeto", rowCabecalho, collNumHeader++);
		if (contains("componente", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Componente", rowCabecalho, collNumHeader++);
		if (contains("sub_comp", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("SubComponente", rowCabecalho, collNumHeader++);
		if (contains("req_doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Requisito de doação", rowCabecalho, collNumHeader++);
		if (contains("stt", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Status", rowCabecalho, collNumHeader++);
		if (contains("entrada", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Entrada", rowCabecalho, collNumHeader++);
		if (contains("saida", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Saída", rowCabecalho, collNumHeader++);

	}

	public void createConteudoOrcamentoExecutadoConsolidado(Projeto projeto, Row row, Filtro filtro,
			BigDecimal orcamento, BigDecimal entrada, BigDecimal saida, BigDecimal saldo) {
		int cellnum = 0;
		createCellCol(projeto.getNome(), row, cellnum++);
		createCellCol(projeto.getCodigo(), row, cellnum++);
		createCellCol(projeto.getNomeCadeia(), row, cellnum++);
		createCellCol(orcamento.doubleValue(), row, cellnum++);
		createCellCol(entrada.doubleValue(), row, cellnum++);
		createCellCol(saida.doubleValue(), row, cellnum++);
		createCellCol(saldo.doubleValue(), row, cellnum++);
	}

	public void createConteudoOrcamentoExecutado(LancamentoAuxiliar relatorio, Row row, Filtro filtro) {

		int cellnum = 0;

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		if (contains("tipo", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getTipo(), row, cellnum++);
		if (contains("dt_pagamento", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(sdf.format(relatorio.getDataPagamento()), row, cellnum++);
		if (contains("dt_emissao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(sdf.format(relatorio.getDataEmissao()), row, cellnum++);
		if (contains("doc", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getNumeroDocumento(), row, cellnum++);
		if (contains("n_lanc", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getId().toString(), row, cellnum++);
		if (contains("nf", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getNotaFiscal(), row, cellnum++);
		if (contains("pagador", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getContaPagadorLbl(), row, cellnum++);
		if (contains("recebedor", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getContaRecebedorLbl(), row, cellnum++);
		if (contains("desc", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getDescricao(), row, cellnum++);
		if (contains("fonte", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getFonte(), row, cellnum++);
		if (contains("doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getDoacao(), row, cellnum++);
		if (contains("codigo", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getCodigo(), row, cellnum++);
		if (contains("projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getNomeProjeto(), row, cellnum++);
		if (contains("componente", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getComponente(), row, cellnum++);
		if (contains("sub_comp", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getSubComponente(), row, cellnum++);
		if (contains("req_doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getRequisitoDeDoador(), row, cellnum++);
		if (contains("stt", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getStatus(), row, cellnum++);
		if (contains("entrada", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getEntrada().doubleValue(), row, cellnum++);
		if (contains("saida", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getSaida().doubleValue(), row, cellnum++);
	}

	public boolean contains(String elemento, String[] lista) {
		for (String string : lista) {
			if (elemento.equals(string)) {
				return true;
			}
		}
		return false;
	}

	public void createConteudo(RelatorioFinanceiro relatorio, Row row) {
		int cellnum = 0;
		createCellCol(relatorio.getPagamento(), row, cellnum++);
		createCellCol(relatorio.getEmissao(), row, cellnum++);
		createCellCol(relatorio.getDoc(), row, cellnum++);
		createCellCol(relatorio.getNumeroLancamento(), row, cellnum++);
		createCellCol(relatorio.getNotaFiscal(), row, cellnum++);
		createCellCol(relatorio.getCategoriaDespesa(), row, cellnum++);
		createCellCol(relatorio.getRubrica(), row, cellnum++);
		createCellCol(relatorio.getPagador(), row, cellnum++);
		createCellCol(relatorio.getRecebedor(), row, cellnum++);
		createCellCol(relatorio.getDescricao(), row, cellnum++);
		createCellCol(relatorio.getProjeto(), row, cellnum++);
		createCellCol(relatorio.getFonte(), row, cellnum++);
		createCellCol(relatorio.getAcao(), row, cellnum++);
		createCellCol(relatorio.getStatus(), row, cellnum++);
		createCellCol(relatorio.getEntrada(), row, cellnum++);
		createCellCol(relatorio.getSaida(), row, cellnum++);
		createCellCol(relatorio.getAdiantamento(), row, cellnum++);

	}

	public void createCellColHeader(String texto, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellValue(texto);
		cell.setCellStyle(styleHeader);
	}

	public void createCellCol(String texto, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellValue(texto);
	}

	public void createCellCol(Double valor, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(styleNumber);
		cell.setCellValue(valor);
	}

}
