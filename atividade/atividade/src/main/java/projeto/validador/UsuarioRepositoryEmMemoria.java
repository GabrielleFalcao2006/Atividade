package projeto.validador;

import java.util.HashMap;
import java.util.Map;

/** Implementação simples em memória, usada no lugar de um banco real. */
public class UsuarioRepositoryEmMemoria implements UsuarioRepository {

    private final Map<String, Usuario> usuarios = new HashMap<>();

    @Override
    public Usuario buscarPorLogin(String login) {
        return usuarios.get(login);
    }

    @Override
    public void salvar(Usuario usuario) {
        usuarios.put(usuario.getLogin(), usuario);
    }
}
