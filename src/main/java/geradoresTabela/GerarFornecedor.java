package geradoresTabela;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.Estado;
import model.Fornecedor;
import util.JpaUtil;

public class GerarFornecedor {

	public static void main(String[] args){

		EntityManager manager = JpaUtil.getEntityManager();
	    EntityTransaction tx = manager.getTransaction();
	    tx.begin();
		
	    
	    criarFornecedor2(manager);
        

        
    	//criarComunidade(manager);
    
    	tx.commit();
    	manager.close();
    	
    	
	
	
	}
	
	
	
	
	public static void criarFornecedor(EntityManager manager){
		try {
			
			
			/*A classe Fornecedor precisa ser modificada pois esta faltando alguns campos relativos ao formulario enviado*/
			/* Vou deixar ao teu criterio oque deve ser adicionado, no entanto ja mapeei o csv segue a baixo o codigo*/
			
			//FileInputStream stream = new FileInputStream("C:/DEV/fornecedores.csv");
			//FileInputStream stream = new FileInputStream("");
			
			
			FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/fornecedores.csv");
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			
			int cont = 0;
			
			
			while (br.ready()) {
				
				
				String linha[] = br.readLine().split(";");
				String nomeFornecedor = linha[0] != null ? linha[0] : "";

				String endereco = linha[1] != null ? linha[1] : "";

				//Integer cidade = linha[2] != null ? Integer.valueOf(linha[2]) : null;
				String nestado = linha[3];

				String cnpj = linha[4] != null ? linha[4] : "";
				String ie = linha[5] != null ? linha[5] : "";
				String im = linha[6] != null ? linha[6] : "";
				String email = linha[7] != null ? linha[7] : "";
				String website = linha[8] != null ? linha[8] : "";
				String telefone = linha[9] != null ? linha[9] : "";
				String fax	= linha[10] != null ? linha[10] : "";
				//String observacoes = linha[11] != null ? linha[11] : "";
				String banco = linha[11] != null ? linha[11] : "";
				String agencia = linha[12] != null ? linha[12] : "";
				String conta = linha[13] != null ? linha[13] : "";
				String id_atividade = linha[14] != null ? linha[14] : "";
				String nomeFantasia = linha[15] != null ? linha[15] : "";
				String cep = linha[16] != null ? linha[16] : "";
				String bairro = linha[17] != null ? linha[18] : "";	
				String pis = linha[19] != null ? linha[19] : "";
				
				
				
				
				
			
				/*gambiarra pra pular uma linha do csv*/
				if(cont > 0){
					
					
					Estado estado = getEstado(manager, "amazonas");
					
					Fornecedor fornecedor = new Fornecedor();
					fornecedor.setRazaoSocial(nomeFornecedor);

					fornecedor.setEndereco(endereco);
					fornecedor.setEstado(estado);
					fornecedor.setCnpj(cnpj);
					fornecedor.setInscricaoEstadual(ie);
					fornecedor.setEmail(email);
					fornecedor.setTelefone(telefone);
				//	fornecedor.setObservacao(observacoes);
					fornecedor.setBanco(banco);
					fornecedor.setAgencia(agencia);
					fornecedor.setConta(conta);
					fornecedor.setNomeFantasia(nomeFantasia);
					fornecedor.setCep(cep);
					fornecedor.setBairro(bairro);
					fornecedor.setPis(pis); 
					
					
					
					
					manager.persist(fornecedor);
				}
				
				cont++;
				
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}
	
	public static void criarFornecedor2(EntityManager manager){
		try {
			
			
			/*A classe Fornecedor precisa ser modificada pois esta faltando alguns campos relativos ao formulario enviado*/
			/* Vou deixar ao teu criterio oque deve ser adicionado, no entanto ja mapeei o csv segue a baixo o codigo*/
			
			//FileInputStream stream = new FileInputStream("C:/DEV/fornecedores.csv");
			//FileInputStream stream = new FileInputStream("");
			
			
			FileInputStream stream = new FileInputStream("C:/fas/fornecedores.csv");
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			
			int cont = 0;
			
			
			while (br.ready()) {
				
				
				String linha[] = br.readLine().split(";");
				String nomeFornecedor = linha[0] != null ? linha[0] : "";
				
			
				/*gambiarra pra pular uma linha do csv*/
				if(cont > 0){
					
					
					//Estado estado = getEstado(manager, "amazonas");
					
					Fornecedor fornecedor = new Fornecedor();
					fornecedor.setRazaoSocial(nomeFornecedor);
					fornecedor.setNomeFantasia(nomeFornecedor);
					
					
					manager.persist(fornecedor);
					
				}
				
				cont++;
				
			}
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
	}

	
	
	
	
	public static Estado getEstado(EntityManager manager, String nome){
		Query query = manager.createQuery("from Estado where lower(nome) like lower(:nome)");
		query.setParameter("nome", "%"+nome+"%");
		Estado estado = (Estado) query.getResultList().get(0);
		return estado;		
	}
	
	
}