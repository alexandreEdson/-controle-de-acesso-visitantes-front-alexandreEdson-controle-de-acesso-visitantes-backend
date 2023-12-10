package com.softhomeliving.softhomeliving.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.softhomeliving.softhomeliving.dto.VisitanteDTO;
import com.softhomeliving.softhomeliving.model.Residente;
import com.softhomeliving.softhomeliving.model.Visitante;
import com.softhomeliving.softhomeliving.repository.ResidenteRepository;
import com.softhomeliving.softhomeliving.repository.VisitanteRepository;

@RestController
@RequestMapping("app/condominio")
public class VisitanteController {
	
	@Autowired
	private VisitanteRepository visitanteRepository;
	
	@Autowired
	private ResidenteRepository residenteRepository;
	
	@GetMapping("/{id}/visitante")
	public ResponseEntity<Map<String, Object>> getVisitantesByCondominio (@PathVariable Long id){
		
		List<Visitante> visitantes = visitanteRepository.findAll();
		List<VisitanteDTO> visitanteDTOs = new ArrayList<>();
		
		visitantes.stream()
			.filter(visitante -> visitante.getCondominio().getId() == id)
			.forEach(visitante -> {
				VisitanteDTO visitanteDTO = new VisitanteDTO();
				
				visitanteDTO.setId(visitante.getId());
				visitanteDTO.setNome(visitante.getNome());
				visitanteDTO.setDataChegada(visitante.getDataChegada());
				visitanteDTO.setEntrada(visitante.getEntrada());
				visitanteDTO.setUnidade(visitante.getResidencia().getUnidade());
				
				visitanteDTOs.add(visitanteDTO);
			});
		
		Map<String, Object> response = new HashMap<>();
		response.put("visitantes", visitanteDTOs);		
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/residencia/{id}/visitante")
	public ResponseEntity<Map<String, Object>> getVisitantesByResidencia (@PathVariable Long id){
		
		List<Visitante> visitantes = visitanteRepository.findAll();
		List<VisitanteDTO> visitanteDTOs = new ArrayList<>();
		
		visitantes.stream()
			.filter(visitante -> visitante.getResidencia().getId() == id)
			.forEach(visitante -> {
				VisitanteDTO visitanteDTO = new VisitanteDTO();
				
				visitanteDTO.setId(visitante.getId());
				visitanteDTO.setNome(visitante.getNome());
				visitanteDTO.setDataChegada(visitante.getDataChegada());
				visitanteDTO.setEntrada(visitante.getEntrada());
				visitanteDTO.setUnidade(visitante.getResidencia().getUnidade());
				
				visitanteDTOs.add(visitanteDTO);
			});
		
		Map<String, Object> response = new HashMap<>();
		response.put("visitantes", visitanteDTOs);		
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/residencia/user/{id}")
	public ResponseEntity<Map<String, Object>> getVisitantesByUserId (@PathVariable UUID id){
		List<Residente> residentes = residenteRepository.findAll();
		List<VisitanteDTO> visitanteDTOs = new ArrayList<>();
		residentes.stream()
			.filter(residente -> residente.getUser().getId().equals(id))
				.forEach(residente -> {
					residente.getVisitantes()
						.forEach(visitante -> {
							VisitanteDTO visitanteObj = new VisitanteDTO();
							visitanteObj.setId(visitante.getId());
							visitanteObj.setNome(visitante.getNome());
							visitanteObj.setDataChegada(visitante.getDataChegada());
							visitanteObj.setEntrada(visitante.getEntrada());
							visitanteObj.setDataFim(visitante.getDataFim());
							visitanteDTOs.add(visitanteObj);
						});
				});
			
			
		Map<String, Object> response = new HashMap<>();
		response.put("visitantes", visitanteDTOs);		
		
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/residencia/visitante/{id}")
	public Visitante getVisitantePorId(@PathVariable Long id) {
		return visitanteRepository.findById(id).orElse(null);
	}
	
	@PostMapping("/residencia/visitante")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Visitante> postVisitante(@RequestBody Visitante visitante) {
		visitanteRepository.save(visitante);	
		return ResponseEntity.ok(visitante);
	}
	
	@DeleteMapping("/residencia/visitante/{id}")
	public void deleteVisitanteById (@PathVariable Long id) {
		visitanteRepository.deleteById(id);
	}

}
