package com.softhomeliving.softhomeliving.enums;

public enum UserRole {

    MANAGER("ROLE_ADMIN"),
    ADMIN("ROLE_STAFF"),
    USER("ROLE_USER");
	
	private String role;
	
	UserRole(String role) {
		this.role = role;
	}
	
	public String getRole () {
		return role;
	}
}
