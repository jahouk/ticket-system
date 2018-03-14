package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Asset;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Integer>{

}
