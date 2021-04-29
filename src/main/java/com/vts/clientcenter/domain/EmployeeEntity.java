package com.vts.clientcenter.domain;

import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tvs_employees")
public class EmployeeEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotNull
	@Column(name = "source_id", nullable = false)
	private String sourceId;

	@NotNull
	@Column(name = "first_name", nullable = false)
	private String firstName;

	@Column(name = "middle_initial", nullable = true)
	private String middleInitial;

	@NotNull
	@Column(name = "last_name", nullable = false)
	private String lastName;

	@NotNull
	@Column(name = "email_address", nullable = false)
	private String emailAddress;

	@NotNull
	@Column(name = "phone_number", nullable = false)
	private String phoneNumber;

	@NotNull
	@Column(name = "street", nullable = false)
	private String street;

	@NotNull
	@Column(name = "city", nullable = false)
	private String city;

	@NotNull
	@Column(name = "state", nullable = false)
	private String state;

	@NotNull
	@Column(name = "zip_code", nullable = false)
	private String zipCode;

	@NotNull
	@Column(name = "birth_date", nullable = false)
	private LocalDate birthDate;

	@NotNull
	@Column(name = "social_security_number", nullable = false)
	private String socialSecurityNumber;
}
