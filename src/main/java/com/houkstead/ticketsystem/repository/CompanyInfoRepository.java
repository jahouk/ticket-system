package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.CompanyInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyInfoRepository extends JpaRepository<CompanyInfo, Integer>  {

}
