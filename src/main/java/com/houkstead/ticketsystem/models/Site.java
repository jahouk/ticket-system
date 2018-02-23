package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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

    @ManyToOne
    @JoinColumn(foreignKey=@ForeignKey(name="FK_ADDRESS_SITE"))
    private Address address;    // street address

    @Column(name = "phone")
    @NotEmpty(message = "*Site Phone Number Required")
    @NotNull
    private String phone;       // site phone number

    @Column(name = "fax")
    private String fax;         // site fax number

    @ManyToOne
    @JoinColumn(foreignKey=@ForeignKey(name="FK_USER_SITE"))
    private User siteContact;   // site Main Contact

    @ManyToOne
    private Company company;

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

    // Site (name)
    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}

