package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;

import model.Acao;
import model.Colaborador;
import model.Comunidade;
import model.Coordenadoria;
import model.Estado;
import model.FontePagadora;
import model.Gestao;
import model.Localidade;
import model.Municipio;
import model.Nucleo;
import model.Perfil;
import model.Produto;
import model.Regional;
import model.Sede;
import model.Superintendencia;
import model.UnidadeConservacao;
import model.User;

public class GeradorDeTabelas {
	
	public static void main(String[] args) {
	  
		EntityManager manager = JpaUtil.getEntityManager();
	    EntityTransaction tx = manager.getTransaction();
	    tx.begin();
		
			//criarComunidade(manager);
	        //criarUnidade(manager);
		    criarNucleos(manager);

		    criarEstado(manager);

			criarMunicipio(manager);

			criarSede(manager);

		    criarGestao(manager);

			criarUsuario(manager);

	    	criarFonteDeRecurso(manager);

	        criarProduto(manager);

	        criarColaborador(manager);
	        
	    	//criarComunidade(manager);
	    
	    tx.commit();
		manager.close();
	}
	
	
	public static void criarFonteDeRecurso(EntityManager manager){
		
		FontePagadora fonte = new FontePagadora();
		fonte.setNome("Bradesco S/A");
		
		FontePagadora fonte2 = new FontePagadora();
		fonte2.setNome("Coca Cola");
		
		FontePagadora fonte3 = new FontePagadora();
		fonte3.setNome("Fundo Amazônia");
		
		FontePagadora fonte4 = new FontePagadora();
		fonte4.setNome("Samsung LTDA");
		
		FontePagadora fonte5 = new FontePagadora();
		fonte5.setNome("Instituto Lojas Renner");
		
		FontePagadora fonte6 = new FontePagadora();
		fonte6.setNome("Camargo Corr�a");
		
		FontePagadora fonte7 = new FontePagadora();
		fonte7.setNome("INNOVA S/A");
		
		FontePagadora fonte8 = new FontePagadora();
		fonte8.setNome("ABRIL Comunicações S/A");
		
		FontePagadora fonte9 = new FontePagadora();
		fonte9.setNome("FUMCAD MARAÃ");
		
		FontePagadora fonte10 = new FontePagadora();
		fonte10.setNome("FUMCAD UARINI");
		
		FontePagadora fonte11 = new FontePagadora();
		fonte11.setNome("EMS");
		
		FontePagadora fonte12 = new FontePagadora();
		fonte12.setNome("SAP");
		
		FontePagadora fonte13 = new FontePagadora();
		fonte13.setNome("BEMOL");
		
		FontePagadora fonte14 = new FontePagadora();
		fonte14.setNome("TIM");
		
		FontePagadora fonte15 = new FontePagadora();
		fonte15.setNome("UNICEF");
		
		FontePagadora fonte16 = new FontePagadora();
		fonte16.setNome("SEBRAE/AM");
		
		FontePagadora fonte17 = new FontePagadora();
		fonte17.setNome("ICCO");
		
		FontePagadora fonte18 = new FontePagadora();
		fonte18.setNome("BID");
		
		FontePagadora fonte19 = new FontePagadora();
		fonte19.setNome("Natura");
		
		FontePagadora fonte20 = new FontePagadora();
		fonte20.setNome("Vale S/A");
		
		FontePagadora fonte21 = new FontePagadora();
		fonte21.setNome("Bernard Van Leer Foundation");
		
		FontePagadora fonte22 = new FontePagadora();
		fonte22.setNome("UNEP - United Nations Envitoment Programme");
		
		FontePagadora fonte23 = new FontePagadora();
		fonte23.setNome("United Nations Development Programme");
		
		FontePagadora fonte24 = new FontePagadora();
		fonte24.setNome("Europen Florest Institute");
		
		FontePagadora fonte25 = new FontePagadora();
		fonte25.setNome("MARRIOT");
		
		FontePagadora fonte26 = new FontePagadora();
		fonte26.setNome("SIATER");
		
		manager.persist(fonte);
		manager.persist(fonte2);
		manager.persist(fonte3);
		manager.persist(fonte4);
		manager.persist(fonte5);
		manager.persist(fonte6);
		manager.persist(fonte7);
		manager.persist(fonte8);
		manager.persist(fonte9);
		manager.persist(fonte10);
		manager.persist(fonte11);
		manager.persist(fonte12);
		manager.persist(fonte13);
		manager.persist(fonte14);
		manager.persist(fonte15);
		manager.persist(fonte16);
		manager.persist(fonte17);
		manager.persist(fonte18);
		manager.persist(fonte19);
		manager.persist(fonte20);
		manager.persist(fonte21);
		manager.persist(fonte22);
		manager.persist(fonte23);
		manager.persist(fonte24);
		manager.persist(fonte25);
		manager.persist(fonte26);
		
	}
	
	public static void criarGestao(EntityManager manager){
		
		Superintendencia supGer = new Superintendencia();
		supGer.setNome("Geral");
		supGer.setSigla("SG");
		
		Superintendencia supTec = new Superintendencia();
		supTec.setNome("Técnico-Científico");
		supTec.setSigla("STC");
		
		Superintendencia supAdm = new Superintendencia();
		supAdm.setNome("Adm-Financeiro");
		supAdm.setSigla("SAF");
		
		Coordenadoria coord = new Coordenadoria();
		coord.setNome("Programa Bolsa Floresta");
		coord.setSuperintendencia(supTec);
		coord.setSigla("PBF");
		
		Coordenadoria coord2 = new Coordenadoria();
		coord2.setNome("Projetos Técnicos");
		coord2.setSuperintendencia(supTec);
		coord2.setSigla("CPT");
		
		Coordenadoria coord3 = new Coordenadoria();
		coord3.setNome("Logística");
		coord3.setSuperintendencia(supTec);
		coord3.setSigla("LOG");
	
		Coordenadoria coord4 = new Coordenadoria();
		coord4.setNome("Projetos Especiais");
		coord4.setSuperintendencia(supTec);
		coord4.setSigla("CPE");
		
		Coordenadoria coord5 = new Coordenadoria();
		coord5.setNome("Educação e Saúde");
		coord5.setSuperintendencia(supTec);
		coord5.setSigla("PES");
		
		Coordenadoria coord6 = new Coordenadoria();
		coord6.setNome("SDSN AM");
		coord6.setSuperintendencia(supTec);
		coord6.setSigla("SDSN");
		
		Coordenadoria coord7 = new Coordenadoria();
		coord7.setNome("Articulação Institucional");
		coord7.setSuperintendencia(supTec);
		coord7.setSigla("CAI");
		
		Coordenadoria coord8 = new Coordenadoria();
		coord8.setNome("Administrativa Financeira");
		coord8.setSuperintendencia(supAdm);
		coord8.setSigla("ADM");
		
		Coordenadoria coord9 = new Coordenadoria();
		coord9.setNome("Jurídica");
		coord9.setSuperintendencia(supAdm);
		coord9.setSigla("JUR");
	
		Coordenadoria coord10 = new Coordenadoria();
		coord10.setNome("Técnologia da Informação");
		coord10.setSuperintendencia(supAdm);
		coord10.setSigla("CTI");
		
		Coordenadoria coord11 = new Coordenadoria();
		coord11.setNome("Obras");
		coord11.setSuperintendencia(supAdm);
		coord11.setSigla("COP");
		
		Coordenadoria coord12 = new Coordenadoria();
		coord12.setNome("Relacionamento Institucional");
		coord12.setSuperintendencia(supGer);
		coord12.setSigla("CRI");
		
		Coordenadoria coord13 = new Coordenadoria();
		coord13.setNome("Comunicação");
		coord13.setSuperintendencia(supGer);
		coord13.setSigla("COM");
		
		Coordenadoria coord14 = new Coordenadoria();
		coord14.setNome("Desenv. Inst. e Parcerias");
		coord14.setSuperintendencia(supGer);
		coord14.setSigla("DIP");
		
		Regional solimoes = new Regional();
		solimoes.setNome("Solimões");
		solimoes.setSigla("BFS");
		
		Regional jurua = new Regional();
		jurua.setNome("Jutuá-Jutaí");
		jurua.setSigla("BFJ");
		
		Regional rioNegro = new Regional();
		rioNegro.setNome("Negro-Amazonas");
		rioNegro.setSigla("BFN");
		
		Regional madeira = new Regional();
		madeira.setNome("Madeira");
		madeira.setSigla("BFM");
		
		UnidadeConservacao uc = new UnidadeConservacao();
		uc.setNome("Reserva de Desenvolvimento Sustentável Piagaçú-Purus");
		uc.setMascara("RDS Piagaçú-Purus");
		uc.setRegional(rioNegro);
		
		
		UnidadeConservacao uc2 = new UnidadeConservacao();
		uc2.setNome("Reserva de Desenvolvimento Sustentável do Uatumã");
		uc2.setMascara("RDS do Uatumã");
		uc2.setRegional(rioNegro);
		
		
		UnidadeConservacao uc3 = new UnidadeConservacao();
		uc3.setNome("FLOREST Maués");
		uc3.setMascara("FE Maués");
		uc3.setRegional(rioNegro);
		
		
		UnidadeConservacao uc4 = new UnidadeConservacao();
		uc4.setNome("Reserva de Desenvolvimento Sustentável Canumã");
		uc4.setMascara("RDS Camunã");
		uc4.setRegional(rioNegro);
		
		
		UnidadeConservacao uc5 = new UnidadeConservacao();
		uc5.setNome("Reserva de Desenvolvimento Sustentável do Rio Negro");
		uc5.setMascara("RDS do Rio Negro");
		uc5.setRegional(rioNegro);
		
		
		UnidadeConservacao uc6 = new UnidadeConservacao();
		uc6.setNome("APA Margen esquerda do Rio Negro setor Aturiá-Apuauzinho");
		uc6.setMascara("APA do Rio Negro");
		uc6.setRegional(rioNegro);
		
		UnidadeConservacao uc7 = new UnidadeConservacao();
		uc7.setNome("Reserva de Desenvolvimento Sustent�vel Puranga Conquista");
		uc7.setMascara("RDS Puranga Conquista");
		uc7.setRegional(rioNegro);
		
		UnidadeConservacao uc8 = new UnidadeConservacao();
		uc8.setNome("Reserva de Desenvolvimento Sustentável Cujubim");
		uc8.setMascara("RDS Cujubim");
		uc8.setRegional(rioNegro);
		
		UnidadeConservacao uc9 = new UnidadeConservacao();
		uc9.setNome("Reserva de Desenvolvimento Sustentável de Uacari");
		uc9.setMascara("RDS de Uacari");
		uc9.setRegional(rioNegro);
		
		UnidadeConservacao uc10 = new UnidadeConservacao();
		uc10.setNome("Reserva Extrativista do Rio Gregório");
		uc10.setMascara("Resex do Rio Gregório");
		uc10.setRegional(rioNegro);
		
		UnidadeConservacao uc11 = new UnidadeConservacao();
		uc11.setNome("Reserva de Desenvolvimento Sustentável Mamirauá");
		uc11.setMascara("RDS Mamirauá");
		uc11.setRegional(rioNegro);
		
		UnidadeConservacao uc12 = new UnidadeConservacao();
		uc12.setNome("Reserva extrativista Catuá-Ipixuna");
		uc12.setMascara("Resex Catuá-Ipixuna");
		uc12.setRegional(rioNegro);
		
		UnidadeConservacao uc13 = new UnidadeConservacao();
		uc13.setNome("Reserva de Desenvolvimento Sustentável Amanã");
		uc13.setMascara("RDS Amanã");
		uc13.setRegional(rioNegro);
		
		UnidadeConservacao uc14 = new UnidadeConservacao();
		uc14.setNome("Reserva de Desenvolvimento Sustentável Juma");
		uc14.setMascara("RDS Juma");
		uc14.setRegional(rioNegro);
		
		UnidadeConservacao uc15 = new UnidadeConservacao();
		uc15.setNome("Reserva de Desenvolvimento Sustentável Rio Madeira");
		uc15.setMascara("RDS Rio Madeira");	
		uc15.setRegional(rioNegro);
		
		UnidadeConservacao uc16 = new UnidadeConservacao();
		uc16.setNome("Reserva de Desenvolvimento Sustentável Rio Amapá");
		uc16.setMascara("RDS Rio Amapá");
		uc16.setRegional(rioNegro);
		
		

				
		Comunidade comunidade =  new Comunidade();
		comunidade.setNome("Tumbira");
		comunidade.setMascara("Tumbira");
		comunidade.setUnidadeConservacao(uc5);
		
		Comunidade comunidade2 =  new Comunidade();
		comunidade2.setNome("Abelha");
		comunidade2.setMascara("Abelha");
		comunidade2.setUnidadeConservacao(uc15);
		
	
		manager.persist(supGer);
		manager.persist(supTec);
		manager.persist(supAdm);
		
		manager.persist(coord);
		manager.persist(coord2);
		manager.persist(coord3);
		manager.persist(coord4);
		manager.persist(coord5);
		manager.persist(coord6);
		manager.persist(coord7);
		manager.persist(coord8);
		manager.persist(coord9);
		manager.persist(coord10);
		manager.persist(coord11);
		manager.persist(coord12);
		manager.persist(coord13);
		manager.persist(coord14);
		
		manager.persist(solimoes);
		manager.persist(jurua);
		manager.persist(rioNegro);
		manager.persist(madeira);
		
		manager.persist(uc);
		manager.persist(uc2);
		manager.persist(uc3);
		manager.persist(uc4);
		manager.persist(uc5);
		manager.persist(uc6);
		manager.persist(uc7);
		manager.persist(uc8);
		manager.persist(uc9);
		manager.persist(uc10);
		manager.persist(uc11);
		manager.persist(uc12);
		manager.persist(uc13);
		manager.persist(uc14);
		manager.persist(uc15);
		manager.persist(uc16);
		
		manager.persist(comunidade);
		manager.persist(comunidade2);
		
		
	}
	
	
	public static void criarNucleos(EntityManager manager){
		
		Nucleo nucleo = new Nucleo();
		nucleo.setMascara("NCS Assy Manana");
		nucleo.setNome("Núcleo Assy Manana");
		
		Nucleo nucleo2 = new Nucleo();
		nucleo2.setMascara("NCS Agnello Bittencourt");
		nucleo2.setNome("Núcleo Agnello Uchôa Bittencourt");
		
		Nucleo nucleo3 = new Nucleo();
		nucleo3.setMascara("NCS Victor Civita");
		nucleo3.setNome("Núcleo Victor Civita");
		
		Nucleo nucleo4 = new Nucleo();
		nucleo4.setMascara("NCS Punã");
		nucleo4.setNome("Núcleo Punã");
		
		Nucleo nucleo5 = new Nucleo();
		nucleo5.setMascara("NCS Uatumã");
		nucleo5.setNome("Núcleo Uatumã");
		
		Nucleo nucleo6 = new Nucleo();
		nucleo6.setMascara("NCS Samuel Benchimol");
		nucleo6.setNome("Núcleo Samuel Benchimol");
		
		Nucleo nucleo7 = new Nucleo();
		nucleo7.setMascara("NCAES Pe. João Rendrix");
		nucleo7.setNome("Núcleo Pe. João Rendrix");
		
		Nucleo nucleo8 = new Nucleo();
		nucleo8.setMascara("NCS Campina");
		nucleo8.setNome("Núcleo Campina");
		
		
		manager.persist(nucleo);
		manager.persist(nucleo2);
		manager.persist(nucleo3);
		manager.persist(nucleo4);
		manager.persist(nucleo5);
		manager.persist(nucleo6);
		manager.persist(nucleo7);
		manager.persist(nucleo8);
		
	}
	
	
	
	
	public static void criarProduto(EntityManager manager){
		try {
			
			//FileInputStream stream = new FileInputStream("C:/DEV/produtos.csv");
			FileInputStream stream = new FileInputStream("/home/odin/Documentos/deve/produtos.csv");
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			while (br.ready()) {
				
				String linha[] = br.readLine().split(";");
				String ncm = linha[0] != null ? linha[0] : "";
				String descricao = linha[1] != null ? linha[1] : "";
				String tipo =  linha[2] != null ? linha[2] : "";
				String categoria = linha[3] != null ? linha[3] : "";
				
				Produto produto = new Produto();
				produto.setNcm(ncm);
				produto.setDescricao(descricao);
				produto.setTipo(tipo);
				produto.setCategoria(categoria);
				
				manager.persist(produto);
				
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	
	public static void criarEstado(EntityManager manager){
		try {
			
			
			//FileInputStream stream = new FileInputStream("C:/DEV/estados.csv");
			FileInputStream stream = new FileInputStream("/home/odin/Documentos/deve/estados.csv");
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			while (br.ready()) {
				String linha[] = br.readLine().split(";");
				String nomeEstado = linha[0];
				Estado estado = new Estado();
				estado.setNome(nomeEstado);
				manager.persist(estado);
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	
	public static void criarColaborador(EntityManager manager){
		try {
			
			
			//FileInputStream stream = new FileInputStream("C:/DEV/colaboradores.csv");
			FileInputStream stream = new FileInputStream("/home/odin/Documentos/deve/colaboradores.csv");
			
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			
			int cont = 0;
			while (br.ready()) {
				
				String linha[] = br.readLine().split(";");
				String cpf = linha[0];
				String dataString = linha[1];
				Date dataNascimento = new Date(dataString);
				String nome =  linha[2];
				String rg = linha[3];
				String cargo =  linha[4];
				Boolean controlePonto = linha[5].equals("TRUE") ? true : false;
				String  ctps =  linha[6] != null ? linha[6] : "";
				String dataAdmissaoString = linha[7] != null ? linha[7] : "";
				
				Date dataAdmissao = new Date();
				
				if (!dataAdmissaoString.equals("")) {
					dataAdmissao =  new Date(dataAdmissaoString);
				}
				
				String ensinoFundamental = linha[8] != null ? linha[8] : "";
				
				String graduacao =  linha[9] != null ? linha[9] : "";
				
				String matricula = linha[10] != null ? linha[10] : "";
				
				String pis =  linha[11] != null  ? linha[11] : "";
				
				String posGraduacao = linha[12] != null ? linha[12] : "";
				
				String quitacaoMilitar = linha[13] != null ? linha[13] : "";
				
				String sexo =  linha[14] != null ? linha[14] : "";
				
				String titulo = linha[15] != null  ? linha[15] : "";
				
				Integer gestao =  linha[16] != null ? Integer.valueOf(linha[16]) : null;
				
				Integer localide = linha[17] != null ? Integer.valueOf(linha[17]) : null;
				
				Colaborador colaborador = new Colaborador();
				colaborador.setCpf(cpf);
				colaborador.setDataDeAdmissao(dataAdmissao != null ? dataAdmissao : null);
				colaborador.setDataDeNascimento(dataNascimento);
				colaborador.setNome(nome);
				colaborador.setRg(rg);
				colaborador.setCargo(cargo);
				colaborador.setControlaPonto(controlePonto);
				colaborador.setCtpsSerieUf(ctps);
				colaborador.setEnsinoFundamentalMedio(ensinoFundamental);
				colaborador.setGraduacao(graduacao);
				colaborador.setMatricula(matricula);
				colaborador.setPis(pis);
				colaborador.setPosGraduacao(posGraduacao);
				colaborador.setQuitacaoMilitar(quitacaoMilitar);
				colaborador.setSexo(sexo);
				colaborador.setTitulo(titulo);
				
				if (gestao != null) {
					colaborador.setGestao(manager.find(Gestao.class,new Long(gestao)));
				}
				
				if(localide != null){
					colaborador.setLotacao(manager.find(Localidade.class,new Long(localide)));
				}
				
				manager.persist(colaborador);
				
				
				cont++;
			}
			
			
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void criarMunicipio(EntityManager manager){
		try {
			
			//FileInputStream stream = new FileInputStream("C:/DEV/municipios.csv");
			FileInputStream stream = new FileInputStream("/home/odin/Documentos/deve/municipios.csv");
			InputStreamReader reader = new InputStreamReader(stream);
			BufferedReader br = new BufferedReader(reader);
			while (br.ready()) {
				String linha[] = br.readLine().split(";");
				String ibge = linha[0];
				String nomeMunicipio = linha[1];
				
				
				Estado estado = getEstado(manager, linha[2]);
				Municipio municipio = new Municipio();
				municipio.setEstado(estado);
				municipio.setMascara(nomeMunicipio);
				municipio.setNome(nomeMunicipio);
				municipio.setNumeroIbge(ibge);
				manager.persist(municipio);
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
	
	public static void criarUsuario(EntityManager manager){
		Perfil perfil =  new Perfil();
		perfil.setDescricao("admin");
		
		User usuario = new User();
		usuario.setAtivo(true);
		usuario.setEmail("itlalmeidaa@gmail.com");
		usuario.setNomeUsuario("admin");
		usuario.setPerfil(perfil);
		usuario.setSenha("admin");

		manager.persist(perfil);
		manager.persist(usuario);

	}
	
	public static void criarSede(EntityManager manager){
		Sede sede = new Sede();
		sede.setNome("Sede São Paulo");
		sede.setMascara("Sede São Paulo");
	
		Sede sede2 = new Sede();
		sede2.setNome("Sede Manaus");
		sede2.setMascara("Sede Manaus");
		
		manager.persist(sede);
		manager.persist(sede2);
	}
	
	
	
	public static void listarComunidade(EntityManager manager){
		
		List<Localidade> locais = manager.createQuery("from Comunidade").getResultList();
		
		for (Localidade local : locais) {
			
			if(local instanceof Comunidade){
				Comunidade comunidade = new Comunidade();
				comunidade = (Comunidade) local;
			}else if(local instanceof UnidadeConservacao){
				UnidadeConservacao unidadeConservacao =  new UnidadeConservacao();
				unidadeConservacao = (UnidadeConservacao) local;
				
			}
		}
	
	}
	
	
	public static void getAcoes(EntityManager manager){
		List<Acao> acoes = manager.createQuery("from Acao", Acao.class).getResultList();
		
		for (Acao acao : acoes) {
			if(acao.getLocalidade() instanceof UnidadeConservacao){
			 
			}else if(acao.getLocalidade() instanceof Comunidade){
			 
			}
		}
		
		
		
	}
	
	
	public static void criarComunidade2(EntityManager manager){
		
		UnidadeConservacao uc = new UnidadeConservacao();
		uc.setNome("Rerserva de desenvolvimento sustentável do Rio Negro");
		uc.setMascara("RDS do Rio Negro");
		
		manager.persist(uc);
		
		uc = new UnidadeConservacao();
		uc.setNome("Rerserva de desenvolvimento sustentável do Rio Negro");
		uc.setMascara("RDS do Rio Negro");
		
		
		
	}
	
	public static void  criarComunidade(EntityManager manager){
		
				
		UnidadeConservacao uc = new UnidadeConservacao();
		UnidadeConservacao uc2 = new UnidadeConservacao();
		uc = manager.find(UnidadeConservacao.class, new Long(5585)); 
		uc2 = manager.find(UnidadeConservacao.class, new Long(5594));
				
		Comunidade comunidade =  new Comunidade();
		comunidade.setNome("Tumbira");
		comunidade.setMascara("Tumbira");
		comunidade.setUnidadeConservacao(uc);
		
		Comunidade comunidade2 =  new Comunidade();
		comunidade2.setNome("Abelha");
		comunidade2.setMascara("Abelha");
		comunidade2.setUnidadeConservacao(uc2);
		
		manager.persist(comunidade);
		manager.persist(comunidade2);
		
		//criarLocalidade(uc, manager);
		//criarLocalidade(comunidade, manager);
	
	}
	
	public static void  criarLocalidade(Localidade localidade, EntityManager manager){
		((Comunidade) localidade).setUnidadeConservacao(manager.merge(((Comunidade)localidade).getUnidadeConservacao()));
		manager.merge(((Comunidade) localidade));
	
	}

}
