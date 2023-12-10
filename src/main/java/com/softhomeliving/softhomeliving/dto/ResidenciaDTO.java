package com.softhomeliving.softhomeliving.dto;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.Data;

@Data
public class ResidenciaDTO {
	
	 	private Long id;
	 	@JsonInclude(JsonInclude.Include.NON_NULL)
	    private String unidade;
	    @JsonInclude(JsonInclude.Include.NON_NULL)
	    private String descricao;
	    @JsonInclude(JsonInclude.Include.NON_NULL)
	    private Long condominioId;
	    @JsonInclude(JsonInclude.Include.NON_NULL)
	    private UUID criador;
}
