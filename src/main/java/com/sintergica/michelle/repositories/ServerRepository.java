package com.sintergica.michelle.repositories;

import com.sintergica.michelle.entities.Server;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ServerRepository extends CrudRepository<Server, Long> {
	@Query(nativeQuery = true,
	value = "SELECT id, address FROM server")
	List<Server> getAll();

	Optional<Server> findByServerName(String serverName);
}
