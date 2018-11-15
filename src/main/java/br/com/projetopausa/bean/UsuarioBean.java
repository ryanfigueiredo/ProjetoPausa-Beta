package br.com.projetopausa.bean;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.event.ActionEvent;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.omnifaces.util.Messages;
import org.primefaces.PrimeFaces;

import br.com.projetopausa.dao.UsuarioDAO;
import br.com.projetopausa.domain.Usuario;

@SuppressWarnings("serial")
@ManagedBean
@SessionScoped
public class UsuarioBean implements Serializable {

	private Usuario usuario;
	private List<Usuario> usuarios;
	private String hora, intervalo, ultimaPausa;
	
	
	public String getIntervalo() {
		return intervalo;
	}

	public void setIntervalo(String intervalo) {
		this.intervalo = intervalo;
	}

	public String getUltimaPausa() {
		return ultimaPausa;
	}

	public void setUltimaPausa(String ultimaPausa) {
		this.ultimaPausa = ultimaPausa;
	}

	public String getHora() {
		return hora;
	}

	public void setHora(String hora) {
		this.hora = hora;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public void zerarTempoDePausas() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarios = usuarioDAO.listar();
		usuario.setHms("00:00");
	}
	
	@PostConstruct
	public void listar() {
		try {
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuarios = usuarioDAO.listar();

		} catch (RuntimeException erro) {
			Messages.addGlobalError("Ocorreu um erro ao tentar listar os usuarios");
			erro.printStackTrace();
		}

	}

	public void novo() {
		try {
			usuario = new Usuario();

			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuarios = usuarioDAO.listar();
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao gerar um novo usuário");
			erro.printStackTrace();
		}
	}

	public void salvar() throws ParseException {
		try {
			
			UsuarioDAO usuarioDAO = new UsuarioDAO();
			SimpleHash hash = new SimpleHash("md5", usuario.getSenhaCriptografada());
			usuario.setSenha(hash.toHex());
			
			//captando string para 
			
			if(this.getHora() != null && this.getIntervalo() != null && this.getUltimaPausa() != null) {
				//novo cadastro
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //criado
				usuario.getHoraDaPausa().setTime(sdf.parse(hora));
				usuario.getHoraDoIntervalo().setTime(sdf.parse(intervalo));
				usuario.getHoraDaUPausa().setTime(sdf.parse(ultimaPausa));
				usuarioDAO.merge(usuario);

				usuario = new Usuario();

				usuarios = usuarioDAO.listar();

				Messages.addGlobalInfo("Usuario salvo com sucesso");
			}
			else if(usuario.getHoraDaPausa() != null && usuario.getHoraDoIntervalo() != null && usuario.getHoraDaUPausa() != null) {
				
			SimpleDateFormat sdf = new SimpleDateFormat("HH:mm"); //criado
			String a = sdf.format(usuario.getHoraDaPausa().getTime());
			String b = sdf.format(usuario.getHoraDoIntervalo().getTime());
			String c = sdf.format(usuario.getHoraDaUPausa().getTime());
			usuario.getHoraDaPausa().setTime(sdf.parse(a));
			usuario.getHoraDoIntervalo().setTime(sdf.parse(b));
			usuario.getHoraDaUPausa().setTime(sdf.parse(c));
			
			usuarioDAO.merge(usuario);

			usuario = new Usuario();

			usuarios = usuarioDAO.listar();

			Messages.addGlobalInfo("Usuario salvo com sucesso");
			}
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar salvar um novo usuario");
			erro.printStackTrace();
		}
	}

	public void editarCadastro(ActionEvent evento) {
		usuario = (Usuario) evento.getComponent().getAttributes().get("usuarioSelecionado");
	}

	public void excluir(ActionEvent evento) {
		try {
			usuario = (Usuario) evento.getComponent().getAttributes().get("usuarioSelecionado");

			UsuarioDAO usuarioDAO = new UsuarioDAO();
			usuarioDAO.excluir(usuario);
			usuarios = usuarioDAO.listar();

			Messages.addGlobalInfo("Usuario removida com sucesso");
		} catch (RuntimeException erro) {
			Messages.addFlashGlobalError("Ocorreu um erro ao tentar remover o usuário");
			erro.printStackTrace();
		}
	}

	public void liberarSessao(ActionEvent evento) {
		usuario = (Usuario) evento.getComponent().getAttributes().get("usuarioSelecionado");
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		if (usuario.isLogado() == true) {
			usuario.setLogado(false);
			usuarioDAO.editar(usuario);
			Messages.addGlobalInfo("Sessão encerrada com sucesso");
		} else {
			Messages.addGlobalInfo("não existe sessão para este usuário");

		}
	}

}
