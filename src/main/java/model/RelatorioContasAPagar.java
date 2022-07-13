package model;

import util.Util;

import java.math.BigDecimal;

public class RelatorioContasAPagar {

	private String emissao;
	private String pagamento;
	private String doc;
	private String pagador;
	private String recebedor;
	private String Doacao;
	private String projeto;
	private String fonte;
	private String acao;
	private String status;
	private String parcela;
	private Double entrada = 0.0;
	private Double saida = 0.0;
	private Double adiantamento = 0.0;
	private Double prestacaoConta = 0.0;
	private Double reembolso = 0.0;
	private Double saldo = 0.0;
	private String despesaReceita = "";
	private String descricao = "";
	private String categoriaDespesa = "";
	private String notaFiscal = "";
	private String rubrica = "";
	private String tipo;
	private String componente;
	private String subcomponente;
	private String sinalizador;
	private String numeroParcela;
	private String localidade;
	private String localidadeUC;
	private String localidadeProjeto;
	
	private String tipoDocumento;
	private BigDecimal valor = BigDecimal.ZERO;

	

	private boolean despesa;

	private String tipoLancamento = "";

	private String numeroLancamento;

	private String condicaoPagamento = "";

	private String gestao = "";

	private String programa = "";

	private String sv = "";

	private String sc = "";

	private String cpfcnpj = "";
	
	private String razaoSocial = "";

	private String gerencia = "";

	private String agenda = "";

	public String getGerencia() {
		return gerencia;
	}

	public void setGerencia(String subPrograma) {
		this.gerencia = subPrograma;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public RelatorioContasAPagar() {
	}

	public RelatorioContasAPagar(Object[] object, Integer idConta) {
		
		
		if (Integer.valueOf(object[14].toString()) == idConta.intValue()) {
			despesa = true;
			despesaReceita = "Despesa";
			this.saida = Double.valueOf(object[8].toString());
		}else{
			despesa = false;
			despesaReceita = "Receita";
			this.entrada = Double.valueOf(object[8].toString());
		}

		this.valor = new BigDecimal(object[8].toString());
		
		this.numeroLancamento = Util.nullEmptyObjectUtil(object[1]);
		this.emissao = Util.nullEmptyObjectUtil(object[2]);
		this.pagamento = Util.nullEmptyObjectUtil(object[3]);
		this.fonte = Util.nullEmptyObjectUtil(object[4]);
		this.projeto = Util.nullEmptyObjectUtil(object[5]);
		this.parcela = object[6].toString() + "/" + object[7].toString();
		this.pagador = Util.nullEmptyObjectUtil(object[9]);
		this.recebedor = Util.nullEmptyObjectUtil(object[10]);
	
		this.status = Util.nullEmptyObjectUtil(object[11]);
		this.doc = Util.nullEmptyObjectUtil(object[16]);
		this.descricao = Util.nullEmptyObjectUtil(object[0]);

		if (object[13] != null){
			switch (object[13].toString()){
				case "rendimento":
					this.tipo = "Rendimento";
					break;
				case "aplicacao_recurso":
					this.tipo = "Aplicação";
					break;
				case "baixa_aplicacao":
					this.tipo = "Baixa";
					break;
				default:
					this.tipo = "Outro";
					break;
			}
		}

	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public String getEmissao() {
		return emissao;
	}
	
	

	public String getRazaoSocial() {
		return razaoSocial;
	}

	public void setRazaoSocial(String razaoSocial) {
		this.razaoSocial = razaoSocial;
	}

	public String getDoacao() {
		return Doacao;
	}

	public void setDoacao(String doacao) {
		Doacao = doacao;
	}

	public void setEmissao(String emissao) {
		this.emissao = emissao;
	}

	public String getPagamento() {
		return pagamento;
	}

	public void setPagamento(String pagamento) {
		this.pagamento = pagamento;
	}

	public String getDoc() {
		return doc;
	}

	public void setDoc(String doc) {
		this.doc = doc;
	}

	public String getPagador() {
		return pagador;
	}

	public void setPagador(String pagador) {
		this.pagador = pagador;
	}

	public String getRecebedor() {
		return recebedor;
	}

	public void setRecebedor(String recebedor) {
		this.recebedor = recebedor;
	}

	public String getProjeto() {
		return projeto;
	}

	public void setProjeto(String projeto) {
		this.projeto = projeto;
	}

	public String getFonte() {
		return fonte;
	}

	public void setFonte(String fonte) {
		this.fonte = fonte;
	}

	public String getAcao() {
		return acao;
	}

	public void setAcao(String acao) {
		this.acao = acao;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Double getEntrada() {
		return entrada;
	}

	public void setEntrada(Double entrada) {
		this.entrada = entrada;
	}

	public Double getSaida() {
		return saida;
	}

	public void setSaida(Double saida) {
		this.saida = saida;
	}

	public Double getAdiantamento() {
		return adiantamento;
	}

	public void setAdiantamento(Double adiantamento) {
		this.adiantamento = adiantamento;
	}

	public Double getPrestacaoConta() {
		return prestacaoConta;
	}

	public void setPrestacaoConta(Double prestacaoConta) {
		this.prestacaoConta = prestacaoConta;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public String getParcela() {
		return parcela;
	}

	public void setParcela(String parcela) {
		this.parcela = parcela;
	}

	public String getNumeroLancamento() {
		return numeroLancamento;
	}

	public void setNumeroLancamento(String numeroLancamento) {
		this.numeroLancamento = numeroLancamento;
	}

	public String getDespesaReceita() {
		return despesaReceita;
	}

	public void setDespesaReceita(String despesaReceita) {
		this.despesaReceita = despesaReceita;
	}

	public Double getReembolso() {
		return reembolso;
	}

	public void setReembolso(Double reembolso) {
		this.reembolso = reembolso;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public String getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public String getCategoriaDespesa() {
		return categoriaDespesa;
	}

	public void setCategoriaDespesa(String categoriaDespesa) {
		this.categoriaDespesa = categoriaDespesa;
	}

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public String getRubrica() {
		return rubrica;
	}

	public void setRubrica(String rubrica) {
		this.rubrica = rubrica;
	}

	public boolean isDespesa() {
		return despesa;
	}

	public void setDespesa(boolean despesa) {
		this.despesa = despesa;
	}

	public String getComponente() {
		return componente;
	}

	public void setComponente(String componente) {
		this.componente = componente;
	}

	public String getSubcomponente() {
		return subcomponente;
	}

	public void setSubcomponente(String subcomponente) {
		this.subcomponente = subcomponente;
	}

	public String getTipoDocumento() {
		return tipoDocumento;
	}

	public void setTipoDocumento(String tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public BigDecimal getValor() {
		return valor;
	}

	public void setValor(BigDecimal valor) {
		this.valor = valor;
	}
	/*
	 * addCaption(sheet, 0, linha, "Emissão"); addCaption(sheet, 1, linha,
	 * "Pagamento"); addCaption(sheet, 2, linha, "Nº doc"); addCaption(sheet, 3,
	 * linha, "Pagador"); addCaption(sheet, 4, linha, "Recebedor");
	 * addCaption(sheet, 5, linha, "Projeto"); addCaption(sheet, 6, linha,
	 * "Fonte"); addCaption(sheet, 7, linha, "Ação"); addCaption(sheet, 8,
	 * linha, "Status"); addCaption(sheet, 9, linha, "Entrada"); // receita
	 * addCaption(sheet, 10, linha, "Saída"); // despesa addCaption(sheet, 10,
	 * linha, "Adiantamentos"); // despesa addCaption(sheet, 11, linha,
	 * "Prestações de conta"); // despesa mas não baixa na conta e sim no
	 * adiantamento addCaption(sheet, 12, linha, "Saldo");
	 */

	public String getCondicaoPagamento() {
		return condicaoPagamento;
	}

	public void setCondicaoPagamento(String condicaoPagamento) {
		this.condicaoPagamento = condicaoPagamento;
	}

	public String getGestao() {
		return gestao;
	}

	public void setGestao(String gestao) {
		this.gestao = gestao;
	}

	public String getPrograma() {
		return programa;
	}

	public void setPrograma(String programa) {
		this.programa = programa;
	}

	public String getSv() {
		return sv;
	}

	public void setSv(String sv) {
		this.sv = sv;
	}

	public String getSc() {
		return sc;
	}

	public void setSc(String sc) {
		this.sc = sc;
	}

	public String getCpfcnpj() {
		return cpfcnpj;
	}

	public void setCpfcnpj(String cpfcnpj) {
		this.cpfcnpj = cpfcnpj;
	}

	public String getSinalizador() {
		return sinalizador;
	}

	public void setSinalizador(String sinalizador) {
		this.sinalizador = sinalizador;
	}

	public String getNumeroParcela() {
		return numeroParcela;
	}

	public void setNumeroParcela(String numeroParcela) {
		this.numeroParcela = numeroParcela;
	}

	public String getLocalidade() {
		return localidade;
	}

	public void setLocalidade(String localidade) {
		this.localidade = localidade;
	}

	public String getLocalidadeUC() {
		return localidadeUC;
	}

	public void setLocalidadeUC(String localidadeUC) {
		this.localidadeUC = localidadeUC;
	}

	public String getLocalidadeProjeto() {
		return localidadeProjeto;
	}

	public void setLocalidadeProjeto(String localidadeProjeto) {
		this.localidadeProjeto = localidadeProjeto;
	}
}
