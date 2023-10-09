package com.eidiko.security.model;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int tokenId;
	
	private String refreshToken;
	
	private Instant expiry;
	
	@OneToOne
	private Users users;
	
//	@OneToOne(mappedBy = "refreshToken")
//	private Employee employee;
	
}
