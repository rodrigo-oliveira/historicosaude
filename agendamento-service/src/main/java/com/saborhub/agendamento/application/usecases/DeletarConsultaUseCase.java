package com.historicosaude.agendamento.application.usecases;

import com.historicosaude.agendamento.infra.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeletarConsultaUseCase {
    
    private final ConsultaRepository consultaRepository;

    public void executar(Long id) {
        if (!consultaRepository.existsById(id)) {
            throw new RuntimeException("Consulta não encontrada");
        }
        consultaRepository.deleteById(id);
    }
}
