package com.historicosaude.agendamento.application.usecases;

import com.historicosaude.agendamento.application.dto.ConsultaDto;
import com.historicosaude.agendamento.domain.entities.Consulta;
import com.historicosaude.agendamento.infra.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ListarConsultasPorCpfUseCase {
    
    private final ConsultaRepository consultaRepository;

    public List<ConsultaDto> executar(String cpf) {
        return consultaRepository.findByCpfPaciente(cpf)
            .stream()
            .map(this::convertToDto)
            .collect(Collectors.toList());
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
