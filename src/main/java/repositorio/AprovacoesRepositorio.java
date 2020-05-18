package repositorio;

import java.io.Serializable;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.LancamentoAuxiliar;
import util.Filtro;

public class AprovacoesRepositorio implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Inject	
	protected EntityManager manager;
	
	public AprovacoesRepositorio(EntityManager manager) {
		this.manager = manager;
	}
	
	public AprovacoesRepositorio() {
	}
	
	private String modifyDateLayout(String inputDate) throws ParseException{
	    Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.S").parse(inputDate);
	    return new SimpleDateFormat("dd/MM/yyyy").format(date);
	}
	
	

	public List<LancamentoAuxiliar> filtar(Filtro filtro) {
		StringBuilder sql = 
				new StringBuilder("SELECT l.id,l.data_emissao,(select f.nome_fantasia ");
		sql.append(" from fornecedor f where f.id = (select cb.fornecedor_id from");
		sql.append(" conta_bancaria cb where cb.id = l.contarecebedor_id)),l.valor_total_com_desconto from lancamento l");		
		sql.append(" where l.statuscompra = 'N_INCIADO' or  l.statuscompra = 'EM_COTACAO' or l.statuscompra = null");
		sql.append(" and  l.versionlancamento = 'MODE01' order by l.data_emissao desc ");
		
		Query query = this.manager.createNativeQuery(sql.toString());
		List<Object[]> result = query.getResultList().isEmpty()?new ArrayList<>():query.getResultList();
		
		List<LancamentoAuxiliar> lancamentos = new ArrayList<LancamentoAuxiliar>();
		try {
			for (Object[] object : result) {
				
				SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
				LancamentoAuxiliar lancamento = new LancamentoAuxiliar();
				lancamento.setCodigoLancamento(object[0]==null?"Não Informado":object[0].toString());
				lancamento.setDataEmissaoString(object[1]==null?"Não Informado":modifyDateLayout(object[1].toString()));
				lancamento.setLabelFornecedor(object[2]==null?"Não Informado":object[2].toString());
				lancamento.setValorLancamento(object[3]==null?new BigDecimal("0"):new BigDecimal(object[3].toString()));
				lancamentos.add(lancamento);
			}
		} catch (Exception e) {
			System.out.println("deu erro");
		}
		
		
		return lancamentos;
	}

}
