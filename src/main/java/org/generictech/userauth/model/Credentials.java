package org.generictech.userauth.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

/**
 * Class to model the credentials table of the database. This table holds password information for each user.
 * Primarily used for authentication. 
 * @author Jaden Wilson
 * @since 1.0
 *
 */
@Entity
public class Credentials {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="credentials_id")
	int id;
	@NotNull
	String password;
	@NotNull
	String salt;
	@ManyToOne
	@JoinColumn(name="system_user_id", unique = true)
	@NotNull
	SystemUser user;
	
	public Credentials() {
		super();
	}

	public Credentials(int id, @NotNull String password, @NotNull String salt, @NotNull SystemUser user) {
		super();
		this.id = id;
		this.password = password;
		this.salt = salt;
		this.user = user;
	}

	public Credentials(@NotNull String password, @NotNull String salt, @NotNull SystemUser user) {
		super();
		this.password = password;
		this.salt = salt;
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public SystemUser getUser() {
		return user;
	}

	public void setUser(SystemUser user) {
		this.user = user;
	}

	@Override
	public String toString() {
		return "Credentials [id=" + id + ", password=" + password + ", salt=" + salt + ", user=" + user + "]";
	}
	
}
