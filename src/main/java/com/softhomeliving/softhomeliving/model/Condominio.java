package com.softhomeliving.softhomeliving.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Data
@Entity
public class Condominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String endereco;
    
    @OneToMany(mappedBy = "condominio", cascade = CascadeType.REMOVE)
    private List<Residencia> residencias;

    @ElementCollection
    private List<String> telefonesCels;

    @ElementCollection
    private List<String> emails;

    @Lob
    private String sobre;
}

