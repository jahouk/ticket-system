package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Office;
import com.houkstead.ticketsystem.models.Site;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;

public interface OfficeRepository extends JpaRepository<Office, Integer> {
    List<Office> findBySite(Site site);
}
