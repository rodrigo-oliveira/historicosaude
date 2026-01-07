package com.historicosaude.notificacoes.infra.messaging;

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
@AllArgsConstructor
public class ConsultaEvent implements Serializable {
    private static final long serialVersionUID = 1L;

    @JsonProperty("eventType")
    private String eventType;
    
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
}
