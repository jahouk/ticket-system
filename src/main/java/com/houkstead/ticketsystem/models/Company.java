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

    @OneToOne
    @JoinColumn(foreignKey=@ForeignKey(name="FK_COMPANY_INFO_COMPANY"))
    private CompanyInfo companyInfo;

    @OneToMany
    @JoinColumn(foreignKey=@ForeignKey(name="FK_ADDRESS_COMPANY"))
    private List<Address> addresses = new ArrayList<>();

    @OneToMany
    @JoinColumn(foreignKey=@ForeignKey(name="FK_COMPUTER_COMPANY"))
    private List<Computer> computers = new ArrayList<>();

    @OneToMany
    @JoinColumn(foreignKey=@ForeignKey(name="FK_SITE_COMPANY"))
    private List<Site> sites = new ArrayList<>();

    @OneToMany
    @JoinColumn(foreignKey=@ForeignKey(name="FK_TICKET_COMPANY"))
    private List<Ticket> tickets = new ArrayList<>();

    @OneToMany
    @JoinColumn(foreignKey=@ForeignKey(name="FK_USER_COMPANY"))
    private List<User> users = new ArrayList<>();

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
    public List<Address> getAddresses(){
        return this.addresses;
    }

    // Computers
    public List<Computer> getComputers(){
        return computers;
    }

    // Sites
    public List<Site> getSites(){
        return sites;
    }

    // Tickets
    public List<Ticket> getTickets(){
        return tickets;
    }

    // Users
    public List<User> getUsers(){
        return users;
    }

}
