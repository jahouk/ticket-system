package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Address> addresses = new ArrayList<Address>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Computer> computers = new ArrayList<Computer>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Site> sites = new ArrayList<Site>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Ticket> tickets = new ArrayList<Ticket>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<User> users = new ArrayList<User>();

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

    public void addAddress(Address address){
        addresses.add(address);
    }

    // Computers
    public List<Computer> getComputers(){
        return computers;
    }

    public void addComputer(Computer computer){
        computers.add(computer);
    }

    // Sites
    public List<Site> getSites(){
        return sites;
    }

    public void addSite(Site site){
        sites.add(site);
    }

    // Tickets
    public List<Ticket> getTickets(){
        return tickets;
    }

    public void addTicket(Ticket ticket){
        tickets.add(ticket);
    }

    // Users
    public List<User> getUsers(){
        return users;
    }

    public void addUser(User user){
        users.add(user);
    }

}
