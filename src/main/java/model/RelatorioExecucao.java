package model;

public class RelatorioExecucao {

	private String emissao;
	private String pagamento;
	private String doc;
	private String pagador;
	private String recebedor;
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

	private String doacao = "";
	
	private String subcategoria = "";

	private String tipoLancamento = "";

	private String numeroLancamento;

	public RelatorioExecucao() {
	}

	// Saída
	public RelatorioExecucao(Object[] object) {
		this.setNumeroLancamento(object[1].toString());
		this.setEmissao(object[2] != null ? object[2].toString() : "");
		this.setPagamento(object[3] != null ? object[3].toString() : "");
		this.setDoacao(object[8].toString());
		this.setProjeto(object[9].toString());
		this.setRubrica(object[10].toString());
		this.setParcela(object[11].toString()+"/"+object[12].toString());
		this.setPagador(object[14] != null ? object[14].toString() : "");
		this.setRecebedor(object[15] != null ? object[15].toString() :  "");
		this.setStatus(object[16] != null ? object[16].toString() : "");
		this.setDoc(object[21] != null ? object[21].toString() : "");
		this.setDescricao(object[22] != null ? object[22].toString() : "");
		this.setSubcategoria(object[23] != null ? object[23].toString() : "");
		this.setNotaFiscal(object[24] != null ? object[24].toString() : "");
		this.setSaida(object[13] != null ? Double.valueOf(object[13].toString()) : new Long(0));
	}
	
	//Entrada
	public RelatorioExecucao(Object[] object, String args) {
		this.setNumeroLancamento(object[1].toString());
		this.setEmissao(object[2] != null ? object[2].toString() : "");
		this.setPagamento(object[3] != null ? object[3].toString() : "");
		this.setDoacao(object[8].toString());
		this.setProjeto(object[9].toString());
		this.setRubrica(object[10].toString());
		this.setParcela(object[11].toString()+"/"+object[12].toString());
		this.setPagador(object[14] != null ? object[14].toString() : "");
		this.setRecebedor(object[15] != null ? object[15].toString() :  "");
		this.setStatus(object[16] != null ? object[16].toString() : "");
		this.setDoc(object[21] != null ? object[21].toString() : "");
		this.setDescricao(object[22] != null ? object[22].toString() : "");
		this.setSubcategoria(object[23] != null ? object[23].toString() : "");
		this.setNotaFiscal(object[24] != null ? object[24].toString() : "");
		this.setEntrada(object[13] != null ? Double.valueOf(object[13].toString()) : new Long(0));
		
		
	}
	
	//Não conta no orçamento, valores demontrativos
	public RelatorioExecucao(Object[] object, String args, String argr) {
		
		this.setNumeroLancamento(object[1].toString());
		this.setEmissao(object[2] != null ? object[2].toString() : "");
		this.setPagamento(object[3] != null ? object[3].toString() : "");
		this.setDoacao(object[8].toString());
		this.setProjeto(object[9].toString());
		this.setRubrica(object[10].toString());
		this.setParcela(object[11].toString()+"/"+object[12].toString());
		this.setPagador(object[14] != null ? object[14].toString() : "");
		this.setRecebedor(object[15] != null ? object[15].toString() :  "");
		this.setStatus(object[16] != null ? object[16].toString() : "");
		this.setDoc(object[21] != null ? object[21].toString() : "");
		this.setDescricao(object[22] != null ? object[22].toString() : "");
		this.setSubcategoria(object[23] != null ? object[23].toString() : "");
		this.setNotaFiscal(object[24] != null ? object[24].toString() : "");
		this.setAdiantamento(object[13] != null ? Double.valueOf(object[13].toString()) : new Long(0));

	}
	

	public String getEmissao() {
		return emissao;
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

	public String getDoacao() {
		return doacao;
	}

	public void setDoacao(String doacao) {
		this.doacao = doacao;
	}

	public String getSubcategoria() {
		return subcategoria;
	}

	public void setSubcategoria(String subcategoria) {
		this.subcategoria = subcategoria;
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

}
