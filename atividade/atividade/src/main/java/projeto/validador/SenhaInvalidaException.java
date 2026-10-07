package projeto.validador;

/** RF06 - Senha inválida. */

public class SenhaInvalidaException extends ErroAutenticacaoException {
    public SenhaInvalidaException() {
        super("Senha inválida");
    }
}
