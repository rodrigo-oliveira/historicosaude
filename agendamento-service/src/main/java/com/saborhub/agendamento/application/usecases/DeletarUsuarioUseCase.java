package com.historicosaude.agendamento.application.usecases;

import com.historicosaude.agendamento.infra.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarUsuarioUseCase {
    
    private final UsuarioRepository usuarioRepository;

    public void executar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
