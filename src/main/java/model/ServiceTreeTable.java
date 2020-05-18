package model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class ServiceTreeTable {

	private Map<String, ComponentFather> leafs;
	private TreeNode root;

	public ServiceTreeTable() {
		root = new DefaultTreeNode(new ComponentFather("root"), null);
	}

	public TreeNode getRoot(List<Acao> acoes) {

		leafs = new HashMap<String, ComponentFather>();
		for (Acao acao : acoes) {
			createComponentFather(acao.getCodigo(), acao);
		}
		return root;
	}

	private void createComponentFather(String key, Acao acao) {
		if (!leafs.containsKey(key)) {
			leafs.put(key, new ComponentFather(key, root));
		}
		
		TreeNode node = new DefaultTreeNode(acao, leafs.get(acao.getCodigo()).getNode());

	}

}
