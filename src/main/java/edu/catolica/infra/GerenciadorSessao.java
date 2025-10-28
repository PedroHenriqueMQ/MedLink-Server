package edu.catolica.infra;

import edu.catolica.exception.usuario.SessaoInvalidaException;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GerenciadorSessao {
    private static GerenciadorSessao INSTANCIA;
    private final Map<String, LocalDateTime> sessoesAtivas;

    private GerenciadorSessao() {
        this.sessoesAtivas = new ConcurrentHashMap<>();
    }

    public static synchronized GerenciadorSessao getInstancia() {
        if (INSTANCIA == null) INSTANCIA = new GerenciadorSessao();

        return INSTANCIA;
    }

    public void login(String email) {
        sessoesAtivas.put(email, LocalDateTime.now());
    }

    public void validarSessao(String email) {
        LocalDateTime horaSessao = sessoesAtivas.get(email);

        if (horaSessao == null)
            throw new SessaoInvalidaException();

        LocalDateTime horaAtual = LocalDateTime.now();

        if (Duration.between(horaSessao, horaAtual).toMinutes() > 30) {
            sessoesAtivas.remove(email, horaSessao);
            throw new SessaoInvalidaException();
        }
    }
}