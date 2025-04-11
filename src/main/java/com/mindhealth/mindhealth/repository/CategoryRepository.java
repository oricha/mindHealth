package com.mindhealth.mindhealth.repository;

import com.mindhealth.mindhealth.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    // Custom queries can be added here
}
