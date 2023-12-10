package com.softhomeliving.softhomeliving.dto;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.softhomeliving.softhomeliving.enums.UserRole;

import lombok.Data;

@Data
public class ResidenteDTO {
	
	private Long id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
    private String nomeCompleto;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String email;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date dataCriacao;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Long condominioId;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<String> unidade;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Date dataAtualizacao;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UserRole role;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ResidenciaDTO> residencias;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private UUID userId;

}
