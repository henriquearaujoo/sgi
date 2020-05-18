package service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import exception.NegocioException;
import model.Colaborador;
import repositorio.FuncionarioRepository;
import util.Filtro;

public class FuncionarioService {
	
	
	@Inject
	private FuncionarioRepository func; 
	
	public FuncionarioService() {
	}
	
	public FuncionarioService(FuncionarioRepository func){
		this.func = func;
	}
	
	public List<Colaborador> filtrados(Filtro filtro) throws NegocioException{
		List<Colaborador> list  = new ArrayList<Colaborador>();
		list = this.func.filtrados(filtro);
		if (list.size() > 0) {
			return list;
		}else{
			throw new NegocioException("Erro ao consultar dados");
		}
		
	}
	
	public int quantidadeFiltrados(Filtro filtro) {
		 return func.quantidadeFiltrados(filtro);
	}
	
	public void salvar(Colaborador funcionario) throws NegocioException{
		if (funcionario != null) {
			this.func.adicionar(funcionario);
		}else{
			throw new NegocioException("Erro ao cadastrar dados");
		}
	}

}
