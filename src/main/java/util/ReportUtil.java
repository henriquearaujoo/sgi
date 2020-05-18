package util;

import java.awt.BorderLayout;
import java.io.IOException;
import java.sql.Connection;
import java.util.Map;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.swing.JFrame;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.swing.JRViewer;

public class ReportUtil {
	
	/**
     * Abre um relat�rio usando um datasource gen�rico.
     *
     * @param titulo T�tulo usado na janela do relat�rio.
     * @param inputStream InputStream que cont�m o relat�rio.
     * @param parametros Par�metros utilizados pelo relat�rio.
     * @param dataSource Datasource a ser utilizado pelo relat�rio.
     * 
     * @throws JRException Caso ocorra algum problema na execu��o do relat�rio
	 * @throws IOException 
     */
    public static void openReport(
            String titulo,
            String nome,
            JasperReport report,
            Map parametros,
            JRDataSource dataSource ) throws JRException, IOException {
 
    	FacesContext fctx = FacesContext.getCurrentInstance();

		HttpServletResponse resp = (HttpServletResponse) fctx.getExternalContext().getResponse();

		resp.setContentType( "application/pdf" );
		resp.setHeader( "Content-Disposition", "inline;filename=" + nome + ".pdf" );

		ServletOutputStream out = resp.getOutputStream();
		
        /*
         * Cria um JasperPrint, que � a vers�o preenchida do relat�rio,
         * usando um datasource gen�rico.
         */
        JasperPrint print = JasperFillManager.fillReport(report, parametros, dataSource);
        //JasperPrint print2 = JasperFillManager.fillReport(report, parametros, dataSource);
 
        // abre o JasperPrint em um JFrame
        //viewReportFrame( titulo, print );
        
        //JasperViewer.viewReport(print, false);
        
        //JasperExportManager.exportReportToPdfStream(print, resp.getOutputStream());
        
        byte[] pdfByteArray = JasperExportManager.exportReportToPdf(print);
        resp.setContentLength( pdfByteArray.length );
        
        out.write(pdfByteArray, 0, pdfByteArray.length);
        out.flush();
        out.close();
        
        fctx.responseComplete();
        
    }

    public static void openReportXls(
            String titulo,
            String nome,
            JasperReport report,
            Map parametros,
            JRDataSource dataSource ) throws JRException, IOException {

        FacesContext fctx = FacesContext.getCurrentInstance();

        HttpServletResponse resp = (HttpServletResponse) fctx.getExternalContext().getResponse();

        resp.setContentType( "application/vnd.ms-excel" );
        resp.setHeader( "Content-Disposition", "atachment;filename=" + nome + ".xls" );

        ServletOutputStream out = resp.getOutputStream();

        /*
         * Cria um JasperPrint, que � a vers�o preenchida do relat�rio,
         * usando um datasource gen�rico.
         */
        JasperPrint print = JasperFillManager.fillReport(report, parametros, dataSource);

        JRXlsExporter xlsExporter = new JRXlsExporter();
        xlsExporter.setExporterInput(new SimpleExporterInput(print));
        xlsExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(out));

        SimpleXlsReportConfiguration xlsReportConfiguration = new SimpleXlsReportConfiguration();
        xlsReportConfiguration.setOnePagePerSheet(false);
        xlsReportConfiguration.setRemoveEmptySpaceBetweenRows(true);
        xlsReportConfiguration.setDetectCellType(true);
        xlsReportConfiguration.setWhitePageBackground(false);
        xlsExporter.setConfiguration(xlsReportConfiguration);

        xlsExporter.exportReport();

        fctx.responseComplete();

    }
    
    
    public static byte[] openReportParaAnexo(
            String titulo,
            String nome,
            JasperReport report,
            Map parametros,
            JRDataSource dataSource ) throws JRException, IOException {

        JasperPrint print = JasperFillManager.fillReport(report, parametros, dataSource);
        byte[] pdfByteArray = JasperExportManager.exportReportToPdf(print);
        return pdfByteArray;
        
    }

    
    
    public static void openReportCon(
            String titulo,
            String nome,
            JasperReport report,
            Map parametros,
            Connection conn) throws JRException, IOException {
 
    	FacesContext fctx = FacesContext.getCurrentInstance();

		HttpServletResponse resp = (HttpServletResponse) fctx.getExternalContext().getResponse();

		resp.setContentType( "application/pdf" );
		resp.setHeader( "Content-Disposition", "atachment;filename=" + nome + ".pdf" );

		ServletOutputStream out = resp.getOutputStream();
		
        /*
         * Cria um JasperPrint, que � a vers�o preenchida do relat�rio,
         * usando um datasource gen�rico.
         */
        JasperPrint print = JasperFillManager.fillReport(report, parametros, conn );
        
        JasperPrint print2 = JasperFillManager.fillReport(report, parametros, conn );
 
        // abre o JasperPrint em um JFrame
        //viewReportFrame( titulo, print );
        
        //JasperViewer.viewReport(print, false);
        
        //JasperExportManager.exportReportToPdfStream(print, resp.getOutputStream());
        
        byte[] pdfByteArray = JasperExportManager.exportReportToPdf(print);
        resp.setContentLength( pdfByteArray.length );
        
        out.write(pdfByteArray, 0, pdfByteArray.length);
        out.flush();
        out.close();
        
        fctx.responseComplete();
        
    }
    
	 /**
     * Cria um JFrame para exibir o relat�rio representado pelo JasperPrint.
     *
     * @param titulo T�tulo do JFrame.
     * @param print JasperPrint do relat�rio.
     */
    private static void viewReportFrame( String titulo, JasperPrint print ) {
 
        /*
         * Cria um JRViewer para exibir o relat�rio.
         * Um JRViewer � uma JPanel.
         */
        JRViewer viewer = new JRViewer( print );
 
        // cria o JFrame
        JFrame frameRelatorio = new JFrame( titulo );
 
        // adiciona o JRViewer no JFrame
        frameRelatorio.add( viewer, BorderLayout.CENTER );
 
        // configura o tamanho padr�o do JFrame
        frameRelatorio.setSize( 500, 500 );
 
        // maximiza o JFrame para ocupar a tela toda.
        frameRelatorio.setExtendedState( JFrame.MAXIMIZED_BOTH );
 
        // configura a opera��o padr�o quando o JFrame for fechado.
        frameRelatorio.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );
 
        // exibe o JFrame
        frameRelatorio.setVisible( true );
 
    }
}
