package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.faces.context.FacesContext;

import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;

import model.RelatorioContasAPagar;

public class GeradorExecucaoFinanceira implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String inputArquivo;

	public void setOutputFile(String inputArquivo) {
		this.inputArquivo = inputArquivo;
	}

	private CellStyle styleNumber; // = workbook.createCellStyle();
	private CellStyle styleNumberNegrito;
	private CellStyle styleHeader;
	private CellStyle styleTituloGeral;
	private CellStyle styleNegrito;
	private DataFormat format;// = workbook.createDataFormat();

	public void createStyleHeader(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);
		styleHeader.setFont(f);

	}

	public void createStyleNumber(HSSFSheet sheetOrcamento, HSSFWorkbook workbook) {
		format = workbook.createDataFormat();
		styleNumber.setDataFormat(format.getFormat("R$ #,#.00"));
	}

	public void createStyleNegrito(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);
		styleNegrito.setFont(f);

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

	public void createCellColHeader(String texto, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellValue(texto);
		cell.setCellStyle(styleHeader);
	}

	public void createTitulos(Row rowCabecalho) {
		Integer collNumHeader = 0;
		createCellColHeader("Nº", rowCabecalho, collNumHeader++);
		createCellColHeader("Emissão", rowCabecalho, collNumHeader++);
		createCellColHeader("Pagamento", rowCabecalho, collNumHeader++);
		createCellColHeader("NF", rowCabecalho, collNumHeader++);
		createCellColHeader("Documento", rowCabecalho, collNumHeader++);
		createCellColHeader("Pagador", rowCabecalho, collNumHeader++);
		createCellColHeader("Recebedor", rowCabecalho, collNumHeader++);
		createCellColHeader("UC", rowCabecalho, collNumHeader++);
		createCellColHeader("Localidade", rowCabecalho, collNumHeader++);
		createCellColHeader("Localidade Projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("Gestão", rowCabecalho, collNumHeader++);
		createCellColHeader("Descrição", rowCabecalho, collNumHeader++);
		createCellColHeader("Fonte", rowCabecalho, collNumHeader++);
		createCellColHeader("Doação", rowCabecalho, collNumHeader++);
		createCellColHeader("Projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("Componente", rowCabecalho, collNumHeader++);
		createCellColHeader("Subcomponente", rowCabecalho, collNumHeader++);
		createCellColHeader("Rubrica", rowCabecalho, collNumHeader++);
		createCellColHeader("Categoria", rowCabecalho, collNumHeader++);
		createCellColHeader("Status", rowCabecalho, collNumHeader++);
		createCellColHeader("Parcela", rowCabecalho, collNumHeader++);
		createCellColHeader("Tipo", rowCabecalho, collNumHeader++);
		createCellColHeader("Entrada", rowCabecalho, collNumHeader++);
		createCellColHeader("Saída", rowCabecalho, collNumHeader++);
		
	}
	
	public void createConteudo(RelatorioContasAPagar relatorio, Row row) {
		int cellnum = 0;
		createCellCol(relatorio.getNumeroLancamento(), row, cellnum++);
		createCellCol(relatorio.getEmissao(), row, cellnum++);
		createCellCol(relatorio.getPagamento(), row, cellnum++);
		createCellCol(relatorio.getNotaFiscal(), row, cellnum++);
		createCellCol(relatorio.getDoc(), row, cellnum++);
		createCellCol(relatorio.getPagador(), row, cellnum++);
		createCellCol(relatorio.getRecebedor(), row, cellnum++);
		createCellCol(relatorio.getLocalidadeUC(), row, cellnum++);
		createCellCol(relatorio.getLocalidade(), row, cellnum++);
		createCellCol(relatorio.getLocalidadeProjeto(), row, cellnum++);
		createCellCol(relatorio.getGestao(), row, cellnum++);
		createCellCol(relatorio.getDescricao(), row, cellnum++);
		createCellCol(relatorio.getFonte(), row, cellnum++);
		createCellCol(relatorio.getDoacao(), row, cellnum++);
		createCellCol(relatorio.getProjeto(), row, cellnum++);
		createCellCol(relatorio.getComponente(), row, cellnum++);
		createCellCol(relatorio.getSubcomponente(), row, cellnum++);
		createCellCol(relatorio.getRubrica(), row, cellnum++);
		createCellCol(relatorio.getCategoriaDespesa(), row, cellnum++);
		createCellCol(relatorio.getStatus(), row, cellnum++);
		createCellCol(relatorio.getParcela(), row, cellnum++);
		createCellCol(relatorio.getTipo(), row, cellnum++);
		createCellCol(relatorio.getEntrada(), row, cellnum++);
		createCellCol(relatorio.getSaida(), row, cellnum++);

	}

	public void createCellCol(String texto, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellValue(texto);
	}

	public void createCellCol(Double valor, Row row, Integer col) {
		if (valor == null)
			return;

		Cell cell = row.createCell(col);
		cell.setCellStyle(styleNumber);
		cell.setCellValue(valor);
	}

	

	public void gerarExecucao(List<RelatorioContasAPagar> lancamentos) throws IOException {

		int rownum = 3;

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
		cell.setCellValue("Execução financeira");
		cell.setCellStyle(styleTituloGeral);

		Row rowCabecalho = sheetOrcamento.createRow(rownum++);
		createTitulos(rowCabecalho);

		for (RelatorioContasAPagar l : lancamentos) {
			Row rowSheet = sheetOrcamento.createRow(rownum++);
			createConteudo(l, rowSheet);
		}


		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		dlwd.downloadFile("Execução financeira" + sdf.format(new Date()) + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

}
