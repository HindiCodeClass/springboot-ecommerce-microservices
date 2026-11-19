package com.ecommerce.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDto {
	@NotBlank(message = "Name Required")
		private String name;
	@Email(message = "Valid Email Required")
	@NotBlank(message = "Email Required")
		private String email;
	@NotBlank(message = "password required")
	@Size(min = 6, message = "password must be at least 6 characters")
		private String password;
		private String phone;
		private String roles;
}
