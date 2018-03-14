package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "SITE",uniqueConstraints = @UniqueConstraint(columnNames = "site_id", name = "SITE_PK_CONSTRAINT"))
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "site_id")
    private int id;             // autonumber

    @Column(name = "site")
    @NotNull
    private String site;

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="address_id", foreignKey=@ForeignKey(name="FK_ADDRESS_SITE"))
    private Address address;    // street address

    @Column(name = "phone")
    @NotEmpty(message = "*SitesController Phone Number Required")
    @NotNull
    private String phone;       // site phone number

    @Column(name = "fax")
    private String fax;         // site fax number

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="user_id", foreignKey=@ForeignKey(name="FK_USER_SITE"))
    private User siteContact;   // site Main Contact

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="company_id", foreignKey=@ForeignKey(name="FK_COMPANY_SITE"))
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval=true)
    private List<Office> offices = new ArrayList<Office>();

    // Constructors -----------------------------------------------------------

    public Site(){}

    public Site(String site, Company company, Address address, String phone,
                String fax, User siteContact){
        setSite(site);
        setCompany(company);
        setAddress(address);
        setPhone(phone);
        setFax(fax);
        setSiteContact(siteContact);
    }


    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Company
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    // Address
    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    // Phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Fax
    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    // SiteContact
    public User getSiteContact() {
        return siteContact;
    }

    public void setSiteContact(User siteContact) {
        this.siteContact = siteContact;
    }

    // SitesController (name)
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    // SitesController Offices
    public List<Office> getOffices(){return offices;}

    public void addOffice(Office office){
        offices.add(office);
    }


}

