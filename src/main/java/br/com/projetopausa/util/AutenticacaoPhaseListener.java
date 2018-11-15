package br.com.projetopausa.util;

import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

import org.omnifaces.util.Faces;

import br.com.projetopausa.bean.AutenticacaoBean;
import br.com.projetopausa.domain.Usuario;

@SuppressWarnings("serial")
public class AutenticacaoPhaseListener implements PhaseListener {

	@Override
	public void afterPhase(PhaseEvent event) {
		String paginaAtual = Faces.getViewId();

		boolean ehPaginaDeAutenticacao = paginaAtual.contains("index.xhtml");

		if (!ehPaginaDeAutenticacao) {
			AutenticacaoBean autenticacaoBean = Faces.getSessionAttribute("autenticacaoBean");

			if (autenticacaoBean == null) {
				Faces.navigate("/pages/index.xhtml");
				return;
			}
			
			Usuario usuario = autenticacaoBean.getUsuarioLogado();
			if(usuario == null) {
				Faces.navigate("/pages/index.xhtml");
				return;
			}
		}

	}

	@Override
	public void beforePhase(PhaseEvent event) {
	}

	@Override
	public PhaseId getPhaseId() {
		return PhaseId.ANY_PHASE;
	}

}
