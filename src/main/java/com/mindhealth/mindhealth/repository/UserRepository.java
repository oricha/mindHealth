package com.mindhealth.mindhealth.repository;

import com.mindhealth.mindhealth.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
