package com.softhomeliving.softhomeliving.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.softhomeliving.softhomeliving.model.Condominio;
import com.softhomeliving.softhomeliving.model.Residencia;
import com.softhomeliving.softhomeliving.model.Residente;

import lombok.Data;

@Data
public class VisitanteDTO {
	
	Long id;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String entrada;
	String nome;
	LocalDateTime dataChegada;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	LocalDateTime dataFim;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Residencia residencia;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Residente criador;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	Condominio condominio;
	@JsonInclude(JsonInclude.Include.NON_NULL)
	String unidade;

}
