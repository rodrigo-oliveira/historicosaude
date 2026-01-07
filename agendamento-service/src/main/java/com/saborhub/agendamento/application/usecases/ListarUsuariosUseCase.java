package com.historicosaude.agendamento.application.usecases;

import com.historicosaude.agendamento.application.dto.UsuarioDto;
import com.historicosaude.agendamento.infra.persistence.UsuarioEntity;
import com.historicosaude.agendamento.infra.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ListarUsuariosUseCase {
    
    private final UsuarioRepository usuarioRepository;

    public ListarUsuariosUseCase(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    public List<UsuarioDto> executar() {
        return usuarioRepository.findAll()
                .stream()
                .map(this::converterParaDto)
                .toList();
    }

    private UsuarioDto converterParaDto(UsuarioEntity usuario) {
        return new UsuarioDto(
                usuario.getId(),
                usuario.getCpf(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                usuario.getDataUltimaAlteracao(),
                usuario.getPerfil()
        );
    }
}
