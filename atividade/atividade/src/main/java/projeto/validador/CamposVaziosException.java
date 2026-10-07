package projeto.validador;

/** RF06 - Campos obrigatórios não informados. */

public class CamposVaziosException extends ErroAutenticacaoException {
    public CamposVaziosException() {
        super("Campos obrigatórios não informados");
    }
}
