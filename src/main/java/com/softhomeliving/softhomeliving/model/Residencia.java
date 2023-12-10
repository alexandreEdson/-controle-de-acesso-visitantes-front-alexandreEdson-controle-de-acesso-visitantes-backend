package com.softhomeliving.softhomeliving.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
public class Residencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String unidade;
    private String descricao;
    
    @ManyToMany
    @JoinTable(
        name = "residente_residencia",
        joinColumns = @JoinColumn(name = "residencia_id"),
        inverseJoinColumns = @JoinColumn(name = "residente_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = { "residencia_id", "residente_id" }))
    private List<Residente> residentes;
    
    @ManyToOne
    @JoinColumn(name = "criador_id")
    private User criador;
    
    @OneToMany(mappedBy = "residencia")
    private List<Visitante> visitantes;

    @ManyToOne
    private Condominio condominio;
}