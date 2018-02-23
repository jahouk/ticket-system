package com.houkstead.ticketsystem.repository;

import com.houkstead.ticketsystem.models.Address;
import com.houkstead.ticketsystem.models.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Integer>{

    List<Address> findByCompany(Company company);

}
