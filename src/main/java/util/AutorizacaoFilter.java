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

public class AutorizacaoFilter implements Filter {

	@Inject
	private UsuarioSessao sessao;

	@Inject
	private AutorizacaoRepositorio autorizacaoRepo;

	public void destroy() {
		// TODO Auto-generated method stub

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletResponse resp = (HttpServletResponse) response;
		HttpServletRequest req = (HttpServletRequest) request;

		// if (sessao.getUsuario() != null)
		// System.out.println(sessao.getUsuario().getSenhaAuto());

		if (!sessao.verificaLogado() && !req.getRequestURI().endsWith("/login.xhtml")
				&& !req.getRequestURI().contains("/javax.faces.resource/")) {

			resp.sendRedirect(req.getContextPath() + "/main/login.xhtml");
			// } else if (req.getRequestURI().endsWith("/lancamentosV2.xhtml")
			// || req.getRequestURI().endsWith("/liberacao_lancamento.xhtml")
			// || req.getRequestURI().endsWith("/lancamentos_diversos.xhtml")
			// || req.getRequestURI().endsWith("/custo_pessoal.xhtml")) {
			//
			// if (sessao.getUsuario().getPerfil().getDescricao().equals("administracao")
			// || sessao.getUsuario().getPerfil().getDescricao().equals("admin")
			// || sessao.getUsuario().getPerfil().getDescricao().equals("financeiro")
			// || sessao.getUsuario().getPerfil().getDescricao().equals("contabil")
			// || sessao.getUsuario().getPerfil().getDescricao().equals("RH"))
			// chain.doFilter(request, response);
			//
			// else
			// resp.sendRedirect(req.getContextPath() + "/main/acesso_negado.xhtml");
//		} else if (sessao.getUsuario() != null && autorizacaoRepo.verificaSolicitacoes(sessao.getIdColadorador())) {
//			resp.sendRedirect(req.getContextPath() + "/main/autorizacoes.xhtml");
		} else {
			chain.doFilter(request, response);
		}
	}

	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub

	}

}
