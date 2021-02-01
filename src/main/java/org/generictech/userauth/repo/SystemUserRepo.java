package org.generictech.userauth.repo;

import java.util.Optional;

import org.generictech.userauth.model.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Interface to implement database interaction. Extends JpaRepository to provide implementations. 
 * @author Jaden Wilson
 * @since 1.0
 *
 */
@Repository
public interface SystemUserRepo extends JpaRepository<SystemUser, Integer>{

	public Optional<SystemUser> findByUsername(String username);
	
	public Optional<SystemUser> findByEmail(String email);
}
