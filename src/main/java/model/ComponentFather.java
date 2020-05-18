package model;



import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class ComponentFather extends Acao{

	private static final long serialVersionUID = 1L;
	
	private TreeNode node;
	
	public ComponentFather(String descricao, TreeNode root){
		setDescricao(descricao);
		this.node = new DefaultTreeNode(this, root);
	}

	public ComponentFather(String descricao){
		setDescricao(descricao);
	}
	
	public TreeNode getNode() {
		return node;
	}

	public void setNode(TreeNode node) {
		this.node = node;
	}

	

	
	
}
