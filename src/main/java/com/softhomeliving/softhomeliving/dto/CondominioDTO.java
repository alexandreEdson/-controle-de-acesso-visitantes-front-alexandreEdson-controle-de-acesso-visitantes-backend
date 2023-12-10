package com.softhomeliving.softhomeliving.dto;

import java.util.List;

import lombok.Data;

@Data
public class CondominioDTO {

	    private Long id;
	    private String nome;
	    private String endereco;
	    private List<ResidenciaDTO> residencias;    

}
