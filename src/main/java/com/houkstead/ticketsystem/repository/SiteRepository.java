package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Company;
import com.houkstead.ticketsystem.models.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, Integer> {
   Site findBySiteAndCompany(String site, Company company);
}
