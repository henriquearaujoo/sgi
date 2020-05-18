package service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.jboss.weld.context.RequestContext;
import org.primefaces.PrimeFaces;

import anotacoes.Transactional;
import model.ContaBancaria;
import model.DespesaReceita;
import model.GuiaImposto;
import model.Imposto;
import model.LancamentoAcao;
import model.StatusCompra;
import model.TipoDeDocumentoFiscal;
import model.TipoParcelamento;
import model.User;
import repositorio.ImpostoRepositorio;
import util.FilterImposto;

public class ImpostoService implements Serializable{


	private static final long serialVersionUID = 1L;
	
	@Inject
	private ImpostoRepositorio repositorio; 

	public ImpostoService() {
		
	}
	

	
	public List<Imposto> buscarImpostos(FilterImposto filter) {
		return repositorio.buscarImpostos(filter);
	}
	
	public GuiaImposto buscarGuiaPorId(Long id) {
		return repositorio.buscarGuiaPorId(id);
	}
	
	public List<GuiaImposto> buscarGuias(){
		return repositorio.buscarGuias();
	}
	
	@Transactional
	public GuiaImposto salvarGuia(GuiaImposto guia) {
		
		
		if (guia.getId() == null) {
			guia.setDataEmissao(new Date());
			guia.setDepesaReceita(DespesaReceita.DESPESA);
			guia.setDescricao("GUIA DE IMPOSTOS");
			guia.setStatusCompra(StatusCompra.CONCLUIDO);
			guia.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
			guia.setQuantidadeParcela(1);
			guia.setVersionLancamento("MODE01");
			guia.setTipoDocumentoFiscal(TipoDeDocumentoFiscal.OUTRO);
		}
		
		
		return repositorio.salvarGuia(guia);
	}
	
	@Transactional
	public GuiaImposto salvarGuia(GuiaImposto guia, User usuario) {
		
		
		
		if (guia.getId() == null) {
			guia.setDataEmissao(new Date());
			guia.setDepesaReceita(DespesaReceita.DESPESA);
			guia.setDescricao("GUIA DE IMPOSTOS");
			guia.setStatusCompra(StatusCompra.CONCLUIDO);
			guia.setTipoParcelamento(TipoParcelamento.PARCELA_UNICA);
			guia.setQuantidadeParcela(1);
			guia.setVersionLancamento("MODE01");
			guia.setTipoDocumentoFiscal(TipoDeDocumentoFiscal.OUTRO);
		}
		
		if (guia.getValorTotalComDesconto().compareTo(new BigDecimal(10)) < 0 ) {
			addMessage("", "O valor do guia não pode ser menor que R$10 reais.", FacesMessage.SEVERITY_ERROR);
			return guia;
		}
		
		guia = repositorio.salvarGuia(guia, usuario);
		
		addMessage("", "Salvo com sucesso", FacesMessage.SEVERITY_INFO);
		closeDialogLoading("guia-dlg");
		
		return guia;
	}
	
	private void closeDialogLoading(String nomeDialogo) {
		PrimeFaces.current().executeScript("PF('"+nomeDialogo+"').hide();");
	}
	
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	
	public GuiaImposto gerarGuia(GuiaImposto guia, List<Imposto> list) {
		
		BigDecimal totalImposto = BigDecimal.ZERO;
		
		if (guia.getId() == null) 
		guia = new GuiaImposto();
		
		guia.setImpostoGuias(new ArrayList<>());
		
		LancamentoAcao lancamentoAcao; //= new LancamentoAcao();
		guia.setLancamentosAcoes(new ArrayList<>());
		
		for (Imposto imposto : list) {
			
			lancamentoAcao = new LancamentoAcao();
			lancamentoAcao.setLancamento(guia);
			lancamentoAcao.setValor(imposto.getValor());
			lancamentoAcao.setProjetoRubrica(imposto.getPagamentoLancamento().getLancamentoAcao().getProjetoRubrica());
			
			guia.getLancamentosAcoes().add(lancamentoAcao);
						
			imposto.setGuia(new GuiaImposto());
			imposto.setGuia(guia);
			guia.getImpostoGuias().add(imposto);
			
			totalImposto = totalImposto.add(imposto.getValor());
		}

		guia.setValorTotalComDesconto(totalImposto);
		
		return guia;
	}
	
	
	
	public List<ContaBancaria> getContasFas() {
		return repositorio.getContasFas();
	}
	
	public List<ContaBancaria> getContasForaFas() {
		return repositorio.getContasForaFas();
	}
}



