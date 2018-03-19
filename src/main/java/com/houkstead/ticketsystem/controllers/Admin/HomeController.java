package com.houkstead.ticketsystem.controllers.Admin;

import com.fasterxml.jackson.databind.annotation.JsonAppend;
import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.Company;
import com.houkstead.ticketsystem.models.User;
import com.houkstead.ticketsystem.repository.CompanyRepository;
import com.houkstead.ticketsystem.repository.RoleRepository;
import com.houkstead.ticketsystem.repository.TechCompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("Admin HomeController")
@RequestMapping("admin")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    // List all techs (users for company 1)
    @RequestMapping(value="", method = RequestMethod.GET)
    public String index(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        // Programatically verify that this is a user admin
        if(!isAdmin(user, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);

        return "admin/home/index";
    }
}
