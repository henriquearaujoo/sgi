package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import model.ContaBancaria;
import model.RelatorioContasAPagar;
import service.ContaService;

public class GeradorContasAPagar implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String inputArquivo;

	public void setOutputFile(String inputArquivo) {
		this.inputArquivo = inputArquivo;
	}

	// ********************************* API MAIS INTUITIVA PARA MONTAR O EXCEL
	// **********************************************//

	private CellStyle styleNumber; // = workbook.createCellStyle();
	private CellStyle styleNumberNegrito;
	private CellStyle styleHeader;
	private CellStyle styleTituloGeral;
	private CellStyle styleNegrito;
	private DataFormat format;// = workbook.createDataFormat();

	public void gerarCP(List<ContaBancaria> selectedContas, ContaService service, Filtro filtro) throws IOException {

		int rownum = 3;

		Double saldo = 0.0;

		Double entrada = new Double(0);
		Double saida = new Double(0);

		Double totalEntrada = new Double(0);
		Double totalSaida = new Double(0);

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetOrcamento = workbook.createSheet("Lançamentos");

		styleNumber = workbook.createCellStyle();
		styleHeader = workbook.createCellStyle();
		styleTituloGeral = workbook.createCellStyle();
		styleNegrito = workbook.createCellStyle();
		styleNumberNegrito = workbook.createCellStyle();

		createStyleHeader(sheetOrcamento);
		createStyleNumber(sheetOrcamento, workbook);
		createStyleNegrito(sheetOrcamento);
		createStyleNumberNegrito(sheetOrcamento, workbook);

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellValue("CP - Contas a pagar");
		cell.setCellStyle(styleTituloGeral);

		Row rowCabecalho = sheetOrcamento.createRow(rownum++);
		createTitulos(rowCabecalho);

		for (ContaBancaria conta : selectedContas) {

			totalEntrada = new Double(0);
			totalSaida = new Double(0);

			filtro.setIdConta(conta.getId());
			ContaBancaria contaBancaria = service.getContaSaldoCA(filtro);
			saldo = contaBancaria.getSaldoInicial().doubleValue() - contaBancaria.getTotalSaida().doubleValue()
					+ contaBancaria.getTotalEntrada().doubleValue();

			rownum++;

			Row row = sheetOrcamento.createRow(rownum++);
			createCellColNegrito(conta.getNomeConta(), row, 0);
			createCellColNegrito("Saldo anterior:", row, 1);
			createCellColNegrito(saldo, row, 2);
			

			filtro.setIdConta(conta.getId());
			int i = 0;
			rownum++;
			for (RelatorioContasAPagar relatorio : service.getPagamentosByContaMODE01(filtro)) {
				
				totalEntrada = new Double(0);
				totalSaida = new Double(0);
				
				Row rowSheet = sheetOrcamento.createRow(rownum++);

				entrada = relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0);
				totalEntrada = totalEntrada + entrada;
				saida = relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0);
				totalSaida = totalSaida + saida;
				saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - relatorio.getAdiantamento();
				createConteudo(relatorio, rowSheet);

			}

			rownum++;
			row = sheetOrcamento.createRow(rownum++);

			rownum++;
			row = sheetOrcamento.createRow(rownum++);

			createCellColNegrito("Saldo no período: ", row, 19);
			createCellColNegrito(saldo, row, 20);

		}

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		
		dlwd.downloadFile("Contas a pagar " + sdf.format(new Date())  + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void gerarCPDetalhado(List<ContaBancaria> selectedContas, ContaService service, Filtro filtro)
			throws IOException {

		int rownum = 3;

		// int contLinha = 5;

		BigDecimal saldoDiarioTotal = BigDecimal.ZERO;
		BigDecimal saldoTotalPorConta = BigDecimal.ZERO;
		Double saldo = 0.0;

		Double entrada = new Double(0);
		Double saida = new Double(0);
		Double prestacao = new Double(0);
		Double adiantamento = new Double(0);

		Double totalEntrada = new Double(0);
		Double totalSaida = new Double(0);
		Double totalPrestacao = new Double(0);
		Double totalAdiantamento = new Double(0);

		String dataAnterior = "";
		String dataAtual = "";

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetOrcamento = workbook.createSheet("Lançamentos");

		styleNumber = workbook.createCellStyle();
		styleHeader = workbook.createCellStyle();
		styleTituloGeral = workbook.createCellStyle();
		styleNegrito = workbook.createCellStyle();
		styleNumberNegrito = workbook.createCellStyle();

		createStyleHeader(sheetOrcamento);
		createStyleNumber(sheetOrcamento, workbook);
		createStyleNegrito(sheetOrcamento);
		createStyleNumberNegrito(sheetOrcamento, workbook);

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellValue("CP - Contas a pagar");
		cell.setCellStyle(styleTituloGeral);

		// sheetOrcamento.addMergedRegion(new CellRangeAddress(
		// 0, //first row (0-based)
		// 0, //last row (0-based)
		// 1, //first column (0-based)
		// 4 //last column (0-based)
		// ));

		Row rowCabecalho = sheetOrcamento.createRow(rownum++);
		createTitulos(rowCabecalho);

		for (ContaBancaria conta : selectedContas) {

			totalEntrada = new Double(0);
			totalSaida = new Double(0);
			totalPrestacao = new Double(0);
			totalAdiantamento = new Double(0);

			if (!conta.getTipo().equals("CA")) {
				saldo = conta.getSaldoInicial().doubleValue() - conta.getTotalSaida().doubleValue()
						+ conta.getTotalEntrada().doubleValue();

				Locale locale = new Locale("pt", "BR");
				NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
				System.out.println(currencyFormatter.format(conta.getTotalEntrada().doubleValue()));
				System.out.println(currencyFormatter.format(conta.getTotalSaida().doubleValue()));
			    System.out.println(currencyFormatter.format(conta.getSaldoInicial().doubleValue()));

			} else {
				filtro.setIdConta(conta.getId());
				ContaBancaria contaBancaria = service.getContaSaldoCA(filtro);
				saldo = contaBancaria.getSaldoInicial().doubleValue() - contaBancaria.getTotalSaida().doubleValue()
						+ contaBancaria.getTotalEntrada().doubleValue();
			}

			rownum++;

			Row row = sheetOrcamento.createRow(rownum++);
			createCellColNegrito(conta.getNomeConta(), row, 0);
			createCellColNegrito("Saldo anterior:", row, 1);
			createCellColNegrito(saldo, row, 2);
			rownum++;

			filtro.setIdConta(conta.getId());
			int i = 0;

			for (RelatorioContasAPagar relatorio : conta.getTipo().equals("CA")
					? service.getPagamentosByContaCAMODE01(filtro)
					: service.getPagamentosByContaMODE01(filtro)) {

				dataAtual = relatorio.getPagamento();

				if (i == 0) {
					dataAnterior = dataAtual;
					i++;
				}

				if (!dataAtual.equals(dataAnterior)) {

					rownum++;

					row = sheetOrcamento.createRow(rownum++);

					createCellColNegrito("Totais do dia " + dataAnterior, row, 18);
					createCellColNegrito(totalEntrada, row, 19);
					createCellColNegrito(totalSaida, row, 20);
					createCellColNegrito(saldo, row, 21);

					totalEntrada = new Double(0);
					totalSaida = new Double(0);
					totalPrestacao = new Double(0);
					totalAdiantamento = new Double(0);

					rownum++;
					rownum++;

					dataAnterior = dataAtual;
				}

				Row rowSheet = sheetOrcamento.createRow(rownum++);

				entrada = relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0);
				totalEntrada = totalEntrada + entrada;
				saida = relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0);
				totalSaida = totalSaida + saida;
				adiantamento = relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0);
				totalAdiantamento = adiantamento;
				prestacao = relatorio.getPrestacaoConta() != null ? relatorio.getPrestacaoConta() : new Double(0);
				saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - relatorio.getAdiantamento();
				createConteudo(relatorio, rowSheet);

			}

			rownum++;
			row = sheetOrcamento.createRow(rownum++);

			createCellColNegrito("Totais do dia " + dataAnterior, row, 18);
			createCellColNegrito(totalEntrada, row, 19);
			createCellColNegrito(totalSaida, row, 20);
			createCellColNegrito(saldo, row, 21);

			rownum++;
			row = sheetOrcamento.createRow(rownum++);

			createCellColNegrito("Saldo no período: ", row, 20);
			createCellColNegrito(saldo, row, 21);

		}

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		dlwd.downloadFile("Contas a pagar " + sdf.format(new Date())  + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void gerarRelatorioContas(List<ContaBancaria> selectedContas, ContaService service, Filtro filtro)
			throws IOException {

		int rownum = 3;

		BigDecimal saldoDiarioTotal = BigDecimal.ZERO;
		BigDecimal saldoTotalPorConta = BigDecimal.ZERO;
		Double saldo = 0.0;

		Double entrada = new Double(0);
		Double saida = new Double(0);
		Double prestacao = new Double(0);
		Double adiantamento = new Double(0);

		Double totalEntrada = new Double(0);
		Double totalSaida = new Double(0);
		Double totalPrestacao = new Double(0);
		Double totalAdiantamento = new Double(0);

		String dataAnterior = "";
		String dataAtual = "";

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetOrcamento = workbook.createSheet("Lançamentos");

		styleNumber = workbook.createCellStyle();
		styleHeader = workbook.createCellStyle();
		styleTituloGeral = workbook.createCellStyle();
		styleNegrito = workbook.createCellStyle();
		styleNumberNegrito = workbook.createCellStyle();

		createStyleHeader(sheetOrcamento);
		createStyleNumber(sheetOrcamento, workbook);
		createStyleNegrito(sheetOrcamento);
		createStyleNumberNegrito(sheetOrcamento, workbook);

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellValue("CP - Contas a pagar");
		cell.setCellStyle(styleTituloGeral);

		Row rowCabecalho = sheetOrcamento.createRow(rownum++);
		createTitulos(rowCabecalho);

		for (ContaBancaria conta : selectedContas) {

			totalEntrada = new Double(0);
			totalSaida = new Double(0);
			totalPrestacao = new Double(0);
			totalAdiantamento = new Double(0);

			if (!conta.getTipo().equals("CA")) {
				saldo = conta.getSaldoInicial().doubleValue() - conta.getTotalSaida().doubleValue()
						+ conta.getTotalEntrada().doubleValue();

				Locale locale = new Locale("pt", "BR");
				NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
				System.out.println(currencyFormatter.format(conta.getTotalEntrada().doubleValue()));
				System.out.println(currencyFormatter.format(conta.getTotalSaida().doubleValue()));
				System.out.println(currencyFormatter.format(conta.getSaldoInicial().doubleValue()));

			} else {
				filtro.setIdConta(conta.getId());
				ContaBancaria contaBancaria = service.getContaSaldoCA(filtro);
				saldo = contaBancaria.getSaldoInicial().doubleValue() - contaBancaria.getTotalSaida().doubleValue()
						+ contaBancaria.getTotalEntrada().doubleValue();
			}

			filtro.setIdConta(conta.getId());
			int i = 0;

			for (RelatorioContasAPagar relatorio : service.getPagamentosByContaMODE01Relatorio(filtro)) {

				dataAtual = relatorio.getPagamento();

				if (i == 0) {
					dataAnterior = dataAtual;
					i++;
				}

				Row rowSheet = sheetOrcamento.createRow(rownum++);

				entrada = relatorio.getEntrada() != null ? relatorio.getEntrada() : new Double(0);
				totalEntrada = totalEntrada + entrada;
				saida = relatorio.getSaida() != null ? relatorio.getSaida() : new Double(0);
				totalSaida = totalSaida + saida;
				adiantamento = relatorio.getAdiantamento() != null ? relatorio.getAdiantamento() : new Double(0);
				totalAdiantamento = adiantamento;
				prestacao = relatorio.getPrestacaoConta() != null ? relatorio.getPrestacaoConta() : new Double(0);
				saldo = saldo + relatorio.getEntrada() - relatorio.getSaida() - relatorio.getAdiantamento();
				createConteudoRelatorio(conta, relatorio, rowSheet);

			}

		}

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		dlwd.downloadFile("Contas a pagar " + sdf.format(new Date())  + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void createStyleNumber(HSSFSheet sheetOrcamento, HSSFWorkbook workbook) {
		format = workbook.createDataFormat();
		styleNumber.setDataFormat(format.getFormat("R$ #,#.00"));
	}

	public void createStyleNumberNegrito(HSSFSheet sheetOrcamento, HSSFWorkbook workbook) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);
		format = workbook.createDataFormat();
		styleNumberNegrito.setDataFormat(format.getFormat("R$ #,#.00"));
		// styleNumberNegrito.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// styleNumberNegrito.setAlignment(CellStyle.ALIGN_CENTER);
		styleNumberNegrito.setFont(f);

		// TODO: VERIFICAR STYLE
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

	public void createStyleNegrito(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);

		// styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
		// styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		// styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		// styleHeader.setBorderTop(CellStyle.BORDER_THIN);
		// styleNegrito.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		// styleNegrito.setAlignment(CellStyle.ALIGN_CENTER);
		styleNegrito.setFont(f);

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

	public void createTitulos(Row rowCabecalho) {
		Integer collNumHeader = 0;
		createCellColHeader("Conta", rowCabecalho, collNumHeader++);
		createCellColHeader("Tipo", rowCabecalho, collNumHeader++);
		createCellColHeader("Pagamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Emissão", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº Documento", rowCabecalho, collNumHeader++);
		createCellColHeader("Tipo de documento", rowCabecalho, collNumHeader++);
		createCellColHeader("Nota fiscal", rowCabecalho, collNumHeader++);
		createCellColHeader("Categoria", rowCabecalho, collNumHeader++);
		createCellColHeader("Condição de pagamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº Lançamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº da SV", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº da SC", rowCabecalho, collNumHeader++);
		createCellColHeader("Pagador", rowCabecalho, collNumHeader++);
		createCellColHeader("Recebedor", rowCabecalho, collNumHeader++);
		createCellColHeader("CPF/CNPJ", rowCabecalho, collNumHeader++);
		createCellColHeader("Descrição", rowCabecalho, collNumHeader++);
		createCellColHeader("Fonte", rowCabecalho, collNumHeader++);
		createCellColHeader("Doação", rowCabecalho, collNumHeader++);
		createCellColHeader("Projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("Componente", rowCabecalho, collNumHeader++);
		createCellColHeader("Subcomponente", rowCabecalho, collNumHeader++);
		createCellColHeader("Gestão", rowCabecalho, collNumHeader++);
		createCellColHeader("Programa", rowCabecalho, collNumHeader++);
		createCellColHeader("Tipo de trensferência", rowCabecalho, collNumHeader++);
		createCellColHeader("Requisitos de doação", rowCabecalho, collNumHeader++);
		createCellColHeader("Ação", rowCabecalho, collNumHeader++);
		createCellColHeader("Status", rowCabecalho, collNumHeader++);
		createCellColHeader("Parcela", rowCabecalho, collNumHeader++);
		createCellColHeader("Entrada", rowCabecalho, collNumHeader++);
		createCellColHeader("Saída", rowCabecalho, collNumHeader++);
	}

	public void createConteudo(RelatorioContasAPagar relatorio, Row row) {
		int cellnum = 0;
		createCellCol(relatorio.getTipo(), row, cellnum++);
		createCellCol(relatorio.getPagamento(), row, cellnum++);
		createCellCol(relatorio.getEmissao(), row, cellnum++);
		createCellCol(relatorio.getDoc(), row, cellnum++);
		createCellCol(relatorio.getTipoDocumento(), row, cellnum++);
		createCellCol(relatorio.getNotaFiscal(), row, cellnum++);
		createCellCol(relatorio.getCategoriaDespesa(), row, cellnum++);
		createCellCol(relatorio.getNumeroLancamento(), row, cellnum++);
		createCellCol(relatorio.getPagador(), row, cellnum++);
		createCellCol(relatorio.getRecebedor(), row, cellnum++);
		createCellCol(relatorio.getDescricao(), row, cellnum++);
		createCellCol(relatorio.getFonte(), row, cellnum++);
		createCellCol(relatorio.getDoacao(), row, cellnum++);
		createCellCol(relatorio.getProjeto(), row, cellnum++);
		createCellCol(relatorio.getComponente(), row, cellnum++);
		createCellCol(relatorio.getSubcomponente(), row, cellnum++);
		createCellCol(relatorio.getRubrica(), row, cellnum++);
		createCellCol(relatorio.getAcao(), row, cellnum++);
		createCellCol(relatorio.getStatus(), row, cellnum++);
		createCellCol(relatorio.getParcela(), row, cellnum++);
		createCellCol(relatorio.getEntrada(), row, cellnum++);
		createCellCol(relatorio.getSaida(), row, cellnum++);

	}

	public void createConteudoRelatorio(ContaBancaria conta, RelatorioContasAPagar relatorio, Row row) {
		int cellnum = 0;
		createCellCol(conta.getContaFornecedor(), row, cellnum++);
		createCellCol(conta.getTipo(), row, cellnum++);
		createCellCol(relatorio.getPagamento(), row, cellnum++);
		createCellCol(relatorio.getEmissao(), row, cellnum++);
		createCellCol(relatorio.getDoc(), row, cellnum++);
		createCellCol(relatorio.getTipoDocumento(), row, cellnum++);
		createCellCol(relatorio.getNotaFiscal(), row, cellnum++);
		createCellCol(relatorio.getCategoriaDespesa(), row, cellnum++);
		createCellCol(relatorio.getCondicaoPagamento(), row, cellnum++);
		createCellCol(relatorio.getNumeroLancamento(), row, cellnum++);
		createCellCol(relatorio.getSv(), row, cellnum++);
		createCellCol(relatorio.getSc(), row, cellnum++);
		createCellCol(relatorio.getPagador(), row, cellnum++);
		createCellCol(relatorio.getRecebedor(), row, cellnum++);
		createCellCol(relatorio.getCpfcnpj(), row, cellnum++);
		createCellCol(relatorio.getDescricao(), row, cellnum++);
		createCellCol(relatorio.getFonte(), row, cellnum++);
		createCellCol(relatorio.getDoacao(), row, cellnum++);
		createCellCol(relatorio.getProjeto(), row, cellnum++);
		createCellCol(relatorio.getComponente(), row, cellnum++);
		createCellCol(relatorio.getSubcomponente(), row, cellnum++);
		createCellCol(relatorio.getGestao(), row, cellnum++);
		createCellCol(relatorio.getPrograma(), row, cellnum++);
		createCellCol(relatorio.getDespesaReceita(), row, cellnum++);
		createCellCol(relatorio.getRubrica(), row, cellnum++);
		createCellCol(relatorio.getAcao(), row, cellnum++);
		createCellCol(relatorio.getStatus(), row, cellnum++);
		createCellCol(relatorio.getParcela(), row, cellnum++);
		createCellCol(relatorio.getEntrada(), row, cellnum++);
		createCellCol(relatorio.getSaida(), row, cellnum++);

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

	public void createCellColNegrito(String texto, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(styleNegrito);
		cell.setCellValue(texto);
	}

	public void createCellCol(Double valor, Row row, Integer col) {
		if (valor == null)
			return;

		Cell cell = row.createCell(col);
		cell.setCellStyle(styleNumber);
		cell.setCellValue(valor);
	}

	public void createCellColNegrito(Double valor, Row row, Integer col) {
		if (valor.longValue() == 0.0)
			return;

		Cell cell = row.createCell(col);
		cell.setCellStyle(styleNumberNegrito);
		cell.setCellValue(valor);
	}

	public CellStyle getStyleNegrito() {
		return styleNegrito;
	}

	public void setStyleNegrito(CellStyle styleNegrito) {
		this.styleNegrito = styleNegrito;
	}

	public CellStyle getStyleNumberNegrito() {
		return styleNumberNegrito;
	}

	public void setStyleNumberNegrito(CellStyle styleNumberNegrito) {
		this.styleNumberNegrito = styleNumberNegrito;
	}

}
