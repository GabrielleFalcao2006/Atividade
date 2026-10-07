package projeto.validador;

/** RF01 - Cadastro de usuário. */

public class CadastroService {

    private final UsuarioRepository repositorio;
    private final ValidacaoSenhaService validacaoSenha = new ValidacaoSenhaService();

    public CadastroService(UsuarioRepository repositorio) {
        this.repositorio = repositorio;
    }

     @return
    public boolean cadastrar(String nome, String login, String senha, String email, NivelUsuario nivel) {
        if (vazio(nome) || vazio(login) || vazio(senha) || vazio(email) || nivel == null) {
            return false;
        }
        if (!validacaoSenha.validarSenha(senha)) {
            return false;
        }
        if (repositorio.buscarPorLogin(login) != null) {
            return false;
        }
        repositorio.salvar(new Usuario(nome, login, senha, email, nivel));
        return true;
    }

    private boolean vazio(String valor) {
        return valor == null || valor.isBlank();
    }
}
