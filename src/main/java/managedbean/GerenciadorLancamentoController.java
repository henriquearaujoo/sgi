package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.LancamentoAcao;
import model.LancamentoAuxiliar;
import model.PagamentoLancamento;
import model.PagamentoPE;
import service.GerenciadorLancamentoService;
import util.ArquivoLancamento;
import util.DataUtil;
import util.DownloadUtil;
import util.Filtro;


@Named(value = "lancamentos_controller")
@ViewScoped
public class GerenciadorLancamentoController implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private LancamentoAuxiliar lancamento;
	private List<LancamentoAuxiliar> lancamentos = new ArrayList<>();;
	//private List<PagamentoPE>  pagamentos = new ArrayList<>();
	private @Inject GerenciadorLancamentoService service;
	private @Inject ArquivoLancamento arqAuxiliar;
	
	private Filtro filtro = new Filtro();
	private List<LancamentoAcao> lancamentosEmAcoes = new ArrayList<>();
	private List<PagamentoLancamento> pagamentos = new ArrayList<>(); 
	private List<ArquivoLancamento> documentos = new ArrayList<>();
		
	
	public void listar(){
		lancamentos = service.getLancamentos(filtro);
	}
	
	public void listarPagamentos(){
		pagamentos = service.getPagamentosINLancamentos(lancamento.getId());
	}
	
	public void listarDocumentos(){
		documentos = service.getDocumentosByLancamento(lancamento.getId());
	}
	
	public void listarDocumentos(String id){
		documentos = service.getDocumentosByLancamento(Long.valueOf(id));
	}
	
	public void listarLancamentosEmAcoes(){
		lancamentosEmAcoes = service.getLancamentosEmAcoes(lancamento.getId());
	}
	
	
	public void visualizarArquivo() throws IOException {
		DownloadUtil.downloadFile(arqAuxiliar.getNome(), arqAuxiliar.getPath(), "application/pdf",
				FacesContext.getCurrentInstance());
		arqAuxiliar = new ArquivoLancamento();
	}

	
	
	public GerenciadorLancamentoController(){}


	public LancamentoAuxiliar getLancamento() {
		return lancamento;
	}


	public void setLancamento(LancamentoAuxiliar lancamento) {
		this.lancamento = lancamento;
	}


	public List<LancamentoAuxiliar> getLancamentos() {
		return lancamentos;
	}


	public void setLancamentos(List<LancamentoAuxiliar> lancamentos) {
		this.lancamentos = lancamentos;
	}

//	public List<PagamentoPE> getPagamentos() {
//		return pagamentos;
//	}
//
//	public void setPagamentos(List<PagamentoPE> pagamentos) {
//		this.pagamentos = pagamentos;
//	}


	public Filtro getFiltro() {
		return filtro;
	}


	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}


	public List<LancamentoAcao> getLancamentosEmAcoes() {
		return lancamentosEmAcoes;
	}


	public void setLancamentosEmAcoes(List<LancamentoAcao> lancamentosEmAcoes) {
		this.lancamentosEmAcoes = lancamentosEmAcoes;
	}


	public List<PagamentoLancamento> getPagamentos() {
		return pagamentos;
	}


	public void setPagamentos(List<PagamentoLancamento> pagamentos) {
		this.pagamentos = pagamentos;
	}


	public List<ArquivoLancamento> getDocumentos() {
		return documentos;
	}


	public void setDocumentos(List<ArquivoLancamento> documentos) {
		this.documentos = documentos;
	}


	public ArquivoLancamento getArqAuxiliar() {
		return arqAuxiliar;
	}


	public void setArqAuxiliar(ArquivoLancamento arqAuxiliar) {
		this.arqAuxiliar = arqAuxiliar;
	}
	
	
	
		
}
