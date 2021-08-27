package repositorio.management.panel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import repositorio.management.panel.models.Filtro;
import repositorio.management.panel.models.Management;
import repositorio.management.panel.models.Project;
import repositorio.management.panel.models.Source;
import util.Util;

public class FilterRepositorio {

	@Inject
	private EntityManager manager;

	public FilterRepositorio() {
	}

	public FilterRepositorio(EntityManager manager) {
		this.manager = manager;
	}

	
	public List<Management> loadManagements(Filtro filtro) {
		Query query = this.manager.createNativeQuery(getSqlToManagements(filtro));
		//setQuery(query, filtro);
		List<Object[]> result =  query.getResultList();
		List<Management> managements = new ArrayList<Management>();
		
		
		for (Object[] object : result) {
			Management management = new Management(new Long(object[0].toString()), object[1].toString());
			managements.add(management);
		}
		
		return managements;
	}
	
	public List<Project> loadProjects(Filtro filtro) {
		Query query = this.manager.createNativeQuery(getSqlToProjects(filtro));
		//setQuery(query, filtro);
		List<Object[]> result =  query.getResultList();
		List<Project> projects = new ArrayList<Project>();
	
		for (Object[] object : result) {
			Project project = new Project(new Long(object[0].toString()), Util.getNullValue(object[1], "") , object[2].toString());
			projects.add(project);
		}
		
		return projects;
	}
	
	public List<Source> loadSources(Filtro filtro) {
		Query query = this.manager.createNativeQuery(getSqlToSources(filtro));
		//setQuery(query, filtro);
		List<Object[]> result =  query.getResultList();
		List<Source> sources = new ArrayList<Source>();
		
		
		for (Object[] object : result) {
			Source source = new Source(new Long(object[0].toString()), object[1].toString());
			sources.add(source);
		}
		
		return sources;
	}

	public void setQuery(Query query, Filtro filtro) {
		query.setParameter("gestao_id", filtro.getGestao());
	}
	
	public String getSqlToManagements(Filtro filtro) {
		StringBuilder hql = new StringBuilder("select ");		
		hql.append("g.id, g.nome from gestao g where g.id = 36 or ");//TODO : ADICIONAR ID_GESTAO
		hql.append("g.superintendencia_id  = 36 ");
		String result = hql.toString();
		return result;
	}

	public String getSqlToProjects(Filtro filtro) {
		StringBuilder hql = new StringBuilder("select ");
		hql.append("p.id, p.codigo , p.nome ");
		hql.append("from projeto p where p.gestao_id = 36 or ");//TODO: ADICIONAR ID_GESTAO
		hql.append("((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = 36) "); //TODO: ADICIONAR ID_GESTAO 
		String result = hql.toString();
		return result;
	}
	
	public String getSqlToSources(Filtro filtro) {
		StringBuilder hql = new StringBuilder("select ");
		hql.append(" fp.id, fp.nome from projeto p ");
		hql.append("join  projeto_rubrica pr on pr.projeto_id = p.id ");
		hql.append("join rubrica_orcamento rb on rb.id = pr.rubricaorcamento_id ");
		hql.append("join orcamento o on o.id = rb.orcamento_id ");
		hql.append("join fonte_pagadora fp on fp.id = o.fonte_id ");
		hql.append("where p.id in  ");
		hql.append("(select p.id from projeto p where p.gestao_id = 36 or ");
		hql.append("((select g.superintendencia_id from gestao g where g.id = p.gestao_id) = 36)) ");// TODO: ID_GESTAO
		hql.append("group by fp.id, fp.nome ");
		String result = hql.toString();
		return result;
	}

}
