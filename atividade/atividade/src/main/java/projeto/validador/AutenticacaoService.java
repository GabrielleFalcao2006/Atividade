package projeto.validador;

/** RF04, RF05, RF06 e RF07 - Autenticação com bloqueio por tentativas */

public class AutenticacaoService {

    public static final int MAX_TENTATIVAS = 3;

    private final UsuarioRepository repositorio;

    public AutenticacaoService(UsuarioRepository repositorio) {
        this.repositorio = repositorio;
    }

    
      @return 
      @throws CamposVaziosException      
      @throws UsuarioInexistenteException 
      @throws ContaBloqueadaException     
      @throws SenhaInvalidaException     
      @throws ErroConexaoBancoException   
     
    public Usuario autenticar(String login, String senha) {
        if (login == null || login.isBlank() || senha == null || senha.isBlank()) {
            throw new CamposVaziosException();
        }

        Usuario usuario = repositorio.buscarPorLogin(login);
        if (usuario == null) {
            throw new UsuarioInexistenteException();
        }

        if (usuario.isBloqueado()) {
            throw new ContaBloqueadaException();
        }

        if (!usuario.getSenha().equals(senha)) {
            usuario.registrarTentativaInvalida();
            throw new SenhaInvalidaException();
        }

        usuario.zerarTentativas();
        return usuario;
    }
}
