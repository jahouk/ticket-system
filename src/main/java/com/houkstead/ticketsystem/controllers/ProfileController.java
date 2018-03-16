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
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        model.addAttribute("user",user);
        model.addAttribute("isTech",isTech(user, roleRepository));
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);

        return "profile/index";
    }


    // User Profile Edit
    @RequestMapping(value="/edit", method = RequestMethod.GET)
    public String editProfile(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        EditUserForm editUserForm = new EditUserForm(user);

        model.addAttribute("user",user);
        model.addAttribute("isTech",isTech(user, roleRepository));
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
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
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        // Programatically verify that this is a tech and not tech company
        if (errors.hasErrors()) {
            model.addAttribute("user",user);
            model.addAttribute("isTech",isTech(user, roleRepository));
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("editUserForm", editUserForm);

            return "profile/edit";
        } else {
            if(!editUserForm.getEmail().equals(user.getUserInfo().getEmail())){
                user.getUserInfo().setEmail(editUserForm.getEmail());
            }

            if(!editUserForm.getFname().equals(user.getUserInfo().getFname())){
                user.getUserInfo().setFname(editUserForm.getFname());
            }

            if(!editUserForm.getLname().equals(user.getUserInfo().getLname())){
                user.getUserInfo().setLname(editUserForm.getLname());
            }

            if(!editUserForm.getTitle().equals(user.getUserInfo().getTitle())){
                user.getUserInfo().setTitle(editUserForm.getTitle());
            }

            if(!editUserForm.getCompanyUsername().equals(user.getUserInfo().getCompanyUserName())){
                user.getUserInfo().setTitle(editUserForm.getCompanyUsername());
            }

            if(!editUserForm.getOffice().equals(user.getUserInfo().getOffice().getOffice())){
                // TODO - impliment way to add office if doesn't exist.
            }

            if(!editUserForm.getUserPhone().equals(user.getUserInfo().getPhone())){
                user.getUserInfo().setPhone(editUserForm.getUserPhone());
            }

            if(!editUserForm.getCellPhone().equals(user.getUserInfo().getCellphone())){
                user.getUserInfo().setCellphone(editUserForm.getCellPhone());
            }

            if(!editUserForm.getCanText().equals(user.getUserInfo().getCanText())){
                user.getUserInfo().setCanText(editUserForm.getCanText());
            }

            if(isUser(user,roleRepository) &&
                    !user.getUsername().equals(user.getUserInfo().getEmail())){
                user.setUsername(user.getUserInfo().getEmail());
            }

            userService.updateUser(user);

            return "redirect:/profile";
        }
    }

    // Change the password
    @RequestMapping(value="/change_password", method = RequestMethod.GET)
    public String changePassword(
            Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();


        model.addAttribute("user",user);
        model.addAttribute("isTech",isTech(user, roleRepository));
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
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
