package com.historicosaude.agendamento.application.usecases;

import com.historicosaude.agendamento.application.dto.ConsultaDto;
import com.historicosaude.agendamento.domain.entities.Consulta;
import com.historicosaude.agendamento.infra.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ObterConsultaUseCase {
    
    private final ConsultaRepository consultaRepository;

    public ConsultaDto executar(Long id) {
        Consulta consulta = consultaRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Consulta não encontrada"));
        
        return convertToDto(consulta);
    }

    private ConsultaDto convertToDto(Consulta consulta) {
        return new ConsultaDto(
            consulta.getId(),
            consulta.getCpfPaciente(),
            consulta.getNomePaciente(),
            consulta.getEmailPaciente(),
            consulta.getDataConsulta(),
            consulta.getMedico(),
            consulta.getEspecialidade(),
            consulta.getMotivo(),
            consulta.getStatus()
        );
    }
}
