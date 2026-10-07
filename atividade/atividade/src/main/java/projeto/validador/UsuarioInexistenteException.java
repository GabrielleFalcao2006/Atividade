package projeto.validador;

/** RF06 - Usuário inexistente. */

public class UsuarioInexistenteException extends ErroAutenticacaoException {
    public UsuarioInexistenteException() {
        super("Usuário inexistente");
    }
}
