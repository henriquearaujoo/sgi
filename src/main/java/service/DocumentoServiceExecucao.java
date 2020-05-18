package service;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.bouncycastle.asn1.isismtt.ocsp.CertHash;
import org.primefaces.model.CheckboxTreeNode;
import org.primefaces.model.TreeNode;

import model.Coordenadoria;
import model.Gestao;
import model.Projeto;
import model.Regional;
import model.Superintendencia;
import util.Document;

public class DocumentoServiceExecucao implements Serializable{

	/**
	 * '
	 */
	private static final long serialVersionUID = 1L;
	
	
	public TreeNode createTreeNodeProjeto(ContaService service){
		TreeNode root = new CheckboxTreeNode(new Document("Files", "-", new Long(0)), null);
			
		List<Superintendencia> superintendencias = service.buscarSuperintendencia();
		List<Coordenadoria> coordenadorias;
		List<Regional> regionais;
		List<Projeto> projetos;
 		for (Gestao gestao : superintendencias) {
 			TreeNode sup =  new CheckboxTreeNode(new Document(gestao.getNome(), gestao.getType(), gestao.getId()), root);
 			projetos = new ArrayList<>();
 			projetos = service.buscarProjetoByGestao(gestao.getId());
 			
 			for (Projeto projeto : projetos) {
				TreeNode proj = new CheckboxTreeNode(new Document(projeto.getNome(), "proj", projeto.getId()), sup);
			}
 			
 			
 			coordenadorias = new ArrayList<>();
			coordenadorias =  service.buscarCoordenadoriaBySup(gestao.getId());
			
			for (Coordenadoria coordenadoria : coordenadorias) {
				TreeNode coord =  new CheckboxTreeNode(new Document(coordenadoria.getNome(), coordenadoria.getType() , coordenadoria.getId()), sup);
				regionais = new ArrayList<>();
				regionais = service.buscarSubCategoriaByCoord(coordenadoria.getId());
				
				projetos = service.buscarProjetoByGestao(coordenadoria.getId());
				for (Projeto projeto : projetos) {
					TreeNode proj = new CheckboxTreeNode(new Document(projeto.getNome(), "proj", projeto.getId()), coord);
				}
				
				for (Regional regional : regionais) {
					TreeNode reg = new  CheckboxTreeNode(new Document(regional.getNome(), regional.getType(), regional.getId()), coord);
					projetos = service.buscarProjetoByGestao(regional.getId());
					for (Projeto projeto : projetos) {
						TreeNode proj = new CheckboxTreeNode(new Document(projeto.getNome(), "proj", projeto.getId()), reg);
					}
					
				}
			}
			
		}
		
		
		
		return root;
	}

	
	
	
	
	
}
