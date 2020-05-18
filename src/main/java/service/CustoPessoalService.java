package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import anotacoes.Transactional;
import model.CategoriaDespesaClass;
import model.ContaBancaria;
import model.CustoPessoal;
import model.DoacaoEfetiva;
import model.Gestao;
import model.LancamentoAcao;
import model.Localidade;
import model.ProjetoRubrica;
import model.Reembolso;
import model.RubricaOrcamento;
import model.StatusCompra;
import model.User;
import repositorio.CategoriaRepositorio;
import repositorio.CustoPessoalRepositorio;
import repositorio.GestaoRepositorio;
import repositorio.LancamentoRepository;
import repositorio.LocalRepositorio;
import repositorio.OrcamentoRepositorio;
import repositorio.PagamentoLancamentoRepository;
import util.Filtro;

public class CustoPessoalService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CustoPessoalRepositorio repositorio;

	@Inject
	private GestaoRepositorio gestaoRepositorio;

	@Inject
	private LocalRepositorio localRepositorio;

	@Inject
	private PagamentoLancamentoRepository pagRepositorio;

	@Inject
	private LancamentoRepository lancRepositorio;

	@Inject
	private OrcamentoRepositorio orcamentoRepositorio;

	@Inject
	private CategoriaRepositorio categoriaRepositorio;

	public Gestao findGestaoByIdDefault() {
		return gestaoRepositorio.findByIdGestao(11l);
	}

	public Localidade findLocalidadeByIdDefault() {
		return localRepositorio.findByIdLocal(5580l);
	}

	public List<CategoriaDespesaClass> buscarGategoriasCusteioPessoal() {
		return categoriaRepositorio.buscarGategoriasCusteioPessoal();
	}

//	public List<ProjetoRubrica> completeRubricasDeProjetoJOIN(String s) {
//		return orcamentoRepositorio.completeRubricasDeProjetoJOIN(s);
//	}

	@Transactional
	public Reembolso salvarReembolso(Reembolso reembolso, User usuario, ContaBancaria contaRecebedor,
			Long idDoacaoReembolsada, List<CustoPessoal> selecionados, Long idFonteReembolsada, Long idRubricaOrcamento) {

		if (reembolso.getLancamentosAcoes().isEmpty()) {
			addMessage("", "Adicione a categoria que custeará o custeio de pessoal.", FacesMessage.SEVERITY_ERROR);
			return reembolso;
		}

		if (reembolso.getGestao() == null) {
			return reembolso;
		} else if (reembolso.getLocalidade() == null) {
			addMessage("", "Campo Tipo de Destino/Localizacao é obrigatorio.", FacesMessage.SEVERITY_ERROR);
			return reembolso;
		} else {
			if (reembolso.getId() == null) {
				reembolso.setDataEmissao(new Date());
			}
			reembolso.setStatusCompra(StatusCompra.CONCLUIDO);
			reembolso = repositorio.salvarReembolso(reembolso, usuario, contaRecebedor, idDoacaoReembolsada, idFonteReembolsada, idRubricaOrcamento);

			for (CustoPessoal custoPessoal : selecionados) {
				custoPessoal = findById(custoPessoal.getId());
				custoPessoal.setIdReembolso(reembolso.getId());
				custoPessoal.setReembolsado(true);
				repositorio.updateCustoPessoal(custoPessoal, usuario);
			}

			addMessage("", "Salvo Com Sucesso", FacesMessage.SEVERITY_INFO);
			return reembolso;
		}

	}

	public List<LancamentoAcao> carregarListaResumo(List<Reembolso> listaSelected) {

		List<LancamentoAcao> list = new ArrayList<>();
		for (Reembolso reembolso : listaSelected) {
			list.addAll(lancRepositorio.getLancamentosAcao(reembolso));
		}

		return list;
	}

	@Transactional
	public CustoPessoal salvar(CustoPessoal custoPessoal, User usuario) {

		try {

			if (custoPessoal.getLancamentosAcoes().isEmpty()) {
				addMessage("", "Adicione a categoria que custeará o custeio de pessoal.", FacesMessage.SEVERITY_ERROR);
				return custoPessoal;
			}

			if (custoPessoal.getGestao() == null) {
				return custoPessoal;
			} else if (custoPessoal.getLocalidade() == null) {
				addMessage("", "Campo Tipo de Destino/Localizacao é obrigatorio.", FacesMessage.SEVERITY_ERROR);
				return custoPessoal;
			} else {
				if (custoPessoal.getId() == null) {
					custoPessoal.setDataEmissao(new Date());
				}
				custoPessoal.setStatusCompra(StatusCompra.CONCLUIDO);
				custoPessoal = repositorio.salvar(custoPessoal, usuario);
				addMessage("", "Salvo Com Sucesso", FacesMessage.SEVERITY_INFO);
				return custoPessoal;
			}
		} catch (Exception e) {
			addMessage("", "Erro ao salvar", FacesMessage.SEVERITY_ERROR);
			return custoPessoal;
		}
	}

	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}

	@Transactional
	public Boolean remover(CustoPessoal custoPessoal) {
		repositorio.remover(custoPessoal);
		return true;
	}
	
	@Transactional
	public Boolean removerDoacaoEfetiva(DoacaoEfetiva doacaoEfetiva) {
		repositorio.removerDoacaoEfetiva(doacaoEfetiva);
		return true;
	}
	
	public DoacaoEfetiva findDoacaoEfetivaById(Long id) {
		return repositorio.findDoacaoEfetivaById(id);
	}

	@Transactional
	public Boolean removerReembolso(List<Reembolso> reembolsos) {
		List<CustoPessoal> list = new ArrayList<>();
		for (Reembolso reembolso : reembolsos) {
			repositorio.mudarStatusReembolsado(reembolso.getId());
			repositorio.removerReembolso(reembolso);
		}
		return true;
	}

	public List<ContaBancaria> getAllConta(String s) {
		return pagRepositorio.getAllConta(s);
	}

	public CustoPessoal findById(Long id) {
		return repositorio.findById(id);
	}

	public List<CustoPessoal> getTodos(Filtro filtro) {
		return repositorio.getTodos(filtro);
	}

	public List<Reembolso> getTodosReembolsos(Filtro filtro) {
		return repositorio.getTodosReembolsos(filtro);
	}

	public List<RubricaOrcamento> completetRubricasDeOrcamento(String s) {
		return orcamentoRepositorio.completeRubricasDeOrcamento(s);
	}

}
