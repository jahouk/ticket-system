package com.houkstead.ticketsystem.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;


@Entity
@Table(name = "USER_INFO",uniqueConstraints = @UniqueConstraint(columnNames = "user_info_id", name = "USER_INFO_PK_CONSTRAINT"))
public class UserInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_info_id")
    private int id;                     // autonumber

    @OneToOne
    @JoinColumn(foreignKey=@ForeignKey(name="FK_USER_USER_INFO"))
    private User userId;                // Link to the users

    @Column(name="company_user_name")
    private String companyUserName;     // User's Login Name at their company /
    // company computer

    @Column(name = "fname")
    @NotEmpty(message = "*First name is required")
    @NotNull
    private String fname;               //  first name

    @Column(name = "lname")
    @NotEmpty(message = "*Last name is required")
    @NotNull
    private String lname;               // last name

    @Column(name = "email")
    @Email(message = "*Please provide a valid Email")
    @NotEmpty(message = "*Please provide an email")
    @NotNull
    private String email;               // email address

    @ManyToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name="office_id", foreignKey=@ForeignKey(name="FK_OFFICE_USER_INFO"))
    private Office office;              // Office Location, which also then has
    // company/site info

    @Column(name = "phone")
    @NotEmpty(message = "*Work phone number is required")
    @NotNull
    private String phone;               // work phone number

    @Column(name = "cell_phone")
    private String cellphone;           // cell phone number

    @Column(name = "can_text")
    private Boolean canText;            // ok to text

    @Column(name = "title")
    @NotNull
    private String title;

    // Constructors -----------------------------------------------------------
    public UserInfo(){}

    public UserInfo(User user, String fname, String lname, String title, String email,
                    String phone, String companyUserName, String cellphone, Boolean canText, Office office ){
        setUserId(user);
        setFname(fname);
        setLname(lname);
        setTitle(title);
        setEmail(email);
        setPhone(phone);
        setCompanyUserName(companyUserName);
        setCellphone(cellphone);
        setCanText(canText);
        setOffice(office);
    }

    // Start of Getters and Setters -------------------------------------------
    // ID
    public int getId() {
        return id;
    }

    // Company UserName
    public String getCompanyUserName() {
        return companyUserName;
    }

    public void setCompanyUserName(String companyUserName) {
        this.companyUserName = companyUserName;
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

    public Office getOffice() {
        return office;
    }

    public void setOffice(Office office) {
        this.office = office;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCellphone() {
        return cellphone;
    }

    public void setCellphone(String cellphone) {
        this.cellphone = cellphone;
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

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public User getUserId() {
        return userId;
    }
}

