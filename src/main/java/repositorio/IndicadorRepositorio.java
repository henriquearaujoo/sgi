package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Indicador;
import model.Projeto;

public class IndicadorRepositorio implements Serializable {

		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		@Inject
		private EntityManager manager;

		public IndicadorRepositorio() {
		}
		
//		@Transactional
		public Indicador salvarIndicador(Indicador indicador) {
			return this.manager.merge(indicador);
		}

		public void removerIndicador(Indicador indicador) {
			this.manager.remove(this.manager.find(Indicador.class, indicador.getId()));
		}

		public Indicador findIndicadorById(Long id) {
			return this.manager.find(Indicador.class, id);
		}

		public List<Indicador> findIndicadorByProjeto(Projeto projeto) {
			StringBuilder jpql = new StringBuilder("select i from  Indicador i where i.projeto = :projeto order by i.nome ");
			Query query = manager.createQuery(jpql.toString());
			query.setParameter("projeto", projeto);
			return query.getResultList().size() > 0  ? query.getResultList() : (List) new ArrayList<Projeto>();
			
		}

	

		

	}


