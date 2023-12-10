package com.softhomeliving.softhomeliving.controller;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.softhomeliving.softhomeliving.dto.AuthenticationDTO;
import com.softhomeliving.softhomeliving.dto.LoginResponseDTO;
import com.softhomeliving.softhomeliving.dto.RegisterDTO;
import com.softhomeliving.softhomeliving.enums.UserRole;
import com.softhomeliving.softhomeliving.model.Residencia;
import com.softhomeliving.softhomeliving.model.Residente;
import com.softhomeliving.softhomeliving.model.User;
import com.softhomeliving.softhomeliving.model.Visitante;
import com.softhomeliving.softhomeliving.repository.ResidenciaRepository;
import com.softhomeliving.softhomeliving.repository.ResidenteRepository;
import com.softhomeliving.softhomeliving.repository.UserRepository;
import com.softhomeliving.softhomeliving.repository.VisitanteRepository;
import com.softhomeliving.softhomeliving.services.TokenService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/usuario")
public class UserController {
	
	@Autowired
	UserRepository user;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private ResidenteRepository residenteRepository;
	
	@Autowired
	private ResidenciaRepository residenciaRepository;
	
	@Autowired
	private VisitanteRepository visitanteRepository;
	
	@Autowired
	private TokenService tokenService;
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.login(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var user = (User) auth.getPrincipal();
		var token = tokenService.generateToken((User) auth.getPrincipal());
		
		return ResponseEntity.ok(new LoginResponseDTO(token, user.getId(), user.getRole(), user.getCondominio().getId()));	
	}
	
	@PostMapping("/criar")
	@ResponseStatus(HttpStatus.CREATED)
	public ResponseEntity<User> registrar(@RequestBody @Valid RegisterDTO data) {
	    if(this.user.findByLogin(data.login()) != null) {
	    	return ResponseEntity.badRequest().build();
	    }
	    User newUser = null;
	    String encryptedPassoword = new BCryptPasswordEncoder().encode(data.password());
	    if(data.condominio() != null) {
	    	newUser = new User(data.login(), encryptedPassoword, data.role(), data.condominio());
	    	this.user.save(newUser);
	    } else {
	    	newUser = new User(data.login(), encryptedPassoword, data.role());
	    	this.user.save(newUser);
	    }        
	    
	    return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<User> updateUsuario(@PathVariable UUID id, @RequestBody User usuarioAtualizado) {
	    // Verificar se o usuário existe
	    Optional<User> usuarioExistenteOptional = user.findById(id);
	    if (usuarioExistenteOptional.isPresent()) {
	        User usuarioExistente = usuarioExistenteOptional.get();

	        // Atualizar apenas a senha, se ela foi fornecida
	        if (usuarioAtualizado.getPassword() != null && !usuarioAtualizado.getPassword().isEmpty()) {
	            usuarioExistente.setPassword(usuarioAtualizado.getPassword());
	            usuarioExistente.codificarSenha(usuarioAtualizado.getPassword());
	        }

	        // Salvar o usuário atualizado
	        User usuarioAtualizadoSalvo = user.save(usuarioExistente);
	        return ResponseEntity.ok(usuarioAtualizadoSalvo);
	    } else {
	        // Se o usuário não existir, retornar um status 404 Not Found
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@PutMapping("/roles/{id}")
	public ResponseEntity<User> updateUserRole(@PathVariable UUID id, @RequestBody String role) {
		// Verificar se o usuário existe
	    Optional<User> usuarioExistenteOptional = user.findById(id);
	    if (usuarioExistenteOptional.isPresent()) {
	        User usuarioExistente = usuarioExistenteOptional.get();
		
			if (role.equals("ADMIN")){
				usuarioExistente.setRole(UserRole.ADMIN);
			} else if (role.equals("MANAGER")) {
				usuarioExistente.setRole(UserRole.MANAGER);
			} else if (role.equals("USER")) {
				usuarioExistente.setRole(UserRole.USER);
			}
			 // Salvar o usuário atualizado
	        User usuarioAtualizadoSalvo = user.save(usuarioExistente);
	        return ResponseEntity.ok(usuarioAtualizadoSalvo);
	    } else {
	        // Se o usuário não existir, retornar um status 404 Not Found
	        return ResponseEntity.notFound().build();
	    }
	}
	
	@DeleteMapping("/apagar/{id}")
	public void deleteUserById(@PathVariable UUID id) {    
	    List<Residente> residentes = residenteRepository.findAll();
	    
	    residentes.stream()
	    	.filter(residente -> residente.getUser().getId().equals(id))
	    		.forEach(residente -> {
	    			Long residenteId = residente.getId();
	    			
	    			List<Visitante> visitantes = visitanteRepository.findAll();
	    			
	    			visitantes.stream()
	    				.filter(visitante -> visitante.getCriador().getId() == residenteId)
	    				.forEach(visitante -> {
	    					visitanteRepository.deleteById(visitante.getId());
	    				});
	    			
	    			 // Remover o residente da lista de residentes de cada residência
	    	        for (Residencia residencia : residente.getResidencias()) {
	    	            residencia.getResidentes().remove(residente);
	    	            residenciaRepository.save(residencia); // Atualizar a residência
	    	        }
	    	        // Excluir o residente
	    	        residenteRepository.deleteById(residenteId);	
	    		});
	    user.deleteById(id);
	}
}
