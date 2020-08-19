package util;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import repositorio.AutorizacaoRepositorio;
import service.HomeService;

public class AutorizacaoFilter implements Filter {

	@Inject
	private UsuarioSessao sessao;

	@Inject
	private AutorizacaoRepositorio autorizacaoRepo;
	
	
	public void destroy() {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;

		if (!sessao.verificaLogado() 
				&& !req.getRequestURI().endsWith("/login.xhtml")
				&& !req.getRequestURI().contains("/javax.faces.resource/")) {

			resp.sendRedirect(req.getContextPath() + "/main/login.xhtml");

		} else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
