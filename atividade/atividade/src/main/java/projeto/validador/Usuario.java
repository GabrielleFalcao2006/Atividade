package projeto.validador;

/** RF01 - Dados do usuário, mais estado de bloqueio (RF07) e nível (RF08). */
public class Usuario {

    private final String nome;
    private final String login;
    private final String senha;
    private final String email;
    private final NivelUsuario nivel;
    private int tentativasInvalidas;
    private boolean bloqueado;

    public Usuario(String nome, String login, String senha, String email, NivelUsuario nivel) {
        this.nome = nome;
        this.login = login;
        this.senha = senha;
        this.email = email;
        this.nivel = nivel;
    }

    public String getNome() { return nome; }
    public String getLogin() { return login; }
    public String getSenha() { return senha; }
    public String getEmail() { return email; }
    public NivelUsuario getNivel() { return nivel; }
    public int getTentativasInvalidas() { return tentativasInvalidas; }
    public boolean isBloqueado() { return bloqueado; }

    void registrarTentativaInvalida() {
        tentativasInvalidas++;
        if (tentativasInvalidas >= AutenticacaoService.MAX_TENTATIVAS) {
            bloqueado = true;
        }
    }

    void zerarTentativas() {
        tentativasInvalidas = 0;
    }
}
