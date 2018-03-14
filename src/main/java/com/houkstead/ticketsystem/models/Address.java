package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@Entity
@Table(name = "ADDRESS",uniqueConstraints = @UniqueConstraint(columnNames = "address_id", name = "ADDRESS_PK_CONSTRAINT"))
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "address_id")
    private int id;             // autonumber

    @NotEmpty
    @Length(max = 40)
    @Column(name = "address1", nullable = false)
    private String address1;    // address line 1

    @Length(max = 40)
    @Column(name = "address2", nullable = true)
    private String address2;    // address line 2

    @NotEmpty
    @Length(max = 40)
    @Column(name = "city", nullable = false)
    private String city;        // city

    @NotEmpty
    @Length(max = 3)
    @Column(name = "state", nullable = false)
    private String state;       // state (consider state table or something)

    @NotNull
    @Length(min = 5, max = 5, message = "{zip.length}")
    @Pattern(regexp = "[0-9]+")
    @Column(name = "zip", nullable = false)
    private String zip;         // zip

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "company_id", foreignKey=@ForeignKey(name="FK_COMPANY_ADDRESS"))
    private Company company;

    // Constructors -----------------------------------------------------------

    public Address(){};

    public Address(String address1, String address2, String city, String state,
                   String zip, Company company){
        setAddress1(address1);
        setAddress2(address2);
        setCity(city);
        setState(state);
        setZip(zip);
        setCompany(company);
    }

    /*
    public Address(String address1, String address2, String city, String state,
                   String zip){
        setAddress1(address1);
        setAddress2(address2);
        setCity(city);
        setState(state);
        setZip(zip);
    }*/

    /*public Address(String address1, String city, String state, String zip){
        this(address1, "", city, state, zip);
    }*/

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Address 1
    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    // Address 2
    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    // City
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    // State
    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    // Zip
    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    // Company
    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

