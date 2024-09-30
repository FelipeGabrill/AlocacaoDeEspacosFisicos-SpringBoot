package com.ucsal.arqsoftware.entities;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_user")
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class User {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Setter
	private Long id;
	
	@Setter
	private String username;
	
	@Column(unique = true)
	@Setter
	private String login;
	
	@Setter
	private String password;
	
	//@ManyToMany
	//@JoinTable(name = "tb_usuario_role", joinColumns = @JoinColumn(name = "usuario_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	//private Set<Role> roles = new HashSet<>();
	
	@OneToMany(mappedBy = "user")
	private List<ApprovalHistory> approvalHistories = new ArrayList<>();
	
	@OneToMany(mappedBy = "user")
	private List<Request> requests = new ArrayList<>();

	public User() {	
	}
}
