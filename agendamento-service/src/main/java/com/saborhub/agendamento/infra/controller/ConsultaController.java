package com.historicosaude.agendamento.infra.controller;

import com.historicosaude.agendamento.application.dto.ConsultaDto;
import com.historicosaude.agendamento.application.dto.RegistroConsultaDto;
import com.historicosaude.agendamento.application.dto.AtualizarConsultaDto;
import com.historicosaude.agendamento.application.usecases.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/consultas")
public class ConsultaController {
    
    private final RegistrarConsultaUseCase registrarConsultaUseCase;
    private final ListarConsultasUseCase listarConsultasUseCase;
    private final ListarConsultasPorCpfUseCase listarConsultasPorCpfUseCase;
    private final ObterConsultaUseCase obterConsultaUseCase;
    private final AtualizarConsultaUseCase atualizarConsultaUseCase;
    private final CancelarConsultaUseCase cancelarConsultaUseCase;
    private final DeletarConsultaUseCase deletarConsultaUseCase;

    public ConsultaController(
        RegistrarConsultaUseCase registrarConsultaUseCase,
        ListarConsultasUseCase listarConsultasUseCase,
        ListarConsultasPorCpfUseCase listarConsultasPorCpfUseCase,
        ObterConsultaUseCase obterConsultaUseCase,
        AtualizarConsultaUseCase atualizarConsultaUseCase,
        CancelarConsultaUseCase cancelarConsultaUseCase,
        DeletarConsultaUseCase deletarConsultaUseCase
    ) {
        this.registrarConsultaUseCase = registrarConsultaUseCase;
        this.listarConsultasUseCase = listarConsultasUseCase;
        this.listarConsultasPorCpfUseCase = listarConsultasPorCpfUseCase;
        this.obterConsultaUseCase = obterConsultaUseCase;
        this.atualizarConsultaUseCase = atualizarConsultaUseCase;
        this.cancelarConsultaUseCase = cancelarConsultaUseCase;
        this.deletarConsultaUseCase = deletarConsultaUseCase;
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ENFERMEIRO')")
    public ResponseEntity<Long> criarConsulta(@RequestBody RegistroConsultaDto dto) {
        Long consultaId = registrarConsultaUseCase.executar(dto);

        return ResponseEntity.status(HttpStatus.CREATED).body(consultaId);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('ENFERMEIRO') or hasRole('MEDICO')")
    public ResponseEntity<List<ConsultaDto>> listarConsultas() {
        List<ConsultaDto> consultas = listarConsultasUseCase.executar();

        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/minhas-consultas")
    @PreAuthorize("hasRole('PACIENTE')")
    public ResponseEntity<List<ConsultaDto>> listarMinhasConsultas() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String cpf = authentication.getName();
        List<ConsultaDto> consultas = listarConsultasPorCpfUseCase.executar(cpf);

        return ResponseEntity.ok(consultas);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('ENFERMEIRO') or hasRole('MEDICO')")
    public ResponseEntity<ConsultaDto> obterConsulta(@PathVariable Long id) {
        ConsultaDto consulta = obterConsultaUseCase.executar(id);

        return ResponseEntity.ok(consulta);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public ResponseEntity<Void> atualizarConsulta(@PathVariable Long id, @RequestBody AtualizarConsultaDto dto) {
        atualizarConsultaUseCase.executar(id, dto);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/cancelar")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MEDICO')")
    public ResponseEntity<Void> cancelarConsulta(@PathVariable Long id) {
        cancelarConsultaUseCase.executar(id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletarConsulta(@PathVariable Long id) {
        deletarConsultaUseCase.executar(id);

        return ResponseEntity.noContent().build();
    }
}
