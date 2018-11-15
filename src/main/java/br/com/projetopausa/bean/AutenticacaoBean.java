package br.com.projetopausa.bean;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import br.com.projetopausa.dao.UsuarioDAO;
import br.com.projetopausa.domain.Metodos;
import br.com.projetopausa.domain.Usuario;

@ManagedBean
@SessionScoped
public class AutenticacaoBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<Metodos> todosOsMetodos = new ArrayList<>();
	private Metodos metodoSelecionado = new Metodos();

	private String hms;

	private Usuario usuarioLogado;
	private Usuario usuario;

	private static int cont = 0;
	private int segundos = 0;

	public void chamarNotificacao() { // notificacao de hora de ir para a pausa
		Calendar cal = Calendar.getInstance();

		if (usuarioLogado.getHoraDaPausa().get(Calendar.HOUR_OF_DAY) == cal.get(Calendar.HOUR_OF_DAY)
				&& usuarioLogado.getHoraDaPausa().get(Calendar.MINUTE) == cal.get(Calendar.MINUTE)
				|| usuarioLogado.getHoraDoIntervalo().get(Calendar.HOUR_OF_DAY) == cal.get(Calendar.HOUR_OF_DAY)
						&& usuarioLogado.getHoraDoIntervalo().get(Calendar.MINUTE) == cal.get(Calendar.MINUTE)
				|| usuarioLogado.getHoraDaUPausa().get(Calendar.HOUR_OF_DAY) == cal.get(Calendar.HOUR_OF_DAY)
						&& usuarioLogado.getHoraDaUPausa().get(Calendar.MINUTE) == cal.get(Calendar.MINUTE)) {
			System.out.println("ESTA NA HORA DE SUA PAUSA");
			PrimeFaces.current().executeScript("mostrarMensagem()");
		} else {
			System.out.println("sua pausa ainda não chegou!");
		}
	}

	@PostConstruct
	public void novo() {
		usuario = new Usuario();
		System.out.println("metodo NOVO CHAMADO");
		preencherLista();
		System.out.println("metodo PREENCHER LISTA CHAMADO");
	}

	public void preencherLista() {
		todosOsMetodos.add(new Metodos(0L, "Pirmeira pausa"));
		todosOsMetodos.add(new Metodos(1L, "Intervalo"));
		todosOsMetodos.add(new Metodos(2L, "Ultima pausa"));
	}

	public void pausa() {  
		if (cont >= 3) {
			Messages.addGlobalWarn("Numero de colaboradores em pausa excedido");
			return;
		} else {
			PrimeFaces.current().executeScript("PF('dlgEmPausa').show();");
			PrimeFaces.current().executeScript("PF('poll').start();");
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			System.out.println("metodo pausa foi chamado!");
			setCont(cont + 1);
			usuarioLogado.setStatus(true);
			usuarioDAO.editar(usuarioLogado);
		}
	}

	public String convertHms(int pSegundos) {

		int vHoras = (pSegundos / 3600);
		int vMinutos = ((pSegundos - (vHoras * 3600)) / 60);
		int vSegundos = ((pSegundos - (vHoras * 3600)) - (vMinutos * 60));
		String vReturn = String.format("%02d:%02d:%02d", vHoras, vMinutos, vSegundos);

		return vReturn;

	}

	public void timerHms() { 

		if (metodoSelecionado.getId() == 0) {
			this.segundos++;
			hms = this.convertHms(this.getSegundos());
			usuarioLogado.setHms(hms);
			UsuarioDAO ud = new UsuarioDAO();
			ud.editar(usuarioLogado);
		} else if (metodoSelecionado.getId() == 1) {

			this.segundos++;
			hms = this.convertHms(this.getSegundos());
			usuarioLogado.setIntervalo(hms);
			UsuarioDAO ud = new UsuarioDAO();
			ud.editar(usuarioLogado);
		} else if (metodoSelecionado.getId() == 2) {

			this.segundos++;
			hms = this.convertHms(this.getSegundos());
			usuarioLogado.setTerceiraPausa(hms);
			UsuarioDAO ud = new UsuarioDAO();
			ud.editar(usuarioLogado);
		}

	}

	public void diminuirPausas() { // diminuir pessoas para ir para pausa.
		if (cont == 0) {
			this.setCont(cont + 1);
			System.out.println("contador agora é: " + cont);
			Messages.addGlobalInfo("Apenas duas pessoas podem tirar pausa");
		} else if (cont == 1) {
			this.setCont(cont + 1);
			System.out.println("contador agora é: " + cont);
			Messages.addGlobalInfo("Apenas uma pessoa pode tirar pausa");
		} else if (cont == 2) {
			this.setCont(cont + 1);
			System.out.println("contador agora é: " + cont);
			Messages.addGlobalInfo("Não é permitido tirar pausas.");

		}
	}

	public void padraoPausas() { // 3 pessoas por vez. 
		cont = 0;
		System.out.println("contador agora é: " + cont);
		Messages.addGlobalInfo("Pausas livres");
	}

	public void aumentarPausas() {
		if (cont == 3) {
			this.setCont(cont - 1);
			System.out.println("contador agora é: " + cont);
			Messages.addGlobalInfo("Apenas uma pessoa pode tirar pausa");
		} else if (cont == 2) {
			this.setCont(cont - 1);
			System.out.println("contador agora é: " + cont);
			Messages.addGlobalInfo("Apenas duas pessoas podem tirar pausa");
		} else if (cont == 1) {
			this.setCont(cont - 1);
			System.out.println("contador agora é: " + cont);
			Messages.addGlobalInfo("Apenas três pessoas podem tirar pausa");
		}

	}

	public void cancelarPausa() { 
		try {
			System.out.println("variavel contadora: " + cont);
			setCont(cont - 1);
			System.out.println("variavel contadora: " + cont);
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuarioLogado.setStatus(false);
			System.out.println("status do usuario: " + usuarioLogado.isStatus());
			usuarioDAO.editar(usuarioLogado);
			this.segundos = 0;
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar cancelar a pausa");
			erro.printStackTrace();
		}

	}

	public void autenticar() {
		try {
			System.out.println("METODO AUTENTICAR CHAMADO");
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuarioLogado = usuarioDAO.autenticar(usuario.getMatricula(), usuario.getSenha());
			System.out.println(usuarioLogado);
			if (usuarioLogado == null) {
				Messages.addGlobalError("Usuário ou senha incorreto");
				return;
			}
			if (usuarioLogado.isLogado() == false) {
				if (usuarioLogado.getMatricula().equalsIgnoreCase("c010816")) {
					Faces.redirect("./pages/telaSecundariaPausa.xhtml");
					System.out.println("Administrador se logou");

				} else {
					Faces.redirect("./pages/telaSecundariaPausa.xhtml");
					usuarioLogado.setLogado(true);
					usuarioDAO.editar(usuarioLogado);
					System.out.println("status do usuario: " + usuarioLogado.isLogado());
					System.out.println("outro usuário se logou." + usuarioLogado.getNome());

				}
			} else {
				Messages.addGlobalError("Usuário ja logado");
				return;
			}
		} catch (IOException erro) {
			erro.printStackTrace();
			Messages.addGlobalError(erro.getMessage());
		}

	}

	public boolean temPermissoes(List<String> permissoes) {
		for (String permissao : permissoes) {
			if (usuarioLogado.getMatricula().equals(permissao)) {
				return true;
			}
		}
		return false;
	}

	public boolean naoTemPermissoes(List<String> permissoes) {
		for (String permissao : permissoes) {
			if (usuarioLogado.getMatricula().equals(permissao)) {
				return false;
			}
		}
		return true;
	}

	public void logout() throws IOException {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		HttpSession sessao = (HttpSession) FacesContext.getCurrentInstance().getExternalContext().getSession(true);
		sessao.invalidate();
		usuarioLogado.setLogado(false);
		usuarioDAO.editar(usuarioLogado);
		System.out.println("status do usuario: " + usuarioLogado.isLogado());
		Faces.redirect("./pages/index.xhtml");
	}

	public void editarSenha() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		SimpleHash hash = new SimpleHash("md5", usuarioLogado.getSenhaCriptografada());
		usuarioLogado.setSenha(hash.toHex());
		usuarioDAO.merge(usuarioLogado);
		Messages.addGlobalInfo("Nova senha salva com sucesso!");
	}

	// M E T O D O S G E T T E R S E S E T T E R S

	public String getHms() {
		return this.hms;
	}

	public List<Metodos> getTodosOsMetodos() {
		return todosOsMetodos;
	}

	public void setTodosOsMetodos(List<Metodos> todosOsMetodos) {
		this.todosOsMetodos = todosOsMetodos;
	}

	public Metodos getMetodoSelecionado() {
		return metodoSelecionado;
	}

	public void setMetodoSelecionado(Metodos metodoSelecionado) {
		this.metodoSelecionado = metodoSelecionado;
	}

	public void setHms(String hms) {
		this.hms = hms;

	}

	public void setSegundos(int segundos) {
		this.segundos = segundos;
	}

	public int getSegundos() {
		return this.segundos;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Usuario getUsuarioLogado() {
		return usuarioLogado;
	}

	public void setUsuarioLogado(Usuario usuarioLogado) {
		this.usuarioLogado = usuarioLogado;
	}

	public int getCont() {
		return cont;
	}

	public void setCont(int cont) {
		AutenticacaoBean.cont = cont;
	}

}