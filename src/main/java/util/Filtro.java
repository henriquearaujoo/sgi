package util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import model.Acao;
import model.Colaborador;
import model.Componente;
import model.ContaBancaria;
import model.FontePagadora;
import model.Fornecedor;
import model.Gestao;
import model.Localidade;
import model.LocalizacaoPedido;
import model.Orcamento;
import model.PlanoDeTrabalho;
import model.Projeto;
import model.ProjetoRubrica;
import model.StatusCompra;
import model.StatusPagamentoLancamento;
import model.TipoGestao;
import model.TipoLocalidade;
import model.User;

public class Filtro {

	private String descricao;
	private Date data;
	private String nome;
	private Localidade destino;
	private Date dataInicio;
	private Date dataFinal;
	private String codigoDaria;
	private PlanoDeTrabalho planoDeTrabalho;
	private Long id;
	private Boolean verificVigenciaMenosDias;
	private Orcamento orcamento;
	private Boolean ativo;
	private Gestao superintendencia;
	private Gestao coordenadoria;
	private Gestao gerencia;
	private String statusConta;
	private String titulo;
	private FontePagadora fontePagadora;
	private Colaborador responsavelTecnico;
	private Colaborador colaborador;
	private Colaborador userColaborador;
	private String email;
	

	public Colaborador getUserColaborador() {
		return userColaborador;
	}

	public void setUserColaborador(Colaborador userColaborador) {
		this.userColaborador = userColaborador;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	private FontePagadora fontePagadoraV2 = new FontePagadora();
	
	public FontePagadora getFontePagadoraV2() {
		return fontePagadoraV2;
	}

	public void setFontePagadoraV2(FontePagadora fontePagadoraV2) {
		this.fontePagadoraV2 = fontePagadoraV2;
	}

	public Colaborador getResponsavelTecnico() {
		return responsavelTecnico;
	}

	public void setResponsavelTecnico(Colaborador responsavelTecnico) {
		this.responsavelTecnico = responsavelTecnico;
	}

	public Colaborador getColaborador() {
		return colaborador;
	}

	public void setColaborador(Colaborador colaborador) {
		this.colaborador = colaborador;
	}

	public FontePagadora getFontePagadora() {
		return fontePagadora;
	}
	
	public void setFontePagadora(FontePagadora fontePagadora) {
		this.fontePagadora = fontePagadora;
	}

	public Orcamento getOrcamento() {
		return orcamento;
	}

	public void setOrcamento(Orcamento orcamento) {
		this.orcamento = orcamento;
	}

	public PlanoDeTrabalho getPlanoDeTrabalho() {
		return planoDeTrabalho;
	}

	public void setPlanoDeTrabalho(PlanoDeTrabalho planoDeTrabalho) {
		this.planoDeTrabalho = planoDeTrabalho;
	}

	//filtro criado para buscar por (numero de adiantemento) numero de documento 03/09/2018
	private String numeroDocumentoAdiantemento;

	
	public String getNumeroDocumentoAdiantemento() {
		return numeroDocumentoAdiantemento;
	}

	public void setNumeroDocumentoAdiantemento(String numeroDocumentoAdiantemento) {
		this.numeroDocumentoAdiantemento = numeroDocumentoAdiantemento;
	}
	//end

	private Date dataInicioEmissao;
	public Gestao getSuperintendencia() {
		return superintendencia;
	}

	public void setSuperintendencia(Gestao superintendencia) {
		this.superintendencia = superintendencia;
	}

	public Gestao getCoordenadoria() {
		return coordenadoria;
	}

	public void setCoordenadoria(Gestao coordenadoria) {
		this.coordenadoria = coordenadoria;
	}

	public Gestao getGerencia() {
		return gerencia;
	}

	public void setGerencia(Gestao gerencia) {
		this.gerencia = gerencia;
	}

	public String getStatusConta() {
		return statusConta;
	}

	public void setStatusConta(String statusConta) {
		this.statusConta = statusConta;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Date getDataPedidoInicial() {
		return dataPedidoInicial;
	}

	public void setDataPedidoInicial(Date dataPedidoInicial) {
		this.dataPedidoInicial = dataPedidoInicial;
	}

	public Date getDataPedidoFinal() {
		return dataPedidoFinal;
	}

	public void setDataPedidoFinal(Date dataPedidoFinal) {
		this.dataPedidoFinal = dataPedidoFinal;
	}

	public Integer getTipoDespesa() {
		return tipoDespesa;
	}

	public void setTipoDespesa(Integer tipoDespesa) {
		this.tipoDespesa = tipoDespesa;
	}

	private Date dataFinalEmissao;
	
	private ContaBancaria fornecedor;
	private ContaBancaria pagador;
	
	public ContaBancaria getFornecedor() {
		return fornecedor;
	}

	public void setFornecedor(ContaBancaria fornecedor) {
		this.fornecedor = fornecedor;
	}

	public ContaBancaria getPagador() {
		return pagador;
	}

	public void setPagador(ContaBancaria pagador) {
		this.pagador = pagador;
	}

	private Boolean detalhado = false;
	private Boolean consolidado = false;

	private String[] colunasRelatorio;
	
	private int primeiroRegistro;
	private int quantidadeRegistros;
	private String propriedadeOrdenacao;
	private boolean ascendente;
	private Boolean status;
	private Boolean tarifado;
	public Integer[] getProjetos() {
		return projetos;
	}

	public void setProjetos(Integer[] projetos) {
		this.projetos = projetos;
	}

	private Integer anoViagem;
	private String premioRifa;
	private String telefone;
	private String nomeMissionario;
	private Integer diaRecebimento;
	private Integer[] contas;
	private Integer[] fontes;
	private Integer[] projetos;
	private Integer[] doacoes;
	private Integer[] planos;
	
	private List<ProjetoRubrica> rubricas;
	

	public Integer[] getDoacoes() {
		return doacoes;
	}

	public void setDoacoes(Integer[] doacoes) {
		this.doacoes = doacoes;
	}

	private Integer[] acoes;
	private List<Fornecedor> fornecedores;
	private Integer[] gestoes;
	private LocalizacaoPedido localizacaoPedido;
	private Componente componente;
	private Componente[] componentes;
	private String notaFiscal;
	private String nomeFornecedor;
	
	private String sp;
	
	private Long idDoacao;
	
	private Long idPlano;

	private String missao;

	private Long idProduto;

	private String statusCadastro = "";
	private String ativoInativoProd = "";

	private Long categoriaID;

	private Localidade localidade;
	private Gestao gestao;
	
	private Gestao gestaoV2;

	public Gestao getGestaoV2() {
		return gestaoV2;
	}

	public void setGestaoV2(Gestao gestaoV2) {
		this.gestaoV2 = gestaoV2;
	}

	private String cnpjcpf;

	private Long solicitacaoExpedicaoID;

	private String codigo;
	
	private String tipoLancamento;

	private ContaBancaria contaFiltro;

	private StatusCompra statusCompra;

	private Long localidadeID;
	private Long gestaoID;
	private Long fornecedorID;
	
	private String competencia;

	private TipoGestao tipoGestao;
	private TipoLocalidade tipoLocalidade;

	private Long acaoId;
	private Long projetoId;

	private Long lancamentoID;

	private User usuario;
	
	private Integer statusPagamentoInt;

	public Integer getStatusPagamentoInt() {
		return statusPagamentoInt;
	}

	public void setStatusPagamentoInt(Integer statusPagamentoInt) {
		this.statusPagamentoInt = statusPagamentoInt;
	}

	private Date dataInicioPE;
	private Date dataFinalPE;

	private Date dataInicioEmissaoPE;
	private Date dataFinalEmissaoPE;

	private String descricaoProduto;

	private Boolean urgencia = false;

	private Acao acao = new Acao();

	private Projeto projeto = new Projeto();

	private Long idReembolso;

	private String conta;
	private String agencia;
	private String tipoConta;

	private StatusPagamentoLancamento statusPagamento;
	private Long idConta;
	private String numeroDocumento;

	private Long solicitanteID;

	private Long componenteClass;

	private Long subComponente;
	// Usados em Recursos de Projetos---------------------------------------------------------------------
	private Orcamento doacao;
	
	private String tipo;
	
	private String tipoRelatorio;
	
	private String statusRecurso;
	
	private Date dataInicialRecurso;
	
	private Date dataFinalRecurso;
	
	private Long rubricaID;
	//----------------------------------------------------------------------------------------------------
	//Alterado 10/09/2018 para adicionar filtro de colaborador -----------------------------------------------
	
	private String nomeColaborador;
	private String CPF;
	private Date dataNascimento;
	private Gestao gestaoColaborador;
	
	
	//alterado para filtro de solicitações detalhadas
	private Date dataPedidoInicial;
	
	private Date dataPedidoFinal;

	private Integer tipoDespesa;
	
	
	public String getNomeColaborador() {
		return nomeColaborador;
	}

	public void setNomeColaborador(String nomeColaborador) {
		this.nomeColaborador = nomeColaborador;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Gestao getGestaoColaborador() {
		return gestaoColaborador;
	}

	public void setGestaoColaborador(Gestao gestaoColaborador) {
		this.gestaoColaborador = gestaoColaborador;
	}
	
	
	//end

	

	private List<Integer> projetosInt = new ArrayList<>();

	public Filtro() {

		this.data = new Date();
		this.nome = "";
		/*
		 * this.descricao = ""; this.data = null; this.nome = ""; this.destino =
		 * ""; this.dataInicio = null; this.dataFinal = null;
		 * this.primeiroRegistro = 0; this.quantidadeRegistros = 0;
		 * this.propriedadeOrdenacao = ""; this.ascendente = false; this.status
		 * = false; this.anoViagem = 0; this.premioRifa = premioRifa;
		 * this.telefone = telefone; this.nomeMissionario = nomeMissionario;
		 * this.diaRecebimento = diaRecebimento; this.codigo = codigo;
		 * this.statusCompra = statusCompra; this.localidadeID = localidadeID;
		 * this.gestaoID = gestaoID; this.tipoGestao = tipoGestao;
		 * this.tipoLocalidade = tipoLocalidade; this.usuario = usuario;
		 */
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Date getData() {
		return data;
	}

	public void setData(Date data) {
		this.data = data;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Date getDataInicio() {
		return dataInicio;
	}

	public void setDataInicio(Date dataInicio) {
		this.dataInicio = dataInicio;
	}

	public Date getDataFinal() {
		return dataFinal;
	}

	public void setDataFinal(Date dataFinal) {
		this.dataFinal = dataFinal;
	}

	public int getPrimeiroRegistro() {
		return primeiroRegistro;
	}

	public void setPrimeiroRegistro(int primeiroRegistro) {
		this.primeiroRegistro = primeiroRegistro;
	}

	public int getQuantidadeRegistros() {
		return quantidadeRegistros;
	}

	public void setQuantidadeRegistros(int quantidadeRegistros) {
		this.quantidadeRegistros = quantidadeRegistros;
	}

	public String getPropriedadeOrdenacao() {
		return propriedadeOrdenacao;
	}

	public void setPropriedadeOrdenacao(String propriedadeOrdenacao) {
		this.propriedadeOrdenacao = propriedadeOrdenacao;
	}

	public boolean isAscendente() {
		return ascendente;
	}

	public void setAscendente(boolean ascendente) {
		this.ascendente = ascendente;
	}

	public Boolean getStatus() {
		return status;
	}

	public void setStatus(Boolean status) {
		this.status = status;
	}

	public Localidade getDestino() {
		return destino;
	}

	public void setDestino(Localidade destino) {
		this.destino = destino;
	}

	public Integer getAnoViagem() {
		return anoViagem;
	}

	public void setAnoViagem(Integer anoViagem) {
		this.anoViagem = anoViagem;
	}

	public String getPremioRifa() {
		return premioRifa;
	}

	public void setPremioRifa(String premioRifa) {
		this.premioRifa = premioRifa;
	}

	public Integer getDiaRecebimento() {
		return diaRecebimento;
	}

	public void setDiaRecebimento(Integer diaRecebimento) {
		this.diaRecebimento = diaRecebimento;
	}

	public String getNomeMissionario() {
		return nomeMissionario;
	}

	public void setNomeMissionario(String nomeMissionario) {
		this.nomeMissionario = nomeMissionario;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public User getUsuario() {
		return usuario;
	}

	public void setUsuario(User usuario) {
		this.usuario = usuario;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public StatusCompra getStatusCompra() {
		return statusCompra;
	}

	public void setStatusCompra(StatusCompra statusCompra) {
		this.statusCompra = statusCompra;
	}

	public Long getLocalidadeID() {
		return localidadeID;
	}

	public void setLocalidadeID(Long localidadeID) {
		this.localidadeID = localidadeID;
	}

	public Long getGestaoID() {
		return gestaoID;
	}

	public void setGestaoID(Long gestaoID) {
		this.gestaoID = gestaoID;
	}

	public TipoGestao getTipoGestao() {
		return tipoGestao;
	}

	public void setTipoGestao(TipoGestao tipoGestao) {
		this.tipoGestao = tipoGestao;
	}

	public TipoLocalidade getTipoLocalidade() {
		return tipoLocalidade;
	}

	public void setTipoLocalidade(TipoLocalidade tipoLocalidade) {
		this.tipoLocalidade = tipoLocalidade;
	}

	public Long getAcaoId() {
		return acaoId;
	}

	public void setAcaoId(Long acaoId) {
		this.acaoId = acaoId;
	}

	public Long getProjetoId() {
		return projetoId;
	}

	public void setProjetoId(Long projetoId) {
		this.projetoId = projetoId;
	}

	public Long getFornecedorID() {
		return fornecedorID;
	}

	public void setFornecedorID(Long fornecedorID) {
		this.fornecedorID = fornecedorID;
	}

	public Date getDataInicioEmissao() {
		return dataInicioEmissao;
	}

	public void setDataInicioEmissao(Date dataInicioEmissao) {
		this.dataInicioEmissao = dataInicioEmissao;
	}

	public Date getDataFinalEmissao() {
		return dataFinalEmissao;
	}

	public void setDataFinalEmissao(Date dataFinalEmissao) {
		this.dataFinalEmissao = dataFinalEmissao;
	}

	public Long getLancamentoID() {
		return lancamentoID;
	}

	public void setLancamentoID(Long lancamentoID) {
		this.lancamentoID = lancamentoID;
	}

	public Date getDataInicioPE() {
		return dataInicioPE;
	}

	public void setDataInicioPE(Date dataInicioPE) {
		this.dataInicioPE = dataInicioPE;
	}

	public Date getDataFinalPE() {
		return dataFinalPE;
	}

	public void setDataFinalPE(Date dataFinalPE) {
		this.dataFinalPE = dataFinalPE;
	}

	public Date getDataInicioEmissaoPE() {
		return dataInicioEmissaoPE;
	}

	public void setDataInicioEmissaoPE(Date dataInicioEmissaoPE) {
		this.dataInicioEmissaoPE = dataInicioEmissaoPE;
	}

	public Date getDataFinalEmissaoPE() {
		return dataFinalEmissaoPE;
	}

	public void setDataFinalEmissaoPE(Date dataFinalEmissaoPE) {
		this.dataFinalEmissaoPE = dataFinalEmissaoPE;
	}

	public StatusPagamentoLancamento getStatusPagamento() {
		return statusPagamento;
	}

	public void setStatusPagamento(StatusPagamentoLancamento statusPagamento) {
		this.statusPagamento = statusPagamento;
	}

	public Long getIdConta() {
		return idConta;
	}

	public void setIdConta(Long idConta) {
		this.idConta = idConta;
	}

	public Integer[] getContas() {
		return contas;
	}

	public void setContas(Integer[] contas) {
		this.contas = contas;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public Integer[] getAcoes() {
		return acoes;
	}

	public void setAcoes(Integer[] acoes) {
		this.acoes = acoes;
	}

	public String getNumeroDocumento() {
		return numeroDocumento;
	}

	public void setNumeroDocumento(String numeroDocumento) {
		this.numeroDocumento = numeroDocumento;
	}

	public Long getSolicitacaoExpedicaoID() {
		return solicitacaoExpedicaoID;
	}

	public void setSolicitacaoExpedicaoID(Long solicitacaoExpedicaoID) {
		this.solicitacaoExpedicaoID = solicitacaoExpedicaoID;
	}

	public LocalizacaoPedido getLocalizacaoPedido() {
		return localizacaoPedido;
	}

	public void setLocalizacaoPedido(LocalizacaoPedido localizacaoPedido) {
		this.localizacaoPedido = localizacaoPedido;
	}

	public Long getSolicitanteID() {
		return solicitanteID;
	}

	public void setSolicitanteID(Long solicitanteID) {
		this.solicitanteID = solicitanteID;
	}

	public Componente getComponente() {
		return componente;
	}

	public void setComponente(Componente componente) {
		this.componente = componente;
	}

	public Componente[] getComponentes() {
		return componentes;
	}

	public void setComponentes(Componente[] componentes) {
		this.componentes = componentes;
	}

	public Integer[] getGestoes() {
		return gestoes;
	}

	public void setGestoes(Integer[] gestoes) {
		this.gestoes = gestoes;
	}

	public String getDescricaoProduto() {
		return descricaoProduto;
	}

	public void setDescricaoProduto(String descricaoProduto) {
		this.descricaoProduto = descricaoProduto;
	}

	public String getNotaFiscal() {
		return notaFiscal;
	}

	public void setNotaFiscal(String notaFiscal) {
		this.notaFiscal = notaFiscal;
	}

	public String getNomeFornecedor() {
		return nomeFornecedor;
	}

	public void setNomeFornecedor(String nomeFornecedor) {
		this.nomeFornecedor = nomeFornecedor;
	}

	public List<Fornecedor> getFornecedores() {
		return fornecedores;
	}

	public void setFornecedores(List<Fornecedor> fornecedores) {
		this.fornecedores = fornecedores;
	}

	public Boolean getUrgencia() {
		return urgencia;
	}

	public void setUrgencia(Boolean urgencia) {
		this.urgencia = urgencia;
	}

	public String getCnpjcpf() {
		return cnpjcpf;
	}

	public void setCnpjcpf(String cnpjcpf) {
		this.cnpjcpf = cnpjcpf;
	}

	public ContaBancaria getContaFiltro() {
		return contaFiltro;
	}

	public void setContaFiltro(ContaBancaria contaFiltro) {
		if (contaFiltro != null) {
			if (contaFiltro.getId() != null)
				idConta = contaFiltro.getId();
		}
		this.contaFiltro = contaFiltro;
	}

	public Acao getAcao() {
		return acao;
	}

	public void setAcao(Acao acao) {
		if (acao != null) {
			if (acao.getId() != null)
				acaoId = acao.getId();
		}

		this.acao = acao;
	}

	public Localidade getLocalidade() {
		return localidade;
	}

	public void setLocalidade(Localidade localidade) {
		this.localidade = localidade;
	}

	public Gestao getGestao() {
		return gestao;
	}

	public void setGestao(Gestao gestao) {
		if (gestao != null) 
		if (gestao.getId() != null) {
			this.gestaoID = gestao.getId();
		}

		this.gestao = gestao;
	}

	public Long getCategoriaID() {
		return categoriaID;
	}

	public void setCategoriaID(Long categoriaID) {
		this.categoriaID = categoriaID;
	}

	public String getStatusCadastro() {
		return statusCadastro;
	}

	public void setStatusCadastro(String statusCadastro) {
		this.statusCadastro = statusCadastro;
	}

	public String getAtivoInativoProd() {
		return ativoInativoProd;
	}

	public void setAtivoInativoProd(String ativoInativoProd) {
		this.ativoInativoProd = ativoInativoProd;
	}

	public Long getIdProduto() {
		return idProduto;
	}

	public void setIdProduto(Long idProduto) {
		this.idProduto = idProduto;
	}

	public Integer[] getFontes() {
		return fontes;
	}

	public void setFontes(Integer[] fontes) {
		this.fontes = fontes;
	}

	public Long getComponenteClass() {
		return componenteClass;
	}

	public void setComponenteClass(Long componenteClass) {
		this.componenteClass = componenteClass;
	}

	public Long getSubComponente() {
		return subComponente;
	}

	public void setSubComponente(Long subComponente) {
		this.subComponente = subComponente;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {

		if (projeto != null) {
			if (projeto.getId() != null)
				projetoId = projeto.getId();
		}

		this.projeto = projeto;
	}

	public List<Integer> getProjetosInt() {
		return projetosInt;
	}

	public void setProjetosInt(List<Integer> projetosInt) {
		this.projetosInt = projetosInt;
	}

	public String getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(String tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getMissao() {
		return missao;
	}

	public void setMissao(String missao) {
		this.missao = missao;
	}

	public String getCompetencia() {
		return competencia;
	}

	public void setCompetencia(String competencia) {
		this.competencia = competencia;
	}

	public Long getIdReembolso() {
		return idReembolso;
	}

	public void setIdReembolso(Long idReembolso) {
		this.idReembolso = idReembolso;
	}

	public Long getIdDoacao() {
		return idDoacao;
	}

	public void setIdDoacao(Long idDoacao) {
		this.idDoacao = idDoacao;
	}

	public String getTipoLancamento() {
		return tipoLancamento;
	}

	public void setTipoLancamento(String tipoLancamento) {
		this.tipoLancamento = tipoLancamento;
	}

	public String getSp() {
		return sp;
	}

	public void setSp(String sp) {
		this.sp = sp;
	}
	
	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public Date getDataInicialRecurso() {
		return dataInicialRecurso;
	}

	public void setDataInicialRecurso(Date dataInicialRecurso) {
		this.dataInicialRecurso = dataInicialRecurso;
	}

	public Date getDataFinalRecurso() {
		return dataFinalRecurso;
	}

	public void setDataFinalRecurso(Date dateFinalRecurso) {
		this.dataFinalRecurso = dateFinalRecurso;
	}

	public Orcamento getDoacao() {
		return doacao;
	}

	public void setDoacao(Orcamento doacao) {
		this.doacao = doacao;
	}

	public String getStatusRecurso() {
		return statusRecurso;
	}

	public void setStatusRecurso(String statusRecurso) {
		this.statusRecurso = statusRecurso;
	}

	public Long getRubricaID() {
		return rubricaID;
	}

	public void setRubricaID(Long rubricaID) {
		this.rubricaID = rubricaID;
	}

	public Boolean getDetalhado() {
		return detalhado;
	}

	public void setDetalhado(Boolean detalhado) {
		this.detalhado = detalhado;
	}

	public String[] getColunasRelatorio() {
		return colunasRelatorio;
	}

	public void setColunasRelatorio(String[] colunasRelatorio) {
		this.colunasRelatorio = colunasRelatorio;
	}

	public Boolean getTarifado() {
		return tarifado;
	}

	public void setTarifado(Boolean tarifado) {
		this.tarifado = tarifado;
	}

	public Long getIdPlano() {
		return idPlano;
	}

	public void setIdPlano(Long idPlano) {
		this.idPlano = idPlano;
	}

	public Boolean getConsolidado() {
		return consolidado;
	}

	public void setConsolidado(Boolean consolidado) {
		this.consolidado = consolidado;
	}
	
	public String getCodigoDaria() {
		return codigoDaria;
	}

	public void setCodigoDaria(String codigoDaria) {
		this.codigoDaria = codigoDaria;
	}

	public List<ProjetoRubrica> getRubricas() {
		return rubricas;
	}

	public void setRubricas(List<ProjetoRubrica> rubricas) {
		this.rubricas = rubricas;
	}

	public String getTipoRelatorio() {
		return tipoRelatorio;
	}

	public void setTipoRelatorio(String tipoRelatorio) {
		this.tipoRelatorio = tipoRelatorio;
	}

	public Integer[] getPlanos() {
		return planos;
	}

	public void setPlanos(Integer[] planos) {
		this.planos = planos;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Boolean getVerificVigenciaMenosDias() {
		return verificVigenciaMenosDias;
	}

	public void setVerificVigenciaMenosDias(Boolean verificVigenciaMenosDias) {
		this.verificVigenciaMenosDias = verificVigenciaMenosDias;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	
}
