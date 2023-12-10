package com.softhomeliving.softhomeliving.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Visitante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String entrada;
    private String nome;
    private LocalDateTime dataChegada;
    private LocalDateTime dataFim;

    @NotNull
    @ManyToOne
    @JoinColumn
    private Residencia residencia;

    @ManyToOne
    @JoinColumn(name = "criador_id")
    private Residente criador;
    
    @NotNull
    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;
}
