
package com.houkstead.ticketsystem.models;

import javax.persistence.*;

@Entity
@Table(name = "TECH_COMPANY",uniqueConstraints = @UniqueConstraint(columnNames = "tech_company_id", name = "TECH_COMPANY_PK_CONSTRAINT"))
public class TechCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "tech_company_id")
    private int id;

    @OneToOne
    @JoinColumn(name="company_id")
    private Company company;

    public TechCompany() {
    }

    public TechCompany(Company company){
        setCompany(company);
    }

    public int getId() {
        return id;
    }


    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

}
