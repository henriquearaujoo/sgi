package util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletResponse;

public class DownloadUtil {
	
	
	
	
	public static synchronized void downloadFile(String filename, String fileLocation, String mimeType,
			FacesContext facesContext) {
		ExternalContext context = facesContext.getExternalContext(); // Context
		String path = fileLocation; // Localizacao do arquivo
		String fullFileName = path + filename;
		File file = new File(path); // Objeto arquivo mesmo :)
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.setHeader("Content-Disposition", "inline;filename=\"" + filename + ".pdf" + "\""); // aki
																									// eu																					// donwload
		response.setContentLength((int) file.length()); 
		response.setContentType(mimeType); 
		try {
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			byte[] buf = new byte[(int) file.length()];
			int count;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.flush();
			out.close();
			facesContext.responseComplete();
		} catch (IOException ex) {
			System.out.println("Error in downloadFile: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public static synchronized void baixarArquivo(String filename, String fileLocation, String mimeType,
			FacesContext facesContext) {
		ExternalContext context = facesContext.getExternalContext(); // Context
		String path = fileLocation; // Localizacao do arquivo
		String fullFileName = path + filename;
		File file = new File(path); // Objeto arquivo mesmo :)
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + ".pdf" + "\""); // aki
																									// eu																					// donwload
		response.setContentLength((int) file.length()); 
		response.setContentType(mimeType); 
		try {
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			byte[] buf = new byte[(int) file.length()];
			int count;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.flush();
			out.close();
			facesContext.responseComplete();
		} catch (IOException ex) {
			System.out.println("Error in downloadFile: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	
	public static synchronized void visualizarArquivo(String filename, String fileLocation, String mimeType,
			FacesContext facesContext) {
		ExternalContext context = facesContext.getExternalContext(); // Context
		String path = fileLocation; // Localizacao do arquivo
		String fullFileName = path + filename;
		File file = new File(path); // Objeto arquivo mesmo :)
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.setHeader("Content-Disposition", "inline;filename=\"" + filename + ".pdf" + "\""); // aki
																									// eu																					// donwload
		response.setContentLength((int) file.length()); 
		response.setContentType(mimeType); 
		try {
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			byte[] buf = new byte[(int) file.length()];
			int count;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.flush();
			out.close();
			facesContext.responseComplete();
		} catch (IOException ex) {
			System.out.println("Error in downloadFile: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
	

	public static synchronized void downloadFileExcel(String filename, String fileLocation, String mimeType,
			FacesContext facesContext) {
		ExternalContext context = facesContext.getExternalContext(); // Context
		String path = fileLocation; // Localizacao do arquivo
		String fullFileName = path + filename;
		File file = new File(path); // Objeto arquivo mesmo :)
		HttpServletResponse response = (HttpServletResponse) context.getResponse();
		response.setHeader("Content-Disposition", "attachment;filename=\"" + filename + ".xlsx" + "\""); 
		response.setContentLength((int) file.length()); // O tamanho do arquivo
		response.setContentType(mimeType); // e obviamente o tipo
		try {
			FileInputStream in = new FileInputStream(file);
			OutputStream out = response.getOutputStream();
			byte[] buf = new byte[(int) file.length()];
			int count;
			while ((count = in.read(buf)) >= 0) {
				out.write(buf, 0, count);
			}
			in.close();
			out.flush();
			out.close();
			facesContext.responseComplete();
		} catch (IOException ex) {
			System.out.println("Error in downloadFile: " + ex.getMessage());
			ex.printStackTrace();
		}
	}
}
