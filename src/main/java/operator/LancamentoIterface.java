package operator;

import model.Lancamento;

public interface LancamentoIterface {

	public void autorizar(Lancamento lancamento);

	public void desautorizar(Lancamento lancamento, String texto);
	
	
	public void imprimir(Lancamento lancamento);
	
}
