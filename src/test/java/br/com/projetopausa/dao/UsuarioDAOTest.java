package br.com.projetopausa.dao;

import java.util.List;

import org.apache.shiro.crypto.hash.SimpleHash;
import org.junit.Ignore;
import org.junit.Test;

import br.com.projetopausa.dao.UsuarioDAO;
import br.com.projetopausa.domain.Usuario;

public class UsuarioDAOTest {

	@Test
	@Ignore
	public void salvar() {

		Usuario usuario = new Usuario();

		usuario.setMatricula("c014758");
		usuario.setNome("Carla");
		usuario.setSenhaCriptografada("123456");
		usuario.setHms("x");
		usuario.setIntervalo("x");
		usuario.setTerceiraPausa("x");
		usuario.setLogado(false);
		usuario.setStatus(false);

		SimpleHash hash = new SimpleHash("md5", usuario.getSenhaCriptografada());
		usuario.setSenha(hash.toHex());

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarioDAO.salvar(usuario);

	}

	@Ignore
	@Test
	public void listar() {
		UsuarioDAO usuarioDAO = new UsuarioDAO();
		List<Usuario> usuarios = usuarioDAO.listar();

		System.out.println(usuarios.size());
	}

	@Test
	@Ignore
	public void buscar() {
		String matricula = "c014587";

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.buscar(matricula);

		System.out.println("Nome do colaborador : " + usuario.getNome());
		System.out.println();

	}

	@Test
	@Ignore
	public void excluir() {
		String matricula = "Admin";

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.buscar(matricula);

		usuarioDAO.excluir(usuario);
		System.out.println("Usuario removido!");
		System.out.println("Nome do colaborador excluido: " + usuario.getNome());
		System.out.println();

	}

	@Test
	@Ignore
	public void editar() {
		String matricula = "c014583";

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.buscar(matricula);

		usuario.setNome("Emanoel");
		usuario.setSenha("654321");

		usuarioDAO.editar(usuario);
		System.out.println("Usuario editado: ");
		System.out.println("Matricula: " + usuario.getMatricula());
		System.out.println("Nome do usuario " + usuario.getNome());
		System.out.println("Senha do usuario: " + usuario.getSenha());
		System.out.println();

	}

	@Test
	@Ignore
	public void autenticar() {

		String matricula = "c014587";
		String senha = "123456";

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.autenticar(matricula, senha);

		System.out.println("nome: " + usuario.getNome());
		System.out.println("senha: " + usuario.getSenha());
		System.out.println("senha Sem cripftografia: " + usuario.getSenhaCriptografada());
	}

	@Test
	@Ignore
	public void salvarsimples() {
		Usuario usuario = new Usuario();
		usuario.setMatricula("c014588");
		usuario.setNome("Wilkins");
		usuario.setSenha("123456");

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		usuarioDAO.salvar(usuario);

	}

	@Test
	@Ignore
	public void merge() {
//		Usuario usuario = new Usuario();
//		usuario.setMatricula("c014588");
//		usuario.setNome("Wilkins");
//		usuario.setSenha("123456");
//		
//		UsuarioDAO usuarioDAO = new UsuarioDAO();
//		usuarioDAO.merge(usuario);

		UsuarioDAO usuarioDAO = new UsuarioDAO();
		Usuario usuario = usuarioDAO.buscar("c014588");
		usuario.setNome("Potter");
		usuarioDAO.merge(usuario);

	}

}
