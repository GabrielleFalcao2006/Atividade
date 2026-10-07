package projeto.validador;

/** Acesso aos usuários. Pode lançar ErroConexaoBancoException (RF06). */

public interface UsuarioRepository {
    Usuario buscarPorLogin(String login);
    void salvar(Usuario usuario);
}
