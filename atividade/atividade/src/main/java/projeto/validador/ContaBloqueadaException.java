package projeto.validador;

/** RF06 - Conta bloqueada. */

public class ContaBloqueadaException extends ErroAutenticacaoException {
    public ContaBloqueadaException() {
        super("Conta bloqueada");
    }
}
