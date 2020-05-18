package repositorio;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import model.Banco;
import model.ContaBancaria;
import model.Evento;
import util.Filtro;

public class EventoRepositorio implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;

	public EventoRepositorio() {
	}

	public Evento salvar(Evento evento) {
		return this.manager.merge(evento);
	}

	public void remover(Evento evento) {
		this.manager.remove(this.manager.find(Evento.class, evento.getId()));
	}

	public Evento findById(Long id) {
		return this.manager.find(Evento.class, id);
	}

	public List<Evento> getAll(Filtro filtro) {
		StringBuilder jpql = new StringBuilder("from Evento as e where 1 = 1 ");

		if (filtro.getTitulo() != null && !filtro.getTitulo().equals("")) {
			jpql.append("and lower(e.titulo) like lower(:titulo) ");
		}
		
		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			jpql.append("and lower(e.descricao) like lower(:descricao) ");
		}
		
		jpql.append(" order by e.id desc ");
		
		Query query = this.manager.createQuery(jpql.toString());

		if (filtro.getTitulo() != null && !filtro.getTitulo().equals("")) {
			query.setParameter("titulo", "%" + filtro.getTitulo() + "%");
		}
		
		if (filtro.getDescricao() != null && !filtro.getDescricao().equals("")) {
			query.setParameter("titulo", "%" + filtro.getTitulo() + "%");
		}
	
		return query.getResultList();
	}
	
	
	public List<Evento> getResumeList() {
		StringBuilder jpql = new StringBuilder("SELECT NEW Evento(e.id,e.titulo, e.descricao, e.dataInicio, e.dataFim, e.linkReuniao, e.linkArquivo) FROM Evento as e ");
			
		jpql.append(" order by e.id desc ");
		
		Query query = this.manager.createQuery(jpql.toString());

		return query.getResultList();
	}

}
