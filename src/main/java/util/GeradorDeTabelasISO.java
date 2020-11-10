//package util;
//
//import java.io.BufferedReader;
//import java.io.FileInputStream;
//import java.io.FileNotFoundException;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.math.BigDecimal;
//import java.util.Date;
//import java.util.List;
//
//import javax.persistence.EntityManager;
//import javax.persistence.EntityTransaction;
//import javax.persistence.Query;
//
//import model.Acao;
//import model.Colaborador;
//import model.Comunidade;
//import model.ContaBancaria;
//import model.Coordenadoria;
//import model.Estado;
//import model.FontePagadora;
//import model.Fornecedor;
//import model.Gestao;
//import model.Localidade;
//import model.Municipio;
//import model.Nucleo;
//import model.Perfil;
//import model.Produto;
//import model.Regional;
//import model.Sede;
//import model.Superintendencia;
//import model.UnidadeConservacao;
//import model.User;
//
//
//public class GeradorDeTabelasISO {
//	
//	public static void main(String[] args) {
//	  
//		EntityManager manager = JpaUtil.getEntityManager();
//	    EntityTransaction tx = manager.getTransaction();
//	    tx.begin();
//		
//			//criarComunidade(manager);
//			//System.out.println("Comunidade cadastradas");
//	        //criarUnidade(manager);
//	        //System.out.println("UCS criadas");
//		  /*  criarNucleos(manager);
//		    System.out.println("N�cleos cadastrados");
//		    criarEstado(manager);
//		    System.out.println("Estados cadastrados");
//			criarMunicipio(manager);
//			System.out.println("Municipios cadastrados");
//			criarSede(manager);
//			System.out.println("Sedes cadastradas");
//		    criarGestao(manager);
//		    System.out.println("Gest�es cadastradas");
//			criarUsuario(manager);
//			System.out.println("Usuarios cadastrados");
//	    	criarFonteDeRecurso(manager);
//	    	System.out.println("Fontes cadastradas");
//	        criarProduto(manager);
//	        System.out.println("Produtos cadastrados");
//	        criarConta(manager);
//	        System.out.println("Contas cadastradas");*/
//	        /*criarFornecedor(manager);
//	        System.out.println("Fornecedores cadastrados");*/
//	    	System.out.println("***********************************************");
//	    	System.out.println("***********************************************");
//	    	
//		
//	        //criarColaborador(manager);
//	        
//	    	System.out.println("Banco populado!!!");
//	        
//	    	//criarComunidade(manager);
//	    
//	    tx.commit();
//		manager.close();
//	}
//	
//	
//	public static void criarFonteDeRecurso(EntityManager manager){
//		
//		FontePagadora fonte = new FontePagadora();
//		fonte.setNome("Bradesco S/A");
//		
//		FontePagadora fonte2 = new FontePagadora();
//		fonte2.setNome("Coca Cola");
//		
//		FontePagadora fonte3 = new FontePagadora();
//		fonte3.setNome("Fundo Amaz�nia");
//		
//		FontePagadora fonte4 = new FontePagadora();
//		fonte4.setNome("Samsung LTDA");
//		
//		FontePagadora fonte5 = new FontePagadora();
//		fonte5.setNome("Instituto Lojas Renner");
//		
//		FontePagadora fonte6 = new FontePagadora();
//		fonte6.setNome("Camargo Correia");
//		
//		FontePagadora fonte7 = new FontePagadora();
//		fonte7.setNome("INNOVA S/A");
//		
//		FontePagadora fonte8 = new FontePagadora();
//		fonte8.setNome("ABRIL Comunica��es S/A");
//		
//		FontePagadora fonte9 = new FontePagadora();
//		fonte9.setNome("FUMCAD MARA�");
//		
//		FontePagadora fonte10 = new FontePagadora();
//		fonte10.setNome("FUMCAD UARINI");
//		
//		FontePagadora fonte11 = new FontePagadora();
//		fonte11.setNome("EMS");
//		
//		FontePagadora fonte12 = new FontePagadora();
//		fonte12.setNome("SAP");
//		
//		FontePagadora fonte13 = new FontePagadora();
//		fonte13.setNome("BEMOL");
//		
//		FontePagadora fonte14 = new FontePagadora();
//		fonte14.setNome("TIM");
//		
//		FontePagadora fonte15 = new FontePagadora();
//		fonte15.setNome("UNICEF");
//		
//		FontePagadora fonte16 = new FontePagadora();
//		fonte16.setNome("SEBRAE/AM");
//		
//		FontePagadora fonte17 = new FontePagadora();
//		fonte17.setNome("ICCO");
//		
//		FontePagadora fonte18 = new FontePagadora();
//		fonte18.setNome("BID");
//		
//		FontePagadora fonte19 = new FontePagadora();
//		fonte19.setNome("Natura");
//		
//		FontePagadora fonte20 = new FontePagadora();
//		fonte20.setNome("Vale S/A");
//		
//		FontePagadora fonte21 = new FontePagadora();
//		fonte21.setNome("Bernard Van Leer Foundation");
//		
//		FontePagadora fonte22 = new FontePagadora();
//		fonte22.setNome("UNEP - United Nations Envitoment Programme");
//		
//		FontePagadora fonte23 = new FontePagadora();
//		fonte23.setNome("United Nations Development Programme");
//		
//		FontePagadora fonte24 = new FontePagadora();
//		fonte24.setNome("Europen Florest Institute");
//		
//		FontePagadora fonte25 = new FontePagadora();
//		fonte25.setNome("MARRIOT");
//		
//		FontePagadora fonte26 = new FontePagadora();
//		fonte26.setNome("SIATER");
//		
//		manager.persist(fonte);
//		manager.persist(fonte2);
//		manager.persist(fonte3);
//		manager.persist(fonte4);
//		manager.persist(fonte5);
//		manager.persist(fonte6);
//		manager.persist(fonte7);
//		manager.persist(fonte8);
//		manager.persist(fonte9);
//		manager.persist(fonte10);
//		manager.persist(fonte11);
//		manager.persist(fonte12);
//		manager.persist(fonte13);
//		manager.persist(fonte14);
//		manager.persist(fonte15);
//		manager.persist(fonte16);
//		manager.persist(fonte17);
//		manager.persist(fonte18);
//		manager.persist(fonte19);
//		manager.persist(fonte20);
//		manager.persist(fonte21);
//		manager.persist(fonte22);
//		manager.persist(fonte23);
//		manager.persist(fonte24);
//		manager.persist(fonte25);
//		manager.persist(fonte26);
//		
//	}
//	
//	public static void criarGestao(EntityManager manager){
//		
//		Superintendencia supGer = new Superintendencia();
//		supGer.setNome("Geral");
//		supGer.setSigla("SG");
//		
//		Superintendencia supTec = new Superintendencia();
//		supTec.setNome("T�cnico-Cient�fico");
//		supTec.setSigla("STC");
//		
//		Superintendencia supAdm = new Superintendencia();
//		supAdm.setNome("Adm-Financeiro");
//		supAdm.setSigla("SAF");
//		
//		Coordenadoria coord = new Coordenadoria();
//		coord.setNome("Programa Bolsa Floresta");
//		coord.setSuperintendencia(supTec);
//		coord.setSigla("PBF");
//		
//		Coordenadoria coord2 = new Coordenadoria();
//		coord2.setNome("Projetos T�cnicos");
//		coord2.setSuperintendencia(supTec);
//		coord2.setSigla("CPT");
//		
//		Coordenadoria coord3 = new Coordenadoria();
//		coord3.setNome("Log�stica");
//		coord3.setSuperintendencia(supTec);
//		coord3.setSigla("LOG");
//	
//		Coordenadoria coord4 = new Coordenadoria();
//		coord4.setNome("Projetos Especiais");
//		coord4.setSuperintendencia(supTec);
//		coord4.setSigla("CPE");
//		
//		Coordenadoria coord5 = new Coordenadoria();
//		coord5.setNome("Educa��o e Sa�de");
//		coord5.setSuperintendencia(supTec);
//		coord5.setSigla("PES");
//		
//		Coordenadoria coord6 = new Coordenadoria();
//		coord6.setNome("SDSN AM");
//		coord6.setSuperintendencia(supTec);
//		coord6.setSigla("SDSN");
//		
//		Coordenadoria coord7 = new Coordenadoria();
//		coord7.setNome("Articula��o Institucional");
//		coord7.setSuperintendencia(supTec);
//		coord7.setSigla("CAI");
//		
//		Coordenadoria coord8 = new Coordenadoria();
//		coord8.setNome("Administrativa Financeira");
//		coord8.setSuperintendencia(supAdm);
//		coord8.setSigla("ADM");
//		
//		Coordenadoria coord9 = new Coordenadoria();
//		coord9.setNome("Jur�dica");
//		coord9.setSuperintendencia(supAdm);
//		coord9.setSigla("JUR");
//	
//		Coordenadoria coord10 = new Coordenadoria();
//		coord10.setNome("T�cnologia da Informa��o");
//		coord10.setSuperintendencia(supAdm);
//		coord10.setSigla("CTI");
//		
//		Coordenadoria coord11 = new Coordenadoria();
//		coord11.setNome("Obras");
//		coord11.setSuperintendencia(supAdm);
//		coord11.setSigla("COP");
//		
//		Coordenadoria coord12 = new Coordenadoria();
//		coord12.setNome("Relacionamento Institucional");
//		coord12.setSuperintendencia(supGer);
//		coord12.setSigla("CRI");
//		
//		Coordenadoria coord13 = new Coordenadoria();
//		coord13.setNome("Comunica��o");
//		coord13.setSuperintendencia(supGer);
//		coord13.setSigla("COM");
//		
//		Coordenadoria coord14 = new Coordenadoria();
//		coord14.setNome("Desenv. Inst. e Parcerias");
//		coord14.setSuperintendencia(supGer);
//		coord14.setSigla("DIP");
//		
//		Regional solimoes = new Regional();
//		solimoes.setNome("Solim�es");
//		solimoes.setSigla("BFS");
//		
//		Regional jurua = new Regional();
//		jurua.setNome("Jutu�-Juta�");
//		jurua.setSigla("BFJ");
//		
//		Regional rioNegro = new Regional();
//		rioNegro.setNome("Negro-Amazonas");
//		rioNegro.setSigla("BFN");
//		
//		Regional madeira = new Regional();
//		madeira.setNome("Madeira");
//		madeira.setSigla("BFM");
//		
//
//		UnidadeConservacao uc4 = new UnidadeConservacao();
//		uc4.setNome("Reserva de Desenvolvimento Sustent�vel Canum�");
//		uc4.setMascara("RDS Camun�");
//		uc4.setRegional(rioNegro);
//		
//		
//		UnidadeConservacao uc7 = new UnidadeConservacao();
//		uc7.setNome("Reserva de Desenvolvimento Sustent�vel Puranga Conquista");
//		uc7.setMascara("RDS Puranga Conquista");
//		uc7.setRegional(rioNegro);
//		
//		UnidadeConservacao uc8 = new UnidadeConservacao();
//		uc8.setNome("Reserva de Desenvolvimento Sustent�vel Cujubim");
//		uc8.setMascara("RDS Cujubim");
//		uc8.setRegional(rioNegro);
//		
//		
//		UnidadeConservacao uc11 = new UnidadeConservacao();
//		uc11.setNome("Reserva de Desenvolvimento Sustent�vel Mamirau�");
//		uc11.setMascara("RDS Mamirau�");
//		uc11.setRegional(rioNegro);
//		
//		
//		
//		UnidadeConservacao uc13 = new UnidadeConservacao();
//		uc13.setNome("Reserva de Desenvolvimento Sustent�vel Aman�");
//		uc13.setMascara("RDS Aman�");
//		uc13.setRegional(rioNegro);
//		
//		UnidadeConservacao uc14 = new UnidadeConservacao();
//		uc14.setNome("Reserva de Desenvolvimento Sustent�vel Juma");
//		uc14.setMascara("RDS Juma");
//		uc14.setRegional(rioNegro);
//		
//		UnidadeConservacao uc15 = new UnidadeConservacao();
//		uc15.setNome("Reserva de Desenvolvimento Sustent�vel Rio Madeira");
//		uc15.setMascara("RDS Rio Madeira");	
//		uc15.setRegional(rioNegro);
//		
//		UnidadeConservacao uc16 = new UnidadeConservacao();
//		uc16.setNome("Reserva de Desenvolvimento Sustent�vel Rio Amap�");
//		uc16.setMascara("RDS Rio Amap�");
//		uc16.setRegional(rioNegro);
//		
//		/*---------------------------Ajustes de unidades -----------------------------------------*/
//			
//	
//		
//		UnidadeConservacao uc17 = new UnidadeConservacao();
//		uc17.setNome("�rea de Prote��o Ambiental Rio Negro");
//		uc17.setMascara("APA");
//		uc17.setRegional(rioNegro);
//		
//		/*UnidadeConservacao uc6 = new UnidadeConservacao();
//		uc6.setNome("APA Margen esquerda do Rio Negro setor Aturi�-Apuauzinho");
//		uc6.setMascara("APA do Rio Negro");
//		uc6.setRegional(rioNegro);*/
//		
//
//		UnidadeConservacao uc18 = new UnidadeConservacao();
//		uc18.setNome("Reserva Extrativista Catu� Ipixuna");
//		uc18.setMascara("RESEX CAT�A-IPIXUNA");
//		uc18.setRegional(solimoes);
//		
//		/*UnidadeConservacao uc12 = new UnidadeConservacao();
//		uc12.setNome("Reserva extrativista Catu�-Ipixuna");
//		uc12.setMascara("Resex Catu�-Ipixuna");
//		uc12.setRegional(rioNegro);*/
//		
//		
//		UnidadeConservacao uc19 = new UnidadeConservacao();
//		uc19.setNome("Reserva Extrativista Rio Greg�rio");
//		uc19.setMascara("RESEX DO RIO GREG�RIO");
//		uc19.setRegional(jurua);
//		
//		/*UnidadeConservacao uc10 = new UnidadeConservacao();
//		uc10.setNome("Reserva Extrativista do Rio Greg�rio");
//		uc10.setMascara("Resex do Rio Greg�rio");
//		uc10.setRegional(rioNegro);*/
//		
//		
//		UnidadeConservacao uc20 = new UnidadeConservacao();
//		uc20.setNome("Floresta Estadual de Mau�s");
//		uc20.setMascara("Florest Mau�s");
//		uc20.setRegional(rioNegro);
//		
//		/*UnidadeConservacao uc3 = new UnidadeConservacao();
//		uc3.setNome("FLOREST Mau�s");
//		uc3.setMascara("FE Mau�s");
//		uc3.setRegional(rioNegro);*/
//		
//	
//		UnidadeConservacao uc21 = new UnidadeConservacao();
//		uc21.setNome("Reserva de Desenvolvimento Sustent�vel Piaga�� Purus");
//		uc21.setMascara("RESEX PIAGA�U-PURUS");
//		uc21.setRegional(rioNegro);
//		
//		/*UnidadeConservacao uc = new UnidadeConservacao();
//		uc.setNome("Reserva de Desenvolvimento Sustent�vel Piaga��-Purus");
//		uc.setMascara("RDS Piaga��-Purus");
//		uc.setRegional(rioNegro);*/
//		
//		
//		UnidadeConservacao uc22 = new UnidadeConservacao();
//		uc22.setNome("Reserva de Desenvolvimento Sustent�vel Rio Negro");
//		uc22.setMascara("RDS Rio Negro");
//		uc22.setRegional(rioNegro);
//		
//		/*UnidadeConservacao uc5 = new UnidadeConservacao();
//		uc5.setNome("Reserva de Desenvolvimento Sustent�vel do Rio Negro");
//		uc5.setMascara("RDS do Rio Negro");
//		uc5.setRegional(rioNegro);*/
//	
//		
//		UnidadeConservacao uc23 = new UnidadeConservacao();
//		uc23.setNome("Reserva de Desenvolvimento Sustent�vel Uacari");
//		uc23.setMascara("RDS DO UACARI");
//		uc23.setRegional(rioNegro);
//		
//		/*UnidadeConservacao uc9 = new UnidadeConservacao();
//		uc9.setNome("Reserva de Desenvolvimento Sustent�vel de Uacari");
//		uc9.setMascara("RDS de Uacari");
//		uc9.setRegional(rioNegro);*/
//		
//		UnidadeConservacao uc24 = new UnidadeConservacao();
//		uc24.setNome("Reserva de Desenvolvimento Sustent�vel Uatum�");
//		uc24.setMascara("RDS DO UATUM�");
//		uc24.setRegional(rioNegro);
//		
//		/*UnidadeConservacao uc2 = new UnidadeConservacao();
//		uc2.setNome("Reserva de Desenvolvimento Sustent�vel do Uatum�");
//		uc2.setMascara("RDS do Uatum�");
//		uc2.setRegional(rioNegro);*/
//		
//		
//		/*----------------------------------------------------------------------*/
//
//				
//		/*Comunidade comunidade =  new Comunidade();
//		comunidade.setNome("Tumbira");
//		comunidade.setMascara("Tumbira");
//		comunidade.setUnidadeConservacao(uc5);
//		
//		Comunidade comunidade2 =  new Comunidade();
//		comunidade2.setNome("Abelha");
//		comunidade2.setMascara("Abelha");
//		comunidade2.setUnidadeConservacao(uc15);*/
//		
//	
//		manager.persist(supGer);
//		manager.persist(supTec);
//		manager.persist(supAdm);
//		
//		manager.persist(coord);
//		manager.persist(coord2);
//		manager.persist(coord3);
//		manager.persist(coord4);
//		manager.persist(coord5);
//		manager.persist(coord6);
//		manager.persist(coord7);
//		manager.persist(coord8);
//		manager.persist(coord9);
//		manager.persist(coord10);
//		manager.persist(coord11);
//		manager.persist(coord12);
//		manager.persist(coord13);
//		manager.persist(coord14);
//		
//		manager.persist(solimoes);
//		manager.persist(jurua);
//		manager.persist(rioNegro);
//		manager.persist(madeira);
//		
//	/*	manager.persist(uc);
//		manager.persist(uc2);
//		manager.persist(uc3);
//		manager.persist(uc5);
//		manager.persist(uc6);
//		manager.persist(uc9);
//		manager.persist(uc10);
//		manager.persist(uc12);*/
//		
//		manager.persist(uc4);
//		
//		
//		
//		manager.persist(uc7);
//		manager.persist(uc8);
//		
//		manager.persist(uc11);
//		
//		manager.persist(uc13);
//		manager.persist(uc14);
//		manager.persist(uc15);
//		manager.persist(uc16);
//		
//		manager.persist(uc17);
//		manager.persist(uc18);
//		manager.persist(uc19);
//		manager.persist(uc20);
//		manager.persist(uc21);
//		manager.persist(uc22);
//		manager.persist(uc23);
//		manager.persist(uc24);
//		
//		
//		
//	
//		
//		
//	}
//	
//	
//	public static void criarNucleos(EntityManager manager){
//		
//		Nucleo nucleo = new Nucleo();
//		nucleo.setMascara("NCS Assy Manana");
//		nucleo.setNome("N�cleo Assy Manana");
//		
//		Nucleo nucleo2 = new Nucleo();
//		nucleo2.setMascara("NCS Agnello Bittencourt");
//		nucleo2.setNome("N�cleo Agnello Uch�a Bittencourt");
//		
//		Nucleo nucleo3 = new Nucleo();
//		nucleo3.setMascara("NCS Victor Civita");
//		nucleo3.setNome("N�cleo Victor Civita");
//		
//		Nucleo nucleo4 = new Nucleo();
//		nucleo4.setMascara("NCS Pun�");
//		nucleo4.setNome("N�cleo Pun�");
//		
//		Nucleo nucleo5 = new Nucleo();
//		nucleo5.setMascara("NCS Uatum�");
//		nucleo5.setNome("N�cleo Uatum�");
//		
//		Nucleo nucleo6 = new Nucleo();
//		nucleo6.setMascara("NCS Samuel Benchimol");
//		nucleo6.setNome("N�cleo Samuel Benchimol");
//		
//		Nucleo nucleo7 = new Nucleo();
//		nucleo7.setMascara("NCAES Pe. Jo�o Rendrix");
//		nucleo7.setNome("N�cleo Pe. Jo�o Rendrix");
//		
//		Nucleo nucleo8 = new Nucleo();
//		nucleo8.setMascara("NCS Campina");
//		nucleo8.setNome("N�cleo Campina");
//		
//		
//		manager.persist(nucleo);
//		manager.persist(nucleo2);
//		manager.persist(nucleo3);
//		manager.persist(nucleo4);
//		manager.persist(nucleo5);
//		manager.persist(nucleo6);
//		manager.persist(nucleo7);
//		manager.persist(nucleo8);
//		
//	}
//	
//	
//	
//	
//	public static void criarProduto(EntityManager manager){
//		try {
//			
//			FileInputStream stream = new FileInputStream("C:/DEV/produtos.csv");
//			//FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/produtos.csv");
//			InputStreamReader reader = new InputStreamReader(stream);
//			BufferedReader br = new BufferedReader(reader);
//			while (br.ready()) {
//				
//				String linha[] = br.readLine().split(";");
//				String ncm = linha[0] != null ? linha[0] : "";
//				String descricao = linha[1] != null ? linha[1] : "";
//				String tipo =  linha[2] != null ? linha[2] : "";
//				String categoria = linha[3] != null ? linha[3] : "";
//				
//				Produto produto = new Produto();
//				produto.setNcm(ncm);
//				produto.setDescricao(descricao);
//				produto.setTipo(tipo);
//				produto.setCategoria(categoria);
//				
//				manager.persist(produto);
//				
//				//System.out.println("Estado: "+estado);
//			}
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
//	
//	
//	public static void criarEstado(EntityManager manager){
//		try {
//			
//			
//			FileInputStream stream = new FileInputStream("C:/DEV/estados.csv");
//			//FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/estados.csv");
//			InputStreamReader reader = new InputStreamReader(stream);
//			BufferedReader br = new BufferedReader(reader);
//			while (br.ready()) {
//				String linha[] = br.readLine().split(";");
//				String nomeEstado = linha[0];
//				Estado estado = new Estado();
//				estado.setNome(nomeEstado);
//				manager.persist(estado);
//				//System.out.println("Estado: "+estado);
//			}
//			
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//	}
//	
//	public static void criarColaborador(EntityManager manager){
//		try {
//			
//			
//			FileInputStream stream = new FileInputStream("C:/DEV/colaboradores.csv");
//			//FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/colaboradores.csv");
//			
//			InputStreamReader reader = new InputStreamReader(stream);
//			BufferedReader br = new BufferedReader(reader);
//			
//			int cont = 0;
//			while (br.ready()) {
//				
//				String linha[] = br.readLine().split(";");
//				String cpf = linha[0];
//				String dataString = linha[1];
//				System.out.println(dataString);
//				Date dataNascimento = new Date(dataString);
//				String nome =  linha[2];
//				String rg = linha[3];
//				String cargo =  linha[4];
//				Boolean controlePonto = linha[5].equals("TRUE") ? true : false;
//				String  ctps =  linha[6] != null ? linha[6] : "";
//				String dataAdmissaoString = linha[7] != null ? linha[7] : "";
//				
//				Date dataAdmissao = new Date();
//				
//				if (!dataAdmissaoString.equals("")) {
//					dataAdmissao =  new Date(dataAdmissaoString);
//				}
//				
//				String ensinoFundamental = linha[8] != null ? linha[8] : "";
//				
//				String graduacao =  linha[9] != null ? linha[9] : "";
//				
//				String matricula = linha[10] != null ? linha[10] : "";
//				
//				String pis =  linha[11] != null  ? linha[11] : "";
//				
//				String posGraduacao = linha[12] != null ? linha[12] : "";
//				
//				String quitacaoMilitar = linha[13] != null ? linha[13] : "";
//				
//				String sexo =  linha[14] != null ? linha[14] : "";
//				
//				String titulo = linha[15] != null  ? linha[15] : "";
//				
//				Integer gestao =  linha[16] != null ? Integer.valueOf(linha[16]) : null;
//				
//				Integer localide = linha[17] != null ? Integer.valueOf(linha[17]) : null;
//				//System.out.println(linha[2]);
//				
//				Colaborador colaborador = new Colaborador();
//				colaborador.setCpf(cpf);
//				colaborador.setDataDeAdmissao(dataAdmissao != null ? dataAdmissao : null);
//				colaborador.setDataDeNascimento(dataNascimento);
//				colaborador.setNome(nome);
//				colaborador.setRg(rg);
//				colaborador.setCargo(cargo);
//				colaborador.setControlaPonto(controlePonto);
//				colaborador.setCtpsSerieUf(ctps);
//				colaborador.setEnsinoFundamentalMedio(ensinoFundamental);
//				colaborador.setGraduacao(graduacao);
//				colaborador.setMatricula(matricula);
//				colaborador.setPis(pis);
//				colaborador.setPosGraduacao(posGraduacao);
//				colaborador.setQuitacaoMilitar(quitacaoMilitar);
//				colaborador.setSexo(sexo);
//				colaborador.setTitulo(titulo);
//				
//				if (gestao != null) {
//					colaborador.setGestao(manager.find(Gestao.class,new Long(gestao)));
//				}
//				
//				if(localide != null){
//					colaborador.setLotacao(manager.find(Localidade.class,new Long(localide)));
//				}
//				
//				manager.persist(colaborador);
//				
//				//System.out.println(cont);
//				
//				cont++;
//			}
//			
//			
//		
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//
//	public static void criarMunicipio(EntityManager manager){
//		try {
//			
//			
//			FileInputStream stream = new FileInputStream("C:/DEV/municipios.csv");
//			//FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/municipios.csv");
//			InputStreamReader reader = new InputStreamReader(stream);
//			BufferedReader br = new BufferedReader(reader);
//			while (br.ready()) {
//				String linha[] = br.readLine().split(";");
//				String ibge = linha[0];
//				String nomeMunicipio = linha[1];
//				
//				//System.out.println(linha[2]);
//				
//				Estado estado = getEstado(manager, linha[2]);
//				Municipio municipio = new Municipio();
//				municipio.setEstado(estado);
//				municipio.setMascara(nomeMunicipio);
//				municipio.setNome(nomeMunicipio);
//				municipio.setNumeroIbge(ibge);
//				manager.persist(municipio);
//				//System.out.println("Estado: "+estado);
//			}
//			
//			
//		
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public static Estado getEstado(EntityManager manager, String nome){
//		Query query = manager.createQuery("from Estado where lower(nome) like lower(:nome)");
//		query.setParameter("nome", "%"+nome+"%");
//		Estado estado = (Estado) query.getResultList().get(0);
//		return estado;		
//	}
//	
//	public static void criarUsuario(EntityManager manager){
//		
//		Perfil perfil =  new Perfil();
//		perfil.setDescricao("admin");
//		
//		User usuario = new User();
//		usuario.setAtivo(true);
//		usuario.setEmail("itlalmeidaa@gmail.com");
//		usuario.setNomeUsuario("admin");
//		usuario.setPerfil(perfil);
//		usuario.setSenha("admin");
//
//		User paulo = new User();
//		paulo.setAtivo(true);
//		paulo.setEmail("paulo.sergio@fas-amazonas.org");
//		paulo.setNomeUsuario("paulo.sergio");
//		paulo.setPerfil(perfil);
//		paulo.setSenha("psfas123");
//		
//		User monique = new User();
//		monique.setAtivo(true);
//		monique.setEmail("monique.bendahan@fas-amazonas.org");
//		monique.setNomeUsuario("monique");
//		monique.setPerfil(perfil);
//		monique.setSenha("mbfas123");
//		
//		User cirlene = new User();
//		cirlene.setAtivo(true);
//		cirlene.setEmail("cirlene.elias@fas-amazonas.org");
//		cirlene.setNomeUsuario("cirlene");
//		cirlene.setPerfil(perfil);
//		cirlene.setSenha("cefas123");
//		
//		User marina = new User();
//		marina.setAtivo(true);
//		marina.setEmail("edilmarina.mota@fas-amazonas.org");
//		marina.setNomeUsuario("marina");
//		marina.setPerfil(perfil);
//		marina.setSenha("mmfas123");
//		
//		User victor = new User();
//		victor.setAtivo(true);
//		victor.setEmail("victor.salviati@fas-amazonas.org");
//		victor.setNomeUsuario("victor.salviati");
//		victor.setPerfil(perfil);
//		victor.setSenha("vsfas123");
//		
//		
//		User edgar = new User();
//		edgar.setAtivo(true);
//		edgar.setEmail("edgar.duarte@fas-amazonas.org");
//		edgar.setNomeUsuario("edgar");
//		edgar.setPerfil(perfil);
//		edgar.setSenha("edfas123");
//		
//		
//		User fabio = new User();
//		fabio.setAtivo(true);
//		fabio.setEmail("fabio.carvalho@fas-amazonas.org");
//		fabio.setNomeUsuario("fabio.carvalho");
//		fabio.setPerfil(perfil);
//		fabio.setSenha("fcfas123");
//		
//		manager.persist(perfil);
//		manager.persist(usuario);
//		manager.persist(paulo);
//		manager.persist(monique);
//		manager.persist(cirlene);
//		manager.persist(marina);
//		manager.persist(victor);
//		manager.persist(edgar);
//		manager.persist(fabio);
//		
//
//	}
//	
//	public static void criarSede(EntityManager manager){
//		Sede sede = new Sede();
//		sede.setNome("Sede S�o Paulo");
//		sede.setMascara("Sede S�o Paulo");
//	
//		Sede sede2 = new Sede();
//		sede2.setNome("Sede Manaus");
//		sede2.setMascara("Sede Manaus");
//		
//		manager.persist(sede);
//		manager.persist(sede2);
//	}
//	
//	
//	
//	public static void listarComunidade(EntityManager manager){
//		
//		List<Localidade> locais = manager.createQuery("from Comunidade").getResultList();
//		
//		/*for (Localidade local : comunidades) {
//			
//			if(local instanceof Comunidade){
//				Comunidade comunidade = new Comunidade();
//				comunidade = (Comunidade) local;
//				System.out.println("RDS: "+comunidade.getUnidadeConservacao().getMascara()+" - Comunidade: "+comunidade.getMascara());
//			}else if(local instanceof UnidadeConservacao){
//				UnidadeConservacao unidadeConservacao =  new UnidadeConservacao();
//				unidadeConservacao = (UnidadeConservacao) local;
//				System.out.println("RDS: "+unidadeConservacao.getMascara()+" - Comunidade: ");
//				
//			}
//		}*/
//		
//		for (Localidade local : locais) {
//			
//			System.out.println("Comunidade: "+local.getMascara());
//			
//		}
//	}
//	
//	
//	public static void getAcoes(EntityManager manager){
//		List<Acao> acoes = manager.createQuery("from Acao", Acao.class).getResultList();
//		
//		for (Acao acao : acoes) {
//			if(acao.getLocalidade() instanceof UnidadeConservacao){
//			 System.out.println("Ação: "+acao.getCodigo()+" - RDS: "+((UnidadeConservacao)acao.getLocalidade()).getMascara()+" - Comunidade: ");
//			}else if(acao.getLocalidade() instanceof Comunidade){
//			 System.out.println("Ação: "+acao.getCodigo()+" - RDS: "+((Comunidade)acao.getLocalidade()).getUnidadeConservacao().getMascara()+" - Comunidade: "+acao.getLocalidade().getMascara());
//			}
//		}
//		
//		
//		
//	}
//	
//	
//	public static void criarComunidade2(EntityManager manager){
//		
//		UnidadeConservacao uc = new UnidadeConservacao();
//		uc.setNome("Rerserva de desenvolvimento sustentável do Rio Negro");
//		uc.setMascara("RDS do Rio Negro");
//		
//		manager.persist(uc);
//		
//		uc = new UnidadeConservacao();
//		uc.setNome("Rerserva de desenvolvimento sustentável do Rio Negro");
//		uc.setMascara("RDS do Rio Negro");
//		
//		
//		
//	}
//	
//	public static void  criarComunidade(EntityManager manager){
//		
//				
//		UnidadeConservacao uc = new UnidadeConservacao();
//		UnidadeConservacao uc2 = new UnidadeConservacao();
//		uc = manager.find(UnidadeConservacao.class, new Long(5585)); 
//		uc2 = manager.find(UnidadeConservacao.class, new Long(5594));
//				
//		Comunidade comunidade =  new Comunidade();
//		comunidade.setNome("Tumbira");
//		comunidade.setMascara("Tumbira");
//		comunidade.setUnidadeConservacao(uc);
//		
//		Comunidade comunidade2 =  new Comunidade();
//		comunidade2.setNome("Abelha");
//		comunidade2.setMascara("Abelha");
//		comunidade2.setUnidadeConservacao(uc2);
//		
//		manager.persist(comunidade);
//		manager.persist(comunidade2);
//		
//		//criarLocalidade(uc, manager);
//		//criarLocalidade(comunidade, manager);
//	
//	}
//	
//	public static void  criarLocalidade(Localidade localidade, EntityManager manager){
//		((Comunidade) localidade).setUnidadeConservacao(manager.merge(((Comunidade)localidade).getUnidadeConservacao()));
//		manager.merge(((Comunidade) localidade));
//	
//	}
//	
//	public static void criarFornecedor(EntityManager manager){
//		try {
//			
//			
//			/*A classe Fornecedor precisa ser modificada pois esta faltando alguns campos relativos ao formulario enviado*/
//			/* Vou deixar ao teu criterio oque deve ser adicionado, no entanto ja mapeei o csv segue a baixo o codigo*/
//			
//			//FileInputStream stream = new FileInputStream("C:/DEV/fornecedores.csv");
//			//FileInputStream stream = new FileInputStream("");
//			
//			
//			FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/fornecedores.csv");
//			InputStreamReader reader = new InputStreamReader(stream);
//			BufferedReader br = new BufferedReader(reader);
//			
//			int cont = 0;
//			
//			
//			while (br.ready()) {
//				
//				
//				String linha[] = br.readLine().split(";");
//				String nomeFornecedor = linha[0] != null ? linha[0] : "";
//				System.out.println("Nome do Fornecedor " + linha[1]);
//				String endereco = linha[1] != null ? linha[1] : "";
//				System.out.println("Endereco" + linha[2]);
//				//Integer cidade = linha[2] != null ? Integer.valueOf(linha[2]) : null;
//				String nestado = linha[3];
//				System.out.println("Estado: " + linha[3]);
//				String cnpj = linha[4] != null ? linha[4] : "";
//				String ie = linha[5] != null ? linha[5] : "";
//				String im = linha[6] != null ? linha[6] : "";
//				String email = linha[7] != null ? linha[7] : "";
//				String website = linha[8] != null ? linha[8] : "";
//				String telefone = linha[9] != null ? linha[9] : "";
//				String fax	= linha[10] != null ? linha[10] : "";
//				//String observacoes = linha[11] != null ? linha[11] : "";
//				String banco = linha[11] != null ? linha[11] : "";
//				String agencia = linha[12] != null ? linha[12] : "";
//				String conta = linha[13] != null ? linha[13] : "";
//				String id_atividade = linha[14] != null ? linha[14] : "";
//				String nomeFantasia = linha[15] != null ? linha[15] : "";
//				String cep = linha[16] != null ? linha[16] : "";
//				String bairro = linha[17] != null ? linha[18] : "";	
//				String pis = linha[19] != null ? linha[19] : "";
//				
//				
//				
//				
//				
//			
//				/*gambiarra pra pular uma linha do csv*/
//				if(cont > 0){
//					
//					
//					Estado estado = getEstado(manager, "amazonas");
//					
//					Fornecedor fornecedor = new Fornecedor();
//					fornecedor.setRazaoSocial(nomeFornecedor);
//					System.out.println("Persistencia:" + fornecedor.getRazaoSocial().toString() );
//					fornecedor.setEndereco(endereco);
//					fornecedor.setEstado(estado);
//					fornecedor.setCnpj(cnpj);
//					fornecedor.setInscricaoEstadual(ie);
//					fornecedor.setEmail(email);
//					fornecedor.setTelefone(telefone);
//				//	fornecedor.setObservacao(observacoes);
//					fornecedor.setBanco(banco);
//					fornecedor.setAgencia(agencia);
//					fornecedor.setConta(conta);
//					fornecedor.setNomeFantasia(nomeFantasia);
//					fornecedor.setCep(cep);
//					fornecedor.setBairro(bairro);
//					fornecedor.setPis(pis); 
//					
//					
//					
//					
//					manager.persist(fornecedor);
//				}
//				
//				cont++;
//				
//			}
//		
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			System.out.println("file error");	
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("outro error");
//		}
//	}
//	
//	public static void criarConta(EntityManager manager){
//		try {
//			
//		
//			FileInputStream stream = new FileInputStream("/home/developer/Documentos/doc/bd/contas.csv");
//			InputStreamReader reader = new InputStreamReader(stream);
//			BufferedReader br = new BufferedReader(reader);
//			
//			int cont = 0;
//			
//			
//			while (br.ready()) {
//				
//				String linha[] = br.readLine().split(";");
//				String tipo = linha[0] != null ? linha[0] : "";
//				String banco = linha[1] != null ? linha[1] : "";
//				String agencia = linha[2] != null ? linha[2] : "";
//				String nomeConta = linha[3] != null ? linha[3] : "";
//				
//				ContaBancaria conta = new ContaBancaria();
//				conta.setTipo(tipo);
//				conta.setNomeBanco(banco);
//				conta.setNumeroAgencia(agencia);
//				conta.setNomeConta(nomeConta);
//				conta.setSaldoAtual(BigDecimal.ZERO);
//				conta.setSaldoAtual(BigDecimal.ZERO);
//		
//				manager.persist(conta);
//			}
//		
//		} catch (FileNotFoundException e) {
//			e.printStackTrace();
//			System.out.println("file error");	
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			System.out.println("outro error");
//		}
//	}
//	
//	
//
//}
