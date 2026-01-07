package com.historicosaude.historico.infra.graphql;

import com.historicosaude.historico.application.dto.ConsultaDto;
import com.historicosaude.historico.application.usecases.ListarConsultasUseCase;
import com.historicosaude.historico.application.usecases.ListarConsultasPorCpfUseCase;
import com.historicosaude.historico.application.usecases.ListarConsultasPorMedicoUseCase;
import com.historicosaude.historico.infra.config.AuthenticationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ConsultaQueryResolver {
    
    private final ListarConsultasUseCase listarConsultasUseCase;
    private final ListarConsultasPorCpfUseCase listarConsultasPorCpfUseCase;
    private final ListarConsultasPorMedicoUseCase listarConsultasPorMedicoUseCase;
    private final AuthenticationService authenticationService;

    @QueryMapping
    public List<ConsultaDto> todasAsConsultas(@Argument Integer limit, @Argument Integer offset) {
        try {
            log.info("Acessando todasAsConsultas - limit: {}, offset: {}", limit, offset);
            authenticationService.validarAcesso("ADMIN", "MEDICO", "ENFERMEIRO");
            log.info("Usuário autorizado para todasAsConsultas");
            List<ConsultaDto> consultas = listarConsultasUseCase.executar();
            log.info("Total de consultas retornadas do banco: {}", consultas.size());
            
            // Aplicar paginação se especificada
            if (limit != null && limit > 0) {
                int start = offset != null && offset > 0 ? offset : 0;
                int end = Math.min(start + limit, consultas.size());
                consultas = consultas.subList(start, end);
                log.info("Paginação aplicada - retornando {} consultas (de {} para {})", consultas.size(), start, end);
            }
            
            log.info("Total de consultas retornadas para GraphQL: {}", consultas.size());
            return consultas;
        } catch (Exception e) {
            log.error("Erro ao listar todas as consultas", e);
            throw e;
        }
    }

    @QueryMapping
    public List<ConsultaDto> consultasPorCpf(@Argument String cpf) {
        try {
            log.info("Acessando consultasPorCpf com CPF: {}", cpf);
            authenticationService.validarAcesso("ADMIN", "MEDICO", "ENFERMEIRO");
            log.info("Usuário autorizado para consultasPorCpf");
            
            if (cpf == null || cpf.trim().isEmpty()) {
                throw new IllegalArgumentException("CPF é obrigatório");
            }
            
            return listarConsultasPorCpfUseCase.executar(cpf);
        } catch (Exception e) {
            log.error("Erro ao listar consultas por CPF: {}", cpf, e);
            throw e;
        }
    }

    @QueryMapping
    public List<ConsultaDto> consultasPorMedico(@Argument String medico) {
        try {
            log.info("Acessando consultasPorMedico com médico: {}", medico);
            authenticationService.validarAcesso("ADMIN", "MEDICO", "ENFERMEIRO");
            log.info("Usuário autorizado para consultasPorMedico");
            
            if (medico == null || medico.trim().isEmpty()) {
                throw new IllegalArgumentException("Médico é obrigatório");
            }
            
            return listarConsultasPorMedicoUseCase.executar(medico);
        } catch (Exception e) {
            log.error("Erro ao listar consultas por médico: {}", medico, e);
            throw e;
        }
    }
}
