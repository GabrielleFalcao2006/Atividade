package projeto.validador;

public class ErroConexaoBancoException extends RuntimeException {
    public ErroConexaoBancoException(String mensagem) {
        super(mensagem);
    }
}
