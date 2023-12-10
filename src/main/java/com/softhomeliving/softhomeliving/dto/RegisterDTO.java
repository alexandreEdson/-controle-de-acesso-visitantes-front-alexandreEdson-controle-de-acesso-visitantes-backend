package com.softhomeliving.softhomeliving.dto;

import com.softhomeliving.softhomeliving.enums.UserRole;
import com.softhomeliving.softhomeliving.model.Condominio;

public record RegisterDTO(String login, String password, UserRole role, Condominio condominio) {

}
