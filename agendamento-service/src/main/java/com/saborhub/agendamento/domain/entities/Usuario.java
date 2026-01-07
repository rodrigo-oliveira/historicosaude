package com.historicosaude.agendamento.domain.entities;

import com.historicosaude.agendamento.domain.enums.Perfil;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Table(name = "usuarios")
@Getter
@Setter
@NoArgsConstructor
public class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String cpf;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String login;

    @Column(nullable = false)
    private String senha;

    @Column(name = "data_ultima_alteracao")
    private ZonedDateTime dataUltimaAlteracao;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Perfil perfil;

    public Usuario(String cpf, String nome, String email, String login, String senha, Perfil perfil) {
        this.cpf = cpf;
        this.nome = nome;
        this.email = email;
        this.login = login;
        this.senha = senha;
        this.perfil = perfil;
        this.dataUltimaAlteracao = ZonedDateTime.now();
    }
}
