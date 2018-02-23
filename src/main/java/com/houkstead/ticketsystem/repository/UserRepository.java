package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("userRepository")
public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUsername(String username);
}
