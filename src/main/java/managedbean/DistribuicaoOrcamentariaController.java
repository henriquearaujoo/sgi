package managedbean;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.Coordenadoria;
import model.Gestao;
import model.GestaoOrcamento;
import model.GestaoRecurso;
import model.Orcamento;
import model.RubricaOrcamento;
import service.GestaoOrcamentariaService;

@Named(value = "dist_orcamentaria_controller")
@ViewScoped
public class DistribuicaoOrcamentariaController implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private GestaoOrcamentariaService service;

	// @Inject
	// private Gestao gestao;

	@Inject
	private GestaoRecurso gestaoRecurso;

	@Inject
	private GestaoOrcamento gestaoOrcamento;

	@Inject
	private RubricaOrcamento rubricaOrcamento;

	private Long idGestao;

	private Long idDoacao;

	private List<GestaoRecurso> recursosAportados = new ArrayList<>();
	// Orçamentos de doações
	private List<Orcamento> doacoes = new ArrayList<>();

	private List<RubricaOrcamento> linhasOrcamentarias = new ArrayList<>();

	private List<Gestao> gestoes = new ArrayList<>();

	private BigDecimal orcamentoAprovado;

	private BigDecimal orcamentoLiberado;

	private BigDecimal orcamentoDistribuido;

	private BigDecimal orcamentoADistribuir;

	private BigDecimal saidaRemanejada;

	private BigDecimal entradaRemanejada;

	@Inject
	private Coordenadoria coordenadoria;

	public DistribuicaoOrcamentariaController() {
	}

	// COMPORTAMENTO

	public void initCadastro() {
		carregarCoordenadoria();
		carregarValoresPainel();
		carregarRecursosAportados();
	}

	public void initListagem() {
		carregarGestoes();
	}

	public void carregarDoacoes() {
		doacoes = new ArrayList<>();
		doacoes = service.getOrcamentos();
	}

	public void carregarRubricasDeOrcamento() {
		linhasOrcamentarias = new ArrayList<>();
		linhasOrcamentarias = service.getRubricasDeOrcamento(idDoacao);
	}
	
	public void editarRecurso() {
		if (gestaoRecurso.getId() != null) {
			idDoacao = gestaoRecurso.getRubricaOrcamento().getOrcamento().getId();
			carregarDoacoes();
			carregarRubricasDeOrcamento();
		}
	}
	
	public void removerRecurso() {
		service.removerRecursoAportado(gestaoRecurso);
		carregarOrcamentoLiberado();
		carregarRecursosAportados();
		gestaoRecurso = new GestaoRecurso();
	}

	public void carregarGestoes() {
		gestoes = service.getGestoesParticipativas();
	}

	public void salvarRecurso() {
		gestaoRecurso.setGestao(coordenadoria);
		gestaoRecurso = service.salvar(gestaoRecurso);
		carregarOrcamentoLiberado();
		carregarRecursosAportados();
	}

	

	public void carregarCoordenadoria() {
		coordenadoria = service.findGestaooById(idGestao);
	}

	public void salvarOrcamentoAprovado() {
		gestaoOrcamento.setGestao(coordenadoria);
		service.salvarOrcamentoDeGestao(gestaoOrcamento);
		carregarValoresPainel();
	}

	public void carregarOrcamentoAprovado() {
		gestaoOrcamento = service.findOrcamentoGestaoByGestaoId(idGestao);
		if (gestaoOrcamento == null) {
			gestaoOrcamento = new GestaoOrcamento();
		}else {
			orcamentoAprovado = gestaoOrcamento.getValor();
		}
	}
	
	
	
	public void carregarRecursosAportados() {
		recursosAportados = service.getRecursosAportados(idGestao);
	}
	
	public void carregarOrcamentoLiberado() {
		orcamentoLiberado = service.getValorLiberado(idGestao);
	}
	
	public void carregarOrcamentoDistribuido() {
		orcamentoDistribuido = BigDecimal.ZERO;
	}
	
	public void carregarOrcamentoAdistrir() {
		orcamentoADistribuir = BigDecimal.ZERO;
	}
	
	public void carregarSaidaRemanejada() {
		saidaRemanejada = BigDecimal.ZERO;
	}
	
	public void carregarEntradaRemanejada() {
		entradaRemanejada = BigDecimal.ZERO;
	}
	
	

	public void carregarValoresPainel() {

		carregarOrcamentoAprovado();
		carregarOrcamentoLiberado();
		carregarOrcamentoDistribuido();
		carregarOrcamentoAdistrir();
		carregarSaidaRemanejada();
		carregarEntradaRemanejada();
	
	}

	public void carregarGestaoOrcamento() {
		gestaoOrcamento = service.findOrcamentoGestaoById(idGestao);
	}

	// GETTERS AND SETTERS
	public GestaoRecurso getGestaoRecurso() {
		return gestaoRecurso;
	}

	public void setGestaoRecurso(GestaoRecurso gestaoRecurso) {
		this.gestaoRecurso = gestaoRecurso;
	}

	public RubricaOrcamento getRubricaOrcamento() {
		return rubricaOrcamento;
	}

	public void setRubricaOrcamento(RubricaOrcamento rubricaOrcamento) {
		this.rubricaOrcamento = rubricaOrcamento;
	}

	public BigDecimal getOrcamentoAprovado() {
		return orcamentoAprovado;
	}

	public void setOrcamentoAprovado(BigDecimal orcamentoAprovado) {
		this.orcamentoAprovado = orcamentoAprovado;
	}

	public BigDecimal getOrcamentoLiberado() {
		return orcamentoLiberado;
	}

	public void setOrcamentoLiberado(BigDecimal orcamentoLiberado) {
		this.orcamentoLiberado = orcamentoLiberado;
	}

	public BigDecimal getOrcamentoDistribuido() {
		return orcamentoDistribuido;
	}

	public void setOrcamentoDistribuido(BigDecimal orcamentoDistribuido) {
		this.orcamentoDistribuido = orcamentoDistribuido;
	}

	public BigDecimal getOrcamentoADistribuir() {
		return orcamentoADistribuir;
	}

	public void setOrcamentoADistribuir(BigDecimal orcamentoADistribuir) {
		this.orcamentoADistribuir = orcamentoADistribuir;
	}

	public BigDecimal getSaidaRemanejada() {
		return saidaRemanejada;
	}

	public void setSaidaRemanejada(BigDecimal saidaRemanejada) {
		this.saidaRemanejada = saidaRemanejada;
	}

	public BigDecimal getEntradaRemanejada() {
		return entradaRemanejada;
	}

	public void setEntradaRemanejada(BigDecimal entradaRemanejada) {
		this.entradaRemanejada = entradaRemanejada;
	}

	public GestaoOrcamento getGestaoOrcamento() {
		return gestaoOrcamento;
	}

	public void setGestaoOrcamento(GestaoOrcamento gestaoOrcamento) {
		this.gestaoOrcamento = gestaoOrcamento;
	}

	public List<GestaoRecurso> getRecursosAportados() {
		return recursosAportados;
	}

	public void setRecursosAportados(List<GestaoRecurso> recursosAportados) {
		this.recursosAportados = recursosAportados;
	}

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
	}

	public Long getIdGestao() {
		return idGestao;
	}

	public void setIdGestao(Long idGestao) {
		this.idGestao = idGestao;
	}

	public Coordenadoria getCoordenadoria() {
		return coordenadoria;
	}

	public void setCoordenadoria(Coordenadoria coordenadoria) {
		this.coordenadoria = coordenadoria;
	}

	public List<Orcamento> getDoacoes() {
		return doacoes;
	}

	public void setDoacoes(List<Orcamento> doacoes) {
		this.doacoes = doacoes;
	}

	public List<RubricaOrcamento> getLinhasOrcamentarias() {
		return linhasOrcamentarias;
	}

	public void setLinhasOrcamentarias(List<RubricaOrcamento> linhasOrcamentarias) {
		this.linhasOrcamentarias = linhasOrcamentarias;
	}

	public Long getIdDoacao() {
		return idDoacao;
	}

	public void setIdDoacao(Long idDoacao) {
		this.idDoacao = idDoacao;
	}

}
