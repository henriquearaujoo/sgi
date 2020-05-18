package managedbean;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.PrimeFaces;

import model.Estado;
import model.ItemPedido;
import model.Localidade;
import model.MenuLateral;
import model.Pedido;
import model.TermoExpedicao;
import model.TipoAprovador;
import model.TipoLocalidade;
import model.TipoTermoExpedicao;
import model.UnidadeConservacao;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import repositorio.LocalRepositorio;
import service.TermoExpedicaoPedidoService;
import util.CDILocator;
import util.Filtro;
import util.MakeMenu;

@ViewScoped
@Named(value = "termo_controller")
public class TermoController implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private @Inject TermoExpedicao termo;
	private List<TermoExpedicao> termos = new ArrayList<>();
	private Filtro filtro = new Filtro();
	private @Inject TermoExpedicaoPedidoService service;
	private MenuLateral menu = new MenuLateral();
	private List<Estado> estados;
	private List<Localidade> localidades = new ArrayList<Localidade>();
	private Long idEstado;
	private Long idPedido;
	

	private String local = "";

	public TermoController() {

	}
	
	public void addMessage(String summary, String detail, Severity severity) {
		FacesMessage message = new FacesMessage(severity, summary, detail);
		FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	@PostConstruct
	public void init() {
		// filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		// filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		carregarTermos();
	}

	public TipoTermoExpedicao[] getTipos() {
		return TipoTermoExpedicao.values();

	}

	private void carregarTermos() {
		termos = service.getTermos(filtro);
	}

	public void salvarTermo() {
		termo.setDataEmissao(new Date());
		service.salvarTermo(termo);
		termo = new TermoExpedicao();
		addMessage("", "Termo salvo com sucesso!", FacesMessage.SEVERITY_INFO);
		init();
	}

	public void editarTermo() {
		termo = service.getTermoById(termo.getId());
		termo.setPedidos(new ArrayList<>());
		termo.setPedidos(service.getPedidosByTermo(termo.getId()));

		local = termo.getTipoLocalidade().getNome();

		if (termo.getLocalidade() instanceof UnidadeConservacao) {
			LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
			localidades = termo.getTipoLocalidade().getLocalidade(repo);
			
			openDialog("PF('dlg_novo_termo').show();");
			
			
		} else {
			
			addMessage("", "Esse Termo não foi alocado em uma unidade de conservação!", FacesMessage.SEVERITY_WARN);
		}

	}
	
	public void openDialog(String nomeDialog) {
		PrimeFaces current = PrimeFaces.current();
		current.executeScript(nomeDialog);
	}
	
	public void iniciarNovoTermo() {
		termo = new TermoExpedicao();
		termo.setPedidos(new ArrayList<>());
	}

	public String redirecionar() {
		return menu.getEndereco() + "?faces-redirect=true";
	}

	public void onLocalChangeForProject() {
		local = "";
		if (termo.getTipoLocalidade() != null) {
			local = termo.getTipoLocalidade().getNome();
		
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = termo.getTipoLocalidade().getLocalidade(repo);
			
		}
	}

	public void removerPedidoDoTermo(int index) {
		Pedido pedido = termo.getPedidos().get(index);
		termo.getPedidos().remove(index);
		if (pedido.getId() != null) {
			service.ajustaDependendiasDoPedido(pedido);
		}

	}

	public void adicionarNovoPedido() {

		// Busca pedido pelo id
		Pedido pedido = service.getPedido(idPedido);
		// Se não existe exibe mensagem
		if (pedido == null) {
			addMessage("", "Pedido não encontrado, por favor verifique se o código está correto.",
					FacesMessage.SEVERITY_ERROR);
			return;
		}

		// Verifica se ja tem o pedido na lista
		// Se ja houver exibe mensagem
		if (termo.getPedidos().stream().filter(ped -> ped.getId().longValue() == pedido.getId().longValue()).findAny()
				.isPresent()) {
			addMessage("", "O Pedido já se encontra na lista.", FacesMessage.SEVERITY_WARN);
			return;
		}

		if (pedido.getTermo() != null) {
			addMessage("", "O Pedido já se encontra em outro termo com o número: "
					+ pedido.getTermo().getId().toString() + ".", FacesMessage.SEVERITY_ERROR);
			return;
		}

		// Adicionar o pedido
		pedido.setTermo(termo);
		termo.getPedidos().add(pedido);

		addMessage("", "Pedido salvo na lista.", FacesMessage.SEVERITY_INFO);

	}

	public Long getIdPedido() {
		return idPedido;
	}

	public TipoLocalidade[] getTipoLocais() {
		return TipoLocalidade.values();
	}

	public void onEstadoChange() {
		localidades = service.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}

	public TipoAprovador[] getAprovadores() {
		return TipoAprovador.values();
	}

	public TipoTermoExpedicao[] getTiposTermos() {
		return TipoTermoExpedicao.values();
	}

	public List<MenuLateral> getMenus() {
		return MakeMenu.getMenuLogistica();
	}

	public TermoExpedicao getTermo() {
		return termo;
	}

	public void setTermo(TermoExpedicao termo) {
		this.termo = termo;
	}

	public List<TermoExpedicao> getTermos() {
		return termos;
	}

	public void setTermos(List<TermoExpedicao> termos) {
		this.termos = termos;
	}

	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public MenuLateral getMenu() {
		return menu;
	}

	public void setMenu(MenuLateral menu) {
		this.menu = menu;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public void setIdPedido(Long idPedido) {
		this.idPedido = idPedido;
	}

	public void geraRelatorio(TermoExpedicao expedicao) {
		HashMap<String, Object> params = new HashMap<String, Object>();

		UnidadeConservacao uc = (UnidadeConservacao) expedicao.getLocalidade();

		String aprovador = "";
		String aprovadorAss = "";
		String sup = "";
		if(uc.getNomeAssociacao()== null && uc.getCnpjassociacao() == null && uc.getPresidenteAssociacao() == null && uc.getRgPresidente()== null && uc.getCpfPresidente() == null) {
			
		}
		String associacao = uc.getNomeAssociacao();
		String cnpjAssociacao = uc.getCnpjassociacao();
		String presidente = uc.getPresidenteAssociacao();
		String rgPresidente = uc.getRgPresidente();
		String cpfPresidente = uc.getCpfPresidente();
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

		String caminho = context.getRealPath("resources");
		
		params.put("ano", expedicao.getAnoReferencia());
		params.put("associacao", associacao);
		params.put("cnpj_associacao", cnpjAssociacao);
		params.put("presidente", presidente);
		params.put("cpf_presidente", cpfPresidente);
		params.put("rg_presidente", rgPresidente);
		if (expedicao.getAprovador().getNome().equals("Luiz Villarez")) {
			aprovador = "Superintendente Administrativo-Financeiro, Sr. LUIZ CRUZ VILLARES, brasileiro, casado, administrador de empresas, portador da cédula de identidade nº 8.882.839-SSP/SP, inscrito no CPF/MF sob nº 066.535.688-90, residente e domiciliado em São Paulo-SP, na rua Almeida Garret";
			aprovadorAss = "LUIZ CRUZ VILLARES";
			sup = "Superintendente Administrativo-Financeiro";
		} else if (expedicao.getAprovador().getNome().equals("Eduardo Taveira")) {
			aprovador = "Superintendente Técnico-Científico, EDUARDO COSTA TAVEIRA , brasileiro, casado, mestre em ciências do ambiente, portador da cédula de identidade nº 1299947-4-SSP/RJ CPF 601.314.622-53, residente e domiciliado em Manaus, na Rua 15, QD12, Nº 08, condominio Villa Verde I, Santo Agostinho";
			aprovadorAss = "EDUARDO COSTA TAVEIRA";
			sup = "Superintendente Técnico-Científico";
		} else if (expedicao.getAprovador().getNome().equals("Valcléia Solidade")) {
			aprovador = "Superintendente de Desenvolvimento Sustentável e Amazônia, Sra. Valcléia dos Santos Lima solidade, brasileira, casada, Gestora de Politicas Públicas, portadora da cédula de identidade nº  2135718-SEGUP/PA, inscrito no CPF/MF sob nº 395.540.902-34, residente e domiciliada em Manaus/Am, na Rua 27, quadra 170, Número 186, Novo Aleixo";
			aprovadorAss = "VALCLEIA DOS SANTOS LIMA SOLIDADE";
			sup = "Superintendente de Desenvolvimento Sustentável e Amazônia";

		}
		params.put("aprovador", aprovador);
		params.put("aprovador_ass", aprovadorAss);
		params.put("sup", sup);

		params.put("logo", caminho + File.separatorChar + "image" + File.separatorChar + "logofas.png");
		String relatorio = "";
		if (expedicao.getTipo().getNome().equals("Entrega")) {
			relatorio = "relatorio" + File.separatorChar + "TermoEntregaLivre.jrxml";

			try {

				List<ItemPedido> listaExpedicao = service.itensPedido(expedicao);
				JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaExpedicao);

				JasperReport jasperReport = JasperCompileManager
						.compileReport(caminho + File.separatorChar + relatorio);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

				byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);

				FacesContext fc = FacesContext.getCurrentInstance();
				HttpServletResponse res = (HttpServletResponse) fc.getExternalContext().getResponse();
				res.setContentType("application/pdf");
				res.setHeader("Content-disposition", "inline;filename=TermodeEntrega.pdf");
				res.getOutputStream().write(b);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				fc.responseComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (expedicao.getTipo().getNome().equals("Doação")) {
			relatorio = "relatorio" + File.separatorChar + "TermoDoacao.jrxml";

			try {

				List<ItemPedido> listaExpedicao = service.itensPedido(expedicao);
				JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaExpedicao);

				JasperReport jasperReport = JasperCompileManager
						.compileReport(caminho + File.separatorChar + relatorio);
				JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

				byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);

				FacesContext fc = FacesContext.getCurrentInstance();
				HttpServletResponse res = (HttpServletResponse) fc.getExternalContext().getResponse();
				res.setContentType("application/pdf");
				res.setHeader("Content-disposition", "inline;filename=TermodeDoacao.pdf");
				res.getOutputStream().write(b);
				res.getOutputStream().flush();
				res.getOutputStream().close();
				fc.responseComplete();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void geraRelatorioCautela(TermoExpedicao expedicao) {
		HashMap<String, Object> params = new HashMap<String, Object>();

		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		String caminho = context.getRealPath("resources");
		params.put("ano", expedicao.getAnoReferencia());
		params.put("logo", caminho + File.separatorChar + "image" + File.separatorChar + "logofas.png");
		String relatorio = "relatorio" + File.separatorChar + "TermoCautela.jrxml";

		try {

			List<Pedido> listaExpedicao = service.getPedidosByTermo(expedicao.getId());
			JRBeanCollectionDataSource ds = new JRBeanCollectionDataSource(listaExpedicao);

			JasperReport jasperReport = JasperCompileManager.compileReport(caminho + File.separatorChar + relatorio);
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, ds);

			byte[] b = JasperExportManager.exportReportToPdf(jasperPrint);

			FacesContext fc = FacesContext.getCurrentInstance();
			HttpServletResponse res = (HttpServletResponse) fc.getExternalContext().getResponse();
			res.setContentType("application/pdf");
			res.setHeader("Content-disposition", "inline;filename=TermodeCautela.pdf");
			res.getOutputStream().write(b);
			res.getOutputStream().flush();
			res.getOutputStream().close();
			fc.responseComplete();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	

}
