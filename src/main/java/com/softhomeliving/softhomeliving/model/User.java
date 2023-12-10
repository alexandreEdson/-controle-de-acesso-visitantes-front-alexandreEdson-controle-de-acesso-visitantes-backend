package com.softhomeliving.softhomeliving.model;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.softhomeliving.softhomeliving.enums.UserRole;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "users")
@Entity(name = "users")
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class User implements UserDetails {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(columnDefinition = "VARCHAR(36)")
	@JdbcTypeCode(SqlTypes.VARCHAR)
	private UUID id;
	@NotNull
    private String login;
	@NotNull
    private String password;
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @OneToMany(mappedBy = "user")
    private List<Residente> residentes;

    @ManyToOne
    @JoinColumn(name = "condominio_id")
    private Condominio condominio;
    
    public User (String login, String password, UserRole role, Condominio condominio) {
		this.login = login;
		this.password = password;
		this.role = role;
		this.condominio = condominio;
	}
    
    public User (String login, String password, UserRole role) {
		this.login = login;
		this.password = password;
		this.role = role;
	}
    
	@Override	
	public Collection<? extends GrantedAuthority> getAuthorities() {
		if (this.role == UserRole.MANAGER) {
			return List.of(
					new SimpleGrantedAuthority("ROLE_ADMIN"), 
	                new SimpleGrantedAuthority("ROLE_STAFF"),
	                new SimpleGrantedAuthority("ROLE_USER"));
		} else if (this.role == UserRole.ADMIN) {
			return List.of( 
					new SimpleGrantedAuthority("ROLE_STAFF"),
	                new SimpleGrantedAuthority("ROLE_USER"));
		} else {
			return List.of(new SimpleGrantedAuthority("ROLE_USER"));
		}
	}
	
	public void setPassword(String password) {
		this.password = password;
	}
	
	public void setRole(UserRole role) {
		this.role = role;
	}
	
	@Override
	public boolean isAccountNonExpired() {		
		return true;
	}
	
	@Override
	public boolean isAccountNonLocked() {		
		return true;
	}
	
	@Override
	public boolean isCredentialsNonExpired() {		
		return true;
	}
	
	@Override
	public boolean isEnabled() {
		return true;
	}
	
	// Método para codificar a password usando BCrypt
    public void codificarSenha(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        this.password = encoder.encode(password);
    }

    // Método para verificar se a password fornecida corresponde à password armazenada
    public boolean verificarSenha(String password) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder.matches(password, this.password);
    }

	public String getLogin() {
		return login;
	}

	@Override
	public String getUsername() {
		return login;
	}
}
