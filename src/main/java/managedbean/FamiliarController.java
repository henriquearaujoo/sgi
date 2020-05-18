package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.jboss.weld.context.RequestContext;
import org.primefaces.PrimeFaces;

import model.AbastecimentoAguaFamiliar;
import model.CadeiaProdutivaFamiliar;
import model.CadeiasProdutivasFamiliar;
import model.Comunidade;
import model.CriacaoAnimal;
import model.CriacaoAnimalFamiliar;
import model.DestinoEsgotoFamiliar;
import model.DestinoLixoFamiliar;
import model.DinamicaPopulacionalFamiliar;
import model.EquipamentoEInfraestruturaFamiliar;
import model.Familiar;
import model.IdentificacaoFamiliar;
import model.Localidade;
import model.MenuLateral;
import model.Municipio;
import model.OrganizacaoSocialFormalFamiliar;
import model.OrganizacaoSocialInformalFamiliar;
import model.OrientacaoReligiosaFamiliar;
import model.ProdutoAgricultura;
import model.ProdutoAgriculturaFamiliar;
import model.ProdutoFlorestalMadeireiro;
import model.ProdutoFlorestalMadeireiroFamiliar;
import model.ProdutoFlorestalNaoMadeireiro;
import model.ProdutoFlorestalNaoMadeireiroFamiliar;
import model.ProdutoPescado;
import model.ProdutoPescadoFamiliar;
import model.SintomaComum;
import model.SintomasComunsFamiliar;
import model.TipoAbastecimentoAguaFamiliar;
import model.TipoAguaConsumoFamiliar;
import model.TipoAmbienteFamiliar;
import model.TipoAnoEscolaridadeFamiliar;
import model.TipoCoberturaHabitacaoFamiliar;
import model.TipoConservamentoPescadoFamiliar;
import model.TipoConvenioFamiliar;
import model.TipoDesbloqueioRecursoFamiliar;
import model.TipoDesligamentoFamiliar;
import model.TipoDestinoEsgotoFamiliar;
import model.TipoDestinoLixoFamiliar;
import model.TipoEquipamentosInfraestruturaFamiliar;
import model.TipoEscolaridadeFamiliar;
import model.TipoEstruturaHabitacaoFamiliar;
import model.TipoFinanceiramenteAtivoFamiliar;
import model.TipoGeneroFamiliar;
import model.TipoHabitacaoFamiliar;
import model.TipoIdentificacaoBancariaFamiliar;
import model.TipoIdentificacaoFamiliar;
import model.TipoIluminacaoFamiliar;
import model.TipoLocalSanitarioFamiliar;
import model.TipoMaterialHabitacaoFamiliar;
import model.TipoOrganizacaoSocialFormalFamiliar;
import model.TipoOrganizacaoSocialInformalFamiliar;
import model.TipoOrientacaoReligiosaFamiliar;
import model.TipoParentescoFamiliar;
import model.TipoPisoHabitacaoFamiliar;
import model.TipoRiosFamiliar;
import model.TipoSaidaComunidadeFamiliar;
import model.TipoStatusCartaoFamiliar;
import model.TipoTerrenoFamiliar;
import model.TipoTransporteFamiliar;
import model.TipoTratamentoAguaFamiliar;
import model.TipoUsoFamiliar;
import model.TransporteFamiliar;
import model.TratamentoAguaFamiliar;
import model.UnidadeConservacao;
import service.FamiliarService;
import util.MakeMenu;
import util.Util;

@Named(value = "familiar_controller")
@ViewScoped
public class FamiliarController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Inject
	private FamiliarService familiarService;
	
	private List<OrganizacaoSocialFormalFamiliar> organizacoesFormais;
	
	private List<OrganizacaoSocialInformalFamiliar> organizacoesInformais;
	
	private List<OrientacaoReligiosaFamiliar> orientacoesReligiosas;
	
	private List<EquipamentoEInfraestruturaFamiliar> equipamentos;
	
	private List<TransporteFamiliar> transportes;
	
	private List<AbastecimentoAguaFamiliar> abastecimentos;
	
	private List<TratamentoAguaFamiliar> tratamentos;
	
	private List<DestinoEsgotoFamiliar> destinosEsgoto;
	
	private List<DestinoLixoFamiliar> destinosLixo;
	
	private Familiar familiar = new Familiar();
	
	private IdentificacaoFamiliar identificacao = new IdentificacaoFamiliar();
	
	private IdentificacaoFamiliar identificacaoAux;
	
	private DinamicaPopulacionalFamiliar dinamicaPopulacional = new DinamicaPopulacionalFamiliar();
	
	private SintomasComunsFamiliar sintoma = new SintomasComunsFamiliar();
	
	private CadeiasProdutivasFamiliar cadeia = new CadeiasProdutivasFamiliar();
	
	private ProdutoAgriculturaFamiliar produtoAgricultura = new ProdutoAgriculturaFamiliar();
	
	private ProdutoPescadoFamiliar produtoPescado = new ProdutoPescadoFamiliar();
	
	private CriacaoAnimalFamiliar criacao = new CriacaoAnimalFamiliar();
	
	private ProdutoFlorestalMadeireiroFamiliar produtoMadeireiro = new ProdutoFlorestalMadeireiroFamiliar();
	
	private ProdutoFlorestalNaoMadeireiroFamiliar produtoNaoMadeireiro = new ProdutoFlorestalNaoMadeireiroFamiliar();
	
	private Boolean editandoIdentificacao = false;
	
	private Boolean editandoDinamica = false;
	
	private Boolean editandoSintoma = false;
	
	private Boolean editandoCadeia = false;
	
	private Boolean editandoProdutoAgricultura = false;
	
	private Boolean editandoProdutoPescado = false;
	
	private Boolean editandoCriacaoAnimal = false;
	
	private Boolean editandoProdutoMadeireiro = false;
	
	private Boolean editandoProdutoNaoMadeireiro = false;
	
	private String modalShow;
	
	private String filtroProtocolo;
	
	private UnidadeConservacao filtroUnidadeConservacao;
	
	private Comunidade filtroComunidade;
	
	private List<Familiar> listaFamiliar;
	
	private MenuLateral menu = new MenuLateral();
	
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public String listagemFamiliar() {
		return "cadastro_familiar?faces-redirect=true";
	}
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuPBF();
	}
	
	public void findListaFamiliar() {
		try {
			if ((filtroProtocolo == null || filtroProtocolo.isEmpty()) && filtroUnidadeConservacao == null && filtroComunidade == null)
				throw new Exception("Informe pelo menos um filtro antes de consultar.");
			
			listaFamiliar = familiarService.getFamiliarComFiltro(filtroProtocolo.trim(), filtroUnidadeConservacao, filtroComunidade);

			for (Familiar familiar :
				 listaFamiliar) {
				familiar.setIdentificacoes(familiarService.getMembros(familiar.getId()));
			}
		} catch (Exception e) {
			// TODO: handle exception
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			context.addMessage("msg", msg);
		}
	}
	
	public void limparFiltros() {
		filtroComunidade = null;
		filtroProtocolo = null;
		filtroUnidadeConservacao = null;
	}
	
	public String insert(){
		
		//Oganizacao Social
		if (familiar.getOraganizacoesFormais() == null)
			familiar.setOraganizacoesFormais(new ArrayList<>());
		
		if (familiar.getOraganizacoesInformais() == null)
			familiar.setOraganizacoesInformais(new ArrayList<>());
		
		for (OrganizacaoSocialFormalFamiliar organizacaoSocialFormalFamiliar : organizacoesFormais) {
			OrganizacaoSocialFormalFamiliar aux = getOrganizacaoFormal(organizacaoSocialFormalFamiliar.getTipo());
			if (organizacaoSocialFormalFamiliar.getSelecionado()){
				organizacaoSocialFormalFamiliar.setFamiliar(familiar);
				if (aux == null)
					familiar.getOraganizacoesFormais().add(organizacaoSocialFormalFamiliar);
				else{
					aux.setResposta(organizacaoSocialFormalFamiliar.getResposta());
				}
			}else{
				if (aux != null)
					familiar.getOraganizacoesFormais().remove(aux);
			}
		}
		
		for (OrganizacaoSocialInformalFamiliar organizacaoSocialInformalFamiliar : organizacoesInformais) {
			OrganizacaoSocialInformalFamiliar aux = getOrganizacaoInformal(organizacaoSocialInformalFamiliar.getTipo());
			if (organizacaoSocialInformalFamiliar.getSelecionado()){
				organizacaoSocialInformalFamiliar.setFamiliar(familiar);
				if (aux == null)
					familiar.getOraganizacoesInformais().add(organizacaoSocialInformalFamiliar);
				else{
					aux.setResposta(organizacaoSocialInformalFamiliar.getResposta());
				}
			}else{
				if (aux != null)
					familiar.getOraganizacoesInformais().remove(aux);
			}
		}
		
		//Orientacao religiosa
		if (familiar.getOrientacoesReligiosas() == null)
			familiar.setOrientacoesReligiosas(new ArrayList<>());
		
		for (OrientacaoReligiosaFamiliar orientacao : orientacoesReligiosas) {
			OrientacaoReligiosaFamiliar aux = getOrientacaoReligiosa(orientacao.getTipo());
			if (orientacao.getSelecionado()){
				orientacao.setFamiliar(familiar);
				if (aux == null)
					familiar.getOrientacoesReligiosas().add(orientacao);
				else{
					aux.setResposta(orientacao.getResposta());
				}
			}else{
				if (aux != null)
					familiar.getOrientacoesReligiosas().remove(aux);
			}
		}
		
		//Equipamentos e infraestrutura
		if (familiar.getEquipamentosInfraestrutura() == null)
			familiar.setEquipamentosInfraestrutura(new ArrayList<>());
		
		for (EquipamentoEInfraestruturaFamiliar obj : equipamentos) {
			EquipamentoEInfraestruturaFamiliar aux = getEquipamento(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getEquipamentosInfraestrutura().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					familiar.getEquipamentosInfraestrutura().remove(aux);
			}
		}
		
		//Transporte
		if (familiar.getTransportes() == null)
			familiar.setTransportes(new ArrayList<>());
		
		for (TransporteFamiliar obj : transportes) {
			TransporteFamiliar aux = getTransporte(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getTransportes().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					familiar.getTransportes().remove(aux);
			}
		}
		
		//Saneamento basico
		if (familiar.getAbastecimentosAgua() == null)
			familiar.setAbastecimentosAgua(new ArrayList<>());
		
		for (AbastecimentoAguaFamiliar obj : abastecimentos) {
			AbastecimentoAguaFamiliar aux = getAbastecimento(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getAbastecimentosAgua().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					familiar.getAbastecimentosAgua().remove(aux);
			}
		}
		
		if (familiar.getTratamentosAgua() == null)
			familiar.setTratamentosAgua(new ArrayList<>());
		
		for (TratamentoAguaFamiliar obj : tratamentos) {
			TratamentoAguaFamiliar aux = getTratamento(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getTratamentosAgua().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					familiar.getTratamentosAgua().remove(aux);
			}
		}
		
		if (familiar.getDestinosEsgoto() == null)
			familiar.setDestinosEsgoto(new ArrayList<>());
		
		for (DestinoEsgotoFamiliar obj : destinosEsgoto) {
			DestinoEsgotoFamiliar aux = getDestinoEsgoto(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getDestinosEsgoto().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					familiar.getDestinosEsgoto().remove(aux);
			}
		}
		
		if (familiar.getDestinosLixo() == null)
			familiar.setDestinosLixo(new ArrayList<>());
		
		for (DestinoLixoFamiliar obj : destinosLixo) {
			DestinoLixoFamiliar aux = getDestinoLixo(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getDestinosLixo().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					familiar.getDestinosLixo().remove(aux);
			}
		}
		
		//Informacoes sobre saude
		/*if (familiar.getSintomasSaude() == null)
			familiar.setSintomasSaude(new ArrayList<>());
		
		for (SintomasComunsFamiliar obj : sintomas) {
			SintomasComunsFamiliar aux = getSintoma(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getSintomasSaude().add(obj);
				else{
					aux.setResposta(obj.getResposta());
					aux.setNivel(obj.getNivel());
				}
			}else{
				if (aux != null)
					familiar.getSintomasSaude().remove(aux);
			}
		}*/
		
		//Renda - atividades economicas
		/*if (familiar.getBeneficiosSociais() == null)
			familiar.setBeneficiosSociais(new ArrayList<>());
		
		for (BeneficiosSociaisFamiliar obj : beneficios) {
			BeneficiosSociaisFamiliar aux = getBeneficio(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getBeneficiosSociais().add(obj);
				else{
					aux.setResposta(obj.getResposta());
					aux.setNivel(obj.getNivel());
				}
			}else{
				if (aux != null)
					familiar.getBeneficiosSociais().remove(aux);
			}
		}
		
		if (familiar.getCadeiasProdutivas() == null)
			familiar.setCadeiasProdutivas(new ArrayList<>());
		
		for (CadeiasProdutivasFamiliar obj : cadeias) {
			CadeiasProdutivasFamiliar aux = getCadeia(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getCadeiasProdutivas().add(obj);
				else{
					aux.setResposta(obj.getResposta());
					aux.setNivel(obj.getNivel());
				}
			}else{
				if (aux != null)
					familiar.getCadeiasProdutivas().remove(aux);
			}
		}*/
		
		/*if (familiar.getProdutosAgricultura() == null)
			familiar.setProdutosAgricultura(new ArrayList<>());
		
		for (ProdutoAgriculturaFamiliar obj : produtosAgricultura) {
			ProdutoAgriculturaFamiliar aux = getProdutoAgricultura(obj.getTipoProduto());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getProdutosAgricultura().add(obj);
				else{
					aux.setCliente(obj.getCliente());
					aux.setTipoAmbiente(obj.getTipoAmbiente());
					aux.setTipoUso(obj.getTipoUso());
					aux.setVendidoEmConjunto(obj.getVendidoEmConjunto());
				}
			}else{
				if (aux != null)
					familiar.getProdutosAgricultura().remove(aux);
			}
		}
		
		if (familiar.getProdutosPescado() == null)
			familiar.setProdutosPescado(new ArrayList<>());
		
		for (ProdutoPescadoFamiliar obj : produtosPescado) {
			ProdutoPescadoFamiliar aux = getProdutoPescado(obj.getTipoProduto());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getProdutosPescado().add(obj);
				else{
					aux.setCliente(obj.getCliente());
					aux.setTipoUso(obj.getTipoUso());
					aux.setVendidoEmConjunto(obj.getVendidoEmConjunto());
				}
			}else{
				if (aux != null)
					familiar.getProdutosPescado().remove(aux);
			}
		}*/
		
		/*if (familiar.getCriacoesAnimais() == null)
			familiar.setCriacoesAnimais(new ArrayList<>());
		
		for (CriacaoAnimalFamiliar obj : criacoesAnimais) {
			CriacaoAnimalFamiliar aux = getCriacaoAnimal(obj.getTipoCriacao());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getCriacoesAnimais().add(obj);
				else{
					aux.setCliente(obj.getCliente());
					aux.setTipoAmbiente(obj.getTipoAmbiente());
					aux.setTipoUso(obj.getTipoUso());
					aux.setVendidoEmConjunto(obj.getVendidoEmConjunto());
					aux.setQuantidade(obj.getQuantidade());
				}
			}else{
				if (aux != null)
					familiar.getCriacoesAnimais().remove(aux);
			}
		}
		
		if (familiar.getProdutosFlorestaisMadeireiros() == null)
			familiar.setProdutosFlorestaisMadeireiros(new ArrayList<>());
		
		for (ProdutoFlorestalMadeireiroFamiliar obj : produtosMadeireiros) {
			ProdutoFlorestalMadeireiroFamiliar aux = getProdutoMadeireiro(obj.getTipoProduto());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getProdutosFlorestaisMadeireiros().add(obj);
				else{
					aux.setCliente(obj.getCliente());
					aux.setTipoAmbiente(obj.getTipoAmbiente());
					aux.setTipoUso(obj.getTipoUso());
					aux.setVendidoEmConjunto(obj.getVendidoEmConjunto());
				}
			}else{
				if (aux != null)
					familiar.getProdutosFlorestaisMadeireiros().remove(aux);
			}
		}
		
		if (familiar.getProdutosFlorestaisNaoMadeireiros() == null)
			familiar.setProdutosFlorestaisNaoMadeireiros(new ArrayList<>());
		
		for (ProdutoFlorestalNaoMadeireiroFamiliar obj : produtosNaoMadeireiros) {
			ProdutoFlorestalNaoMadeireiroFamiliar aux = getProdutoNaoMadeireiro(obj.getTipoProduto());
			if (obj.getSelecionado()){
				obj.setFamiliar(familiar);
				if (aux == null)
					familiar.getProdutosFlorestaisNaoMadeireiros().add(obj);
				else{
					aux.setCliente(obj.getCliente());
					aux.setTipoAmbiente(obj.getTipoAmbiente());
					aux.setTipoUso(obj.getTipoUso());
					aux.setVendidoEmConjunto(obj.getVendidoEmConjunto());
				}
			}else{
				if (aux != null)
					familiar.getProdutosFlorestaisNaoMadeireiros().remove(aux);
			}
		}*/
		
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			if(familiarService.salvar(familiar)){
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Familiar salvo com Sucesso!", "");
				context.addMessage("msg", msg);
			}else{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Familiar","");
				context.addMessage("msg", msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
			
		return "cadastro_familiar";
	}
	
	public String delete(Familiar familiar){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if(familiarService.update(familiar)){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Familiar salvo com Sucesso!", "");
			context.addMessage("msg", msg);
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Familiar","");
			context.addMessage("msg", msg);
		}
		return "cadastro_familiar";
	}
	
	public void novo(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("cadastro_familiar_edit.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String redirect(){
		return "cadastro_familiar_edit";
	}
	
	@PostConstruct
	public void init(){
		String ide = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idFamiliar");
		if((ide != null)&&(!(ide.equals("null")))){
			familiar = familiarService.getFamiliarById(new Long(ide));
		}else{
			familiar = new Familiar();
			familiar.setFinanceiramenteAtivo(TipoFinanceiramenteAtivoFamiliar.NAO);
			familiar.setTipoConvenio(TipoConvenioFamiliar.FAS);
			familiar.setIdentificacaoBancaria(TipoIdentificacaoBancariaFamiliar.POR_CPF);
			familiar.setParticipacaoOficina(true);
			familiar.setDeclaracaoMoradia(true);
			familiar.setDataModificacaoMembros(new Date());
		}
	}
	
	public TipoIdentificacaoFamiliar[] getTiposIdentificacao(){
		
		if (!editandoIdentificacao){
			List<TipoIdentificacaoFamiliar> list = new ArrayList<>();
			if (familiar != null && familiar.getIdentificacoes() != null){
				
				for (TipoIdentificacaoFamiliar tipo : TipoIdentificacaoFamiliar.values()) {
					IdentificacaoFamiliar i = getIdentificacao(tipo);
					if (i == null && tipo != TipoIdentificacaoFamiliar.OUTRO){
						list.add(tipo);
							
					}
				}
				
				TipoIdentificacaoFamiliar[] tipos = new TipoIdentificacaoFamiliar[list.size() + 1];
				
				int count = 0;
				if (list.size() != 0){
					for (TipoIdentificacaoFamiliar tipoIdentificacaoFamiliar : list) {
						tipos[count] = tipoIdentificacaoFamiliar;
						count++;
					}
					tipos[count] = TipoIdentificacaoFamiliar.OUTRO;
				}else
					tipos[0] = TipoIdentificacaoFamiliar.OUTRO;
				
				if (tipos.length == 1)
					if (identificacao != null)
						identificacao.setTipoIdentificacao(TipoIdentificacaoFamiliar.OUTRO);
				
				return tipos;
			}else{
				return TipoIdentificacaoFamiliar.values();
			}
		}else
			return TipoIdentificacaoFamiliar.values();
	}
	
	public IdentificacaoFamiliar getIdentificacao(TipoIdentificacaoFamiliar tipo){
		if (familiar.getIdentificacoes() != null){
			for (IdentificacaoFamiliar ident : familiar.getIdentificacoes()) {
				if (ident.getTipoIdentificacao() == tipo)
					return ident;
			}
		}
		
		return null;
	}
	
	public void prepararCadastroIdentificacao(){
		editandoIdentificacao = false;
		identificacao = new IdentificacaoFamiliar();
	}
	
	public void alterarStatusCartao(){
		if (familiar.getFinanceiramenteAtivo().equals(TipoFinanceiramenteAtivoFamiliar.DESLIGADO))
			familiar.setStatusCartao(TipoStatusCartaoFamiliar.SUSPENSO);
	}
	
	public void prepararEditarIdentificacao(){
		editandoIdentificacao = true;
		identificacaoAux = new IdentificacaoFamiliar();
		identificacaoAux.setTipoIdentificacao(identificacao.getTipoIdentificacao());
		identificacaoAux.setCpf(identificacao.getCpf());
		identificacaoAux.setApelido(identificacao.getApelido());
		identificacaoAux.setAtualmenteEstuda(identificacao.getAtualmenteEstuda());
		identificacaoAux.setDataNascimento(identificacao.getDataNascimento());
		identificacaoAux.setMunicipio(identificacao.getMunicipio());
		identificacaoAux.setNome(identificacao.getNome());
		identificacaoAux.setOutroParentesco(identificacao.getOutroParentesco());
		identificacaoAux.setProfissaoAtividade(identificacao.getProfissaoAtividade());
		identificacaoAux.setQuantidadeAnosMoradia(identificacao.getQuantidadeAnosMoradia());
		identificacaoAux.setRg(identificacao.getRg());
		identificacaoAux.setSempreMorouNesteLugar(identificacao.getSempreMorouNesteLugar());
		identificacaoAux.setTemCarteiraTrabalho(identificacao.getTemCarteiraTrabalho());
		identificacaoAux.setTemCertidaoCasamento(identificacao.getTemCertidaoCasamento());
		identificacaoAux.setTemCertidaoNascimento(identificacao.getTemCertidaoNascimento());
		identificacaoAux.setTemCPF(identificacao.getTemCPF());
		identificacaoAux.setTemRegMilitar(identificacao.getTemRegMilitar());
		identificacaoAux.setTemRG(identificacao.getTemRG());
		identificacaoAux.setTemTituloEleitor(identificacao.getTemTituloEleitor());
		identificacaoAux.setTipoAnoEscolaridade(identificacao.getTipoAnoEscolaridade());
		identificacaoAux.setTipoEscolaridade(identificacao.getTipoEscolaridade());
		identificacaoAux.setTipoGenero(identificacao.getTipoGenero());
		identificacaoAux.setTipoIdentificacao(identificacao.getTipoIdentificacao());
		identificacaoAux.setTipoParentesco(identificacao.getTipoParentesco());
		identificacaoAux.setUltimoLugarMorou(identificacao.getUltimoLugarMorou());
	}
	
	private Boolean membroModificado(){
		
		if (!identificacao.getTipoIdentificacao().equals(identificacaoAux.getTipoIdentificacao()))
			return true;
		if (!identificacao.getCpf().equals(identificacaoAux.getCpf()))
			return true;
		if (!identificacao.getApelido().equals(identificacaoAux.getApelido()))
			return true;
		if (identificacao.getAtualmenteEstuda() != identificacaoAux.getAtualmenteEstuda())
			return true;
		if (identificacao.getDataNascimento().getTime() != identificacaoAux.getDataNascimento().getTime())
			return true;
		if (identificacao.getMunicipio().getId().longValue() != identificacaoAux.getMunicipio().getId().longValue())
			return true;
		if (!identificacao.getNome().equals(identificacaoAux.getNome()))
			return true;
		if (!identificacao.getOutroParentesco().equals(identificacaoAux.getOutroParentesco()))
			return true;
		if (!identificacao.getProfissaoAtividade().equals(identificacaoAux.getProfissaoAtividade()))
			return true;
		if (identificacao.getQuantidadeAnosMoradia() != identificacaoAux.getQuantidadeAnosMoradia())
			return true;
		if (!identificacao.getRg().equals(identificacaoAux.getRg()))
			return true;
		if (identificacao.getSempreMorouNesteLugar() != identificacaoAux.getSempreMorouNesteLugar())
			return true;
		if (identificacao.getTemCarteiraTrabalho() != identificacaoAux.getTemCarteiraTrabalho())
			return true;
		if (identificacao.getTemCertidaoCasamento() != identificacaoAux.getTemCertidaoCasamento())
			return true;
		if (identificacao.getTemCertidaoNascimento() != identificacaoAux.getTemCertidaoNascimento())
			return true;
		if (identificacao.getTemCPF() != identificacaoAux.getTemCPF())
			return true;
		if (identificacao.getTemRegMilitar() != identificacaoAux.getTemRegMilitar())
			return true;
		if (identificacao.getTemRG() != identificacaoAux.getTemRG())
			return true;
		if (identificacao.getTemTituloEleitor() != identificacaoAux.getTemTituloEleitor())
			return true;
		if (!identificacao.getTipoAnoEscolaridade().equals(identificacaoAux.getTipoAnoEscolaridade()))
			return true;
		if (!identificacao.getTipoEscolaridade().equals(identificacaoAux.getTipoEscolaridade()))
			return true;
		if (!identificacao.getTipoGenero().equals(identificacaoAux.getTipoGenero()))
			return true;
		if (!identificacao.getTipoIdentificacao().equals(identificacaoAux.getTipoIdentificacao()))
			return true;
		if (!identificacao.getTipoParentesco().equals(identificacaoAux.getTipoParentesco()))
			return true;
		if (!identificacao.getUltimoLugarMorou().equals(identificacaoAux.getUltimoLugarMorou()))
			return true;
			
		return false;
	}
	
	public Boolean qtdeTitularConjugeValido(){
		int qtdeTitular = 0;
		int qtdeConjuge = 0;
		
		for (IdentificacaoFamiliar ident : familiar.getIdentificacoes()) {
			if (ident.getTipoIdentificacao().toString().equals(TipoIdentificacaoFamiliar.TITULAR.toString()))
				qtdeTitular++;
			if (ident.getTipoIdentificacao().toString().equals(TipoIdentificacaoFamiliar.CONJUGE.toString()))
				qtdeConjuge++;
		}
		
		if (qtdeTitular > 1 || qtdeConjuge > 1)
			return false;
		else
			return true;
	}
	
	public Boolean idadeValida(){
		
		if (!identificacao.getTipoIdentificacao().equals(TipoIdentificacaoFamiliar.TITULAR))
			return true;
		
		Calendar calIdentificacao = Calendar.getInstance();
		calIdentificacao.setTime(identificacao.getDataNascimento());
		
		Calendar calAtual = Calendar.getInstance();
		calAtual.setTime(new Date());
		
		if ((calAtual.get(Calendar.YEAR) - calIdentificacao.get(Calendar.YEAR)) < 18)
			return false;
		else
			return true;
		
	}
	
	public Boolean cpfJaCadastrado(){
		
		for (IdentificacaoFamiliar i : familiar.getIdentificacoes()) {
			if (identificacao.getCpf().equals(i.getCpf()))
				return true;
		}
		
		return familiarService.verificaCPFCadastrado(identificacao.getCpf(), identificacao.getId());
	}
	
	public void adicionarIdentificacao(){
		FacesContext context = FacesContext.getCurrentInstance();
		try{
			Boolean valido = true;
			
			if (identificacao.getNome() == null || identificacao.getNome().isEmpty())
				throw new Exception("Nome é obrigatório");
			
			valido = qtdeTitularConjugeValido();
			if (valido){
				if (!identificacao.getTipoIdentificacao().toString().equals(TipoIdentificacaoFamiliar.OUTRO.toString())){
					valido = Util.validarCPF(identificacao.getCpf().replace(".", "").replace("-", ""));
					if (valido){
						valido = cpfJaCadastrado();
						if (!valido){
							valido = idadeValida();
							if (valido){
								if (!editandoIdentificacao){
									identificacao.setFamiliar(familiar);
									familiar.getIdentificacoes().add(identificacao);
								}else{
									if (membroModificado())
										familiar.setDataModificacaoMembros(new Date());
								}
							}else
								throw new Exception("O titular deve ter idade acima ou igual a 18 anos");
								
						}else
							throw new Exception("CPF já cadastrado");
					}else
						throw new Exception("CPF Inválido");
				}
				else{
					if (!editandoIdentificacao){
						identificacao.setFamiliar(familiar);
						familiar.getIdentificacoes().add(identificacao);
					}else{
						if (membroModificado())
							familiar.setDataModificacaoMembros(new Date());
					}
				}
			}else
				throw new Exception("Só deve haver um Titular e um Cônjuge cadastrados");
			
			//RequestContext.getCurrentInstance().execute("PF('dlg_cadastro_membro').hide()");
			PrimeFaces.current().executeScript("PF('dlg_cadastro_membro').hide()");
		}catch (Exception e) {
			e.printStackTrace();
			if (editandoIdentificacao){
				identificacao.setCpf(identificacaoAux.getCpf());
				identificacao.setTipoIdentificacao(identificacaoAux.getTipoIdentificacao());
			}
			//RequestContext.getCurrentInstance().execute("PF('dlg_cadastro_membro').show()");
			PrimeFaces.current().executeScript("PF('dlg_cadastro_membro').hide()");
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "", e.getMessage());
			context.addMessage("msg", msg);
		}
		
	}
	
	public void deleteIdentificacao(){
		familiar.getIdentificacoes().remove(identificacao);
	}
	//---
	
	//Cadastro de dinamica populacional
	public void prepararCadastroDinamica(){
		editandoDinamica = false;
		dinamicaPopulacional = new DinamicaPopulacionalFamiliar();
	}
	
	public void prepararEditarDinamica(){
		editandoDinamica = true;
	}
	
	private IdentificacaoFamiliar obterMembro(String nome){
		for (IdentificacaoFamiliar i : familiar.getIdentificacoes()) {
			if (nome.equals(i.getNome()))
				return i;
		}
		
		return null;
	}
	
	public void adicionarDinamica(){
		if (!editandoDinamica){
			
			if (dinamicaPopulacional.getMembro().getId() == null)
				dinamicaPopulacional.setMembro(obterMembro(dinamicaPopulacional.getMembro().getNome()));
			
			dinamicaPopulacional.setFamiliar(familiar);
			familiar.getDinamicas().add(dinamicaPopulacional);
		}
	}
	
	public void deleteDinamica(){
		familiar.getDinamicas().remove(dinamicaPopulacional);
	}
	//---
	
	//Cadastro de Sintomas comuns
	public void prepararCadastroSintoma(){
		editandoSintoma = false;
		sintoma = new SintomasComunsFamiliar();
		
		if (familiar.getSintomasSaude() == null || familiar.getSintomasSaude().size() == 0)
			sintoma.setNivel(1);
		else{
			int nivel = familiar.getSintomasSaude().get(familiar.getSintomasSaude().size() - 1).getNivel();
			
			sintoma.setNivel(nivel + 1);
		}
	}
	
	public void prepararEditarSintoma(){
		editandoSintoma = true;
	}
	
	public void adicionarSintoma(){
		if (!editandoSintoma){
			sintoma.setFamiliar(familiar);
			familiar.getSintomasSaude().add(sintoma);
		}
	}
	
	public void deleteSintoma(){
		familiar.getSintomasSaude().remove(sintoma);
	}
	//--
	
	//Cadastro de cadeias produtivas
	public void prepararCadastroCadeia(){
		editandoCadeia = false;
		cadeia = new CadeiasProdutivasFamiliar();
		
		if (familiar.getCadeiasProdutivas() == null || familiar.getCadeiasProdutivas().size() == 0)
			cadeia.setNivel(1);
		else{
			int nivel = familiar.getCadeiasProdutivas().get(familiar.getCadeiasProdutivas().size() - 1).getNivel();
			
			cadeia.setNivel(nivel + 1);
		}
	}
	
	public void prepararEditarCadeia(){
		editandoCadeia = true;
	}
	
	public void adicionarCadeia(){
		if (!editandoCadeia){
			cadeia.setFamiliar(familiar);
			familiar.getCadeiasProdutivas().add(cadeia);
		}
	}
	
	public void deleteCadeia(){
		familiar.getCadeiasProdutivas().remove(cadeia);
	}
	//--
	
	//Cadastro de produtos agricultura
	public void prepararCadastroProdutoAgricultura(){
		editandoProdutoAgricultura = false;
		produtoAgricultura = new ProdutoAgriculturaFamiliar();
	}
	
	public void prepararEditarProdutoAgricultura(){
		editandoProdutoAgricultura = true;
	}
	
	public void adicionarProdutoAgricultura(){
		if (!editandoProdutoAgricultura){
			produtoAgricultura.setFamiliar(familiar);
			familiar.getProdutosAgricultura().add(produtoAgricultura);
		}
	}
	
	public void deleteProdutoAgricultura(){
		familiar.getProdutosAgricultura().remove(produtoAgricultura);
	}
	//--
	
	//Cadastro de produtos pescado
	public void prepararCadastroProdutoPescado(){
		editandoProdutoPescado = false;
		produtoPescado = new ProdutoPescadoFamiliar();
	}
	
	public void prepararEditarProdutoPescado(){
		editandoProdutoPescado = true;
	}
	
	public void adicionarProdutoPescado(){
		if (!editandoProdutoPescado){
			produtoPescado.setFamiliar(familiar);
			familiar.getProdutosPescado().add(produtoPescado);
		}
	}
	
	public void deleteProdutoPescado(){
		familiar.getProdutosPescado().remove(produtoPescado);
	}
	//--
	
	//Cadastro de criacao animal
	public void prepararCadastroCriacaoAnimal(){
		editandoCriacaoAnimal = false;
		criacao = new CriacaoAnimalFamiliar();
	}
	
	public void prepararEditarCriacaoAnimal(){
		editandoCriacaoAnimal = true;
	}
	
	public void adicionarCriacaoAnimal(){
		if (!editandoCriacaoAnimal){
			criacao.setFamiliar(familiar);
			familiar.getCriacoesAnimais().add(criacao);
		}
	}
	
	public void deleteCriacaoAnimal(){
		familiar.getCriacoesAnimais().remove(criacao);
	}
	//--
	
	//Cadastro de produto florestal madeireiro
	public void prepararCadastroProdutoFlorestalMadeireiro(){
		editandoProdutoMadeireiro = false;
		produtoMadeireiro = new ProdutoFlorestalMadeireiroFamiliar();
	}
	
	public void prepararEditarProdutoFlorestalMadeireiro(){
		editandoProdutoMadeireiro = true;
	}
	
	public void adicionarProdutoFlorestalMadeireiro(){
		if (!editandoProdutoMadeireiro){
			produtoMadeireiro.setFamiliar(familiar);
			familiar.getProdutosFlorestaisMadeireiros().add(produtoMadeireiro);
		}
	}
	
	public void deleteProdutoFlorestalMadeireiro(){
		familiar.getProdutosFlorestaisMadeireiros().remove(produtoMadeireiro);
	}
	//--
	
	//Cadastro de produto florestal madeireiro
	public void prepararCadastroProdutoFlorestalNaoMadeireiro(){
		editandoProdutoNaoMadeireiro = false;
		produtoNaoMadeireiro = new ProdutoFlorestalNaoMadeireiroFamiliar();
	}
	
	public void prepararEditarProdutoFlorestalNaoMadeireiro(){
		editandoProdutoNaoMadeireiro = true;
	}
	
	public void adicionarProdutoFlorestalNaoMadeireiro(){
		if (!editandoProdutoNaoMadeireiro){
			produtoNaoMadeireiro.setFamiliar(familiar);
			familiar.getProdutosFlorestaisNaoMadeireiros().add(produtoNaoMadeireiro);
		}
	}
	
	public void deleteProdutoFlorestalNaoMadeireiro(){
		familiar.getProdutosFlorestaisNaoMadeireiros().remove(produtoNaoMadeireiro);
	}
	//--
	
	//Organizacao social
	public List<OrganizacaoSocialFormalFamiliar> getOrganizacoesFormais(){
		
		organizacoesFormais = new ArrayList<>();
		
		for (TipoOrganizacaoSocialFormalFamiliar tipo : TipoOrganizacaoSocialFormalFamiliar.values()) {
			OrganizacaoSocialFormalFamiliar aux = getOrganizacaoFormal(tipo);
			OrganizacaoSocialFormalFamiliar mun = new OrganizacaoSocialFormalFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			organizacoesFormais.add(mun);
		}
		
		return organizacoesFormais;
	}
	
	public OrganizacaoSocialFormalFamiliar getOrganizacaoFormal(TipoOrganizacaoSocialFormalFamiliar tipo){
		if (familiar.getOraganizacoesFormais() != null){
			for (OrganizacaoSocialFormalFamiliar organizacao : familiar.getOraganizacoesFormais()) {
				if (organizacao.getTipo().toString().equals(tipo.toString()))
					return organizacao;
			}
		}
		
		return null;
	}
	
	public List<OrganizacaoSocialInformalFamiliar> getOrganizacoesInformais(){
		
		organizacoesInformais = new ArrayList<>();
		
		for (TipoOrganizacaoSocialInformalFamiliar tipo : TipoOrganizacaoSocialInformalFamiliar.values()) {
			OrganizacaoSocialInformalFamiliar aux = getOrganizacaoInformal(tipo);
			OrganizacaoSocialInformalFamiliar mun = new OrganizacaoSocialInformalFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			organizacoesInformais.add(mun);
		}
		
		return organizacoesInformais;
	}
	
	public OrganizacaoSocialInformalFamiliar getOrganizacaoInformal(TipoOrganizacaoSocialInformalFamiliar tipo){
		if (familiar.getOraganizacoesInformais() != null){
			for (OrganizacaoSocialInformalFamiliar organizacao : familiar.getOraganizacoesInformais()) {
				if (organizacao.getTipo().toString().equals(tipo.toString()))
					return organizacao;
			}
		}
		
		return null;
	}
	//--
	
	//Orientacao religiosa
	public List<OrientacaoReligiosaFamiliar> getOrientacoesReligiosas(){
		
		orientacoesReligiosas = new ArrayList<>();
		
		for (TipoOrientacaoReligiosaFamiliar tipo : TipoOrientacaoReligiosaFamiliar.values()) {
			OrientacaoReligiosaFamiliar aux = getOrientacaoReligiosa(tipo);
			OrientacaoReligiosaFamiliar mun = new OrientacaoReligiosaFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			orientacoesReligiosas.add(mun);
		}
		
		return orientacoesReligiosas;
	}
	
	public OrientacaoReligiosaFamiliar getOrientacaoReligiosa(TipoOrientacaoReligiosaFamiliar tipo){
		if (familiar.getOrientacoesReligiosas() != null){
			for (OrientacaoReligiosaFamiliar orientacao : familiar.getOrientacoesReligiosas()) {
				if (orientacao.getTipo().toString().equals(tipo.toString()))
					return orientacao;
			}
		}
		
		return null;
	}
	//--
	
	//Equipamentos e infraestrutura
	public List<EquipamentoEInfraestruturaFamiliar> getEquipamentos(){
		
		equipamentos = new ArrayList<>();
		
		for (TipoEquipamentosInfraestruturaFamiliar tipo : TipoEquipamentosInfraestruturaFamiliar.values()) {
			EquipamentoEInfraestruturaFamiliar aux = getEquipamento(tipo);
			EquipamentoEInfraestruturaFamiliar mun = new EquipamentoEInfraestruturaFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			equipamentos.add(mun);
		}
		
		return equipamentos;
	}
	
	public EquipamentoEInfraestruturaFamiliar getEquipamento(TipoEquipamentosInfraestruturaFamiliar tipo){
		if (familiar.getEquipamentosInfraestrutura() != null){
			for (EquipamentoEInfraestruturaFamiliar obj : familiar.getEquipamentosInfraestrutura()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	//--
	
	//Transporte
	public List<TransporteFamiliar> getTransportes(){
		
		transportes = new ArrayList<>();
		
		for (TipoTransporteFamiliar tipo : TipoTransporteFamiliar.values()) {
			TransporteFamiliar aux = getTransporte(tipo);
			TransporteFamiliar mun = new TransporteFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			transportes.add(mun);
		}
		
		return transportes;
	}
	
	public TransporteFamiliar getTransporte(TipoTransporteFamiliar tipo){
		if (familiar.getTransportes() != null){
			for (TransporteFamiliar obj : familiar.getTransportes()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	//--
	
	//Saneamento basico
	public List<AbastecimentoAguaFamiliar> getAbastecimentos(){
		
		abastecimentos = new ArrayList<>();
		
		for (TipoAbastecimentoAguaFamiliar tipo : TipoAbastecimentoAguaFamiliar.values()) {
			AbastecimentoAguaFamiliar aux = getAbastecimento(tipo);
			AbastecimentoAguaFamiliar mun = new AbastecimentoAguaFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			abastecimentos.add(mun);
		}
		
		return abastecimentos;
	}
	
	public AbastecimentoAguaFamiliar getAbastecimento(TipoAbastecimentoAguaFamiliar tipo){
		if (familiar.getTransportes() != null){
			for (AbastecimentoAguaFamiliar obj : familiar.getAbastecimentosAgua()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<TratamentoAguaFamiliar> getTratamentos(){
		
		tratamentos = new ArrayList<>();
		
		for (TipoTratamentoAguaFamiliar tipo : TipoTratamentoAguaFamiliar.values()) {
			TratamentoAguaFamiliar aux = getTratamento(tipo);
			TratamentoAguaFamiliar mun = new TratamentoAguaFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			tratamentos.add(mun);
		}
		
		return tratamentos;
	}
	
	public TratamentoAguaFamiliar getTratamento(TipoTratamentoAguaFamiliar tipo){
		if (familiar.getTratamentosAgua() != null){
			for (TratamentoAguaFamiliar obj : familiar.getTratamentosAgua()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<DestinoEsgotoFamiliar> getDestinosEsgoto(){
		
		destinosEsgoto = new ArrayList<>();
		
		for (TipoDestinoEsgotoFamiliar tipo : TipoDestinoEsgotoFamiliar.values()) {
			DestinoEsgotoFamiliar aux = getDestinoEsgoto(tipo);
			DestinoEsgotoFamiliar mun = new DestinoEsgotoFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			destinosEsgoto.add(mun);
		}
		
		return destinosEsgoto;
	}
	
	public DestinoEsgotoFamiliar getDestinoEsgoto(TipoDestinoEsgotoFamiliar tipo){
		if (familiar.getDestinosEsgoto() != null){
			for (DestinoEsgotoFamiliar obj : familiar.getDestinosEsgoto()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<DestinoLixoFamiliar> getDestinosLixo(){
		
		destinosLixo = new ArrayList<>();
		
		for (TipoDestinoLixoFamiliar tipo : TipoDestinoLixoFamiliar.values()) {
			DestinoLixoFamiliar aux = getDestinoLixo(tipo);
			DestinoLixoFamiliar mun = new DestinoLixoFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			destinosLixo.add(mun);
		}
		
		return destinosLixo;
	}
	
	public DestinoLixoFamiliar getDestinoLixo(TipoDestinoLixoFamiliar tipo){
		if (familiar.getDestinosLixo() != null){
			for (DestinoLixoFamiliar obj : familiar.getDestinosLixo()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	//--
	
	//Protocolo
	
	public void gerarProtocolo(){
		try{
			
//			if (familiar.getContaBancaria() != null)
//				throw new Exception("A conta bancária já foi definida, não é possível gerar o protocolo.");
//			
//			if (familiar.getUnidadeConservacao() == null)
//				throw new Exception("Unidade de conservação não selecionada.");
//			
//			if (familiar.getMunicipio() == null)
//				throw new Exception("Município não selecionado.");
//			
//			String uc = String.format("%03d", familiar.getUnidadeConservacao().getCodigo() != null ? familiar.getUnidadeConservacao().getCodigo() : 0);
//			String mun = String.format("%02d", familiar.getMunicipio().getCodigo() != null ? familiar.getMunicipio().getCodigo() : 0);
//			Integer seq = familiarService.getSequencialFamiliasUC(familiar.getUnidadeConservacao().getId());
//			if (seq == 0)
//				seq = 1;
//			
//			String sequencial = String.format("%04d", seq);
//			
//			String protocolo = uc + mun + sequencial;
//		
//		
//			if (familiarService.existeProtocolo(protocolo))
//				throw new Exception("Protocolo já cadastrado, tente novamente.");
//			else{
//				familiar.setProtocolo(protocolo);
//				familiarService.salvar(familiar);
//			}
			
			throw new Exception("Em manutenção");
				
		}catch (Exception e) {
			FacesContext context = FacesContext.getCurrentInstance();
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_WARN, e.getMessage(), e.getMessage());
			context.addMessage("msg", msg);
		}
	}
	//--
	
	//Informacoes sobre saude
	/*public List<SintomasComunsFamiliar> getSintomas(){
		
		sintomas = new ArrayList<>();
		
		for (TipoSintomasComunsFamiliar tipo : TipoSintomasComunsFamiliar.values()) {
			SintomasComunsFamiliar aux = getSintoma(tipo);
			SintomasComunsFamiliar mun = new SintomasComunsFamiliar();
			mun.setTipo(tipo);
			mun.setSelecionado(true);
			if (aux != null){
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
				mun.setNivel(aux.getNivel());
			}
			
			mun.setTemResposta(tipo.getTemReposta());
			
			sintomas.add(mun);
		}
		
		return sintomas;
	}
	
	public SintomasComunsFamiliar getSintoma(TipoSintomasComunsFamiliar tipo){
		if (familiar.getSintomasSaude() != null){
			for (SintomasComunsFamiliar obj : familiar.getSintomasSaude()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}*/
	//--
	
	//Renda - atividades economicas
	/*public List<BeneficiosSociaisFamiliar> getBeneficios(){
		
		beneficios = new ArrayList<>();
		
		for (TipoBeneficiosSociaisFamiliar tipo : TipoBeneficiosSociaisFamiliar.values()) {
			BeneficiosSociaisFamiliar aux = getBeneficio(tipo);
			BeneficiosSociaisFamiliar mun = new BeneficiosSociaisFamiliar();
			mun.setTipo(tipo);
			mun.setSelecionado(true);
			if (aux != null){
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
				mun.setNivel(aux.getNivel());
			}
			
			mun.setTemResposta(tipo.getTemReposta());
			
			beneficios.add(mun);
		}
		
		return beneficios;
	}
	
	public BeneficiosSociaisFamiliar getBeneficio(TipoBeneficiosSociaisFamiliar tipo){
		if (familiar.getSintomasSaude() != null){
			for (BeneficiosSociaisFamiliar obj : familiar.getBeneficiosSociais()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<CadeiasProdutivasFamiliar> getCadeias(){
		
		cadeias = new ArrayList<>();
		
		for (TipoCadeiasProdutivasFamiliar tipo : TipoCadeiasProdutivasFamiliar.values()) {
			CadeiasProdutivasFamiliar aux = getCadeia(tipo);
			CadeiasProdutivasFamiliar mun = new CadeiasProdutivasFamiliar();
			mun.setTipo(tipo);
			mun.setSelecionado(true);
			if (aux != null){
				mun.setResposta(aux.getResposta());
				mun.setFamiliar(aux.getFamiliar());
				mun.setNivel(aux.getNivel());
			}
			
			mun.setTemResposta(tipo.getTemReposta());
			
			cadeias.add(mun);
		}
		
		return cadeias;
	}
	
	public CadeiasProdutivasFamiliar getCadeia(TipoCadeiasProdutivasFamiliar tipo){
		if (familiar.getSintomasSaude() != null){
			for (CadeiasProdutivasFamiliar obj : familiar.getCadeiasProdutivas()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}*/
	
	/*public List<ProdutoAgriculturaFamiliar> getProdutosAgricultura(){
		
		produtosAgricultura = new ArrayList<>();
		
		for (TipoProdutoAgriculturaFamiliar tipo : TipoProdutoAgriculturaFamiliar.values()) {
			ProdutoAgriculturaFamiliar aux = getProdutoAgricultura(tipo);
			ProdutoAgriculturaFamiliar mun = new ProdutoAgriculturaFamiliar();
			mun.setTipoProduto(tipo);
			mun.setSelecionado(true);
			if (aux != null){
				mun.setCliente(aux.getCliente());
				mun.setFamiliar(aux.getFamiliar());
				mun.setTipoAmbiente(aux.getTipoAmbiente());
				mun.setTipoUso(aux.getTipoUso());
				mun.setVendidoEmConjunto(aux.getVendidoEmConjunto());
			}
			
			produtosAgricultura.add(mun);
		}
		
		return produtosAgricultura;
	}
	
	public ProdutoAgriculturaFamiliar getProdutoAgricultura(TipoProdutoAgriculturaFamiliar tipo){
		if (familiar.getProdutosAgricultura() != null){
			for (ProdutoAgriculturaFamiliar obj : familiar.getProdutosAgricultura()) {
				if (obj.getTipoProduto().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<ProdutoPescadoFamiliar> getProdutosPescado(){
		
		produtosPescado = new ArrayList<>();
		
		for (TipoProdutoAgriculturaFamiliar tipo : TipoProdutoAgriculturaFamiliar.values()) {
			ProdutoPescadoFamiliar aux = getProdutoPescado(tipo);
			ProdutoPescadoFamiliar mun = new ProdutoPescadoFamiliar();
			mun.setTipoProduto(tipo);
			mun.setSelecionado(true);
			if (aux != null){
				mun.setCliente(aux.getCliente());
				mun.setFamiliar(aux.getFamiliar());
				mun.setTipoUso(aux.getTipoUso());
				mun.setVendidoEmConjunto(aux.getVendidoEmConjunto());
			}
			
			produtosPescado.add(mun);
		}
		
		return produtosPescado;
	}
	
	public ProdutoPescadoFamiliar getProdutoPescado(TipoProdutoAgriculturaFamiliar tipo){
		if (familiar.getProdutosPescado() != null){
			for (ProdutoPescadoFamiliar obj : familiar.getProdutosPescado()) {
				if (obj.getTipoProduto().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}*/
	
	/*public void prepararCadastroEspecie(){
		editandoEspecie = false;
		especie = new EspecieEvitaPescarFamiliar();
	}
	
	public void prepararEditarEspecie(){
		editandoEspecie = true;
	}
	
	public void adicionarEspecie(){
		if (!editandoEspecie){
			especie.setFamiliar(familiar);
			familiar.getEspeciesEvitaPescar().add(especie);
		}
	}
	
	public void deleteEspecie(){
		familiar.getEspeciesEvitaPescar().remove(especie);
	}*/
	
	/*public List<CriacaoAnimalFamiliar> getCriacoesAnimais(){
		
		criacoesAnimais = new ArrayList<>();
		
		for (TipoCriacaoAnimalFamiliar tipo : TipoCriacaoAnimalFamiliar.values()) {
			CriacaoAnimalFamiliar aux = getCriacaoAnimal(tipo);
			CriacaoAnimalFamiliar mun = new CriacaoAnimalFamiliar();
			mun.setTipoCriacao(tipo);
			mun.setSelecionado(true);
			if (aux != null){
				mun.setCliente(aux.getCliente());
				mun.setFamiliar(aux.getFamiliar());
				mun.setTipoUso(aux.getTipoUso());
				mun.setVendidoEmConjunto(aux.getVendidoEmConjunto());
				mun.setQuantidade(aux.getQuantidade());
				mun.setTipoAmbiente(aux.getTipoAmbiente());
			}
			
			criacoesAnimais.add(mun);
		}
		
		return criacoesAnimais;
	}
	
	public CriacaoAnimalFamiliar getCriacaoAnimal(TipoCriacaoAnimalFamiliar tipo){
		if (familiar.getCriacoesAnimais() != null){
			for (CriacaoAnimalFamiliar obj : familiar.getCriacoesAnimais()) {
				if (obj.getTipoCriacao().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<ProdutoFlorestalMadeireiroFamiliar> getProdutosMadeireiros(){
		
		produtosMadeireiros = new ArrayList<>();
		
		for (TipoProdutosFlorestaisMadeireirosFamiliar tipo : TipoProdutosFlorestaisMadeireirosFamiliar.values()) {
			ProdutoFlorestalMadeireiroFamiliar aux = getProdutoMadeireiro(tipo);
			ProdutoFlorestalMadeireiroFamiliar mun = new ProdutoFlorestalMadeireiroFamiliar();
			mun.setTipoProduto(tipo);
			mun.setSelecionado(true);
			if (aux != null){
				mun.setCliente(aux.getCliente());
				mun.setFamiliar(aux.getFamiliar());
				mun.setTipoUso(aux.getTipoUso());
				mun.setVendidoEmConjunto(aux.getVendidoEmConjunto());
				mun.setTipoAmbiente(aux.getTipoAmbiente());
			}
			
			produtosMadeireiros.add(mun);
		}
		
		return produtosMadeireiros;
	}
	
	public ProdutoFlorestalMadeireiroFamiliar getProdutoMadeireiro(TipoProdutosFlorestaisMadeireirosFamiliar tipo){
		if (familiar.getProdutosFlorestaisMadeireiros() != null){
			for (ProdutoFlorestalMadeireiroFamiliar obj : familiar.getProdutosFlorestaisMadeireiros()) {
				if (obj.getTipoProduto().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<ProdutoFlorestalNaoMadeireiroFamiliar> getProdutosNaoMadeireiros(){
		
		produtosNaoMadeireiros = new ArrayList<>();
		
		for (TipoProdutosFlorestaisNaoMadeireirosFamiliar tipo : TipoProdutosFlorestaisNaoMadeireirosFamiliar.values()) {
			ProdutoFlorestalNaoMadeireiroFamiliar aux = getProdutoNaoMadeireiro(tipo);
			ProdutoFlorestalNaoMadeireiroFamiliar mun = new ProdutoFlorestalNaoMadeireiroFamiliar();
			mun.setTipoProduto(tipo);
			mun.setSelecionado(true);
			if (aux != null){
				mun.setCliente(aux.getCliente());
				mun.setFamiliar(aux.getFamiliar());
				mun.setTipoUso(aux.getTipoUso());
				mun.setVendidoEmConjunto(aux.getVendidoEmConjunto());
				mun.setTipoAmbiente(aux.getTipoAmbiente());
			}
			
			produtosNaoMadeireiros.add(mun);
		}
		
		return produtosNaoMadeireiros;
	}
	
	public ProdutoFlorestalNaoMadeireiroFamiliar getProdutoNaoMadeireiro(TipoProdutosFlorestaisNaoMadeireirosFamiliar tipo){
		if (familiar.getProdutosFlorestaisNaoMadeireiros() != null){
			for (ProdutoFlorestalNaoMadeireiroFamiliar obj : familiar.getProdutosFlorestaisNaoMadeireiros()) {
				if (obj.getTipoProduto().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}*/
	//--
	
	public List<Comunidade> completeComunidade(String s) {
		if (s.length() > 2)
			return familiarService.buscarComunidades(s);

		return new ArrayList<Comunidade>();
	}
	
	public List<Municipio> completeMunicipio(String s) {
		if (s.length() > 2)
			return familiarService.buscarMunicipios(s);

		return new ArrayList<Municipio>();
	}
	
	public List<UnidadeConservacao> completeUnidadeConservacao(String s) {
		if (s.length() > 2)
			return familiarService.buscarUnidadesConservacao(s);

		return new ArrayList<UnidadeConservacao>();
	}
	
	public List<Localidade> completeLocalidade(String s) {
		if (s.length() > 2)
			return familiarService.buscarLocalidades(s);

		return new ArrayList<Localidade>();
	}
	
	public List<Localidade> completeLocalidadePorUc(String s) {
		if (s.length() > 2 && familiar.getUnidadeConservacao() != null){
			return familiarService.buscarLocalidadesPorUc(s, familiar.getUnidadeConservacao().getId());
		}

		return new ArrayList<Localidade>();
	}
	
	public List<Localidade> completeLocalidadePorUcFiltro(String s) {
		if (s.length() > 2 && filtroUnidadeConservacao != null){
			return familiarService.buscarLocalidadesPorUc(s, filtroUnidadeConservacao.getId());
		}

		return new ArrayList<Localidade>();
	}
	
	public List<Localidade> completeSetores(String s) {
		if (s.length() > 2 ){
			return familiarService.buscarPolos(s);
		}

		return new ArrayList<Localidade>();
	}
	
	public List<Localidade> completePolos(String s) {
		if (s.length() > 2 ){
			return familiarService.buscarPolos(s);
		}

		return new ArrayList<Localidade>();
	}
	
	public List<SintomaComum> completeSintomas(String s) {
		if (s.length() > 2)
			return familiarService.buscarSintomas(s);

		return new ArrayList<SintomaComum>();
	}
	
	public List<CadeiaProdutivaFamiliar> completeCadeias(String s) {
		if (s.length() > 2)
			return familiarService.buscarCadeias(s);

		return new ArrayList<CadeiaProdutivaFamiliar>();
	}
	
	public List<ProdutoAgricultura> completeProdutosAgricultura(String s) {
		if (s.length() > 2)
			return familiarService.buscarProdutosAgricultura(s);

		return new ArrayList<ProdutoAgricultura>();
	}
	
	public List<ProdutoPescado> completeProdutosPescado(String s) {
		if (s.length() > 2)
			return familiarService.buscarProdutosPescado(s);

		return new ArrayList<ProdutoPescado>();
	}
	
	public List<CriacaoAnimal> completeCriacoesAnimais(String s) {
		if (s.length() > 2)
			return familiarService.buscarCricoesAnimal(s);

		return new ArrayList<CriacaoAnimal>();
	}
	
	public List<ProdutoFlorestalMadeireiro> completeProdutosMadeireiros(String s) {
		if (s.length() > 2)
			return familiarService.buscarProdutosMadeireiros(s);

		return new ArrayList<ProdutoFlorestalMadeireiro>();
	}
	
	public List<ProdutoFlorestalNaoMadeireiro> completeProdutosNaoMadeireiros(String s) {
		if (s.length() > 2)
			return familiarService.buscarProdutosNaoMadeireiros(s);

		return new ArrayList<ProdutoFlorestalNaoMadeireiro>();
	}
	
	public List<TipoRiosFamiliar> completeRios(String s){
		List<TipoRiosFamiliar> rios = new ArrayList<>();
		
		if (s.length() > 2){
			for (TipoRiosFamiliar tipoRiosFamiliar : TipoRiosFamiliar.values()) {
				if (tipoRiosFamiliar.getDescricao().toLowerCase().contains(s.toLowerCase()))
					rios.add(tipoRiosFamiliar);
			}
		}
		
		return rios;
	}
	
	public void limparComunidade(){
		familiar.setComunidade(null);
	}
	
	//Selects
	public TipoParentescoFamiliar[] getTiposParentesco(){
		return TipoParentescoFamiliar.values();
	}
	
	public TipoGeneroFamiliar[] getGeneros(){
		return TipoGeneroFamiliar.values();
	}
	
	public TipoEscolaridadeFamiliar[] getEscolaridades(){
		return TipoEscolaridadeFamiliar.values();
	}
	
	public TipoAnoEscolaridadeFamiliar[] getAnosEscolaridade(){
		return TipoAnoEscolaridadeFamiliar.values();
	}
	
	public TipoSaidaComunidadeFamiliar[] getTiposSaida(){
		return TipoSaidaComunidadeFamiliar.values();
	}
	
	public TipoAmbienteFamiliar[] getTiposAmbiente(){
		return TipoAmbienteFamiliar.values();
	}
	
	public TipoHabitacaoFamiliar[] getTiposHabitacao(){
		return TipoHabitacaoFamiliar.values();
	}
	
	public TipoEstruturaHabitacaoFamiliar[] getTiposEstrutura(){
		return TipoEstruturaHabitacaoFamiliar.values();
	}
	
	public TipoPisoHabitacaoFamiliar[] getTiposPiso(){
		return TipoPisoHabitacaoFamiliar.values();
	}
	
	public TipoMaterialHabitacaoFamiliar[] getTiposMaterial(){
		return TipoMaterialHabitacaoFamiliar.values();
	}
	
	public TipoCoberturaHabitacaoFamiliar[] getTiposCobertura(){
		return TipoCoberturaHabitacaoFamiliar.values();
	}
	
	public TipoIluminacaoFamiliar[] getTiposIluminacao(){
		return TipoIluminacaoFamiliar.values();
	}
	
	public TipoAguaConsumoFamiliar[] getTiposAguaConsumo(){
		return TipoAguaConsumoFamiliar.values();
	}
	
	public TipoLocalSanitarioFamiliar[] getTiposLocalSanitario(){
		return TipoLocalSanitarioFamiliar.values();
	}
	
	public TipoUsoFamiliar[] getTiposUso(){
		return TipoUsoFamiliar.values();
	}
	
	public TipoConservamentoPescadoFamiliar[] getTiposConservamento(){
		return TipoConservamentoPescadoFamiliar.values();
	}
	
	public TipoRiosFamiliar[] getRios(){
		return TipoRiosFamiliar.values();
	}
	
	public TipoFinanceiramenteAtivoFamiliar[] getTiposFinanceiramenteAtivo(){
		return TipoFinanceiramenteAtivoFamiliar.values();
	}
	
	public TipoStatusCartaoFamiliar[] getStatusCartao(){
		return TipoStatusCartaoFamiliar.values();
	}
	
	public TipoConvenioFamiliar[] getConvenios(){
		return TipoConvenioFamiliar.values();
	}
	
	public TipoIdentificacaoBancariaFamiliar[] getIdentificacoesBancarias(){
		return TipoIdentificacaoBancariaFamiliar.values();
	}
	
	public TipoDesligamentoFamiliar[] getTiposDesligamento(){
		return TipoDesligamentoFamiliar.values();
	}
	
	public TipoTerrenoFamiliar[] getTiposTerreno(){
		return TipoTerrenoFamiliar.values();
	}
	
	public TipoDesbloqueioRecursoFamiliar[] getTiposDesbloqueioRecurso(){
		return TipoDesbloqueioRecursoFamiliar.values();
	}
	
	//Getter and Setter ----------------------------------------------------------------------------------------
	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public Familiar getFamiliar() {
		return familiar;
	}

	public void setFamiliar(Familiar familiar) {
		this.familiar = familiar;
	}

	public IdentificacaoFamiliar getIdentificacao() {
		return identificacao;
	}

	public void setIdentificacao(IdentificacaoFamiliar identificacao) {
		this.identificacao = identificacao;
	}

	public DinamicaPopulacionalFamiliar getDinamicaPopulacional() {
		return dinamicaPopulacional;
	}

	public void setDinamicaPopulacional(DinamicaPopulacionalFamiliar dinamicaPopulacional) {
		this.dinamicaPopulacional = dinamicaPopulacional;
	}

	public SintomasComunsFamiliar getSintoma() {
		return sintoma;
	}

	public void setSintoma(SintomasComunsFamiliar sintoma) {
		this.sintoma = sintoma;
	}

	public CadeiasProdutivasFamiliar getCadeia() {
		return cadeia;
	}

	public void setCadeia(CadeiasProdutivasFamiliar cadeia) {
		this.cadeia = cadeia;
	}

	public ProdutoAgriculturaFamiliar getProdutoAgricultura() {
		return produtoAgricultura;
	}

	public void setProdutoAgricultura(ProdutoAgriculturaFamiliar produtoAgricultura) {
		this.produtoAgricultura = produtoAgricultura;
	}

	public ProdutoPescadoFamiliar getProdutoPescado() {
		return produtoPescado;
	}

	public void setProdutoPescado(ProdutoPescadoFamiliar produtoPescado) {
		this.produtoPescado = produtoPescado;
	}

	public CriacaoAnimalFamiliar getCriacao() {
		return criacao;
	}

	public void setCriacao(CriacaoAnimalFamiliar criacao) {
		this.criacao = criacao;
	}

	public ProdutoFlorestalMadeireiroFamiliar getProdutoMadeireiro() {
		return produtoMadeireiro;
	}

	public void setProdutoMadeireiro(ProdutoFlorestalMadeireiroFamiliar produtoMadeireiro) {
		this.produtoMadeireiro = produtoMadeireiro;
	}

	public ProdutoFlorestalNaoMadeireiroFamiliar getProdutoNaoMadeireiro() {
		return produtoNaoMadeireiro;
	}

	public void setProdutoNaoMadeireiro(ProdutoFlorestalNaoMadeireiroFamiliar produtoNaoMadeireiro) {
		this.produtoNaoMadeireiro = produtoNaoMadeireiro;
	}

	public String getModalShow() {
		return modalShow;
	}

	public void setModalShow(String modalShow) {
		this.modalShow = modalShow;
	}

	public String getFiltroProtocolo() {
		return filtroProtocolo;
	}

	public void setFiltroProtocolo(String filtroProtocolo) {
		this.filtroProtocolo = filtroProtocolo;
	}

	public UnidadeConservacao getFiltroUnidadeConservacao() {
		return filtroUnidadeConservacao;
	}

	public void setFiltroUnidadeConservacao(UnidadeConservacao filtroUnidadeConservacao) {
		this.filtroUnidadeConservacao = filtroUnidadeConservacao;
	}

	public Comunidade getFiltroComunidade() {
		return filtroComunidade;
	}

	public void setFiltroComunidade(Comunidade filtroComunidade) {
		this.filtroComunidade = filtroComunidade;
	}

	public List<Familiar> getListaFamiliar() {
		if (listaFamiliar == null)
			listaFamiliar = new ArrayList<>();
		return listaFamiliar;
	}

	public void setListaFamiliar(List<Familiar> listaFamiliar) {
		this.listaFamiliar = listaFamiliar;
	}
	
	
	
}
