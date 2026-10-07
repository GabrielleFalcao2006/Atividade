package projeto.validador;

/** RF06 - Erro genérico de autenticação (base das demais exceções de login). */

public class ErroAutenticacaoException extends RuntimeException {
    public ErroAutenticacaoException(String mensagem) {
        super(mensagem);
    }
}
