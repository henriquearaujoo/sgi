package util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import jxl.write.WritableCellFormat;
import model.Projeto;
import model.RelatorioAtividade;
import service.OrcamentoService;
import service.ProjetoService;

public class GeradorRelatorioGeralDeProjeto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private WritableCellFormat numberFormat;
	private String inputArquivo;
	private @Inject OrcamentoService orcService;
	private int colConteudoLinhaOrcamentaria;
	private HSSFWorkbook workbook;
	private CellStyle styleNumber;
	private CellStyle styleHeader;
	private CellStyle styleTituloGeral;
	private DataFormat format;
	private CellStyle styleNegrito;

	public void setOutputFile(String inputArquivo) {
		this.inputArquivo = inputArquivo;
	}

	public void gerarRelatorioGeralDeProjetos(Filtro filtro, ProjetoService service)
			throws IOException, NumberFormatException, ParseException {

		int rownum = 3;

		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFSheet sheetOrcamento = workbook.createSheet("Relatório geral de projetos");

		styleNumber = workbook.createCellStyle();
		styleHeader = workbook.createCellStyle();
		styleTituloGeral = workbook.createCellStyle();
		styleNegrito = workbook.createCellStyle();

		createStyleHeader(sheetOrcamento);
		createStyleNumber(sheetOrcamento, workbook);

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellStyle(styleHeader);
		cell.setCellValue("Relatório consolidado");
		Row linha = sheetOrcamento.createRow(rownum++);
		List<Projeto> projetos = service.getProjetosRelatorioGeral(filtro);

		createTitulo(linha, filtro);

		for (Projeto projeto : projetos) {

			filtro.setProjetoId(projeto.getId());
			if (filtro.getDataInicio() == null)
				filtro.setDataInicio(projeto.getDataInicio());
			if (filtro.getDataFinal() == null)
				filtro.setDataFinal(projeto.getDataFinal());

			linha = sheetOrcamento.createRow(rownum++);

			createConteudo(projeto, linha, filtro);

		}

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();
		DownloadFileHandler dlwd = new DownloadFileHandler();
		dlwd.downloadFile("Relatorio de atividades" + new Date() + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void createCellColNegrito(String texto, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(styleHeader);
		cell.setCellValue(texto);
	}

	public void createStyleNumber(HSSFSheet sheetOrcamento, HSSFWorkbook workbook) {
		format = workbook.createDataFormat();
		styleNumber.setDataFormat(format.getFormat("R$ #,#.00"));
	}

	public void createStyleHeader(HSSFSheet sheetOrcamento) {
		Font f = sheetOrcamento.getWorkbook().createFont();
		f.setBold(true);
		styleHeader.setFont(f);
	}

	public void defaultTitle(Row rowCabecalho, Filtro filtro) {

		Integer collNumHeader = 0;

		createCellColHeader("Projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("Gestao", rowCabecalho, collNumHeader++);
		createCellColHeader("Local do projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("orçado", rowCabecalho, collNumHeader++);
		createCellColHeader("Executado", rowCabecalho, collNumHeader++);
		createCellColHeader("Saldo", rowCabecalho, collNumHeader++);
		createCellColHeader("% Financeiro", rowCabecalho, collNumHeader++);
		createCellColHeader("Atividades planejadas", rowCabecalho, collNumHeader++);
		createCellColHeader("Atividades executadas", rowCabecalho, collNumHeader++);
		createCellColHeader("Pendentes", rowCabecalho, collNumHeader++);
		createCellColHeader("% Físico", rowCabecalho, collNumHeader++);
		createCellColHeader("Atividades não planejadas", rowCabecalho, collNumHeader++);

	}

	public void customizedTitle(Row rowCabecalho, Filtro filtro) {

		Integer collNumHeader = 0;

		if (contains("atividade", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Atividade", rowCabecalho, collNumHeader++);
		if (contains("responsavel", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Responsável", rowCabecalho, collNumHeader++);
		if (contains("localizador", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Localizador", rowCabecalho, collNumHeader++);
		if (contains("orcado", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Valor orçado", rowCabecalho, collNumHeader++);
		if (contains("executado", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Valor executado", rowCabecalho, collNumHeader++);
		if (contains("saldo", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Saldo", rowCabecalho, collNumHeader++);
		if (contains("percent_financeiro", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Percentual Financeiro", rowCabecalho, collNumHeader++);
		if (contains("qtd_planejado", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Atividade planejadas", rowCabecalho, collNumHeader++);
		if (contains("qtd_executado", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Atividades executadas", rowCabecalho, collNumHeader++);
		if (contains("pendente", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Pendentes", rowCabecalho, collNumHeader++);
		if (contains("percent_fisico", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Percentual Físico", rowCabecalho, collNumHeader++);

	}

	public void createTitulo(Row rowCabecalho, Filtro filtro) {

		if (filtro.getColunasRelatorio() != null && filtro.getColunasRelatorio().length > 0) {
			customizedTitle(rowCabecalho, filtro);
		} else {
			defaultTitle(rowCabecalho, filtro);
		}
	}

	SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

	public void defaultContent(Projeto projeto, Row row, Filtro filtro) {

		int cellnum = 0;

		createCellCol(projeto.getNome(), row, cellnum++);

		createCellCol(projeto.getNomeGestao(), row, cellnum++);

		createCellCol(projeto.getNomeLocalidade(), row, cellnum++);

		createCellCol(projeto.getValor().doubleValue(), row, cellnum++);

		createCellCol(projeto.getTotalVLlExecutado().doubleValue(), row, cellnum++);

		createCellCol(projeto.getTotalSaldo().doubleValue(), row, cellnum++);

		createCellCol(String.format("%.2f",
				!projeto.getTotalPercentualFinanceiro().isNaN() ? projeto.getTotalPercentualFinanceiro() : 0) + "%",
				row, cellnum++);

		createCellCol(projeto.getTotalQtdPlanejada().intValue() + "", row, cellnum++);

		createCellCol(projeto.getTotalQtdExecutada().intValue() + "", row, cellnum++);

		createCellCol(projeto.getTotalPendentes().intValue() + "", row, cellnum++);

		createCellCol(
				String.format("%.2f",
						!projeto.getTotalPercentualFisico().isNaN() ? projeto.getTotalPercentualFisico() : 0) + "%",
				row, cellnum++);

		createCellCol(projeto.getTotalQtdNaoPlanejada().intValue() + "", row, cellnum++);

	}

	public void customizedContent(RelatorioAtividade atividade, Row row, Filtro filtro) {
		int cellnum = 0;

		// SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		// if (contains("tipo", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getTipo(), row, cellnum++);
		// if (contains("dt_pagamento", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(sdf.format(relatorio.getDataPagamento()), row, cellnum++);
		// if (contains("dt_emissao", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(sdf.format(relatorio.getDataEmissao()), row, cellnum++);
		// if (contains("doc", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getNumeroDocumento(), row, cellnum++);
		// if (contains("n_lanc", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getId().toString(), row, cellnum++);
		// if (contains("nf", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getNotaFiscal(), row, cellnum++);
		// if (contains("pagador", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getContaPagadorLbl(), row, cellnum++);
		// if (contains("recebedor", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getContaRecebedorLbl(), row, cellnum++);
		// if (contains("desc", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getDescricao(), row, cellnum++);
		// if (contains("stt", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getStatus(), row, cellnum++);
		// if (contains("fonte", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getFonte(), row, cellnum++);
		// if (contains("doacao", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getDoacao(), row, cellnum++);
		// if (contains("codigo", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getCodigo(), row, cellnum++);
		// if (contains("projeto", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getNomeProjeto(), row, cellnum++);
		// if (contains("componente", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getComponente(), row, cellnum++);
		// if (contains("sub_comp", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getSubComponente(), row, cellnum++);
		// if (contains("req_doacao", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getRequisitoDeDoador(), row, cellnum++);
		//
		// if (contains("entrada", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getEntrada().doubleValue(), row, cellnum++);
		// if (contains("saida", filtro.getColunasRelatorio()) ||
		// filtro.getColunasRelatorio() == null
		// || filtro.getColunasRelatorio().length == 0)
		// createCellCol(relatorio.getSaida().doubleValue(), row, cellnum++);
	}

	public void createConteudo(Projeto projeto, Row row, Filtro filtro) {
		if (filtro.getColunasRelatorio() != null && filtro.getColunasRelatorio().length > 0) {
			// customizedContent(atividade, row, filtro);
		} else {
			defaultContent(projeto, row, filtro);
		}
	}

	public boolean contains(String elemento, String[] lista) {
		for (String string : lista) {
			if (elemento.equals(string)) {
				return true;
			}
		}
		return false;
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
		// if (valor.longValue() == 0.0)
		// return;

		Cell cell = row.createCell(col);
		cell.setCellStyle(styleNumber);

		cell.setCellValue(valor);
	}

	public void createCellColTotais(Double valor, Row row, Integer col) {
		Cell cell = row.createCell(col);
		cell.setCellStyle(styleNumber);
		cell.setCellValue(valor);
	}

}
