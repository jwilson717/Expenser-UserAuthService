package org.generictech.userauth.repo;

import java.util.Optional;

import org.generictech.userauth.model.Credentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * Interface to implement database interaction for the credentials table. Interface extends JpaRepository to provide
 * implementations. 
 * @author Jaden Wilson
 * @since 1.0
 *
 */
@Repository
public interface CredentialsRepo extends JpaRepository<Credentials, Integer>{
	@Query("SELECT c from Credentials c WHERE c.user.id = ?1")
	public Optional<Credentials> findCredentials(int id);
}
