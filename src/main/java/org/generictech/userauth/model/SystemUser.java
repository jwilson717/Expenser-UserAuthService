package org.generictech.userauth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

/**
 * Class to model the system_user table of the database
 * @author Jaden Wilson
 * @since 1.0
 *
 */
@Entity
public class SystemUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="system_user_id")
	int id;
	@Column(name="first_name")
	@NotNull
	String firstName;
	@Column(name="last_name")
	@NotNull
	String lastName;
	@Column(unique = true)
	@NotNull
	String email;
	@Column(unique = true)
	@NotNull
	String username;
	
	public SystemUser() {
		super();
	}

	public SystemUser(int id, @NotNull String firstName, @NotNull String lastName, @NotNull String email,
			@NotNull String username) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
	}

	public SystemUser(@NotNull String firstName, @NotNull String lastName, @NotNull String email,
			@NotNull String username) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.username = username;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@Override
	public String toString() {
		return "SystemUser [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", username=" + username + "]";
	}
		
}
