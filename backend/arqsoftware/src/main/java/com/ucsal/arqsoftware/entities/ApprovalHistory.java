package com.ucsal.arqsoftware.entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "tb_approval_histories")
@Getter
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ApprovalHistory {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	@Setter
	private Long id;
	
	@Setter
	private Date dateTime;
	
	@Setter
	private boolean decision;
	
	@Setter
	private String observation;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	@Setter
	private User user;
	
	@ManyToMany
	@JoinTable(name = "tb_history_request",
				joinColumns = @JoinColumn(name = "history_id"),
				inverseJoinColumns = @JoinColumn(name = "request_id"))
	private Set<Request> requests = new HashSet<>();
	
	public ApprovalHistory() {
	}

}
