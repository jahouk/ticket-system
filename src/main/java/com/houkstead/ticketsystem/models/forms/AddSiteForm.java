/*
 * MIT License
 *
 * Copyright (c) 2018 Jason Houk
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.houkstead.ticketsystem.models.forms;

import com.houkstead.ticketsystem.models.User;
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

    private User siteContact; // Site User from list

    //-------------------------------------------------------------------------
    // Constructors

    public AddSiteForm() {
    }

    public AddSiteForm(String streetAddress1,
                       String streetAddress2,
                       String streetAddressCity,
                       String streetAddressState,
                       String streetAddressZip,
                       String site,
                       String companyPhone,
                       String fax,
                       User siteContact) {
        setStreetAddress1(streetAddress1);
        setStreetAddress2(streetAddress2);
        setStreetAddressCity(streetAddressCity);
        setStreetAddressState(streetAddressState);
        setStreetAddressZip(streetAddressZip);
        setSite(site);
        setCompanyPhone(companyPhone);
        setFax(fax);
        setSiteContact(siteContact);
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

    public User getSiteContact() {
        return siteContact;
    }

    public void setSiteContact(User siteContact) {
        this.siteContact = siteContact;
    }
}
