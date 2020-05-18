package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import model.*;
import service.ComunitarioService;
import util.MakeMenu;

@Named(value = "comunitario_controller")
@ViewScoped
public class ComunitarioController implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
		
	@Inject
	private ComunitarioService comunitarioService;
	
	private List<DestinoEsgotoFamiliar> destinosEsgoto;
	
	private List<DestinoLixoFamiliar> destinosLixo;
	
	private List<RealizacaoReuniaoComunitario> realizacoesReunioes;
	
	private List<FormaPegarAguaComunitario> formasPegarAgua;
	
	private List<FinanciamentoVoadeiraComunitario> financiamentosVoadeira;
	
	private List<FinanciamentoTransporteComunitario> financiamentosTransporte;
	
	private List<TratamentoAguaFamiliar> tratamentosAgua;
	
	private Comunitario comunitario = new Comunitario();
	
	private InfraestruturaComunitariaComunitario infraEstrutura = new InfraestruturaComunitariaComunitario();
	
	private SintomasComunsFamiliar sintoma = new SintomasComunsFamiliar();
	
	private FonteAguaConsumoComunitario fonteAguaConsumo = new FonteAguaConsumoComunitario();
	
	private MeioComunicacaoComunitario meioComunicacao = new MeioComunicacaoComunitario();
	
	private MaterialInformativoComunitario materialInformativo = new MaterialInformativoComunitario();
	
	private AtividadeRendaComunitario atividadeRenda = new AtividadeRendaComunitario();
	
	private FonteEnergiaComunitario fonteEnergia = new FonteEnergiaComunitario();
	
	private Boolean editandoInfraEstrutura = false;
	
	private Boolean editandoSintoma = false;
	
	private Boolean editandoFonteAguaConsumo = false;
	
	private Boolean editandoMeioComunicacao = false;
	
	private Boolean editandoMaterialInformativo = false;
	
	private Boolean editandoAtividadeRenda = false;
	
	private Boolean editandoFonteEnergia = false;
	
	public List<Comunitario> getListaComunidades(){
		List<Comunitario> list = comunitarioService.getComunitario();
		
		return list;
	}
	
	
	private MenuLateral menu = new MenuLateral();
	
	
	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}
	
	public String listagemComunitario() {
		return "cadastro_comunitario?faces-redirect=true";
	}
	
	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuPBF();
	}
	
	public String insert(){
		
		if (comunitario.getDestinosEsgoto() == null)
			comunitario.setDestinosEsgoto(new ArrayList<>());
		
		for (DestinoEsgotoFamiliar obj : destinosEsgoto) {
			DestinoEsgotoFamiliar aux = getDestinoEsgoto(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setComunitario(comunitario);
				if (aux == null)
					comunitario.getDestinosEsgoto().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					comunitario.getDestinosEsgoto().remove(aux);
			}
		}
		
		if (comunitario.getDestinosLixo() == null)
			comunitario.setDestinosLixo(new ArrayList<>());
		
		for (DestinoLixoFamiliar obj : destinosLixo) {
			DestinoLixoFamiliar aux = getDestinoLixo(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setComunitario(comunitario);
				if (aux == null)
					comunitario.getDestinosLixo().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					comunitario.getDestinosLixo().remove(aux);
			}
		}
		
		if (comunitario.getRealizacaoReunioes() == null)
			comunitario.setRealizacaoReunioes(new ArrayList<>());
		
		for (RealizacaoReuniaoComunitario obj : realizacoesReunioes) {
			RealizacaoReuniaoComunitario aux = getRealizacaoReuniao(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setComunitario(comunitario);
				if (aux == null)
					comunitario.getRealizacaoReunioes().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					comunitario.getRealizacaoReunioes().remove(aux);
			}
		}
		
		if (comunitario.getFinanciamentosVoadeira() == null)
			comunitario.setFinanciamentosVoadeira(new ArrayList<>());
		
		for (FinanciamentoVoadeiraComunitario obj : financiamentosVoadeira) {
			FinanciamentoVoadeiraComunitario aux = getFinanciamentoVoadeira(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setComunitario(comunitario);
				if (aux == null)
					comunitario.getFinanciamentosVoadeira().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					comunitario.getFinanciamentosVoadeira().remove(aux);
			}
		}
		
		if (comunitario.getFinanciamentosTransporte() == null)
			comunitario.setFinanciamentosTransporte(new ArrayList<>());
		
		for (FinanciamentoTransporteComunitario obj : financiamentosTransporte) {
			FinanciamentoTransporteComunitario aux = getFinanciamentoTransporte(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setComunitario(comunitario);
				if (aux == null)
					comunitario.getFinanciamentosTransporte().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					comunitario.getFinanciamentosTransporte().remove(aux);
			}
		}
		
		if (comunitario.getFormasPegarAgua() == null)
			comunitario.setFormasPegarAgua(new ArrayList<>());
		
		for (FormaPegarAguaComunitario obj : formasPegarAgua) {
			FormaPegarAguaComunitario aux = getFormaPegarAgua(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setComunitario(comunitario);
				if (aux == null)
					comunitario.getFormasPegarAgua().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					comunitario.getFormasPegarAgua().remove(aux);
			}
		}
		
		if (comunitario.getTratamentosAgua() == null)
			comunitario.setTratamentosAgua(new ArrayList<>());
		
		for (TratamentoAguaFamiliar obj : tratamentosAgua) {
			TratamentoAguaFamiliar aux = getTratamento(obj.getTipo());
			if (obj.getSelecionado()){
				obj.setComunitario(comunitario);
				if (aux == null)
					comunitario.getTratamentosAgua().add(obj);
				else{
					aux.setResposta(obj.getResposta());
				}
			}else{
				if (aux != null)
					comunitario.getTratamentosAgua().remove(aux);
			}
		}
		
		try{
			FacesContext context = FacesContext.getCurrentInstance();
			if(comunitarioService.salvar(comunitario)){
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Comunidade salva com Sucesso!", "");
				context.addMessage("msg", msg);
			}else{
				FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Comunidade","");
				context.addMessage("msg", msg);
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
			
		return "cadastro_comunitario";
	}
	
	public String delete(Comunitario comunitario){
		FacesContext context = FacesContext.getCurrentInstance();
		
		if(comunitarioService.update(comunitario)){
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, "Comunidade salva com Sucesso!", "");
			context.addMessage("msg", msg);
		}else{
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_ERROR, "Erro ao salvar Comunidade","");
			context.addMessage("msg", msg);
		}
		return "cadastro_comunitario";
	}
	
	public void novo(){
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("cadastro_comunitario_edit.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void prepararCadastroInfraEstrutura(){
		editandoInfraEstrutura = false;
		infraEstrutura = new InfraestruturaComunitariaComunitario();
	}
	
	public void prepararEditarInfraEstrutura(){
		editandoInfraEstrutura = true;
	}
	
	public void adicionarInfraEstrutura(){
		if (!editandoInfraEstrutura){
			infraEstrutura.setComunitario(comunitario);
			comunitario.getInfraestruturasComunitarias().add(infraEstrutura);
		}
	}
	
	public void deleteInfraEstrutura(){
		comunitario.getInfraestruturasComunitarias().remove(infraEstrutura);
	}
	
	public String redirect(){
		return "cadastro_comunitario_edit";
	}
	
	//Cadastro de Sintomas comuns
	public void prepararCadastroSintoma(){
		editandoSintoma = false;
		sintoma = new SintomasComunsFamiliar();
		
		if (comunitario.getSintomasSaude() == null || comunitario.getSintomasSaude().size() == 0)
			sintoma.setNivel(1);
		else{
			int nivel = comunitario.getSintomasSaude().get(comunitario.getSintomasSaude().size() - 1).getNivel();
			
			sintoma.setNivel(nivel + 1);
		}
	}
	
	public void prepararEditarSintoma(){
		editandoSintoma = true;
	}
	
	public void adicionarSintoma(){
		if (!editandoSintoma){
			sintoma.setComunitario(comunitario);
			comunitario.getSintomasSaude().add(sintoma);
		}
	}
	
	public void deleteSintoma(){
		comunitario.getSintomasSaude().remove(sintoma);
	}
	//--
	
	//Cadastro de Fonte de agua de consumo
	public void prepararCadastroFonteAgua(){
		editandoFonteAguaConsumo = false;
		fonteAguaConsumo = new FonteAguaConsumoComunitario();
		
		if (comunitario.getFontesAguaConsumo() == null || comunitario.getFontesAguaConsumo().size() == 0)
			fonteAguaConsumo.setNivel(1);
		else{
			int nivel = comunitario.getFontesAguaConsumo().get(comunitario.getFontesAguaConsumo().size() - 1).getNivel();
			
			fonteAguaConsumo.setNivel(nivel + 1);
		}
	}
	
	public void prepararEditarFonteAgua(){
		editandoFonteAguaConsumo = true;
	}
	
	public void adicionarFonteAgua(){
		if (!editandoFonteAguaConsumo){
			fonteAguaConsumo.setComunitario(comunitario);
			comunitario.getFontesAguaConsumo().add(fonteAguaConsumo);
		}
	}
	
	public void deleteFonteAgua(){
		comunitario.getFontesAguaConsumo().remove(fonteAguaConsumo);
	}
	//--
	
	//Cadastro de fonte de energia
	public void prepararCadastroFonteEnergia(){
		editandoFonteEnergia = false;
		fonteEnergia = new FonteEnergiaComunitario();
		
		if (comunitario.getFontesEnergia() == null || comunitario.getFontesEnergia().size() == 0)
			fonteEnergia.setNivel(1);
		else{
			int nivel = comunitario.getFontesEnergia().get(comunitario.getFontesEnergia().size() - 1).getNivel();
			
			fonteEnergia.setNivel(nivel + 1);
		}
	}
	
	public void prepararEditarFonteEnergia(){
		editandoFonteEnergia = true;
	}
	
	public void adicionarFonteEnergia(){
		if (!editandoFonteEnergia){
			fonteEnergia.setComunitario(comunitario);
			comunitario.getFontesEnergia().add(fonteEnergia);
		}
	}
	
	public void deleteFonteEnergia(){
		comunitario.getFontesEnergia().remove(fonteEnergia);
	}
	//--
	
	//Cadastro de meio de comunicacao
	public void prepararCadastroMeioComunicacao(){
		editandoMeioComunicacao = false;
		meioComunicacao = new MeioComunicacaoComunitario();
		
		if (comunitario.getMeiosComunicacao() == null || comunitario.getMeiosComunicacao().size() == 0)
			meioComunicacao.setNivel(1);
		else{
			int nivel = comunitario.getMeiosComunicacao().get(comunitario.getMeiosComunicacao().size() - 1).getNivel();
			
			meioComunicacao.setNivel(nivel + 1);
		}
	}
	
	public void prepararEditarMeioComunicacao(){
		editandoMeioComunicacao = true;
	}
	
	public void adicionarMeioComunicacao(){
		if (!editandoMeioComunicacao){
			meioComunicacao.setComunitario(comunitario);
			comunitario.getMeiosComunicacao().add(meioComunicacao);
		}
	}
	
	public void deleteMeioComunicacao(){
		comunitario.getMeiosComunicacao().remove(meioComunicacao);
	}
	//--
	
	//Cadastro de material informativo
	public void prepararCadastroMaterialInformativo(){
		editandoMaterialInformativo = false;
		materialInformativo = new MaterialInformativoComunitario();
		
		if (comunitario.getMateriaisInformativos() == null || comunitario.getMateriaisInformativos().size() == 0)
			materialInformativo.setNivel(1);
		else{
			int nivel = comunitario.getMateriaisInformativos().get(comunitario.getMateriaisInformativos().size() - 1).getNivel();
			
			materialInformativo.setNivel(nivel + 1);
		}
	}
	
	public void prepararEditarMaterialInformativo(){
		editandoMaterialInformativo = true;
	}
	
	public void adicionarMaterialInformativo(){
		if (!editandoMaterialInformativo){
			materialInformativo.setComunitario(comunitario);
			comunitario.getMateriaisInformativos().add(materialInformativo);
		}
	}
	
	public void deleteMaterialInformativo(){
		comunitario.getMateriaisInformativos().remove(materialInformativo);
	}
	//--
	
	//Cadastro de atividades de renda
	public void prepararCadastroAtividadeRenda(){
		editandoAtividadeRenda = false;
		atividadeRenda = new AtividadeRendaComunitario();
		
		if (comunitario.getAtividadesRenda() == null || comunitario.getAtividadesRenda().size() == 0)
			atividadeRenda.setNivel(1);
		else{
			int nivel = comunitario.getAtividadesRenda().get(comunitario.getAtividadesRenda().size() - 1).getNivel();
			
			atividadeRenda.setNivel(nivel + 1);
		}
	}
	
	public void prepararEditarAtividadeRenda(){
		editandoAtividadeRenda = true;
	}
	
	public void adicionarAtividadeRenda(){
		if (!editandoAtividadeRenda){
			atividadeRenda.setComunitario(comunitario);
			comunitario.getAtividadesRenda().add(atividadeRenda);
		}
	}
	
	public void deleteAtividadeRenda(){
		comunitario.getAtividadesRenda().remove(atividadeRenda);
	}
	//--
	
	@PostConstruct
	public void init(){
		String ide = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap().get("idComunitario");
		if((ide != null)&&(!(ide.equals("null")))){
			comunitario = comunitarioService.getComunitarioById(new Long(ide));
		}else{
			comunitario = new Comunitario();
		}
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
				mun.setComunitario(aux.getComunitario());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			destinosEsgoto.add(mun);
		}
		
		return destinosEsgoto;
	}
	
	public DestinoEsgotoFamiliar getDestinoEsgoto(TipoDestinoEsgotoFamiliar tipo){
		if (comunitario.getDestinosEsgoto() != null){
			for (DestinoEsgotoFamiliar obj : comunitario.getDestinosEsgoto()) {
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
				mun.setComunitario(aux.getComunitario());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			destinosLixo.add(mun);
		}
		
		return destinosLixo;
	}
	
	public DestinoLixoFamiliar getDestinoLixo(TipoDestinoLixoFamiliar tipo){
		if (comunitario.getDestinosLixo() != null){
			for (DestinoLixoFamiliar obj : comunitario.getDestinosLixo()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<RealizacaoReuniaoComunitario> getRealizacoesReunioes(){
		
		realizacoesReunioes = new ArrayList<>();
		
		for (TipoReunioesComunidadeComunitario tipo : TipoReunioesComunidadeComunitario.values()) {
			RealizacaoReuniaoComunitario aux = getRealizacaoReuniao(tipo);
			RealizacaoReuniaoComunitario mun = new RealizacaoReuniaoComunitario();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setComunitario(aux.getComunitario());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			realizacoesReunioes.add(mun);
		}
		
		return realizacoesReunioes;
	}
	
	public RealizacaoReuniaoComunitario getRealizacaoReuniao(TipoReunioesComunidadeComunitario tipo){
		if (comunitario.getRealizacaoReunioes() != null){
			for (RealizacaoReuniaoComunitario obj : comunitario.getRealizacaoReunioes()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<FinanciamentoVoadeiraComunitario> getFinanciamentosVoadeira(){
		
		financiamentosVoadeira = new ArrayList<>();
		
		for (TipoFinanciadorComunitario tipo : TipoFinanciadorComunitario.values()) {
			FinanciamentoVoadeiraComunitario aux = getFinanciamentoVoadeira(tipo);
			FinanciamentoVoadeiraComunitario mun = new FinanciamentoVoadeiraComunitario();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setComunitario(aux.getComunitario());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			financiamentosVoadeira.add(mun);
		}
		
		return financiamentosVoadeira;
	}
	
	public FinanciamentoVoadeiraComunitario getFinanciamentoVoadeira(TipoFinanciadorComunitario tipo){
		if (comunitario.getFinanciamentosVoadeira() != null){
			for (FinanciamentoVoadeiraComunitario obj : comunitario.getFinanciamentosVoadeira()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<FinanciamentoTransporteComunitario> getFinanciamentosTransporte(){
		
		financiamentosTransporte = new ArrayList<>();
		
		for (TipoFinanciadorComunitario tipo : TipoFinanciadorComunitario.values()) {
			FinanciamentoTransporteComunitario aux = getFinanciamentoTransporte(tipo);
			FinanciamentoTransporteComunitario mun = new FinanciamentoTransporteComunitario();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setComunitario(aux.getComunitario());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			financiamentosTransporte.add(mun);
		}
		
		return financiamentosTransporte;
	}
	
	public FinanciamentoTransporteComunitario getFinanciamentoTransporte(TipoFinanciadorComunitario tipo){
		if (comunitario.getFinanciamentosTransporte() != null){
			for (FinanciamentoTransporteComunitario obj : comunitario.getFinanciamentosTransporte()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<FormaPegarAguaComunitario> getFormasPegarAgua(){
		
		formasPegarAgua = new ArrayList<>();
		
		for (TipoFormasPegarAguaComunitario tipo : TipoFormasPegarAguaComunitario.values()) {
			FormaPegarAguaComunitario aux = getFormaPegarAgua(tipo);
			FormaPegarAguaComunitario mun = new FormaPegarAguaComunitario();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setComunitario(aux.getComunitario());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			formasPegarAgua.add(mun);
		}
		
		return formasPegarAgua;
	}
	
	public FormaPegarAguaComunitario getFormaPegarAgua(TipoFormasPegarAguaComunitario tipo){
		if (comunitario.getFormasPegarAgua() != null){
			for (FormaPegarAguaComunitario obj : comunitario.getFormasPegarAgua()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<TratamentoAguaFamiliar> getTratamentos(){
		
		tratamentosAgua = new ArrayList<>();
		
		for (TipoTratamentoAguaFamiliar tipo : TipoTratamentoAguaFamiliar.values()) {
			TratamentoAguaFamiliar aux = getTratamento(tipo);
			TratamentoAguaFamiliar mun = new TratamentoAguaFamiliar();
			mun.setTipo(tipo);
			if (aux == null)
				mun.setSelecionado(false);
			else{
				mun.setSelecionado(true);
				mun.setResposta(aux.getResposta());
				mun.setComunitario(aux.getComunitario());
			}
			mun.setTemResposta(tipo.getTemReposta());
			
			tratamentosAgua.add(mun);
		}
		
		return tratamentosAgua;
	}
	
	public TratamentoAguaFamiliar getTratamento(TipoTratamentoAguaFamiliar tipo){
		if (comunitario.getTratamentosAgua() != null){
			for (TratamentoAguaFamiliar obj : comunitario.getTratamentosAgua()) {
				if (obj.getTipo().toString().equals(tipo.toString()))
					return obj;
			}
		}
		
		return null;
	}
	
	public List<Comunidade> completeComunidade(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarComunidades(s);

		return new ArrayList<Comunidade>();
	}
	
	public List<Municipio> completeMunicipio(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarMunicipios(s);

		return new ArrayList<Municipio>();
	}
	
	public List<UnidadeConservacao> completeUnidadeConservacao(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarUnidadesConservacao(s);

		return new ArrayList<UnidadeConservacao>();
	}
	
	public List<Localidade> completeLocalidade(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarLocalidades(s);

		return new ArrayList<Localidade>();
	}
	
	public List<Localidade> completeLocalidadePorUc(String s) {
		if (s.length() > 2 && comunitario.getUnidadeConservacao() != null){
			return comunitarioService.buscarLocalidadesPorUc(s, comunitario.getUnidadeConservacao().getId());
		}

		return new ArrayList<Localidade>();
	}
	
	public List<Localidade> completeSetores(String s) {
		if (s.length() > 2 ){
			return comunitarioService.buscarPolos(s);
		}

		return new ArrayList<Localidade>();
	}
	
	public List<Localidade> completePolos(String s) {
		if (s.length() > 2 ){
			return comunitarioService.buscarPolos(s);
		}

		return new ArrayList<Localidade>();
	}
	
	public List<SintomaComum> completeSintomas(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarSintomas(s);

		return new ArrayList<SintomaComum>();
	}
	
	public List<FonteAguaConsumo> completeFontesAgua(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarFontesAgua(s);

		return new ArrayList<FonteAguaConsumo>();
	}
	
	public List<FonteEnergia> completeFonteEnergia(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarFontesEnergia(s);

		return new ArrayList<FonteEnergia>();
	}
	
	public List<MeioComunicacao> completeMeiosComunicacao(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarMeiosComunicacao(s);

		return new ArrayList<MeioComunicacao>();
	}
	
	public List<MaterialInformativo> completeMateriaisInformativos(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarMateriaisInformativos(s);

		return new ArrayList<MaterialInformativo>();
	}
	
	public List<AtividadeRenda> completeAtividadesRenda(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarAtividadesDeRenda(s);

		return new ArrayList<AtividadeRenda>();
	}
	
	public List<CadeiaProdutivaFamiliar> completeCadeias(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarCadeias(s);

		return new ArrayList<CadeiaProdutivaFamiliar>();
	}
	
	public List<ProdutoAgricultura> completeProdutosAgricultura(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarProdutosAgricultura(s);

		return new ArrayList<ProdutoAgricultura>();
	}
	
	public List<ProdutoPescado> completeProdutosPescado(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarProdutosPescado(s);

		return new ArrayList<ProdutoPescado>();
	}
	
	public List<CriacaoAnimal> completeCriacoesAnimais(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarCricoesAnimal(s);

		return new ArrayList<CriacaoAnimal>();
	}
	
	public List<ProdutoFlorestalMadeireiro> completeProdutosMadeireiros(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarProdutosMadeireiros(s);

		return new ArrayList<ProdutoFlorestalMadeireiro>();
	}
	
	public List<ProdutoFlorestalNaoMadeireiro> completeProdutosNaoMadeireiros(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarProdutosNaoMadeireiros(s);

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
	
	public List<InfraestruturaComunitaria> completeInfraEstuturas(String s) {
		if (s.length() > 2)
			return comunitarioService.buscarInfraestruturas(s);

		return new ArrayList<InfraestruturaComunitaria>();
	}
	
	public void limparComunidade(){
		comunitario.setComunidade(null);
	}
	
	//Selects
	public TipoAmbienteFamiliar[] getTiposAmbiente(){
		return TipoAmbienteFamiliar.values();
	}
	
	public TipoSituacaoComunitario[] getTiposSituacao(){
		return TipoSituacaoComunitario.values();
	}
	
	public TipoEstadoConservacaoComunitario[] getTiposEstadoConservacao(){
		return TipoEstadoConservacaoComunitario.values();
	}

	public TipoPotenciaMotor[] getTiposPotenciaMotor(){
		return TipoPotenciaMotor.values();
	}
	
	public TipoFinanciadorComunitario[] getTiposFinanciador(){
		return TipoFinanciadorComunitario.values();
	}
	
	//Getter and Setter ----------------------------------------------------------------------------------------
	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public Comunitario getComunitario() {
		return comunitario;
	}

	public void setComunitario(Comunitario comunitario) {
		this.comunitario = comunitario;
	}

	public InfraestruturaComunitariaComunitario getInfraEstrutura() {
		return infraEstrutura;
	}

	public void setInfraEstrutura(InfraestruturaComunitariaComunitario infraEstrutura) {
		this.infraEstrutura = infraEstrutura;
	}

	public SintomasComunsFamiliar getSintoma() {
		return sintoma;
	}

	public void setSintoma(SintomasComunsFamiliar sintoma) {
		this.sintoma = sintoma;
	}

	public FonteAguaConsumoComunitario getFonteAguaConsumo() {
		return fonteAguaConsumo;
	}

	public void setFonteAguaConsumo(FonteAguaConsumoComunitario fonteAguaConsumo) {
		this.fonteAguaConsumo = fonteAguaConsumo;
	}

	public MeioComunicacaoComunitario getMeioComunicacao() {
		return meioComunicacao;
	}

	public void setMeioComunicacao(MeioComunicacaoComunitario meioComunicacao) {
		this.meioComunicacao = meioComunicacao;
	}

	public MaterialInformativoComunitario getMaterialInformativo() {
		return materialInformativo;
	}

	public void setMaterialInformativo(MaterialInformativoComunitario materialInformativo) {
		this.materialInformativo = materialInformativo;
	}

	public AtividadeRendaComunitario getAtividadeRenda() {
		return atividadeRenda;
	}

	public void setAtividadeRenda(AtividadeRendaComunitario atividadeRenda) {
		this.atividadeRenda = atividadeRenda;
	}

	public FonteEnergiaComunitario getFonteEnergia() {
		return fonteEnergia;
	}

	public void setFonteEnergia(FonteEnergiaComunitario fonteEnergia) {
		this.fonteEnergia = fonteEnergia;
	}
	
	
	
}
