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

package com.houkstead.ticketsystem.controllers;

import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.Company;
import com.houkstead.ticketsystem.models.Office;
import com.houkstead.ticketsystem.models.User;
import com.houkstead.ticketsystem.models.forms.EditUserForm;
import com.houkstead.ticketsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.*;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("profile")
@RequestMapping("profile")
public class ProfileController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OfficeRepository officeRepository;

    // User Profile View
    @RequestMapping(value="", method = RequestMethod.GET)
    public String index(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        model.addAttribute("user",myUser);
        model.addAttribute("isTech",isTech(myUser, roleRepository));
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);

        return "profile/index";
    }


    // User Profile Edit
    @RequestMapping(value="/edit", method = RequestMethod.GET)
    public String editProfile(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        EditUserForm editUserForm = new EditUserForm(myUser);

        model.addAttribute("user",myUser);
        model.addAttribute("isTech",isTech(myUser, roleRepository));
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("editUserForm", editUserForm);

        return "profile/edit";
    }



    // This will edit a user
    @RequestMapping(value="/edit", method = RequestMethod.POST)
    public String editProfile(@ModelAttribute @Valid EditUserForm editUserForm,
                          Errors errors,
                          Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        // Programatically verify that this is a tech and not tech company
        if (errors.hasErrors()) {
            model.addAttribute("user",myUser);
            model.addAttribute("isTech",isTech(myUser, roleRepository));
            model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
            model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("editUserForm", editUserForm);

            return "profile/edit";
        } else {
            if(!editUserForm.getEmail().equals(myUser.getUserInfo().getEmail())){
                myUser.getUserInfo().setEmail(editUserForm.getEmail());
            }

            if(!editUserForm.getFname().equals(myUser.getUserInfo().getFname())){
                myUser.getUserInfo().setFname(editUserForm.getFname());
            }

            if(!editUserForm.getLname().equals(myUser.getUserInfo().getLname())){
                myUser.getUserInfo().setLname(editUserForm.getLname());
            }

            if(!editUserForm.getTitle().equals(myUser.getUserInfo().getTitle())){
                myUser.getUserInfo().setTitle(editUserForm.getTitle());
            }

            if(!editUserForm.getCompanyUsername().equals(myUser.getUserInfo().getCompanyUserName())){
                myUser.getUserInfo().setTitle(editUserForm.getCompanyUsername());
            }

            if(!editUserForm.getOffice().equals(myUser.getUserInfo().getOffice().getOffice())){
                // TODO - impliment way to add office if doesn't exist.
            }

            if(!editUserForm.getUserPhone().equals(myUser.getUserInfo().getPhone())){
                myUser.getUserInfo().setPhone(editUserForm.getUserPhone());
            }

            if(!editUserForm.getCellPhone().equals(myUser.getUserInfo().getCellphone())){
                myUser.getUserInfo().setCellphone(editUserForm.getCellPhone());
            }

            if(!editUserForm.getCanText().equals(myUser.getUserInfo().getCanText())){
                myUser.getUserInfo().setCanText(editUserForm.getCanText());
            }

            if(isUser(myUser,roleRepository) &&
                    !myUser.getUsername().equals(myUser.getUserInfo().getEmail())){
                myUser.setUsername(myUser.getUserInfo().getEmail());
            }else if(isTech(myUser,roleRepository) &&
                    !myUser.getUsername().equals(myUser.getUserInfo().getCompanyUserName())){
                myUser.setUsername(myUser.getUserInfo().getCompanyUserName());
            }

            userService.updateUser(myUser);

            return "redirect:/profile";
        }
    }

    // Change the password
    @RequestMapping(value="/change_password", method = RequestMethod.GET)
    public String changePassword(
            Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();


        model.addAttribute("user",myUser);
        model.addAttribute("isTech",isTech(myUser, roleRepository));
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);

        return "profile/change_password";
    }

    // This will edit a user
    @RequestMapping(value="/change_password", method = RequestMethod.POST)
    public String changePassword(Model model,
                                 @RequestParam String password,
                                 @RequestParam String confirmPassword){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        if(password!=null && !(password.isEmpty()) &&
                confirmPassword!=null && !(confirmPassword.isEmpty()) &&
                password.equals(confirmPassword)
                ){
                userService.updateUser(user, password);
                return "redirect:/profile";
        }else{
            model.addAttribute("errors", "Passwords must match");
            model.addAttribute("user",user);
            model.addAttribute("isTech",isTech(user, roleRepository));
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);

            return "profile/change_password";
        }
    }
}
