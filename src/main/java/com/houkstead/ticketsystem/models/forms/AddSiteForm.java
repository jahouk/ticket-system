package com.houkstead.ticketsystem.models.forms;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class AddSiteForm {

    //-------------------------------------------------------------------------
    // Street ADDRESS FIELDS
    @NotEmpty(message="*Street Address is required")
    @Length(max = 40)
    private String streetAddress1;    // address line 1

    @Length(max = 40)
    private String streetAddress2;    // address line 2

    @NotEmpty(message="*City is required")
    @Length(max = 40)
    private String streetAddressCity;        // city

    @NotEmpty(message="*State is required")
    @Length(max = 3)
    private String streetAddressState;       // state (consider state table or something)

    @NotEmpty(message="*Zip code is required")
    @Length(min = 5, max = 5, message = "{zip.length}")
    private String streetAddressZip;         // zip


    //-------------------------------------------------------------------------
    // SITE FIELDS
    @NotEmpty(message = "*SitesController must have a name")
    private String site;                // site name

    @NotEmpty(message = "*Main Company Phone Number Required")
    private String companyPhone;       // site (here, also company main) phone number

    private String fax;         // site fax number

    //-------------------------------------------------------------------------
    // Constructors

    public AddSiteForm() {
    }

    //-------------------------------------------------------------------------
    // Getter / Setters (Auto created)

    public String getStreetAddress1() {
        return streetAddress1;
    }

    public void setStreetAddress1(String streetAddress1) {
        this.streetAddress1 = streetAddress1;
    }

    public String getStreetAddress2() {
        return streetAddress2;
    }

    public void setStreetAddress2(String streetAddress2) {
        this.streetAddress2 = streetAddress2;
    }

    public String getStreetAddressCity() {
        return streetAddressCity;
    }

    public void setStreetAddressCity(String streetAddressCity) {
        this.streetAddressCity = streetAddressCity;
    }

    public String getStreetAddressState() {
        return streetAddressState;
    }

    public void setStreetAddressState(String streetAddressState) {
        this.streetAddressState = streetAddressState;
    }

    public String getStreetAddressZip() {
        return streetAddressZip;
    }

    public void setStreetAddressZip(String streetAddressZip) {
        this.streetAddressZip = streetAddressZip;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }
}
