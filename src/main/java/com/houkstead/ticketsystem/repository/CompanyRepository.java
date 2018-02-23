package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

}
