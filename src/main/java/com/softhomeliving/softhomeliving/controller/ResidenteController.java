package com.softhomeliving.softhomeliving.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.softhomeliving.softhomeliving.dto.ResidenciaDTO;
import com.softhomeliving.softhomeliving.dto.ResidenteDTO;
import com.softhomeliving.softhomeliving.model.Residencia;
import com.softhomeliving.softhomeliving.model.Residente;
import com.softhomeliving.softhomeliving.repository.ResidenciaRepository;
import com.softhomeliving.softhomeliving.repository.ResidenteRepository;

@RestController
@RequestMapping("/residente")
public class ResidenteController {
	
	@Autowired
	ResidenteRepository residenteRepository;
	
	@Autowired
	ResidenciaRepository residenciaRepository;
	
	@GetMapping
	public ResponseEntity<Map<String, Object>> getResidentes() {
        
        List<Residente> residentes = residenteRepository.findAll();
        List<Residencia> residencias = residenciaRepository.findAll();

        List<ResidenteDTO> residenteDTOs = new ArrayList<>();

        for (Residente residente : residentes) {
            ResidenteDTO residenteDTO = new ResidenteDTO();
            residenteDTO.setNomeCompleto(residente.getNome() + " " + residente.getSobrenome());
            residenteDTO.setDataAtualizacao(residente.getDataAtualizacao());
            residenteDTO.setEmail(residente.getEmail());
            residenteDTO.setUserId(residente.getUser().getId());
            
            List<ResidenciaDTO> residenciaDTOs = new ArrayList<>();
			residencias.forEach(residencia -> {
				residencia.getResidentes().stream()
				.filter(residenteF -> residenteF.getUser().getId().equals(residenteDTO.getUserId()))
				.forEach(residenteF -> {
					residenteF.getResidencias()
					.forEach(residenciaF -> {
						ResidenciaDTO residenciaDTO = new ResidenciaDTO();
						residenciaDTO.setUnidade(residenciaF.getUnidade());
						residenciaDTO.setId(residenciaF.getId());
						residenciaDTOs.add(residenciaDTO);
					});
				});
			});
			residenteDTO.setResidencias(residenciaDTOs);
            residenteDTO.setCondominioId(residente.getCondominio().getId());
            residenteDTOs.add(residenteDTO);
            }
        Map<String, Object> response = new HashMap<>();
        response.put("residentes", residenteDTOs);
        
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/condominio/{id}")
	public ResponseEntity<Map<String, Object>> getResidenteByCondominio(@PathVariable Long id) {
        
        List<Residente> residentes = residenteRepository.findAll();
        List<ResidenteDTO> residenteDTOs = new ArrayList<>();
        
        residentes.stream()
        	.filter(residente -> residente.getCondominio().getId() == id)
        	.forEach(residente -> {
        		ResidenteDTO residenteDTO = new ResidenteDTO();
        		List<String> unidades = new ArrayList<>();
        		
        		residenteDTO.setId(residente.getId());
        		residenteDTO.setNomeCompleto(residente.getNome() + " " + residente.getSobrenome());
        		residenteDTO.setEmail(residente.getEmail());
        		residenteDTO.setUserId(residente.getUser().getId());
        		
        		residente.getResidencias().forEach(residencia -> {
        			unidades.add(residencia.getUnidade());
        		});
        		
        		residenteDTO.setUnidade(unidades);
        		residenteDTO.setRole(residente.getUser().getRole());
        		residenteDTOs.add(residenteDTO);
        	});
        
        
        Map<String, Object> response = new HashMap<>();
        response.put("residentes", residenteDTOs);
        
        return ResponseEntity.ok(response);
    }
	
	@GetMapping("/user/{id}")
	public ResponseEntity<Map<String, Object>> getResidentePorUserId(@PathVariable UUID id) {
		List<Residente> residentes = residenteRepository.findAll();
		List<Residencia> residencias = residenciaRepository.findAll();
		
		List<ResidenteDTO> residenteDTOs = new ArrayList<>();
		
		residentes.stream()
			.filter(residente -> residente.getUser().getId().equals(id))
			.forEach(residente -> {
				ResidenteDTO residenteDTO = new ResidenteDTO();
				residenteDTO.setId(residente.getId());
				residenteDTO.setNomeCompleto(residente.getNome() + " " + residente.getSobrenome());
				residenteDTO.setCondominioId(residente.getCondominio().getId());
				residenteDTO.setDataAtualizacao(residente.getDataAtualizacao());
				residenteDTO.setDataCriacao(residente.getDataCriacao());
				residenteDTO.setEmail(residente.getEmail());
				residenteDTO.setUserId(residente.getUser().getId());
				
				List<ResidenciaDTO> residenciaDTOs = new ArrayList<>();
				residencias.forEach(residencia -> {
					residencia.getResidentes().stream()
					.filter(residenteF -> residenteF.getUser().getId().equals(id))
					.forEach(residenteF -> {
						residenteF.getResidencias()
						.forEach(residenciaF -> {
							ResidenciaDTO residenciaDTO = new ResidenciaDTO();
							residenciaDTO.setUnidade(residenciaF.getUnidade());
							residenciaDTO.setId(residenciaF.getId());
							residenciaDTOs.add(residenciaDTO);
						});
					});
				});
				residenteDTO.setResidencias(residenciaDTOs);
				residenteDTOs.add(residenteDTO);
			});
		
		Map<String, Object> response = new HashMap<>();
        response.put("residentes", residenteDTOs);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/{id}")
	public Residente getResidentePorId(@PathVariable Long id) {
		return residenteRepository.findById(id).orElse(null);
	}	
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<Residente> adicionarResidente(@RequestBody Residente residente) {
		Residente residenteResponse = residenteRepository.save(residente);
		
		residenteResponse.getResidencias()
		.stream()
		.forEach(residenciaItem ->{
			Optional<Residencia> residenciaOptional = residenciaRepository.findById(residenciaItem.getId());
			
			if (residenciaOptional.isPresent()) {
				Residencia residencia = residenciaOptional.get();
				residencia.getResidentes().add(residenteResponse);
				residenciaRepository.save(residencia);
			}
		});
	    return ResponseEntity.ok(residente);
	}
	
	@PutMapping("/{id}")
    public ResponseEntity<Residente> updateResidente(@PathVariable Long id, @RequestBody Residente residenteAtualizado) {
        // Verificar se o residente existe
        Optional<Residente> residenteExistenteOptional = residenteRepository.findById(id);
        if (residenteExistenteOptional.isPresent()) {
            Residente residenteExistente = residenteExistenteOptional.get();

            // Verificar e atualizar o email
            if (residenteAtualizado.getEmail() != null && !residenteAtualizado.getEmail().isEmpty()) {
                residenteExistente.setEmail(residenteAtualizado.getEmail());
            }

            // Verificar e atualizar as residências
            if (residenteAtualizado.getResidencias() != null) {
                residenteExistente.setResidencias(residenteAtualizado.getResidencias());
            }

            // Salvar as alterações
            Residente residenteAtualizadoSalvo = residenteRepository.save(residenteExistente);
            return ResponseEntity.ok(residenteAtualizadoSalvo);
        } else {
            // Se o residente não existir, retornar um status 404 Not Found
            return ResponseEntity.notFound().build();
        }
    }
	
	@DeleteMapping("/{id}")
	public void deleteResidenteById(@PathVariable Long id) {
	    Optional<Residente> residenteOptional = residenteRepository.findById(id);
	    
	    if (residenteOptional.isPresent()) {
	        Residente residente = residenteOptional.get();
	        
	        // Remover o residente da lista de residentes de cada residência
	        for (Residencia residencia : residente.getResidencias()) {
	            residencia.getResidentes().remove(residente);
	            residenciaRepository.save(residencia); // Atualizar a residência
	        }

	        // Excluir o residente
	        residenteRepository.deleteById(id);
	    }
	}
}
