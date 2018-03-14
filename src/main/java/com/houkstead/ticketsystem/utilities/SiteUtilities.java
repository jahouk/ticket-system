package com.houkstead.ticketsystem.utilities;

import com.houkstead.ticketsystem.models.Company;
import com.houkstead.ticketsystem.models.TechCompany;
import com.houkstead.ticketsystem.repository.CompanyRepository;
import com.houkstead.ticketsystem.repository.TechCompanyRepository;

import java.util.List;

public class SiteUtilities {

    public static Company getTechCompany(
            TechCompanyRepository techCompanyRepository,
            CompanyRepository companyRepository) {
        Company returnCompany = null;
        for(TechCompany techCompany : techCompanyRepository.findAll()){
            returnCompany = techCompany.getCompany();
            break;
        }

        if(returnCompany == null){
            // Hardcoded fail over in case something is wrong
            returnCompany = companyRepository.findOne(1);
        }

        return returnCompany;
    }
}
