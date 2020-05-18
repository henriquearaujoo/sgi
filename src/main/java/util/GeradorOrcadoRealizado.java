package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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

import model.Projeto;
import model.RelatorioExecucao;
import service.ContaService;

public class GeradorOrcadoRealizado implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String inputArquivo;

	public void setOutputFile(String inputArquivo) {
		this.inputArquivo = inputArquivo;
	}

	// ********************************* API MAIS INTUITIVA PARA MONTAR O EXCEL **********************************************//

	private CellStyle styleNumber; // = workbook.createCellStyle();
	private CellStyle styleNumberNegrito;
	private CellStyle styleHeader;
	private CellStyle styleTituloGeral;
	private CellStyle styleNegrito;
	private DataFormat format;// = workbook.createDataFormat();

	public void gerarOrcadoRealizado(List<Projeto> projetos, ContaService service, Filtro filtro) throws IOException {

		
		int rownum = 3;
		
		//int contLinha = 5;

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetOrcamento = workbook.createSheet("Lançamentos");
		
		styleNumber = workbook.createCellStyle();
		styleHeader = workbook.createCellStyle();
		styleTituloGeral = workbook.createCellStyle();
		styleNegrito =  workbook.createCellStyle();
		styleNumberNegrito = workbook.createCellStyle();
		
		createStyleHeader(sheetOrcamento);
		createStyleNumber(sheetOrcamento, workbook);
		createStyleNegrito(sheetOrcamento);
		createStyleNumberNegrito(sheetOrcamento, workbook);
		
		Row rowMergin = sheetOrcamento.createRow((short) 0);
	    Cell cell = rowMergin.createCell((short) 0);
	    cell.setCellValue("Executado x Orçado");
	    cell.setCellStyle(styleTituloGeral);

//	    sheetOrcamento.addMergedRegion(new CellRangeAddress(
//	            0, //first row (0-based)
//	            0, //last row  (0-based)
//	            1, //first column (0-based)
//	            4  //last column  (0-based)
//	    ));

		
		Row rowCabecalho = sheetOrcamento.createRow(rownum++);
		createTitulos(rowCabecalho);
		List<RelatorioExecucao> relatorio;
		Double totalSaida = new Double(0);
		Double totalEntrada = new Double(0);
		Double saldoProjeto = new Double(0);
		
		Integer linhaSaldo = 0;
 		
		for (Projeto projeto : projetos) {
			saldoProjeto = projeto.getValor().doubleValue();
			rownum++;
			Row rowSaldo = sheetOrcamento.createRow(rownum++);
			createCellColNegrito(projeto.getNome(), rowSaldo, 0);
			createCellColNegrito("Saldo Inicial:" , rowSaldo, 13);
			createCellColNegrito(projeto.getValor().doubleValue() , rowSaldo, 14);
			linhaSaldo = rownum;
			rownum++;
			rownum++;
			relatorio = new ArrayList<>();
			relatorio = service.getPagamentoByProjeto(projeto.getId(), filtro);
			
			for (RelatorioExecucao relatorioExecucao : relatorio) {
				Row row = sheetOrcamento.createRow(rownum++);
				
				if (relatorioExecucao.getSaida() != null) {
					totalSaida =+ relatorioExecucao.getSaida(); 
				}
				
				if (relatorioExecucao.getEntrada() != null) {
					totalEntrada =+ relatorioExecucao.getEntrada();
				}
				
				saldoProjeto = (saldoProjeto - totalSaida) + totalEntrada;
				
				createConteudo(relatorioExecucao, row);
			}
			
			
			rowSaldo = sheetOrcamento.createRow(linhaSaldo);
			createCellColNegrito("Saldo Atual:" , rowSaldo, 13);
			createCellColNegrito(saldoProjeto , rowSaldo, 14);
			
			//Row row = sheetOrcamento.createRow(rownum++);
		}
		
		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		dlwd.downloadFile("OrcadoxRealizado" + new Date() + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void createStyleNumber(HSSFSheet sheetOrcamento, HSSFWorkbook workbook) {
		format = workbook.createDataFormat();
		styleNumber.setDataFormat(format.getFormat("R$ #,#.00"));
	}
	
	public void createStyleNumberNegrito(HSSFSheet sheetOrcamento, HSSFWorkbook workbook) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		//TODO: VERIFICAR STYLE
		f.setBold(true);
		format = workbook.createDataFormat();
		styleNumberNegrito.setDataFormat(format.getFormat("R$ #,#.00"));
		//styleNumberNegrito.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//styleNumberNegrito.setAlignment(CellStyle.ALIGN_CENTER);
		styleNumberNegrito.setFont(f);
	}

	
	public void createStyleTitulo(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);
		
		//styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
		//styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		//styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		//styleHeader.setBorderTop(CellStyle.BORDER_THIN);
		//styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeader.setFont(f);

	}
	
	public void createStyleNegrito(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);
		
		//styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
		//styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
		//styleHeader.setBorderRight(CellStyle.BORDER_THIN);
		//styleHeader.setBorderTop(CellStyle.BORDER_THIN);
		//styleNegrito.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
		//styleNegrito.setAlignment(CellStyle.ALIGN_CENTER);
		styleNegrito.setFont(f);

	}
	
	
	public void createStyleHeader(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);
//		styleHeader.setBorderBottom(CellStyle.BORDER_THIN);
//		styleHeader.setBorderLeft(CellStyle.BORDER_THIN);
//		styleHeader.setBorderRight(CellStyle.BORDER_THIN);
//		styleHeader.setBorderTop(CellStyle.BORDER_THIN);
//		styleHeader.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
//		styleHeader.setAlignment(CellStyle.ALIGN_CENTER);
		styleHeader.setFont(f);

	}

	public void createTitulos(Row rowCabecalho) {
		Integer collNumHeader = 0;
		createCellColHeader("Pagamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Emissão", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº Documento", rowCabecalho, collNumHeader++);
		createCellColHeader("Nota fiscal", rowCabecalho, collNumHeader++);
		createCellColHeader("Subcategoria", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº Lançamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Pagador", rowCabecalho, collNumHeader++);
		createCellColHeader("Recebedor", rowCabecalho, collNumHeader++);
		createCellColHeader("Descrição", rowCabecalho, collNumHeader++);
		createCellColHeader("Projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("Doação", rowCabecalho, collNumHeader++);
		createCellColHeader("Categoria", rowCabecalho, collNumHeader++);
		createCellColHeader("Status", rowCabecalho, collNumHeader++);
		createCellColHeader("Entrada", rowCabecalho, collNumHeader++);
		createCellColHeader("Saída", rowCabecalho, collNumHeader++);
	}

	public void createConteudo(RelatorioExecucao relatorio, Row row) {
		int cellnum = 0;
		createCellCol(relatorio.getPagamento(), row, cellnum++);
		createCellCol(relatorio.getEmissao(), row, cellnum++);
		createCellCol(relatorio.getDoc(), row, cellnum++);
		createCellCol(relatorio.getNotaFiscal(), row, cellnum++);
		createCellCol(relatorio.getCategoriaDespesa(), row, cellnum++);
		createCellCol(relatorio.getNumeroLancamento(), row, cellnum++);
		createCellCol(relatorio.getPagador(), row, cellnum++);
		createCellCol(relatorio.getRecebedor(), row, cellnum++);	
		createCellCol(relatorio.getDescricao(), row, cellnum++);
		createCellCol(relatorio.getProjeto(), row, cellnum++);
		createCellCol(relatorio.getDoacao(), row, cellnum++);
		createCellCol(relatorio.getRubrica(), row, cellnum++);
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
	
	public void createCellColNegrito(String texto, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(styleNegrito);
		cell.setCellValue(texto);
	}

	public void createCellCol(Double valor, Row row, Integer col) {
		if (valor.longValue() == 0.0)
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
