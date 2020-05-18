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
import model.LancamentoAuxiliar;
import model.Projeto;
import model.ProjetoRubrica;
import service.OrcamentoService;
import service.ProjetoService;

public class GeradorRelatorioLinhaOrcamentaria implements Serializable {

	private WritableCellFormat timesBoldUnderline;
	private WritableCellFormat times;
	private WritableCellFormat numberFormat;
	private String inputArquivo;
	private @Inject OrcamentoService orcService;
	private int colConteudoLinhaOrcamentaria;

	public void setOutputFile(String inputArquivo) {
		this.inputArquivo = inputArquivo;
	}

	// ********************************* API MAIS INTUITIVA DO EXCEL

	private HSSFWorkbook workbook;
	private CellStyle styleNumber; // = workbook.createCellStyle();
	private CellStyle styleHeader;
	private CellStyle styleTituloGeral;
	private DataFormat format;// = workbook.createDataFormat();
	private CellStyle styleNegrito;

	/**
	 * 
	 * SIGLA RELATÓRIO PORL = Orçamento inicial, excução financeira (entrada e
	 * saídas) e saldo de cada linha orçamentária de projeto
	 * 
	 * Informações de linha orçamentária
	 * 
	 * - Código do projeto - Projeto - Localidade do projeto - Componente -
	 * Subcomponente - Requisitos de doação - Cadeia produtiva
	 * 
	 */

	public void gerarRelatorioPORL(Filtro filtro, ProjetoService service)
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

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellStyle(styleHeader);
		cell.setCellValue("Orçado x Realizado");

		BigDecimal totalEntrada = BigDecimal.ZERO;
		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal totalOrcado = BigDecimal.ZERO;
		BigDecimal saldo = BigDecimal.ZERO;

		BigDecimal totalEntradaLinha = BigDecimal.ZERO;
		BigDecimal totalSaidaLinha = BigDecimal.ZERO;
		BigDecimal saldoLinha = BigDecimal.ZERO;

		BigDecimal totalLinha = BigDecimal.ZERO;

		List<Projeto> projetosAux = service.getProjetosFilter(filtro);

		Row linha = sheetOrcamento.createRow(rownum++);

		linha = sheetOrcamento.createRow(rownum++);

		createTituloPORL(linha, filtro);

		// createCellColNegrito("Linha orçamentária", linha, 0);
		// createCellColNegrito("Orçamento", linha, 1);
		// createCellColNegrito("Entradas", linha, 2);
		// createCellColNegrito("Saídas", linha, 3);
		// createCellColNegrito("Saldo", linha, 4);

		for (Projeto projeto : projetosAux) {

			filtro.setProjetoId(projeto.getId());
			if (filtro.getDataInicio() == null)
				filtro.setDataInicio(projeto.getDataInicio());
			if (filtro.getDataFinal() == null)
				filtro.setDataFinal(projeto.getDataFinal());

			filtro.setTarifado(projeto.getTarifado());
			// linha = sheetOrcamento.createRow(rownum++);

			totalOrcado = BigDecimal.ZERO;
			totalEntrada = BigDecimal.ZERO;
			totalSaida = BigDecimal.ZERO;
			saldo = BigDecimal.ZERO;

			totalLinha = BigDecimal.ZERO;

			List<ProjetoRubrica> rubricasDeProjeto = service.getRubricaDeProjeto(filtro.getProjetoId(), filtro);
			filtro.setRubricas(new ArrayList<>());
			filtro.setRubricas(rubricasDeProjeto);

			for (ProjetoRubrica projetoRubrica : rubricasDeProjeto) {

				// ProjetoRubrica projetoRubrica =
				// service.getProjetoRubricaByIdDetail(rubrica.getId());

				totalEntradaLinha = BigDecimal.ZERO;
				totalSaidaLinha = BigDecimal.ZERO;

				linha = sheetOrcamento.createRow(rownum++);

				// createCellCol(projetoRubrica.getNomeComponente() + "/" +
				// projetoRubrica.getNomeCategoria(), linha, 0);
				//
				// createCellColTotais(projetoRubrica.getValor().doubleValue(), linha, 1);

				createConteudoPORL(linha, filtro, projetoRubrica);

				totalOrcado = totalOrcado.add(projetoRubrica.getValor());
				saldoLinha = projetoRubrica.getValor();

				totalLinha = totalLinha.add(projetoRubrica.getValor());

				filtro.setRubricaID(projetoRubrica.getId());
				// List<LancamentoAuxiliar> lancamentos =
				// service.getExtratoOrcamentoExecutadoLinhaOrcamentaria(filtro);
				List<LancamentoAuxiliar> lancamentos = service.getRelacaoDespesasLinhaOrcamentaria(filtro);

				for (LancamentoAuxiliar relatorio : lancamentos) {
					// Row row = sheetOrcamento.createRow(rownum++);
					totalEntradaLinha = totalEntradaLinha.add(relatorio.getEntrada());
					totalSaidaLinha = totalSaidaLinha.add(relatorio.getSaida());

					// totalEntrada = totalEntrada.add(relatorio.getEntrada());
					// totalSaida = totalSaida.add(relatorio.getSaida());
					// createConteudoOrcamentoExecutado(relatorio, row, filtro);
				}

				saldoLinha = saldoLinha.add(totalEntradaLinha).subtract(totalSaidaLinha);

				if (contains("entrada", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
						|| filtro.getColunasRelatorio().length == 0)
					createCellColTotais(totalEntradaLinha.doubleValue(), linha, colConteudoLinhaOrcamentaria++);
				if (contains("saida", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
						|| filtro.getColunasRelatorio().length == 0)
					createCellColTotais(totalSaidaLinha.doubleValue(), linha, colConteudoLinhaOrcamentaria++);
				if (contains("saldo", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
						|| filtro.getColunasRelatorio().length == 0)
					createCellColTotais(saldoLinha.doubleValue(), linha, colConteudoLinhaOrcamentaria++);
				if (contains("stt", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
						|| filtro.getColunasRelatorio().length == 0)
					createCellCol(projeto.getAtivo() ? "ATIVADO" : "DESATIVADO", linha, colConteudoLinhaOrcamentaria++);

				totalEntrada = totalEntrada.add(totalEntradaLinha);
				totalSaida = totalSaida.add(totalSaidaLinha);
				saldo = saldo.add(saldoLinha);

			}

			BigDecimal resto = BigDecimal.ZERO;

			resto = resto.add(projeto.getValor().subtract(totalLinha));
			if (resto.compareTo(BigDecimal.ZERO) > 0) {
				linha = sheetOrcamento.createRow(rownum++);
				if (filtro.getColunasRelatorio().length > 0) {
					contentProjetoCustumizedPORL(linha, projeto, filtro, resto);
				} else {
					contentProjetoDefaultPORL(linha, projeto);
				}
			}
		}

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		dlwd.downloadFile("Orçado x Executado" + new Date() + ".xls", new File(inputArquivo),
				"application/vnd.ms-excel", FacesContext.getCurrentInstance());

	}

	public void createTituloPORL(Row linha, Filtro filtro) {
		if (filtro.getColunasRelatorio().length > 0) {
			colCustumizedPORL(linha, filtro);
		} else {
			colDefaultPORL(linha);
		}
	}

	public void colDefaultProjetoPORL(Row linha) {

		int coll = 0;

		createCellColNegrito("Gestão", linha, coll++);
		// createCellColNegrito("Código do plano", linha, coll++);
		createCellColNegrito("Nome do plano", linha, coll++);
		// createCellColNegrito("Localidade do projeto", linha, coll++);
		createCellColNegrito("Cadeia", linha, coll++);
		createCellColNegrito("Fonte", linha, coll++);
		createCellColNegrito("Docao", linha, coll++);
		createCellColNegrito("Código projeto", linha, coll++);
		createCellColNegrito("Nome projeto", linha, coll++);
		createCellColNegrito("Componente", linha, coll++);
		createCellColNegrito("SubComponente", linha, coll++);
		createCellColNegrito("Linha orçamentária", linha, coll++);
		createCellColNegrito("Orçamento", linha, coll++);
		createCellColNegrito("Entradas", linha, coll++);
		createCellColNegrito("Saídas", linha, coll++);
		createCellColNegrito("Saldo", linha, coll++);
		createCellColNegrito("Status projeto", linha, coll++);

	}

	public void colCustumizedProjetoPORL(Row linha, Filtro filtro) {
		int coll = 0;
		if (contains("gestao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Gestão", linha, coll++);
		if (contains("code_plano", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Código do plano", linha, coll++);
		if (contains("nome_plano", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Nome do plano", linha, coll++);
		if (contains("loc_projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Localidade do projeto", linha, coll++);
		if (contains("cadeia", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Cadeia", linha, coll++);
		if (contains("fonte", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Fonte", linha, coll++);
		if (contains("doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Doacao", linha, coll++);
		if (contains("code_projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Código projeto", linha, coll++);
		if (contains("projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Nome projeto", linha, coll++);
		if (contains("componente", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Componente", linha, coll++);
		if (contains("sub_comp", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("SubComponente", linha, coll++);
		if (contains("req_doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Linha orçamentária", linha, coll++);
		if (contains("orcado", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Orçamento", linha, coll++);
		if (contains("entrada", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Entradas", linha, coll++);
		if (contains("saida", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Saídas", linha, coll++);
		if (contains("saldo", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Saldo", linha, coll++);
		if (contains("stt", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			;
		createCellColNegrito("Status", linha, coll++);
	}

	public void colDefaultPORL(Row linha) {

		int coll = 0;

		createCellColNegrito("Gestão", linha, coll++);
		// createCellColNegrito("Código do plano", linha, coll++);
		createCellColNegrito("Nome do plano", linha, coll++);
		// createCellColNegrito("Localidade do projeto", linha, coll++);
		createCellColNegrito("Cadeia", linha, coll++);
		createCellColNegrito("Fonte", linha, coll++);
		createCellColNegrito("Docao", linha, coll++);
		createCellColNegrito("Código projeto", linha, coll++);
		createCellColNegrito("Nome projeto", linha, coll++);
		createCellColNegrito("Componente", linha, coll++);
		createCellColNegrito("SubComponente", linha, coll++);
		createCellColNegrito("Linha orçamentária", linha, coll++);
		createCellColNegrito("Orçamento", linha, coll++);
		createCellColNegrito("Entradas", linha, coll++);
		createCellColNegrito("Saídas", linha, coll++);
		createCellColNegrito("Saldo", linha, coll++);
		createCellColNegrito("Status projeto", linha, coll++);

	}

	public void colCustumizedPORL(Row linha, Filtro filtro) {
		int coll = 0;
		if (contains("gestao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Gestão", linha, coll++);
		if (contains("code_plano", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Código do plano", linha, coll++);
		if (contains("nome_plano", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Nome do plano", linha, coll++);
		if (contains("loc_projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Localidade do projeto", linha, coll++);
		if (contains("cadeia", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Cadeia", linha, coll++);
		if (contains("fonte", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Fonte", linha, coll++);
		if (contains("doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Doacao", linha, coll++);
		if (contains("code_projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Código projeto", linha, coll++);
		if (contains("projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Nome projeto", linha, coll++);
		if (contains("componente", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Componente", linha, coll++);
		if (contains("sub_comp", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("SubComponente", linha, coll++);
		if (contains("req_doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Linha orçamentária", linha, coll++);
		if (contains("orcado", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Orçamento", linha, coll++);
		if (contains("entrada", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Entradas", linha, coll++);
		if (contains("saida", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Saídas", linha, coll++);
		if (contains("saldo", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColNegrito("Saldo", linha, coll++);
		if (contains("stt", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			;
		createCellColNegrito("Status", linha, coll++);
	}

	public void createConteudoPORL(Row linha, Filtro filtro, ProjetoRubrica projetoRubrica) {
		if (filtro.getColunasRelatorio().length > 0) {
			contentCustumizedPORL(linha, projetoRubrica, filtro);
		} else {
			contentDefaultPORL(linha, projetoRubrica);
		}
	}

	public void contentProjetoDefaultPORL(Row linha, Projeto projeto) {

		colConteudoLinhaOrcamentaria = 0;
		createCellCol(projeto.getNomeGestao(), linha, colConteudoLinhaOrcamentaria++);
		// createCellCol(projetoRubrica.getCodigoPlano(), linha,
		// colConteudoLinhaOrcamentaria++);
		createCellCol(projeto.getPlano(), linha, colConteudoLinhaOrcamentaria++);
		// createCellCol(projetoRubrica.getLocalProjeto(), linha,
		// colConteudoLinhaOrcamentaria++);
		createCellCol(projeto.getNomeCadeia(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol("Não informado", linha, colConteudoLinhaOrcamentaria++);
		createCellCol("Não informado", linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projeto.getCodigo(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projeto.getNome(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projeto.getNomeComponente(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projeto.getNomeSubComponente(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol("Não informado", linha, colConteudoLinhaOrcamentaria++);
		createCellColTotais(projeto.getFaltaEmpenhar().doubleValue(), linha, colConteudoLinhaOrcamentaria++);
		createCellColTotais(new BigDecimal(0).doubleValue(), linha, colConteudoLinhaOrcamentaria++);
		createCellColTotais(new BigDecimal(0).doubleValue(), linha, colConteudoLinhaOrcamentaria++);
		createCellColTotais(projeto.getFaltaEmpenhar().doubleValue(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projeto.getAtivo() ? "ATIVADO" : "DESATIVADO", linha, colConteudoLinhaOrcamentaria++);

	}

	public void contentProjetoCustumizedPORL(Row linha, Projeto projeto, Filtro filtro, BigDecimal valor) {
		colConteudoLinhaOrcamentaria = 0;

		if (contains("gestao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getNomeGestao(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("code_plano", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getCodePlano(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("nome_plano", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getPlano(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("loc_projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getNomeLocalidade(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("cadeia", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getNomeCadeia(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("fonte", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol("Não informado", linha, colConteudoLinhaOrcamentaria++);
		if (contains("doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol("Não informado", linha, colConteudoLinhaOrcamentaria++);
		if (contains("code_projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getCodigo(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getNome(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("componente", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getNomeComponente(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("sub_comp", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getNomeSubComponente(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("req_doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol("Não informado", linha, colConteudoLinhaOrcamentaria++);
		if (contains("orcado", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColTotais(projeto.getFaltaEmpenhar().doubleValue(), linha, colConteudoLinhaOrcamentaria++);

		if (contains("entrada", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColTotais(new BigDecimal(0).doubleValue(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("saida", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColTotais(new BigDecimal(0).doubleValue(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("saldo", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColTotais(projeto.getFaltaEmpenhar().doubleValue(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("stt", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projeto.getAtivo() ? "ATIVADO" : "DESATIVADO", linha, colConteudoLinhaOrcamentaria++);

	}

	public void contentDefaultPORL(Row linha, ProjetoRubrica projetoRubrica) {
		colConteudoLinhaOrcamentaria = 0;
		createCellCol(projetoRubrica.getNomeGestao(), linha, colConteudoLinhaOrcamentaria++);
		// createCellCol(projetoRubrica.getCodigoPlano(), linha,
		// colConteudoLinhaOrcamentaria++);
		createCellCol(projetoRubrica.getNomePlano(), linha, colConteudoLinhaOrcamentaria++);
		// createCellCol(projetoRubrica.getLocalProjeto(), linha,
		// colConteudoLinhaOrcamentaria++);
		createCellCol(projetoRubrica.getNomeCadeia(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projetoRubrica.getNomeFonte(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projetoRubrica.getNomeDoacao(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projetoRubrica.getCodigoProjeto(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projetoRubrica.getNomeProjeto(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projetoRubrica.getNomeComponente(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projetoRubrica.getNomeSubComponente(), linha, colConteudoLinhaOrcamentaria++);
		createCellCol(projetoRubrica.getNomeRequisitoDoador(), linha, colConteudoLinhaOrcamentaria++);
		createCellColTotais(projetoRubrica.getValor().doubleValue(), linha, colConteudoLinhaOrcamentaria++);
	}

	public void contentCustumizedPORL(Row linha, ProjetoRubrica projetoRubrica, Filtro filtro) {
		colConteudoLinhaOrcamentaria = 0;

		if (contains("gestao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getNomeGestao(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("code_plano", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getCodigoPlano(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("nome_plano", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getNomePlano(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("loc_projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getLocalProjeto(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("cadeia", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getNomeCadeia(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("fonte", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getNomeFonte(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getNomeDoacao(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("code_projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getCodigoProjeto(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("projeto", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getNomeProjeto(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("componente", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getNomeComponente(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("sub_comp", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getNomeSubComponente(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("req_doacao", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(projetoRubrica.getNomeRequisitoDoador(), linha, colConteudoLinhaOrcamentaria++);
		if (contains("orcado", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColTotais(projetoRubrica.getValor().doubleValue(), linha, colConteudoLinhaOrcamentaria++);

	}

	public void gerarXLSdeOrcamentoExecutadoLinhaOrcamentaria(Filtro filtro, ProjetoService service)
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

		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		Row rowMergin = sheetOrcamento.createRow((short) 0);
		Cell cell = rowMergin.createCell((short) 0);
		cell.setCellStyle(styleHeader);
		cell.setCellValue("Orçado x Realizado");

		BigDecimal totalEntrada = BigDecimal.ZERO;
		BigDecimal totalSaida = BigDecimal.ZERO;
		BigDecimal totalOrcado = BigDecimal.ZERO;
		BigDecimal saldo = BigDecimal.ZERO;

		BigDecimal totalEntradaLinha = BigDecimal.ZERO;
		BigDecimal totalSaidaLinha = BigDecimal.ZERO;
		BigDecimal saldoLinha = BigDecimal.ZERO;

		List<Projeto> projetosAux = service.getProjetosFilter(filtro);
		for (Projeto projeto : projetosAux) {

			filtro.setProjetoId(projeto.getId());
			if (filtro.getDataInicio() == null)
				filtro.setDataInicio(projeto.getDataInicio());
			if (filtro.getDataFinal() == null)
				filtro.setDataFinal(projeto.getDataFinal());

			filtro.setTarifado(projeto.getTarifado());

			rownum++;
			Row linha = sheetOrcamento.createRow(rownum++);

			createCellColNegrito("Projeto:", linha, 0);
			createCellCol(projeto.getNome(), linha, 1);

			linha = sheetOrcamento.createRow(rownum++);

			createCellColNegrito("Vigência:", linha, 0);
			createCellCol(sdf.format(projeto.getDataInicio()) + " a " + sdf.format(projeto.getDataFinal()), linha, 1);

			linha = sheetOrcamento.createRow(rownum++);

			createCellColNegrito("Saldo inicial:", linha, 0);
			createCellCol(projeto.getValor().doubleValue(), linha, 1);

			linha = sheetOrcamento.createRow(rownum++);

			int linhaSaldoFinal = rownum;

			rownum++;

			totalOrcado = BigDecimal.ZERO;
			totalEntrada = BigDecimal.ZERO;
			totalSaida = BigDecimal.ZERO;
			saldo = BigDecimal.ZERO;

			// TODO: FILTRAR AS CONDIÇÕES DE FONTE
			List<ProjetoRubrica> rubricasDeProjeto = service.getRubricaDeProjeto(filtro.getProjetoId(), filtro);
			filtro.setRubricas(new ArrayList<>());
			filtro.setRubricas(rubricasDeProjeto);

			linha = sheetOrcamento.createRow(rownum++);
			createCellColNegrito("Linha orçamentária", linha, 0);
			createCellColNegrito("Orçamento", linha, 1);
			createCellColNegrito("Entradas", linha, 2);
			createCellColNegrito("Saídas", linha, 3);
			createCellColNegrito("Saldo", linha, 4);

			for (ProjetoRubrica rubrica : rubricasDeProjeto) {

				ProjetoRubrica projetoRubrica = service.getProjetoRubricaByIdDetail(rubrica.getId());

				totalEntradaLinha = BigDecimal.ZERO;
				totalSaidaLinha = BigDecimal.ZERO;

				linha = sheetOrcamento.createRow(rownum++);

				createCellCol(projetoRubrica.getNomeComponente() + "/" + projetoRubrica.getNomeCategoria(), linha, 0);
				createCellColTotais(projetoRubrica.getValor().doubleValue(), linha, 1);

				totalOrcado = totalOrcado.add(projetoRubrica.getValor());
				saldoLinha = projetoRubrica.getValor();

				filtro.setRubricaID(projetoRubrica.getId());
				// List<LancamentoAuxiliar> lancamentos =
				// service.getExtratoOrcamentoExecutadoLinhaOrcamentaria(filtro); // TODO:
				// Extrato
				// por
				// linha

				List<LancamentoAuxiliar> lancamentos = service.getRelacaoDespesasLinhaOrcamentaria(filtro);

				for (LancamentoAuxiliar relatorio : lancamentos) {
					// Row row = sheetOrcamento.createRow(rownum++);
					totalEntradaLinha = totalEntradaLinha.add(relatorio.getEntrada());
					totalSaidaLinha = totalSaidaLinha.add(relatorio.getSaida());

					// totalEntrada = totalEntrada.add(relatorio.getEntrada());
					// totalSaida = totalSaida.add(relatorio.getSaida());
					// createConteudoOrcamentoExecutado(relatorio, row, filtro);
				}

				saldoLinha = saldoLinha.add(totalEntradaLinha).subtract(totalSaidaLinha);

				createCellColTotais(totalEntradaLinha.doubleValue(), linha, 2);
				createCellColTotais(totalSaidaLinha.doubleValue(), linha, 3);
				createCellColTotais(saldoLinha.doubleValue(), linha, 4);

				totalEntrada = totalEntrada.add(totalEntradaLinha);
				totalSaida = totalSaida.add(totalSaidaLinha);
				saldo = saldo.add(saldoLinha);

			}

			linha = sheetOrcamento.createRow(rownum++);
			Row rowSaldoFinal = sheetOrcamento.createRow(linhaSaldoFinal - 1);

			createCellColNegrito("Totais:", linha, 0);
			createCellColTotais(totalOrcado.doubleValue(), linha, 1);
			createCellColTotais(totalEntrada.doubleValue(), linha, 2);
			createCellColTotais(totalSaida.doubleValue(), linha, 3);
			createCellColTotais(saldo.doubleValue(), linha, 4);

			createCellColNegrito("Saldo final em: " + sdf.format(filtro.getDataFinal()), rowSaldoFinal, 0);
			createCellColTotais(projeto.getValor().subtract(totalSaida).add(totalEntrada).doubleValue(), rowSaldoFinal,
					1);

			projeto.getValor();

			filtro.setRubricaID(null);
			filtro.setProjetoId(projeto.getId());
			List<LancamentoAuxiliar> lancamentos = service.getRelacaoDespesasLinhaOrcamentaria(filtro); // TODO :
																										// Extrato
																										// por
																										// projeto

			rownum++;
			linha = sheetOrcamento.createRow(rownum++);

			createTitulosdeOrcamentoExectuado(linha, filtro);

			for (LancamentoAuxiliar relatorio : lancamentos) {
				if (relatorio.getDataPagamento().after(filtro.getDataInicio())) {
					linha = sheetOrcamento.createRow(rownum++);
					createConteudoOrcamentoExecutado(relatorio, linha, filtro);
				}
			}

			rownum++;

		}

		FileOutputStream out = new FileOutputStream(new File(inputArquivo));
		workbook.write(out);
		out.close();

		DownloadFileHandler dlwd = new DownloadFileHandler();
		dlwd.downloadFile("Orçado x Executado" + new Date() + ".xls", new File(inputArquivo),
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

	public void defaultTitle(Row rowCabecalho, Filtro filtro) {

		Integer collNumHeader = 0;

		createCellColHeader("Tipo", rowCabecalho, collNumHeader++);
		createCellColHeader("Pagamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Emissão", rowCabecalho, collNumHeader++);
		createCellColHeader("Nº Lançamento", rowCabecalho, collNumHeader++);
		createCellColHeader("Nota fiscal", rowCabecalho, collNumHeader++);
		createCellColHeader("Recebedor", rowCabecalho, collNumHeader++);
		createCellColHeader("Descrição", rowCabecalho, collNumHeader++);
		createCellColHeader("Status", rowCabecalho, collNumHeader++);
		createCellColHeader("Fonte", rowCabecalho, collNumHeader++);
		createCellColHeader("Doação", rowCabecalho, collNumHeader++);
		createCellColHeader("Componente", rowCabecalho, collNumHeader++);
		createCellColHeader("SubComponente", rowCabecalho, collNumHeader++);
		createCellColHeader("Código", rowCabecalho, collNumHeader++);
		createCellColHeader("Projeto", rowCabecalho, collNumHeader++);
		createCellColHeader("Requisito de doação", rowCabecalho, collNumHeader++);
		createCellColHeader("Entrada", rowCabecalho, collNumHeader++);
		createCellColHeader("Saída", rowCabecalho, collNumHeader++);

	}

	public void customizedTitle(Row rowCabecalho, Filtro filtro) {

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
		if (contains("stt", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Status", rowCabecalho, collNumHeader++);
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
		if (contains("entrada", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Entrada", rowCabecalho, collNumHeader++);
		if (contains("saida", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellColHeader("Saída", rowCabecalho, collNumHeader++);

	}

	public void createTitulosdeOrcamentoExectuado(Row rowCabecalho, Filtro filtro) {
		if (filtro.getColunasRelatorio().length > 0) {
			customizedTitle(rowCabecalho, filtro);
		} else {
			defaultTitle(rowCabecalho, filtro);
		}
	}

	public void defaultContent(LancamentoAuxiliar relatorio, Row row, Filtro filtro) {

		int cellnum = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

		createCellCol(relatorio.getTipo(), row, cellnum++);
		createCellCol(sdf.format(relatorio.getDataPagamento()), row, cellnum++);
		createCellCol(sdf.format(relatorio.getDataEmissao()), row, cellnum++);
		createCellCol(relatorio.getId().toString(), row, cellnum++);
		createCellCol(relatorio.getNotaFiscal(), row, cellnum++);
		createCellCol(relatorio.getContaRecebedorLbl(), row, cellnum++);
		createCellCol(relatorio.getDescricao(), row, cellnum++);
		createCellCol(relatorio.getStatus(), row, cellnum++);
		createCellCol(relatorio.getFonte(), row, cellnum++);
		createCellCol(relatorio.getDoacao(), row, cellnum++);
		createCellCol(relatorio.getComponente(), row, cellnum++);
		createCellCol(relatorio.getSubComponente(), row, cellnum++);
		createCellCol(relatorio.getCodigo(), row, cellnum++);
		createCellCol(relatorio.getNomeProjeto(), row, cellnum++);
		createCellCol(relatorio.getRequisitoDeDoador(), row, cellnum++);
		createCellCol(relatorio.getEntrada().doubleValue(), row, cellnum++);
		createCellCol(relatorio.getSaida().doubleValue(), row, cellnum++);
	}

	public void customizedContent(LancamentoAuxiliar relatorio, Row row, Filtro filtro) {
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
		if (contains("stt", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getStatus(), row, cellnum++);
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

		if (contains("entrada", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getEntrada().doubleValue(), row, cellnum++);
		if (contains("saida", filtro.getColunasRelatorio()) || filtro.getColunasRelatorio() == null
				|| filtro.getColunasRelatorio().length == 0)
			createCellCol(relatorio.getSaida().doubleValue(), row, cellnum++);
	}

	public void createConteudoOrcamentoExecutado(LancamentoAuxiliar relatorio, Row row, Filtro filtro) {
		if (filtro.getColunasRelatorio().length > 0) {
			customizedContent(relatorio, row, filtro);
		} else {
			defaultContent(relatorio, row, filtro);
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
