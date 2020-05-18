package util;






import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import repositorio.LancamentoRepository;

//@Path("/arquivo")
//public class ArquivoResource {
//	@Inject
//	private ColaboradorRepositorio colaboradorBC;
//
//	@Path("/anexo/{id}")
//	@GET
//	@Produces("image/png")
//	public Response anexo(@PathParam("id") Long id) {
//		Arquivo arquivo = colaboradorBC.load(id);
//		byte[] img = arquivo.getArquivo();
//		return Response.ok(img).build();
//
//	}
//
//}
@WebServlet("/get_imagem_lancamento")
public class ArquivoLancamentoResource extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@Inject
	private LancamentoRepository repositorio;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		
		
			if(!(req.getParameter("id").equals(""))){
			Long id = new Long(req.getParameter("id"));
		
			//Arquivo arquivo = colaboradorBC.load(id);
			ArquivoLancamento arquivo = repositorio.getArquivoById(id);
			
			byte[] img = arquivo.getConteudo();
			BufferedImage imagem = ImageIO.read(new ByteArrayInputStream(img));
			resp.setContentType("image/jpg");
			OutputStream out = resp.getOutputStream();
			ImageIO.write(imagem, "JPG", out);
			
			}
		
	}

}


