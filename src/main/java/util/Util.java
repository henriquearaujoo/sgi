package util;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.*;

import javax.inject.Inject;

import model.Coordenadoria;
import model.Gestao;
import model.Lancamento;
import model.UnidadeConservacao;
import service.CompraService;
import service.HomeService;
import service.SolicitacaoPagamentoService;

public class Util implements Serializable{
	
	
	
	private static final Boolean MENU_SV = false;
	private static final Boolean MENU_SC = false;
	
	@Inject
	private static HomeService homeService;
	
	public Util(){}
	
	
	public static String nullEmptyObjectUtil(Object obj){
		return obj == null ? "" : obj.toString();
	}
	
	public static Boolean nullEmptyBooleanObjectUtil(Object obj){
		return obj == null ? false : Boolean.valueOf(obj.toString());
	}
	
	public static String getNullValue(String value, String valueIfNull) {
		if (value == null) {
			return valueIfNull;
		} else {
			return value;
		}
	}

	public static Long getNullValue(Object value, Long valueIfNull) {
		if (value == null) {
			return valueIfNull;
		} else {
			return new Long(value.toString());
		}
	}
	
	public static BigDecimal getNullValue(BigDecimal value) {
		if (value == null) {
			return BigDecimal.ZERO;
		} else {
			return  value;
		}
	}
	
	public static String getNullValue(Object value, String valueIfNull) {
		if (value == null) {
			return valueIfNull;
		} else {
			return value.toString();
		}
	}
	
	public static String decimal(double numero) {
		Locale locale = new Locale("pt", "BR");
		DecimalFormat form = new DecimalFormat("###,###,##0.00", new DecimalFormatSymbols(locale));
		return String.valueOf(form.format(numero));
	}
	
	
	//Colunas padrões orçado x realizado
	public static List<Coluna> getColunasPadraoReportOR() {
		List<Coluna> list = new ArrayList<Coluna>();
		Coluna col = new Coluna("Tipo", "tipo");
		list.add(col);
		col = new Coluna("Dt de pagamento", "dt_pagamento");
		list.add(col);
		col = new Coluna("Dt de emissão", "dt_emissao");
		list.add(col);
		col = new Coluna("Nº Documento", "doc");
		list.add(col);
		col = new Coluna("Nº Lançamento", "n_lanc");
		list.add(col);
		col = new Coluna("Nota fiscal", "nf");
		list.add(col);
		col = new Coluna("Pagador", "pagador");
		list.add(col);
		col = new Coluna("Recebedor", "recebedor");
		list.add(col);
		col = new Coluna("Descrição", "desc");
		list.add(col);
		col = new Coluna("Fonte", "fonte");
		list.add(col);
		col = new Coluna("Doação", "doacao");
		list.add(col);
		col = new Coluna("Código", "codigo");
		list.add(col);
		col = new Coluna("Projeto", "projeto");
		list.add(col);
		col = new Coluna("Componente", "componente");
		list.add(col);
		col = new Coluna("Subcomponente", "sub_comp");
		list.add(col);
		col = new Coluna("Requisitos de doação", "req_doacao");
		list.add(col);
		col = new Coluna("Status", "stt");
		list.add(col);
		col = new Coluna("Entrada", "entrada");
		list.add(col);
		col = new Coluna("Saída", "saida");
		list.add(col);
		
		
		return list;
	}
	
	
	public static List<Coluna> getColunasReportLINHA() {
		List<Coluna> list = new ArrayList<Coluna>();
		Coluna col = new Coluna("Gestão", "gestao");
		list.add(col);
		col = new Coluna("Código do plano", "code_plano");
		list.add(col);
		col = new Coluna("Nome do plano", "nome_plano");
		list.add(col);
		col = new Coluna("Localidade do projeto", "loc_projeto");
		list.add(col);
		col = new Coluna("Cadeia produtiva", "cadeia");
		list.add(col);
		col = new Coluna("Fonte", "fonte");
		list.add(col);
		col = new Coluna("Doação", "doacao");
		list.add(col);
		col = new Coluna("Código do projeto", "code_projeto");
		list.add(col);
		col = new Coluna("Projeto", "projeto");
		list.add(col);
		col = new Coluna("Componente", "componente");
		list.add(col);
		col = new Coluna("Subcomponente", "sub_comp");
		list.add(col);
		col = new Coluna("Requisitos de doação", "req_doacao");
		list.add(col);
		col = new Coluna("Orçado", "orcado");
		list.add(col);
		col = new Coluna("Entrada", "entrada");
		list.add(col);
		col = new Coluna("Saída", "saida");
		list.add(col);
		col = new Coluna("Saldo", "saldo");
		list.add(col);
		col = new Coluna("Status do projeto", "stt");
		list.add(col);
	
		return list;
	}
	
	public void setarParamRegional(Map parametros, Lancamento lancamento){
		String sup = "Técnica-Científica";
		String regional = lancamento.getGestao().getNome();
		String coordenadoria = "Programa Bolsa Floresta";
	
		parametros.put("superintendencia", sup);
		parametros.put("regional", regional);
		parametros.put("coordenadoria", coordenadoria);
	}
	
	public void setarParamCoordenadoria(Map parametros, Lancamento lancamento){
		Coordenadoria coord = (Coordenadoria) lancamento.getGestao();
		String sup = coord.getSuperintendencia().getNome();
		String coordenadoria = coord.getNome();
		parametros.put("superintendencia", sup);
		parametros.put("regional", "");
		parametros.put("coordenadoria", coordenadoria);
	}
	
	public void setarParamSuperintendencia(Map parametros, Lancamento lancamento){
		String sup = lancamento.getGestao().getNome();
		parametros.put("superintendencia", sup);
		parametros.put("regional", "");
		parametros.put("coordenadoria", "");
	}

	
	public void setarDestino(Map parametros, Lancamento lancamento){
		parametros.put("destino_final", lancamento.getLocalidade().getMascara());	
	}
	
	public void setarDestinoComunidade(Map parametros, Lancamento lancamento){
		parametros.put("destino_final", lancamento.getLocalidade().getMascara());
		parametros.put("uc", lancamento.getLocalidade().getUnidadeConservacao().getMascara());
		parametros.put("beneficiario_pbf", lancamento.getLocalidade().getUnidadeConservacao().getNomeAssociacao());
		
	}
	
	public void setarDestinoUC(Map parametros, Lancamento lancamento, CompraService compraService){
		UnidadeConservacao uc = (UnidadeConservacao) compraService.findByIdUnidadeConservacao(lancamento.getLocalidade().getId());
		parametros.put("destino_final", uc.getMascara());
		parametros.put("uc", uc.getMascara());
		parametros.put("beneficiario_pbf", uc.getNomeAssociacao());
		
	}
	
	public void setarDestinoUC(Map parametros, Lancamento lancamento, SolicitacaoPagamentoService service){
		UnidadeConservacao uc = (UnidadeConservacao) service.findByIdUnidadeConservacao(lancamento.getLocalidade().getId());
		parametros.put("destino_final", uc.getMascara());
		parametros.put("uc", uc.getMascara());
		parametros.put("beneficiario_pbf", uc.getNomeAssociacao());
		
	}
	
	public static Boolean validarCPF(String cpf){
		int d1, d2;
		int digito1, digito2, resto;
		int digitoCPF;
		String nDigResult;
		d1 = d2 = 0;
		digito1 = digito2 = resto = 0;
		for (int n_Count = 1; n_Count < cpf.length() -1; n_Count++) {
			digitoCPF = Integer.valueOf(cpf.substring(n_Count -1, n_Count)).intValue();
			//--------- Multiplique a ultima casa por 2 a seguinte por 3 a seguinte por 4 e assim por diante.
			d1 = d1 + ( 11 - n_Count ) * digitoCPF;
			//--------- Para o segundo digito repita o procedimento incluindo o primeiro digito calculado no passo anterior.
			d2 = d2 + ( 12 - n_Count ) * digitoCPF;
		};
		//--------- Primeiro resto da divisão por 11.
		resto = (d1 % 11);
		//--------- Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
		if (resto < 2)
			digito1 = 0;
		else
			digito1 = 11 - resto;
		
		d2 += 2 * digito1;
		//--------- Segundo resto da divisão por 11.
		resto = (d2 % 11);
		//--------- Se o resultado for 0 ou 1 o digito é 0 caso contrário o digito é 11 menos o resultado anterior.
		if (resto < 2)
			digito2 = 0;
		else
			digito2 = 11 - resto;
		//--------- Digito verificador do CPF que está sendo validado.
		String nDigVerific = cpf.substring(cpf.length()-2, cpf.length());
		//--------- Concatenando o primeiro resto com o segundo.
		nDigResult = String.valueOf(digito1) + String.valueOf(digito2);
		//--------- Comparar o digito verificador do cpf com o primeiro resto + o segundo resto.
		return nDigVerific.equals(nDigResult);
	}

	public static boolean isNumeric(String str)  
	{  
	  try  
	  {  
		  double d = Double.parseDouble(str);  
	  }  
	  catch(NumberFormatException nfe)  
	  {  
		  return false;  
	  }
	  
	  return true;  
	}


	public static boolean verificarSolicitacoes(Long idColadorador) {
		return homeService.verificarSolicitacoes(idColadorador);
	}
	
	public static String gerarNovaSenha() {
		String senha = "";
		String[] carct = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f", "g", "h",
				"i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B", "C",
				"D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X",
				"Y", "Z" };
		for (int x = 0; x < 10; x++) {
			int j = (int) (Math.random() * carct.length);
			senha += carct[j];
		}
		return senha;
	}

	public static boolean isCNPJ(String CNPJ) {
		if (CNPJ.equals("00000000000000") || CNPJ.equals("11111111111111") ||
				CNPJ.equals("22222222222222") || CNPJ.equals("33333333333333") ||
				CNPJ.equals("44444444444444") || CNPJ.equals("55555555555555") ||
				CNPJ.equals("66666666666666") || CNPJ.equals("77777777777777") ||
				CNPJ.equals("88888888888888") || CNPJ.equals("99999999999999") ||
				(CNPJ.length() != 14))
			return(false);

		char dig13, dig14;
		int sm, i, r, num, peso;

		try {
			sm = 0;
			peso = 2;
			for (i=11; i>=0; i--) {

				num = (int)(CNPJ.charAt(i) - 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig13 = '0';
			else dig13 = (char)((11-r) + 48);

			sm = 0;
			peso = 2;
			for (i=12; i>=0; i--) {
				num = (int)(CNPJ.charAt(i)- 48);
				sm = sm + (num * peso);
				peso = peso + 1;
				if (peso == 10)
					peso = 2;
			}

			r = sm % 11;
			if ((r == 0) || (r == 1))
				dig14 = '0';
			else dig14 = (char)((11-r) + 48);

			if ((dig13 == CNPJ.charAt(12)) && (dig14 == CNPJ.charAt(13)))
				return(true);
			else return(false);
		} catch (InputMismatchException erro) {
			return(false);
		}
	}

}
