package com.historicosaude.agendamento.application.usecases;

import com.historicosaude.agendamento.application.dto.RegistroConsultaDto;
import com.historicosaude.agendamento.domain.entities.Consulta;
import com.historicosaude.agendamento.infra.repository.ConsultaRepository;
import com.historicosaude.agendamento.infra.messaging.ConsultaEvent;
import com.historicosaude.agendamento.infra.messaging.ConsultaEventProducer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegistrarConsultaUseCase {
    
    private final ConsultaRepository consultaRepository;
    private final ConsultaEventProducer eventProducer;

    public Long executar(RegistroConsultaDto dto) {
        Consulta consulta = new Consulta(
            dto.cpfPaciente(),
            dto.nomePaciente(),
            dto.emailPaciente(),
            dto.dataConsulta(),
            dto.medico(),
            dto.especialidade(),
            dto.motivo()
        );

        Consulta consultaSalva = consultaRepository.save(consulta);

        // Publicar evento
        ConsultaEvent event = new ConsultaEvent(
            "CONSULTA_CRIADA",
            consultaSalva.getId(),
            consultaSalva.getCpfPaciente(),
            consultaSalva.getNomePaciente(),
            consultaSalva.getEmailPaciente(),
            consultaSalva.getDataConsulta(),
            consultaSalva.getMedico(),
            consultaSalva.getEspecialidade(),
            consultaSalva.getMotivo(),
            consultaSalva.getStatus().toString(),
            consultaSalva.getCriadoEm(),
            consultaSalva.getAtualizadoEm()
        );
        eventProducer.publishConsultaEvent(event);

        return consultaSalva.getId();
    }
}
