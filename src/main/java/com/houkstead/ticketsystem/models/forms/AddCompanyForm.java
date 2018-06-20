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

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class AddCompanyForm {

    /*
    Order of creating entries
           Add Tech Company (just relationships)
             - Add address
             - Add admin users
             - Add site
             - Add company_info
             - Add office
             - Add user_info
     */


    //-------------------------------------------------------------------------
    // COMPANY_INFO FIELDS
    @NotEmpty(message="*Company Name is required")
    private String companyName;                // Company name

    private String website;                // url for company

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
    // Billing ADDRESS FIELDS
    @NotEmpty(message="*Billing Address is required")
    @Length(max = 40)
    private String billingAddress1;    // address line 1

    @Length(max = 40)
    private String billingAddress2;    // address line 2

    @NotEmpty(message="*City is required")
    @Length(max = 40)
    private String billingAddressCity;        // city

    @NotEmpty(message="*State is required")
    @Length(max = 3)
    private String billingAddressState;       // state (consider state table or something)

    @NotEmpty(message="*Zip code is required")
    @Length(min = 5, max = 5, message = "{zip.length}")
    private String billingAddressZip;         // zip


    //-------------------------------------------------------------------------
    // USER FIELDS
   //@NotEmpty(message = "*Username is required")
   // private String username;            // system username

    /* NOTE!!!
        Username is removed since it is redundant

        For techs, the username is the company username

        For customers, the username is their email address

        Not Empty Validation is now moved to Company Username

     */

    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;            // Password

    //-------------------------------------------------------------------------
    // SITE FIELDS
    @NotEmpty(message = "*SitesController must have a name")
    private String site;                // site name

    @NotEmpty(message = "*Main Company Phone Number Required")
    private String companyPhone;       // site (here, also company main) phone number

    private String fax;         // site fax number

    //-------------------------------------------------------------------------
    // OFFICE FIELDS
    @NotEmpty(message="*Office Location Description/Name is required")
    private String office;    // Physical Location of Office by Description or Room Number

    //-------------------------------------------------------------------------
    // USER_INFO FIELDS
    @NotEmpty(message = "*Company Username is required")
    private String companyUsername;     // User's Login Name at their company /
                                        // company computer

    @NotEmpty(message = "*First name is required")
    private String fname;               //  first name

    @NotEmpty(message = "*Last name is required")
    private String lname;               // last name

    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    private String email;               // email address

    @NotEmpty(message = "*Work phone number is required")
    private String userPhone;               // work phone number

    private String cellPhone;           // cell phone number

    private Boolean canText;            // ok to text

    @NotEmpty(message = "*Please provide a title")
    private String title;

    //-------------------------------------------------------------------------
    // Constructors

    public AddCompanyForm(){}

    //-------------------------------------------------------------------------
    // Getters and setters (bulk created)


    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

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

    public String getBillingAddress1() {
        return billingAddress1;
    }

    public void setBillingAddress1(String billingAddress1) {
        this.billingAddress1 = billingAddress1;
    }

    public String getBillingAddress2() {
        return billingAddress2;
    }

    public void setBillingAddress2(String billingAddress2) {
        this.billingAddress2 = billingAddress2;
    }

    public String getBillingAddressCity() {
        return billingAddressCity;
    }

    public void setBillingAddressCity(String billingAddressCity) {
        this.billingAddressCity = billingAddressCity;
    }

    public String getBillingAddressState() {
        return billingAddressState;
    }

    public void setBillingAddressState(String billingAddressState) {
        this.billingAddressState = billingAddressState;
    }

    public String getBillingAddressZip() {
        return billingAddressZip;
    }

    public void setBillingAddressZip(String billingAddressZip) {
        this.billingAddressZip = billingAddressZip;
    }



    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getOffice() {
        return office;
    }

    public void setOffice(String office) {
        this.office = office;
    }

    public String getCompanyUsername() {
        return companyUsername;
    }

    public void setCompanyUsername(String companyUsername) {
        this.companyUsername = companyUsername;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getCellPhone() {
        return cellPhone;
    }

    public void setCellPhone(String cellPhone) {
        this.cellPhone = cellPhone;
    }

    public Boolean getCanText() {
        return canText;
    }

    public void setCanText(Boolean canText) {
        this.canText = canText;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }
}
