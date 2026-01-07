package com.historicosaude.agendamento.infra.repository;

import com.historicosaude.agendamento.domain.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByCpfPaciente(String cpfPaciente);
    Optional<Consulta> findByIdAndCpfPaciente(Long id, String cpfPaciente);
    List<Consulta> findAll();
}
