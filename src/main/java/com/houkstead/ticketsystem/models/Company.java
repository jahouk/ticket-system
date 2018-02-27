package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "COMPANY",uniqueConstraints = @UniqueConstraint(columnNames = "company_id", name = "COMPANY_PK_CONSTRAINT"))
public class Company {
    //todo

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_id")
    private int id;                     // autonumber

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey=@ForeignKey(name="FK_COMPANY_INFO_COMPANY"))
    private CompanyInfo companyInfo;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey=@ForeignKey(name="FK_ADDRESS_COMPANY"))
    private Set<Address> addresses;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey=@ForeignKey(name="FK_COMPUTER_COMPANY"))
    private Set<Computer> computers;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey=@ForeignKey(name="FK_SITE_COMPANY"))
    private Set<Site> sites;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey=@ForeignKey(name="FK_TICKET_COMPANY"))
    private Set<Ticket> tickets;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(foreignKey=@ForeignKey(name="FK_USER_COMPANY"))
    private Set<User> users;

    // Constructors -----------------------------------------------------------

    public Company(){}

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // CompanyInfo
    public CompanyInfo getCompanyInfo() {
        return companyInfo;
    }

    public void setCompanyInfo(CompanyInfo companyInfo) {
        this.companyInfo = companyInfo;
    }

    // Addresses
    public Set<Address> getAddresses(){
        return this.addresses;
    }

    // Computers
    public Set<Computer> getComputers(){
        return computers;
    }

    // Sites
    public Set<Site> getSites(){
        return sites;
    }

    // Tickets
    public Set<Ticket> getTickets(){
        return tickets;
    }

    // Users
    public Set<User> getUsers(){
        return users;
    }

}
