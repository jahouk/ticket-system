package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Integer> {

    Status findByStatus(String status);
}
