package edu.catolica.infra; // Ajuste o pacote conforme necessário

import edu.catolica.exception.usuario.SessaoInvalidaException;
import org.springframework.stereotype.Component;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GerenciadorSessao {
    private final Map<String, LocalDateTime> sessoesAtivas;

    public GerenciadorSessao() {
        this.sessoesAtivas = new ConcurrentHashMap<>();
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
            sessoesAtivas.remove(email);
            throw new SessaoInvalidaException();
        }
    }
}