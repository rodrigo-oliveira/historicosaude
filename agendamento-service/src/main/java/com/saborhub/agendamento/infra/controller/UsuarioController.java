package com.historicosaude.agendamento.infra.controller;

import com.historicosaude.agendamento.application.dto.UsuarioDto;
import com.historicosaude.agendamento.application.usecases.ListarUsuariosUseCase;
import com.historicosaude.agendamento.application.usecases.DeletarUsuarioUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {
    
    private final ListarUsuariosUseCase listarUsuariosUseCase;
    private final DeletarUsuarioUseCase deletarUsuarioUseCase;

    public UsuarioController(
        ListarUsuariosUseCase listarUsuariosUseCase,
        DeletarUsuarioUseCase deletarUsuarioUseCase
    ) {
        this.listarUsuariosUseCase = listarUsuariosUseCase;
        this.deletarUsuarioUseCase = deletarUsuarioUseCase;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UsuarioDto>> listarUsuarios() {
        List<UsuarioDto> usuarios = listarUsuariosUseCase.executar();
        return ResponseEntity.ok(usuarios);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarUsuario(@PathVariable Long id) {
        deletarUsuarioUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }
}
