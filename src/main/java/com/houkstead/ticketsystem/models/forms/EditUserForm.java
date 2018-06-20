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
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotEmpty;

public class EditUserForm {

        //-------------------------------------------------------------------------
        // USER FIELDS --> Email address is used for username to avoid name collisions
        //@NotEmpty(message = "*Username is required")
        //private String username;            // system username


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

        public EditUserForm(){}

        public EditUserForm(String fname, String lname, String title, String email,
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

        public EditUserForm(User user){
            setFname(user.getUserInfo().getFname());
            setLname(user.getUserInfo().getLname());
            setTitle(user.getUserInfo().getTitle());
            setEmail(user.getUserInfo().getEmail());
            setCompanyUsername(user.getUserInfo().getCompanyUserName());
            setOffice(user.getUserInfo().getOffice().getOffice());
            setUserPhone(user.getUserInfo().getPhone());
            setCellPhone(user.getUserInfo().getCellphone());
            setCanText(user.getUserInfo().getCanText());
        }

        //-------------------------------------------------------------------------
        // Getters and setters (bulk created)

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
