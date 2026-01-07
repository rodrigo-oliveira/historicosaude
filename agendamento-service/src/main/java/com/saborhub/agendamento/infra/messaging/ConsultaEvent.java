package com.historicosaude.agendamento.infra.messaging;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ConsultaEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("eventType")
    private String eventType; // CONSULTA_CRIADA, CONSULTA_ATUALIZADA, CONSULTA_CANCELADA
    
    @JsonProperty("id")
    private Long id;
    
    @JsonProperty("cpfPaciente")
    private String cpfPaciente;
    
    @JsonProperty("nomePaciente")
    private String nomePaciente;
    
    @JsonProperty("emailPaciente")
    private String emailPaciente;
    
    @JsonProperty("dataConsulta")
    private LocalDateTime dataConsulta;
    
    @JsonProperty("medico")
    private String medico;
    
    @JsonProperty("especialidade")
    private String especialidade;
    
    @JsonProperty("motivo")
    private String motivo;
    
    @JsonProperty("status")
    private String status;
    
    @JsonProperty("criadoEm")
    private LocalDateTime criadoEm;
    
    @JsonProperty("atualizadoEm")
    private LocalDateTime atualizadoEm;
    
    @JsonProperty("timestamp")
    private LocalDateTime timestamp;

    // Constructor para criar evento com campos básicos
    public ConsultaEvent(String eventType, Long id, String cpfPaciente, String nomePaciente, String emailPaciente,
                         LocalDateTime dataConsulta, String medico, String especialidade, String motivo, 
                         String status, LocalDateTime criadoEm, LocalDateTime atualizadoEm) {
        this.eventType = eventType;
        this.id = id;
        this.cpfPaciente = cpfPaciente;
        this.nomePaciente = nomePaciente;
        this.emailPaciente = emailPaciente;
        this.dataConsulta = dataConsulta;
        this.medico = medico;
        this.especialidade = especialidade;
        this.motivo = motivo;
        this.status = status;
        this.criadoEm = criadoEm;
        this.atualizadoEm = atualizadoEm;
        this.timestamp = LocalDateTime.now();
    }
}
