package teste;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projeto.validador.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de autenticação, exceções e bloqueio (RF04 a RF07).
 * O @BeforeEach cria um repositório novo a cada teste (RNF03 - independência).
 */
class AutenticacaoServiceTest {

    private static final String LOGIN = "ana";
    private static final String SENHA = "Java@12345";

    private UsuarioRepositoryEmMemoria repositorio;
    private AutenticacaoService service;
    private Usuario ana;

    @BeforeEach
    void preparar() {
        repositorio = new UsuarioRepositoryEmMemoria();
        ana = new Usuario("Ana", LOGIN, SENHA, "ana@empresa.com", NivelUsuario.CLIENTE);
        repositorio.salvar(ana);
        service = new AutenticacaoService(repositorio);
    }

    // ---------- Atividade 6: autenticação ----------

    @Test
    @DisplayName("AUT01 - RF05 - usuário e senha corretos autenticam")
    void AUT01_deveAutenticarUsuarioValido() {
        Usuario autenticado = service.autenticar(LOGIN, SENHA);
        assertSame(ana, autenticado);
    }

    @Test
    @DisplayName("AUT02 - RF05/RF06 - usuário inexistente é rejeitado")
    void AUT02_deveRejeitarUsuarioInexistente() {
        assertThrows(UsuarioInexistenteException.class, () -> service.autenticar("naoexiste", SENHA));
    }

    @Test
    @DisplayName("AUT03 - RF05/RF06 - senha incorreta é rejeitada")
    void AUT03_deveRejeitarSenhaIncorreta() {
        assertThrows(SenhaInvalidaException.class, () -> service.autenticar(LOGIN, "Errada@1234"));
    }

    @Test
    @DisplayName("AUT04 - RF04 - usuário nulo é rejeitado")
    void AUT04_deveRejeitarUsuarioNulo() {
        assertThrows(CamposVaziosException.class, () -> service.autenticar(null, SENHA));
    }

    @Test
    @DisplayName("AUT05 - RF04 - senha nula é rejeitada")
    void AUT05_deveRejeitarSenhaNula() {
        assertThrows(CamposVaziosException.class, () -> service.autenticar(LOGIN, null));
    }

    @Test
    @DisplayName("AUT06 - RF04 - usuário vazio é rejeitado")
    void AUT06_deveRejeitarUsuarioVazio() {
        assertThrows(CamposVaziosException.class, () -> service.autenticar("", SENHA));
    }

    @Test
    @DisplayName("AUT07 - RF04 - senha vazia é rejeitada")
    void AUT07_deveRejeitarSenhaVazia() {
        assertThrows(CamposVaziosException.class, () -> service.autenticar(LOGIN, ""));
    }

    @Test
    @DisplayName("AUT08 - RF07 - usuário bloqueado é rejeitado")
    void AUT08_deveRejeitarUsuarioBloqueado() {
        errar(3);
        assertThrows(ContaBloqueadaException.class, () -> service.autenticar(LOGIN, "Errada@1234"));
    }

    // ---------- Atividade 6/7: bloqueio por tentativas ----------

    @Test
    @DisplayName("AUT09 - RF07 - 1ª tentativa inválida: conta continua desbloqueada")
    void AUT09_primeiraTentativaInvalidaNaoBloqueia() {
        errar(1);
        assertFalse(ana.isBloqueado());
        assertEquals(1, ana.getTentativasInvalidas());
    }

    @Test
    @DisplayName("AUT10 - RF07 - 2ª tentativa inválida: conta continua desbloqueada")
    void AUT10_segundaTentativaInvalidaNaoBloqueia() {
        errar(2);
        assertFalse(ana.isBloqueado());
        assertEquals(2, ana.getTentativasInvalidas());
    }

    @Test
    @DisplayName("AUT11 - RF07 - 3ª tentativa inválida: conta é bloqueada")
    void AUT11_terceiraTentativaInvalidaBloqueia() {
        errar(3);
        assertTrue(ana.isBloqueado());
    }

    @Test
    @DisplayName("AUT12 / Cenário D - RF07 - após bloqueio, nem a senha correta autentica")
    void AUT12_naoDeveAutenticarAposBloqueioMesmoComSenhaCorreta() {
        errar(3);
        assertThrows(ContaBloqueadaException.class, () -> service.autenticar(LOGIN, SENHA));
    }

    @Test
    @DisplayName("RF07 - login correto zera o contador (as 3 tentativas precisam ser consecutivas)")
    void loginCorretoZeraContadorDeTentativas() {
        errar(2);
        service.autenticar(LOGIN, SENHA);
        assertEquals(0, ana.getTentativasInvalidas());
        errar(2);
        assertFalse(ana.isBloqueado());
    }

    // ---------- RF06: exceções ----------

    @Test
    @DisplayName("RF06 - todas as falhas de login são ErroAutenticacaoException")
    void falhasDeLoginSaoErroAutenticacao() {
        assertThrows(ErroAutenticacaoException.class, () -> service.autenticar("naoexiste", SENHA));
        assertThrows(ErroAutenticacaoException.class, () -> service.autenticar(LOGIN, "Errada@1234"));
        assertThrows(ErroAutenticacaoException.class, () -> service.autenticar("", ""));
    }

    @Test
    @DisplayName("RF06 - erro de conexão com o banco é propagado")
    void deveSinalizarErroDeConexaoComBanco() {
        UsuarioRepository quebrado = new UsuarioRepository() {
            @Override public Usuario buscarPorLogin(String login) {
                throw new ErroConexaoBancoException("banco indisponível");
            }
            @Override public void salvar(Usuario usuario) {
                throw new ErroConexaoBancoException("banco indisponível");
            }
        };
        AutenticacaoService comBancoFora = new AutenticacaoService(quebrado);
        assertThrows(ErroConexaoBancoException.class, () -> comBancoFora.autenticar(LOGIN, SENHA));
    }

    // ---------- RF08: níveis ----------

    @Test
    @DisplayName("RF08 - o sistema contempla ADMIN, GERENTE e CLIENTE")
    void deveContemplarOsTresNiveis() {
        assertArrayEquals(
                new NivelUsuario[]{NivelUsuario.ADMIN, NivelUsuario.GERENTE, NivelUsuario.CLIENTE},
                NivelUsuario.values());
    }

    @Test
    @DisplayName("RF08 - usuário autenticado mantém o nível cadastrado")
    void usuarioAutenticadoMantemNivel() {
        assertEquals(NivelUsuario.CLIENTE, service.autenticar(LOGIN, SENHA).getNivel());
    }

    private void errar(int vezes) {
        for (int i = 0; i < vezes; i++) {
            try {
                service.autenticar(LOGIN, "Errada@1234");
            } catch (SenhaInvalidaException ignorada) {
                // esperado: cada chamada soma uma tentativa inválida
            }
        }
    }
}
