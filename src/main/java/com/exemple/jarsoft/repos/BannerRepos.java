package com.exemple.jarsoft.repos;

import com.exemple.jarsoft.domain.Banner;
import com.exemple.jarsoft.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;
import java.util.Locale;

@RepositoryRestResource
public interface BannerRepos extends JpaRepository<Banner, Integer> {
    List<Banner> findByIsDeleted(boolean isDeleted);
    List<Banner> findByCategoryAndIsDeleted(Category category, boolean isDeleted);
    Banner findByNameAndCategory(String name, Category category);
}
