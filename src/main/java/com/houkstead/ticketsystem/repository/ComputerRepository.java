package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Computer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ComputerRepository extends JpaRepository<Computer, Integer>{

}
