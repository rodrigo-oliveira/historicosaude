package com.historicosaude.historico.domain.entities;

import com.historicosaude.historico.domain.enums.StatusConsulta;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "consultas")
@Getter
@Setter
@NoArgsConstructor
public class Consulta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cpfPaciente;

    @Column(nullable = false)
    private String nomePaciente;

    @Column(nullable = false)
    private String emailPaciente;

    @Column(nullable = false)
    private LocalDateTime dataConsulta;

    @Column(nullable = false)
    private String medico;

    private String especialidade;
    private String motivo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusConsulta status = StatusConsulta.AGENDADA;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime atualizadoEm = LocalDateTime.now();
}
