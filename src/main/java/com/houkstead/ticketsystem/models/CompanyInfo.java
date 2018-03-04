package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "COMPANY_INFO",uniqueConstraints = @UniqueConstraint(columnNames = "company_info_id", name = "COMPANY_INFO_PK_CONSTRAINT"))
public class CompanyInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "company_info_id")
    private int id;                         // autonumber

    @OneToOne(cascade = CascadeType.ALL)
    private Company company;                // Company

    @NotEmpty(message="*Company Name is required")
    @NotNull
    private String name;                 //  name

    @Column(name = "website")
    private String website;                 // Website

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="address_id", foreignKey=@ForeignKey(name="FK_ADDRESS_COMPANY_INFO"))
    private Address billingAddress;         // billing address

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="site_id", foreignKey=@ForeignKey(name="FK_SITE_COMPANY_INFO"))
    private Site primarySite;               // primary site for companye


    // Constructors -----------------------------------------------------------

    public CompanyInfo(){}

    public CompanyInfo(
            String name,
            Company company,
            Site primarySite,
            Address billingAddress,
            String website
            ){
        setName(name);
        setCompany(company);
        setPrimarySite(primarySite);
        setBillingAddress(billingAddress);
        setWebsite(website);
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public Address getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(Address billingAddress) {
        this.billingAddress = billingAddress;
    }

    public Site getPrimarySite() {
        return primarySite;
    }

    public void setPrimarySite(Site primarySite) {
        this.primarySite = primarySite;
    }
}
