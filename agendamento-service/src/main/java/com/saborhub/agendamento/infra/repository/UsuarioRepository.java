package com.historicosaude.agendamento.infra.repository;

import com.historicosaude.agendamento.infra.persistence.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {
    Optional<UsuarioEntity> findByLogin(String login);
    Optional<UsuarioEntity> findByCpf(String cpf);
    Optional<UsuarioEntity> findByEmail(String email);
    UserDetails findByLoginAndSenha(String login, String senha);
}
