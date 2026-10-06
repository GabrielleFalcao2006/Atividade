package teste;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projeto.validador.CadastroService;
import projeto.validador.NivelUsuario;
import projeto.validador.UsuarioRepositoryEmMemoria;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Testes do RF01 - cadastro de usuário. */
class CadastroServiceTest {

    private UsuarioRepositoryEmMemoria repositorio;
    private CadastroService service;

    @BeforeEach
    void preparar() {
        repositorio = new UsuarioRepositoryEmMemoria();
        service = new CadastroService(repositorio);
    }

    @Test
    @DisplayName("RF01 - cadastra usuário com todos os dados válidos")
    void deveCadastrarUsuarioValido() {
        assertTrue(service.cadastrar("Ana", "ana", "Java@12345", "ana@empresa.com", NivelUsuario.CLIENTE));
        assertNotNull(repositorio.buscarPorLogin("ana"));
    }

    @Test
    @DisplayName("RF01 - rejeita campos nulos ou vazios")
    void deveRejeitarCamposNulosOuVazios() {
        assertFalse(service.cadastrar(null, "ana", "Java@12345", "a@b.com", NivelUsuario.CLIENTE));
        assertFalse(service.cadastrar("Ana", "", "Java@12345", "a@b.com", NivelUsuario.CLIENTE));
        assertFalse(service.cadastrar("Ana", "ana", null, "a@b.com", NivelUsuario.CLIENTE));
        assertFalse(service.cadastrar("Ana", "ana", "Java@12345", "  ", NivelUsuario.CLIENTE));
        assertFalse(service.cadastrar("Ana", "ana", "Java@12345", "a@b.com", null));
        assertNull(repositorio.buscarPorLogin("ana"));
    }

    @Test
    @DisplayName("RF01/RF02/RF03 - rejeita cadastro com senha que não cumpre as regras")
    void deveRejeitarSenhaFraca() {
        assertFalse(service.cadastrar("Ana", "ana", "senhafraca", "a@b.com", NivelUsuario.CLIENTE));
        assertNull(repositorio.buscarPorLogin("ana"));
    }

    @Test
    @DisplayName("RF01 - rejeita login já cadastrado")
    void deveRejeitarLoginDuplicado() {
        assertTrue(service.cadastrar("Ana", "ana", "Java@12345", "a@b.com", NivelUsuario.ADMIN));
        assertFalse(service.cadastrar("Outra", "ana", "Java@12345", "o@b.com", NivelUsuario.GERENTE));
    }
}
