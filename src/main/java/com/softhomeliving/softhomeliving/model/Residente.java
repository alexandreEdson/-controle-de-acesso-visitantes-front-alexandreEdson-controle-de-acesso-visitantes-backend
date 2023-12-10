package com.softhomeliving.softhomeliving.model;

import java.util.Date;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Entity
//@JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "condominio.residencias"})
public class Residente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String sobrenome;
    private String email;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    @NotNull
    private User user;
    
    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_criacao", updatable = false)
    private Date dataCriacao;
    
    @UpdateTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "data_atualizacao")
    private Date dataAtualizacao;
    
    @OneToMany(mappedBy = "criador")
    private List<Visitante> visitantes;
    
    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;

    @ManyToMany(mappedBy = "residentes", cascade = CascadeType.REMOVE)
    private List<Residencia> residencias;
}

