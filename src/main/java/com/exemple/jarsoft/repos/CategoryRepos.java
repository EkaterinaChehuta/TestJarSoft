package com.exemple.jarsoft.repos;

import com.exemple.jarsoft.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CategoryRepos extends JpaRepository<Category, Integer> {
    List<Category> findByIsDeleted(boolean isDeleted);
    List<Category> findByNameOrRequestId(String name, String requestId);
    List<Category> findByNameContainingAndIsDeleted(String string, boolean isDeleted);
    Category findByRequestIdAndIsDeleted(String category, boolean isDeleted);
}
