package com.softhomeliving.softhomeliving.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.softhomeliving.softhomeliving.dto.ResidenciaDTO;
import com.softhomeliving.softhomeliving.model.Residencia;
import com.softhomeliving.softhomeliving.model.Residente;
import com.softhomeliving.softhomeliving.repository.ResidenciaRepository;
import com.softhomeliving.softhomeliving.repository.ResidenteRepository;

@RestController
@RequestMapping("/app/condominio")
public class ResidenciaController {
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private ResidenteRepository residenteRepository;
	
	@GetMapping("/residencia")
	public ResponseEntity<Map<String, Object>> getResidencias() {
        
        List<Residencia> residencias = residenciaRepository.findAll();

        
        List<ResidenciaDTO> residenciaDTOs = new ArrayList<>();

        for (Residencia residencia : residencias) {
            ResidenciaDTO residenciaDTO = new ResidenciaDTO();
            residenciaDTO.setId(residencia.getId());
            residenciaDTO.setUnidade(residencia.getUnidade());
            residenciaDTO.setDescricao(residencia.getDescricao());
            residenciaDTO.setCondominioId(residencia.getCondominio().getId());
            residenciaDTO.setCriador(residencia.getCriador().getId());
            residenciaDTOs.add(residenciaDTO);
            }
        Map<String, Object> response = new HashMap<>();
        response.put("residencias", residenciaDTOs);
        
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/{id}/residencia")
	public ResponseEntity<Map<String, Object>> getResidenciasByCondominioId(@PathVariable Long id) {
        
        List<Residencia> residencias = residenciaRepository.findAll();

        
        List<ResidenciaDTO> residenciaDTOs = new ArrayList<>();
        
        residencias.stream()
        	.filter(residencia -> residencia.getCondominio().getId() == id)
        	.forEach(residencia -> {
        		ResidenciaDTO residenciaDTO = new ResidenciaDTO();
                residenciaDTO.setId(residencia.getId());
                residenciaDTO.setUnidade(residencia.getUnidade());
                residenciaDTO.setDescricao(residencia.getDescricao());
                residenciaDTO.setCondominioId(residencia.getCondominio().getId());
                residenciaDTO.setCriador(residencia.getCriador().getId());
                residenciaDTOs.add(residenciaDTO);
        	});
       
        Map<String, Object> response = new HashMap<>();
        response.put("residencias", residenciaDTOs);
        
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/residencia/{id}")
	public Residencia getResidenciaById(@PathVariable Long id) {
		return residenciaRepository.findById(id).get();
	}
	
	@PostMapping("/residencia")
	@ResponseStatus(HttpStatus.CREATED)//Status 201 ok! backend
	public Residencia postResidencia(@RequestBody Residencia residencia) {
		return residenciaRepository.save(residencia);
	}
	
	@DeleteMapping("/residencia/{id}")
	public void deleteResidenciaById(@PathVariable Long id) {
		Optional<Residencia> residenciaOptional = residenciaRepository.findById(id);
	    
	    if (residenciaOptional.isPresent()) {
	        Residencia residencia = residenciaOptional.get();
	        
	        // Remover a residencia da lista de residencias de cada residente
	        for (Residente residente : residencia.getResidentes()) {
	            residente.getResidencias().remove(residencia);
	            residenteRepository.save(residente); // Atualizar o residente
	        }

	        // Excluir a residencia
	        residenciaRepository.deleteById(id);
	    }
	}

}
