package model;



import java.math.BigDecimal;

import javax.persistence.MappedSuperclass;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

@MappedSuperclass
public class ComponentAtividade extends AtividadeAcao{

	private static final long serialVersionUID = 1L;
	
	
	private String descricao;
	private BigDecimal orcado;
	private BigDecimal realizado;
	
	private TreeNode node;
	
	public ComponentAtividade(){}
	
	public ComponentAtividade(String descricao, TreeNode root){
		setDescricao(descricao);;
		this.node = new DefaultTreeNode(this, root);
	}

	public ComponentAtividade(String descricao){
		setDescricao(descricao);
	}
	
	public TreeNode getNode() {
		return node;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public BigDecimal getOrcado() {
		return orcado;
	}

	public void setOrcado(BigDecimal orcado) {
		this.orcado = orcado;
	}

	public BigDecimal getRealizado() {
		return realizado;
	}

	public void setRealizado(BigDecimal realizado) {
		this.realizado = realizado;
	}

	public int compareTo(ComponentAtividade o) {
		return 0;
	}

	

	
	
}
