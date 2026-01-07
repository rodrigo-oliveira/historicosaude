package com.historicosaude.historico.infra.repository;

import com.historicosaude.historico.domain.entities.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findById(Long id);
    Optional<Usuario> findByLogin(String login);
    Optional<Usuario> findByCpf(String cpf);
}
