package com.houkstead.ticketsystem.models.forms;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class AddUserForm {
    //-------------------------------------------------------------------------
    // USER FIELDS --> Email address is used for username to avoid name collisions
    //@NotEmpty(message = "*Username is required")
    //private String username;            // system username

    @Length(min = 5, message = "*Your password must have at least 5 characters")
    @NotEmpty(message = "*Please provide your password")
    private String password;            // Password

    //-------------------------------------------------------------------------
    // OFFICE FIELDS
    @NotEmpty(message="*Office Location Description/Name is required")
    private String office;    // Physical Location of Office by Description or Room Number

    //-------------------------------------------------------------------------
    // USER_INFO FIELDS
    private String companyUsername;     // User's Login Name at their company /


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

    public AddUserForm(){}

    public AddUserForm(String fname, String lname, String title, String email,
                        String companyUsername, String office,
                       String userPhone, String cellPhone, Boolean canText){
        setFname(fname);
        setLname(lname);
        setTitle(title);
        setEmail(email);
        setCompanyUsername(companyUsername);
        setOffice(office);
        setUserPhone(userPhone);
        setCellPhone(cellPhone);
        setCanText(canText);
    }

    //-------------------------------------------------------------------------
    // Getters and setters (bulk created)

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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
}
