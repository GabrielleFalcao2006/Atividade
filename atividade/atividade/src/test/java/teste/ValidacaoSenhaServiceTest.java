package teste;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import projeto.validador.ValidacaoSenhaService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidacaoSenhaServiceTest {

    private final ValidacaoSenhaService service = new ValidacaoSenhaService();

    @Test
    @DisplayName("CT07 - RF04 - senha nula é rejeitada (1ª condição do if)")
    void CT07_deveRejeitarSenhaNula() {
        assertFalse(service.validarSenha(null));
    }

    @Test
    @DisplayName("CT08 - RF04 - senha vazia é rejeitada (isBlank com string vazia)")
    void CT08_deveRejeitarSenhaVazia() {
        assertFalse(service.validarSenha(""));
    }

    @Test
    @DisplayName("CT08b - RF04 - senha só com espaços é rejeitada (isBlank, mesmo com 10 caracteres)")
    void CT08b_deveRejeitarSenhaSoComEspacos() {
        assertFalse(service.validarSenha("          "));
    }


    @Test
    @DisplayName("CT02/CT13 - RF02 - 9 caracteres é rejeitada (length < 10)")
    void CT02_deveRejeitarSenhaMenorQue10() {
        assertFalse(service.validarSenha("Java@1234")); // 9 caracteres
    }

    @Test
    @DisplayName("CT03 - RF02 - 13 caracteres é rejeitada (length > 12)")
    void CT03_deveRejeitarSenhaMaiorQue12() {
        assertFalse(service.validarSenha("Java@12345678")); // 13 caracteres
    }

    // ---------- Caminho 3: composição da senha ----------

    @Test
    @DisplayName("CT04 - RF03 - 11 caracteres, sem número, é rejeitada")
    void CT04_deveRejeitarSenhaSemNumero() {
        assertFalse(service.validarSenha("Java@Testes")); // 11 caracteres
    }

    @Test
    @DisplayName("CT04 (entrada original do enunciado) - 'Java@Test' tem 9 caracteres: cai na regra de tamanho, não na de número")
    void CT04_entradaOriginalCaiNaRegraDeTamanho() {
        assertFalse(service.validarSenha("Java@Test"));
    }

    @Test
    @DisplayName("CT05/CT15 - RF03 - 10 caracteres, sem letra, é rejeitada")
    void CT05_deveRejeitarSenhaSemLetra() {
        assertFalse(service.validarSenha("123456@789"));
    }

    @Test
    @DisplayName("CT06/CT14 - RF03 - 10 caracteres, sem especial, é rejeitada")
    void CT06_deveRejeitarSenhaSemEspecial() {
        assertFalse(service.validarSenha("Java123456"));
    }

    @Test
    @DisplayName("CT17 - RF03 - '_' não está na lista de especiais aceitos, então é rejeitada")
    void CT17_deveRejeitarSenhaComCaractereForaDaListaDeEspeciais() {
        assertFalse(service.validarSenha("Java_123456"));
    }

    // ---------- Caminho 4: senha válida ----------

    @Test
    @DisplayName("CT01 - RF02/RF03 - senha válida de 10 caracteres é aceita")
    void CT01_deveAceitarSenhaValida() {
        assertTrue(service.validarSenha("Java@12345"));
    }

    @Test
    @DisplayName("CT01 (entrada original do enunciado) - 'Java@1234' tem 9 caracteres e por isso NÃO é aceita")
    void CT01_entradaOriginalTem9CaracteresEhRejeitada() {
        assertFalse(service.validarSenha("Java@1234"));
    }

    @Test
    @DisplayName("CT09/CT11 - RF02 - exatamente 10 caracteres, demais regras ok, é aceita")
    void CT09_deveAceitarSenhaComExatamente10Caracteres() {
        assertTrue(service.validarSenha("Java@12345"));
    }

    @Test
    @DisplayName("CT10/CT12 - RF02 - exatamente 12 caracteres, demais regras ok, é aceita")
    void CT10_deveAceitarSenhaComExatamente12Caracteres() {
        assertTrue(service.validarSenha("Java@1234567"));
    }

    // ---------- Atividade 4: valores de fronteira ----------

    @ParameterizedTest(name = "[{index}] {0} caracteres -> {1}")
    @CsvSource({
            "9,  false",   // imediatamente abaixo do limite inferior
            "10, true",    // limite inferior
            "11, true",    // valor intermediário
            "12, true",    // limite superior
            "13, false"    // imediatamente acima do limite superior
    })
    @DisplayName("Atividade 4 - fronteira de tamanho 9, 10, 11, 12 e 13")
    void fronteiraDeTamanho(int tamanho, boolean esperado) {
        // "Aa@" garante letra, especial e (com os 1s) número; só o tamanho varia.
        String senha = "Aa@" + "1".repeat(tamanho - 3);
        assertEquals(tamanho, senha.length());
        assertEquals(esperado, service.validarSenha(senha));
    }

    @ParameterizedTest(name = "especial ''{0}'' é aceito")
    @ValueSource(strings = {"!", "@", "#", "$", "%", "&", "*", "(", ")"})
    @DisplayName("CT18 - RF03 - cada caractere especial da lista é aceito")
    void CT18_deveAceitarCadaCaractereEspecialDaLista(String especial) {
        assertTrue(service.validarSenha("Java" + especial + "12345"));
    }
}
