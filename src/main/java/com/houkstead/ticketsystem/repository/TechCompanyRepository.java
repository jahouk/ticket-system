package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Company;
import com.houkstead.ticketsystem.models.TechCompany;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechCompanyRepository extends JpaRepository<TechCompany, Integer>{

}
