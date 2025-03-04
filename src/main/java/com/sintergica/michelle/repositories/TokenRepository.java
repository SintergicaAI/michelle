package com.sintergica.michelle.repositories;

import com.sintergica.michelle.entities.Token;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenRepository extends CrudRepository<Token, Long> {
	@NotNull
	List<Token> findAll();
}
