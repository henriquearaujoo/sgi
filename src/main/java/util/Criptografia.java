package util;

import java.util.ArrayList;
import java.util.List;

import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.BasicTextEncryptor;

import anotacoes.Transactional;
import model.Colaborador;
import service.ColaboradorService;

public class Criptografia {
	
	ColaboradorService colaboradorService;
	
	private static final String SECRET = "@!#SADAS@!#FWG213daSDSA@!#"; // <- PRECISA SER UMA VARIÁVEL DE AMBIENTE
	private BasicTextEncryptor textEncryptor = new BasicTextEncryptor();

	public Criptografia() {
		textEncryptor.setPassword(SECRET);
		
	}
	
	public Colaborador encrypt(Colaborador colaborador) {
		if(colaborador.getCpf() != null || colaborador.getCpf() != "") {
			colaborador.setCpf(textEncryptor.encrypt(colaborador.getCpf()));			
		}
		if(colaborador.getCtpsSerieUf() != null || colaborador.getCtpsSerieUf() != "") {
			colaborador.setCtpsSerieUf(textEncryptor.encrypt(colaborador.getCtpsSerieUf()));			
		}
		if(colaborador.getRg() != null || colaborador.getRg() != "") {
			colaborador.setRg(textEncryptor.encrypt(colaborador.getRg()));			
		}
		if(colaborador.getTitulo() != null || colaborador.getTitulo() != "") {
			colaborador.setTitulo(textEncryptor.encrypt(colaborador.getTitulo()));
		}
		if(colaborador.getPis() != null || colaborador.getPis() != "") {
			colaborador.setPis(textEncryptor.encrypt(colaborador.getPis()));			
		}
		if(colaborador.getQuitacaoMilitar() != null || colaborador.getQuitacaoMilitar() != "") {
			colaborador.setQuitacaoMilitar(textEncryptor.encrypt(colaborador.getQuitacaoMilitar()));			
		}
		if(colaborador.getOrgaoExpedidor() != null || colaborador.getOrgaoExpedidor() != "") {
			colaborador.setOrgaoExpedidor(textEncryptor.encrypt(colaborador.getOrgaoExpedidor()));			
		}
		return colaborador;
	}
	
	public String decrypt(String s) {
		return textEncryptor.decrypt(s);
	}
	
	public String encrypt(String s) {
		return textEncryptor.encrypt(s);
	}
	
	public Colaborador decrypt(Colaborador colaborador) {
		if(colaborador.getCpf() != null || colaborador.getCpf() != "") {
			colaborador.setCpf(textEncryptor.decrypt(colaborador.getCpf()));			
		}
		if(colaborador.getCtpsSerieUf() != null || colaborador.getCtpsSerieUf() != "") {
			colaborador.setCtpsSerieUf(textEncryptor.decrypt(colaborador.getCtpsSerieUf()));			
		}
		if(colaborador.getRg() != null || colaborador.getRg() != "") {
			colaborador.setRg(textEncryptor.decrypt(colaborador.getRg()));			
		}
		if(colaborador.getTitulo() != null || colaborador.getTitulo() != "") {
			colaborador.setTitulo(textEncryptor.decrypt(colaborador.getTitulo()));
		}
		if(colaborador.getPis() != null || colaborador.getPis() != "") {
			colaborador.setPis(textEncryptor.decrypt(colaborador.getPis()));			
		}
		if(colaborador.getQuitacaoMilitar() != null || colaborador.getQuitacaoMilitar() != "") {
			colaborador.setQuitacaoMilitar(textEncryptor.decrypt(colaborador.getQuitacaoMilitar()));			
		}
		if(colaborador.getOrgaoExpedidor() != null || colaborador.getOrgaoExpedidor() != "") {
			colaborador.setOrgaoExpedidor(textEncryptor.decrypt(colaborador.getOrgaoExpedidor()));			
		}
		return colaborador;
	}
	
	public List<Colaborador> colaboradorEncrypt(List<Colaborador> colaboradores) {
		List<Colaborador> newColaborador = new ArrayList<>();
		for(Colaborador colaborador: colaboradores) {
			try {
				Colaborador tmp = decrypt(colaborador);
				newColaborador.add(encrypt(tmp));				
			} catch(EncryptionOperationNotPossibleException e) {
				newColaborador.add(encrypt(colaborador));
			}
		}
		return newColaborador;
	}
	
	public List<Colaborador> colaboradorDecrypt(List<Colaborador> colaboradores) {
		List<Colaborador> newColaborador = new ArrayList<>();
		for(Colaborador colaborador: colaboradores) {
			try {
				newColaborador.add(decrypt(colaborador));
			} catch (EncryptionOperationNotPossibleException e) {
				Colaborador tmp = encrypt(colaborador);
				newColaborador.add(decrypt(tmp));
			}
		}
		return newColaborador;
	}
}
