package util;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import model.Aprouve;
import model.AprouveSigla;
import model.Perfil;
import model.User;
import service.PagamentoService;

public class ValidarAcesso implements Serializable {

    private static final long serialVersionUID = 1L;
    
    public static void checkEnterPage(User currentUser) throws IOException {
        ExternalContext context = FacesContext.getCurrentInstance()
        									  .getExternalContext();

        Perfil currentPerfil = currentUser.getPerfil();
        
        // verifica se o perfil do usuário é do setor financeiro ou é o admin do sistema
        if (!(currentPerfil.getId() == 5L || currentPerfil.getId() == 1L)) {
        	context.redirect(context.getApplicationContextPath() + "/main/home_new.xhtml");
        }
    }
    
    public static boolean checkIfUserCanApprove(User currentUser,
    											AprouveSigla tipoAprovacao,
    											PagamentoService service) {
    	
    	List<Aprouve> listAprouve = service.findAprouve(currentUser);
    	
    	if (listAprouve != null) {

    		switch(tipoAprovacao) {
        	case VALIDACAO:
        		for(Aprouve ap : listAprouve) {
        			if (ap.getSigla().equals(tipoAprovacao.toString())) {
        				return true;
        			}
        		}
        		break;
        	case CONCILIACAO:
    			for(Aprouve ap : listAprouve) {
    				if(ap.getSigla().equals(tipoAprovacao.toString())) {
    					return true;
    				}
    			}
        		break;
        	default:
        		break;
        	}

    	}
    	return false;
    }
    
    public static boolean checkPasswordAprouve(User currentUser,
    										   AprouveSigla tipoAprovacao,
    										   Aprouve currentAprouve,
    										   PagamentoService service) {
    	List<Aprouve> listAprouve = service.findAprouve(currentUser);
    	
    	if(listAprouve != null) {
	    	switch(tipoAprovacao) {
	    	case VALIDACAO:		
    			for(Aprouve ap : listAprouve) {
    				if (ap.getSenha().equals(currentAprouve.getSenha())) {
    					return true;
    				}
    			}
	    		break;
	    	case CONCILIACAO:
	    		for(Aprouve ap : listAprouve) {
	    			if(ap.getSenha().equals(currentAprouve.getSenha())) {
	    				return true;
	    			}
	    		}
			default:
				break;
	    	}
    	}
    	
    	return false;
    }
}
