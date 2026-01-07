package com.historicosaude.agendamento.infra.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Utilitário para gerar hashes BCrypt de senhas.
 * Use este programa para gerar hashes seguros de senhas.
 */
public class PasswordHashGenerator {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        
        String password = "123456";
        String hash = encoder.encode(password);
        
        System.out.println("Senha: " + password);
        System.out.println("Hash BCrypt: " + hash);
        System.out.println("\nVerificação: " + encoder.matches(password, hash));
    }
}
