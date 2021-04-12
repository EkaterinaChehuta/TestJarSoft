package com.exemple.jarsoft.repos;

import com.exemple.jarsoft.domain.Request;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RequestRepos extends JpaRepository<Request, Integer> {
}
