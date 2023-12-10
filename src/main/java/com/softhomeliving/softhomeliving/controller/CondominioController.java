package com.softhomeliving.softhomeliving.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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

import com.softhomeliving.softhomeliving.dto.CondominioDTO;
import com.softhomeliving.softhomeliving.dto.ResidenciaDTO;
import com.softhomeliving.softhomeliving.model.Condominio;
import com.softhomeliving.softhomeliving.model.Residencia;
import com.softhomeliving.softhomeliving.repository.CondominioRepository;
import com.softhomeliving.softhomeliving.repository.ResidenciaRepository;

@RestController
@RequestMapping("/app")
public class CondominioController {
	
	@Autowired
	private CondominioRepository condominioRepository;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	
	@GetMapping("/condominio")
	public ResponseEntity<List<CondominioDTO>> getCondominios() {
        List<Condominio> condominios = condominioRepository.findAll();
        List<Residencia> residencias = residenciaRepository.findAll();

        List<CondominioDTO> condominioDTOs = new ArrayList<>();

        for (Condominio condominio : condominios) {
            CondominioDTO condominioDTO = new CondominioDTO();
            condominioDTO.setId(condominio.getId());
            condominioDTO.setNome(condominio.getNome());
            condominioDTO.setEndereco(condominio.getEndereco());

            List<ResidenciaDTO> residenciaDTOs = new ArrayList<>();
            for (Residencia residencia : residencias) {
                if (residencia.getCondominio().getId().equals(condominio.getId())) {
                    ResidenciaDTO residenciaDTO = new ResidenciaDTO();
                    residenciaDTO.setId(residencia.getId());
                    residenciaDTO.setUnidade(residencia.getUnidade());
                    residenciaDTO.setDescricao(residencia.getDescricao());
                    residenciaDTOs.add(residenciaDTO);
                }
            }
            condominioDTO.setResidencias(residenciaDTOs);
            condominioDTOs.add(condominioDTO);
        }

        return ResponseEntity.ok(condominioDTOs);
    }
	
	@GetMapping("/condominio/{id}")
	public ResponseEntity<Map<String, Object>> getCondominioById(@PathVariable Long id) {
		// Pegar o condominio por Id se existir
	    Optional<Condominio> condominioOptional = condominioRepository.findById(id);

	    if (condominioOptional.isPresent()) {
	    	// Pegar o condominio
	        Condominio condominio = condominioOptional.get();
	        
	        // Pegando todas as residencias
	        List<Residencia> residencias = residenciaRepository.findAll();
	        List<ResidenciaDTO> residenciaDTOs = new ArrayList<>();
	        
	        // Filtrando apenas as residencias do condominio
	        residencias.stream()
	            .filter(residencia -> residencia.getCondominio().getId() == id)
	            .forEach(residencia -> {
	                ResidenciaDTO residenciaDTO = new ResidenciaDTO();
	                residenciaDTO.setId(residencia.getId());
	                residenciaDTO.setUnidade(residencia.getUnidade());
	                residenciaDTO.setDescricao(residencia.getDescricao());
	                residenciaDTO.setCondominioId(residencia.getCondominio().getId());
	                residenciaDTOs.add(residenciaDTO);
	            });

	        // convertendo residenciaDTO para residencia e setando para as residencias do condominio
	        condominio.setResidencias(residenciaDTOs.stream()
	        	    .map(residenciaDTO -> {
	        	        Residencia residencia = new Residencia();
	        	        residencia.setId(residenciaDTO.getId());
	        	        residencia.setUnidade(residenciaDTO.getUnidade());
	        	        residencia.setDescricao(residenciaDTO.getDescricao());
	        	        // Outros campos, se houver
	        	        return residencia;
	        	    })
	        	    .collect(Collectors.toList()));
	        
	        Map<String, Object> response = new HashMap<>();
	        response.put("condominio", condominio);

	        // Retorne o Condominio personalizado
	        return ResponseEntity.ok(response);
	    } else {
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@PostMapping("/condominio")
	@ResponseStatus(HttpStatus.CREATED)//Status 201 ok! backend
	public Condominio postCondominio(@RequestBody Condominio condominio) {
		return condominioRepository.save(condominio);
	}
	
	@PutMapping("/condominio/{id}")
	public ResponseEntity<Condominio> updateCondominio (@PathVariable Long id, @RequestBody Condominio condominioAtualizado) {
	    // Verificar se o condominio existe
	    Optional<Condominio> condominioExistenteOptional = condominioRepository.findById(id);
	    if (condominioExistenteOptional.isPresent()) {

	        // Salvar o usuário atualizado
	        Condominio condominioAtualizadoSalvo = condominioRepository.save(condominioAtualizado);
	        return ResponseEntity.ok(condominioAtualizadoSalvo);
	    } else {
	        // Se o usuário não existir, retornar um status 404 Not Found
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@DeleteMapping("/condominio/{id}")
	public void deleteCondominioById(@PathVariable Long id) {
		condominioRepository.deleteById(id);
	}

}
