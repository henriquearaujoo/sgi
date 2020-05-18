package util;


import java.util.ArrayList;
import java.util.List;

import model.MenuLateral;

public class MakeMenu {

	
	public MakeMenu(){
		
	}
	

	
	
	public static List<MenuLateral> getMenuRelatorios() {

		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Bens", "relatorios", "fa fa-shopping-cart");
		lista.add(menu);
		menu = new MenuLateral("Ações", "produtos", "fa fa-database");
		lista.add(menu);
		
		return lista;
	}

	
	
	public static List<MenuLateral> getMenuFinanceiro(){
		
		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Solicitaçao de Pagamento", "pagamento", "fa fa-shopping-cart");
		lista.add(menu);
		menu = new MenuLateral("Solicitacao de Viagem", "SolicitacaoViagem", "fa fa-fighter-jet");
		lista.add(menu);
//		menu = new MenuLateral("Solicitação de Diaria", "SolicitacaoDiaria", "fa fa-user");
//		lista.add(menu);
		menu = new MenuLateral("Lançamentos", "lancamentos", "fa fa-money");
		lista.add(menu);
		menu = new MenuLateral("LançamentosMODE01", "lancamentosMODE01", "fa fa-money");
		lista.add(menu);
		menu = new MenuLateral("Lançamentos avulsos", "lancamento_avulso", "fa fa-money");
		lista.add(menu);
		menu = new MenuLateral("Relatórios", "relatorios_fin", "fa fa-money");
		lista.add(menu);
//		menu = new MenuLateral("Relatórios F", "relatorio_financeiros", "fa fa-money");
//		lista.add(menu);
		menu = new MenuLateral("Doações", "orcamentos", "fa fa-money");
		lista.add(menu);
		menu = new MenuLateral("Adiantamentos", "prestacaodecontas", "fa fa-money");
		lista.add(menu);
		menu = new MenuLateral("Bancos", "bancos", "fa fa-money");
		lista.add(menu);
		
		menu = new MenuLateral("Contas Bancárias", "contas", "fa fa-money");
		lista.add(menu);
		
		menu = new MenuLateral("Liberação/Lançamentos", "liberacao_lancamento", "fa fa-money");
		lista.add(menu);
		
//		menu = new MenuLateral("Formulario de campo", "formulario_campo", "fa fa-fighter-jet");
//		lista.add(menu);
		
		
		return lista;
				
	}
	
	
	public static List<MenuLateral> getMenuBolsaFloresta(){
		
		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Solicitacao de Viagem", "SolicitacaoViagem", "fa fa-fighter-jet");
		lista.add(menu);
		menu = new MenuLateral("Solicitação de Diaria", "SolicitacaoDiaria", "fa fa-user");
		lista.add(menu);
		menu = new MenuLateral("Formulario de campo", "formulario_campo", "fa fa-fighter-jet");
		lista.add(menu);
		
		
		return lista;
				
	}
	
	
	
	public static List<MenuLateral> getMenuCompra(){
		
		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Solicitação de Compras", "compras", "fa fa-shopping-cart");
		lista.add(menu);
		
		menu = new MenuLateral("Pedidos", "gerenciamento_pedido", "fa fa-file-code-o");
		lista.add(menu);
		
		menu = new MenuLateral("Produtos", "produtos", "fa fa-database");
		lista.add(menu);
//		lista.add(menu);
//		menu = new MenuLateral("Fornecedores", "fornecedores", "fa fa-user");
		
		
		

		return lista;
	}
	
	
	
	public static List<MenuLateral> getMenuConfigurações(){
		List<MenuLateral> lista = new ArrayList<>();
		MenuLateral menu = new MenuLateral("Usuarios", "cadastro_usuarios_att", "fa fa-user-plus");
		lista.add(menu);
		menu = new MenuLateral("Aprovadores", "aprovadores", "fa fa-lock");
		lista.add(menu);
		menu = new MenuLateral("Categorias", "categoria", "fa fa-cog");
		lista.add(menu);
		menu = new MenuLateral("Produtos", "produtos", "fa fa-database");
		lista.add(menu);
		
		menu = new MenuLateral("Fornecedores", "fornecedores", "fa fa-group");
		lista.add(menu);
		
		menu = new MenuLateral("Terceiros", "fornecedores_terceiros", "fa fa-group");
		lista.add(menu);
		
		menu = new MenuLateral("Rubricas", "rubrica", "fa fa-money");
		lista.add(menu);
		
		menu = new MenuLateral("Gestão", "gestao", "fa fa-user-plus");
		lista.add(menu);
		
		menu = new MenuLateral("Configuração", "configuracaoTrava","fa fa-gears");
		lista.add(menu);
		return lista;
	}
	
	
	
	
	
	public static List<MenuLateral> getMenuLogistica(){
		
		List<MenuLateral> lista = new ArrayList<>();
		/*MenuLateral menu = new MenuLateral("Solicitação de expedição", "solicitacao_expedicao", "fa fa-cart-arrow-down");
		lista.add(menu);*/
		MenuLateral menu = new MenuLateral("Controle de expedição", "controle_expedicao", "fa fa-cart-arrow-down");
		lista.add(menu);
		menu = new MenuLateral("Termos", "termo_expedicao", "fa fa-file");
		lista.add(menu);
		
		
		return lista;
	}
	
	public static List<MenuLateral> getMenuPBF(){
		List<MenuLateral> lista = new ArrayList<>();
		
		MenuLateral menu = new MenuLateral("Familiar", "cadastro_familiar", "fa fa-user");
		lista.add(menu);
		menu = new MenuLateral("Comunitário", "cadastro_comunitario", "fa fa-user");
		lista.add(menu);
		
		return lista;
	}
	
	
	
}


/*---------------------------Como utilizar-----------------------------------------------------*/

/* 
	private MakeMenu hybrid = new MakeMenu();
	
	
	private List<MenuLateral> utilMenu = hybrid.getMenuFinanceiro();
	
*/