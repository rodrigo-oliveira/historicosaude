package com.historicosaude.historico.application.usecases;

import com.historicosaude.historico.application.dto.ConsultaDto;
import com.historicosaude.historico.domain.entities.Consulta;
import com.historicosaude.historico.infra.repository.ConsultaRepository;
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
            .map(ConsultaDto::fromEntity)
            .collect(Collectors.toList());
    }
}
