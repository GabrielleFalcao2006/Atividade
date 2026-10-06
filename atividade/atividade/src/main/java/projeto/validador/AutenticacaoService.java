package projeto.validador;

/** RF04, RF05, RF06 e RF07 - Autenticação com bloqueio por tentativas. */
public class AutenticacaoService {

    public static final int MAX_TENTATIVAS = 3;

    private final UsuarioRepository repositorio;

    public AutenticacaoService(UsuarioRepository repositorio) {
        this.repositorio = repositorio;
    }

    /**
     * @return o usuário autenticado
     * @throws CamposVaziosException       login ou senha nulos/vazios (RF04)
     * @throws UsuarioInexistenteException login não cadastrado (RF05)
     * @throws ContaBloqueadaException     conta bloqueada, mesmo com senha correta (RF07)
     * @throws SenhaInvalidaException      senha incorreta (RF05)
     * @throws ErroConexaoBancoException   falha no repositório (RF06)
     */
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
