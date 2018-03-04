package com.houkstead.ticketsystem.controllers.Tech;

import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.Company;
import com.houkstead.ticketsystem.models.TechCompany;
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

import java.util.List;

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isTech;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("Tech HomeController")
@RequestMapping("tech")
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

    // This will display a list of companies
    @RequestMapping(value="", method = RequestMethod.GET)
    public String index(Model model){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        // Programatically verify that this is a tech
        if(!isTech(user, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("companies", companyRepository.findAll());
        model.addAttribute("tickets", ticketRepository.findByStatusNot(
                statusRepository.findByStatus("Closed by Customer")));
        model.addAttribute("techCompany", techCompany);
        return "tech/index";
    }

    // This will display a company view
    // NOTE: Add / Edit company is only allowed at an Admin level,
    // so is in Admin Controller
    @RequestMapping(value="/{companyId}", method = RequestMethod.GET)
    public String companyView(Model model, @PathVariable int companyId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) || myCompany == null || myCompany.getId() == techCompany.getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);

        return "tech/view_company";
    }

}