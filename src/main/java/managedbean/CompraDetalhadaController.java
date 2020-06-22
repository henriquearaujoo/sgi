package managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.FillPatternType;

import model.Estado;
import model.Gestao;
import model.ItemPedido;
import model.Localidade;
import model.StatusCompra;
import model.TipoGestao;
import model.TipoLocalidade;
import repositorio.GestaoRepositorio;
import repositorio.LocalRepositorio;
import service.CompraDetalhadaService;
import util.CDILocator;
import util.DataUtil;
import util.Filtro;

@Named(value = "compraDetalhadaController")
@ViewScoped
public class CompraDetalhadaController implements Serializable{

	

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Inject 
	private CompraDetalhadaService compraDetalhadaService;
	
	private Filtro filtro = new Filtro();
	
	private String local;
	
	private Long idEstado;
	
	private String tipoGestao = "";
	
	private List<Gestao> gestoes;
	
	private List<ItemPedido> itensPedidos = new ArrayList<ItemPedido>();
	
	private List<Localidade> localidades = new ArrayList<Localidade>();
	
	private List<Estado> estados;
	
	
	
	@PostConstruct
	public void init() {
		filtro = new Filtro();
		filtro.setDataInicio(DataUtil.getDataInicio(new Date()));
		filtro.setDataFinal(DataUtil.getDataFinal(new Date()));
		
	}
	
	public void postProcessXLS(Object document) {
	    HSSFWorkbook wb = (HSSFWorkbook) document;
	    HSSFSheet sheet = wb.getSheetAt(0);
	    HSSFRow header = sheet.getRow(0);
	    HSSFCellStyle cellStyle = wb.createCellStyle();
	    cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
	    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);;
	  
	    for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
	        header.getCell(i).setCellStyle(cellStyle);
	    }
	}
	
	public void preProcessXLS(Object document) {
	    
		HSSFWorkbook wb = (HSSFWorkbook) document;
	    HSSFSheet sheet = wb.getSheetAt(0);
	    HSSFRow header = sheet.getRow(0);
	    
	    
	    
	    
	    /*HSSFCellStyle cellStyle = wb.createCellStyle();
	    cellStyle.setFillForegroundColor(HSSFColor.GREEN.index);
	    cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);*/
	  
	    /*for(int i=0; i < header.getPhysicalNumberOfCells();i++) {
	        header.getCell(i).setCellStyle(cellStyle);
	        
	    }*/
	}

	public void carregarItensPedido() {
		itensPedidos = compraDetalhadaService.getItensPedidos(filtro);
	}
	
	public void filtrarItens() {
		carregarItensPedido();
	}
	
	public void limparFiltro() {
		filtro = new Filtro();
		local = "";
		tipoGestao = "";
	}
	
	public void onEstadoChange() {
		localidades = compraDetalhadaService.getMunicipioByEstado(idEstado);
		idEstado = new Long(0);
	}
	
	
	public Filtro getFiltro() {
		return filtro;
	}

	public void setFiltro(Filtro filtro) {
		this.filtro = filtro;
	}

	public List<ItemPedido> getItensPedidos() {
		return itensPedidos;
	}
	
	public void setItensPedidos(List<ItemPedido> itensPedidos) {
		this.itensPedidos = itensPedidos;
	}
	
	public void onLocalChangeFilter() {
		local = "";
		if (filtro.getTipoLocalidade() != null) {
			local = filtro.getTipoLocalidade().getNome();
			if (local.equals("Municipio")) {
				estados = new ArrayList<>();
				estados = compraDetalhadaService.getEstados(new Filtro());
				localidades = new ArrayList<>();
				idEstado = new Long(0);
			} else {
				LocalRepositorio repo = CDILocator.getBean(LocalRepositorio.class);
				localidades = filtro.getTipoLocalidade().getLocalidade(repo);
			}
		}
	}
	
	public void onTipoGestaoChangeFiltro() {
		tipoGestao = "";
		if (filtro.getTipoGestao() != null) {
			tipoGestao = filtro.getTipoGestao().getNome();
			GestaoRepositorio repo = CDILocator.getBean(GestaoRepositorio.class);
			gestoes = new ArrayList<>();
			gestoes = filtro.getTipoGestao().getGestao(repo);
		}
	}

	public List<Estado> getEstados() {
		return estados;
	}

	public void setEstados(List<Estado> estados) {
		this.estados = estados;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getTipoGestao() {
		return tipoGestao;
	}
	
	public TipoLocalidade[] getTipoLocais() {
		return TipoLocalidade.values();
	}
	
	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public void setTipoGestao(String tipoGestao) {
		this.tipoGestao = tipoGestao;
	}
	
	public StatusCompra[] listaStatusCompras() {
		return StatusCompra.values();
	}
	
	public TipoGestao[] getTiposGestao() {
		return TipoGestao.values();
	}

	public List<Gestao> getGestoes() {
		return gestoes;
	}

	public void setGestoes(List<Gestao> gestoes) {
		this.gestoes = gestoes;
	}
	
	public List<Localidade> getLocalidades() {
		return localidades;
	}

	public void setLocalidades(List<Localidade> localidades) {
		this.localidades = localidades;
	}
	
	
}
