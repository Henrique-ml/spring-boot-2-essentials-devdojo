package academy.devdojo.springboot2.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import academy.devdojo.springboot2.entities.DevDojoUser;

public interface DevDojoUserRepository extends JpaRepository<DevDojoUser, Long>{
	
	DevDojoUser findByUsername(String username);
}
