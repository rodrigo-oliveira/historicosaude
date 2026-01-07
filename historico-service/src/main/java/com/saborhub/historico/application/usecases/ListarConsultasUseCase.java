package com.historicosaude.historico.application.usecases;

import com.historicosaude.historico.application.dto.ConsultaDto;
import com.historicosaude.historico.domain.entities.Consulta;
import com.historicosaude.historico.infra.repository.ConsultaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ListarConsultasUseCase {
    
    private final ConsultaRepository consultaRepository;

    public List<ConsultaDto> executar() {
        List<Consulta> consultas = consultaRepository.findAll();
        log.info("Total de consultas encontradas no banco de dados: {}", consultas.size());
        List<ConsultaDto> dtos = consultas
            .stream()
            .map(ConsultaDto::fromEntity)
            .collect(Collectors.toList());
        log.info("Total de DTOs convertidas: {}", dtos.size());
        return dtos;
    }
}
