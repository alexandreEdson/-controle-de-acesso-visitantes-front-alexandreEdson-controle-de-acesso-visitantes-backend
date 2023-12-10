package com.softhomeliving.softhomeliving.dto;

import java.util.UUID;

import com.softhomeliving.softhomeliving.enums.UserRole;

public record LoginResponseDTO(String token, UUID userId, UserRole role, Long condominioId) {

}
