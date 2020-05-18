package repositorio;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import auxiliar.RelatorioItensPorUc;
import model.Projeto;
import model.UnidadeConservacao;
import util.Filtro;

public class RelatorioRepositorio implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;

	public RelatorioRepositorio() {
	}

	
	public RelatorioRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	public Projeto getProjetoPorId(Long id) {
		return this.manager.find(Projeto.class, id);
	}

	
	public List<UnidadeConservacao> getUcs(){
		String jpql = "from UnidadeConservacao";
		Query query = manager.createQuery(jpql);
		return query.getResultList();
	}
	
	public List<RelatorioItensPorUc> getListagemParaRelatorio(Filtro filtro){
		StringBuilder jpql = new StringBuilder("select pe.id as pedido,date(pe.data_emissao) as emissao,"); 
				      jpql.append("case loc.tipo ");
		  			  jpql.append("when 'com' then (select l.mascara from localidade l where l.id = loc.unidadeconservacao_id) ");
		  			  jpql.append("when 'uc' then loc.mascara ");
		  			  jpql.append("else ''  end as uc, ");
		  			  jpql.append("case loc.tipo when 'uc' then '' else loc.nome end as comunidade, ");
		  			  jpql.append("ip.descricaoproduto as produto, ");
		  			  jpql.append("(select po.categoria from cotacao co ");
		  			  jpql.append("left join item_compra ic on co.itemcompra_id = ic.id ");
		  			  jpql.append("left join produto po on ic.produto_id = po.id  ");
		  			  jpql.append("where co.id = ip.cotacao_id) as categoria,ip.tipo as tipo, ");
		  			  jpql.append("ip.quantidade as quantidade, ");
		  			  jpql.append("ip.valor_unitario_sem_desconto as valor_sem_desconto, ");
		  			  jpql.append("ip.valor_unitario_com_desconto as valor_com_desconto, ");
		  			  jpql.append("(ip.quantidade * ip.valor_unitario_sem_desconto) as total_sem_desconto, ");
		  			  jpql.append("(ip.quantidade * ip.valor_unitario_com_desconto) as total_com_desconto ");
		  			  jpql.append("from item_pedido ip left join ");
		  			  jpql.append("lancamento pe on ip.pedido_id = pe.id  ");
		  			  jpql.append("left join localidade loc on pe.localidade_id = loc.id ");
		  			  jpql.append("where (loc.tipo = 'com' or loc.tipo = 'uc')  ");
		  			  jpql.append("and date(pe.data_emissao) between  date('"+new SimpleDateFormat("yyyy-MM-dd").format(filtro.getDataInicio())+"') and  date('"+new SimpleDateFormat("yyyy-MM-dd").format(filtro.getDataFinal())+"') ");
		  			  // colocar no formato de data do banco postgresql
		  			  if(filtro.getLocalidadeID() != null){
		  				  jpql.append(" and (loc.id = "+filtro.getLocalidadeID()+"or loc.unidadeconservacao_id = "+filtro.getLocalidadeID()+")");
		  			  }
		  			  
		  			  
		Query query =  manager.createNativeQuery(jpql.toString());
		//query.setParameter("dt_inicio", "date('"+new SimpleDateFormat("yyyy-mm-dd").format(filtro.getDataInicio())+"')");
		//query.setParameter("dt_final", "date('"+new SimpleDateFormat("yyyy-mm-dd").format(filtro.getDataFinal())+"')");
		
		List<Object[]> result = query.getResultList();
		List<RelatorioItensPorUc> listagem = new ArrayList<RelatorioItensPorUc>();
		for (Object[] objects : result) {
			RelatorioItensPorUc rel = new RelatorioItensPorUc();
			rel.setPedido(objects[0].toString());
			//rel.setEmissao(emissao);
			rel.setUc(objects[2].toString());
			rel.setComunidade(objects[3].toString());
			rel.setProduto(objects[4].toString());
			rel.setCategoria(objects[5].toString());
			rel.setTipo(objects[6].toString());
			rel.setQuantidade(Double.valueOf(objects[7].toString()));
			rel.setValorSemDesconto(Double.valueOf(objects[8].toString()));
			rel.setValorComDesconto(Double.valueOf(objects[9].toString()));
			rel.setTotalSemDesconto(Double.valueOf(objects[10].toString()));
			rel.setTotalComDesconto(Double.valueOf(objects[11].toString()));
			
			listagem.add(rel);
			
			
		}
		
		return listagem;
	}
	
	

	

	

}
