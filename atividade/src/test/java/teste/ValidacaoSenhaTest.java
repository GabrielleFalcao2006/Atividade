package teste;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import projeto.validador.ValidacaoSenha;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidacaoSenhaTest {

    private ValidacaoSenha service = new ValidacaoSenha();

    @Test
    @DisplayName("CT01 - Senha válida deve ser aceita")
    void deveAceitarSenhaValida() {
        assertTrue(service.validarSenha("Java@123456"));
    }

    @Test
    @DisplayName("CT02 - Senha com 9 caracteres deve ser rejeitada")
    void deveRejeitarSenhaMenorQue10() {
        assertFalse(service.validarSenha("Ja@123456"));
    }

    @Test
    @DisplayName("CT03 - Senha com 13 caracteres deve ser rejeitada")
    void deveRejeitarSenhaMaiorQue12() {
        assertFalse(service.validarSenha("Java@12345678"));
    }

    @Test
    @DisplayName("CT04 - Senha sem número deve ser rejeitada")
    void deveRejeitarSenhaSemNumero() {
        assertFalse(service.validarSenha("Java@Testes"));
    }

    @Test
    @DisplayName("CT05 - Senha sem letra deve ser rejeitada")
    void deveRejeitarSenhaSemLetra() {
        assertFalse(service.validarSenha("123456@789"));
    }

    @Test
    @DisplayName("CT06 - Senha sem caractere especial deve ser rejeitada")
    void deveRejeitarSenhaSemEspecial() {
        assertFalse(service.validarSenha("Java123456"));
    }

    @Test
    @DisplayName("CT07 - Senha nula deve ser rejeitada")
    void deveRejeitarSenhaNula() {
        assertFalse(service.validarSenha(null));
    }

    @Test
    @DisplayName("CT08 - Senha vazia deve ser rejeitada")
    void deveRejeitarSenhaVazia() {
        assertFalse(service.validarSenha(""));
    }

    @Test
    @DisplayName("CT09 - Senha com exatamente 10 caracteres deve ser aceita")
    void deveAceitarSenhaCom10Caracteres() {
        assertTrue(service.validarSenha("Abcdef@123"));
    }

    @Test
    @DisplayName("CT10 - Senha com exatamente 12 caracteres deve ser aceita")
    void deveAceitarSenhaCom12Caracteres() {
        assertTrue(service.validarSenha("Java@1234567"));
    }


    @Test
    @DisplayName("EXTRA01 - Senha só com espaços deve ser rejeitada")
    void deveRejeitarSenhaSoComEspacos() {
        assertFalse(service.validarSenha("          "));
    }

    @Test
    @DisplayName("EXTRA02 - Senha só com letras (sem número e sem especial) deve ser rejeitada")
    void deveRejeitarSenhaSoComLetras() {
        assertFalse(service.validarSenha("abcdefghijk"));
    }

    @Test
    @DisplayName("EXTRA03 - Sublinhado não é caractere especial permitido")
    void deveRejeitarSublinhado() {
        assertFalse(service.validarSenha("Java_123456"));
    }


    @Test
    @DisplayName("FRONTEIRA - 9 caracteres: rejeitar")
    void fronteira9() {
        assertFalse(service.validarSenha("Ja@123456"));
    }

    @Test
    @DisplayName("FRONTEIRA - 10 caracteres: aceitar")
    void fronteira10() {
        assertTrue(service.validarSenha("Ja@1234567"));
    }

    @Test
    @DisplayName("FRONTEIRA - 11 caracteres: aceitar")
    void fronteira11() {
        assertTrue(service.validarSenha("Ja@12345678"));
    }

    @Test
    @DisplayName("FRONTEIRA - 12 caracteres: aceitar")
    void fronteira12() {
        assertTrue(service.validarSenha("Ja@123456789"));
    }

    @Test
    @DisplayName("FRONTEIRA - 13 caracteres: rejeitar")
    void fronteira13() {
        assertFalse(service.validarSenha("Ja@1234567890"));
    }
}