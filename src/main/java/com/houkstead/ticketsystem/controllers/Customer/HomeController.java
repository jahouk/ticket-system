package com.houkstead.ticketsystem.controllers.Customer;

import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.Company;
import com.houkstead.ticketsystem.models.User;
import com.houkstead.ticketsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isTech;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isUserAdmin;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("Customer HomeController")
@RequestMapping("customer")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String companyView(Model model){
        // Get user info
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        // Now get their company
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        // Programatically verify that this is a tech and not tech company
        if(myCompany == null || myCompany.getId() == techCompany.getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);

        return "customer/index";
    }

}