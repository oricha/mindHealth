package com.mindhealth.mindhealth.repos;

import com.mindhealth.mindhealth.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
