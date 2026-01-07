package com.historicosaude.historico.infra.repository;

import com.historicosaude.historico.domain.entities.Consulta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConsultaRepository extends JpaRepository<Consulta, Long> {
    List<Consulta> findByCpfPaciente(String cpfPaciente);
    List<Consulta> findByMedico(String medico);
    List<Consulta> findAll();
}
