package com.historicosaude.historico.infra.controller;

import com.historicosaude.historico.application.dto.ConsultaDto;
import com.historicosaude.historico.application.usecases.ListarMinhasConsultasUseCase;
import com.historicosaude.historico.infra.config.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/meu-historico")
@RequiredArgsConstructor
public class MeuHistoricoController {

    private final ListarMinhasConsultasUseCase listarMinhasConsultasUseCase;

    @GetMapping
    public ResponseEntity<List<ConsultaDto>> obterMinhasConsultas(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        String cpfPaciente = userDetails.getCpf();
        List<ConsultaDto> consultas = listarMinhasConsultasUseCase.executar(cpfPaciente);
        
        return ResponseEntity.ok(consultas);
    }
}
