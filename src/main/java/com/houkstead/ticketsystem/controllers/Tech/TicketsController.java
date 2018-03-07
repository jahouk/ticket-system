package com.houkstead.ticketsystem.controllers.Tech;


import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.Company;
import com.houkstead.ticketsystem.models.TechCompany;
import com.houkstead.ticketsystem.models.Ticket;
import com.houkstead.ticketsystem.models.User;
import com.houkstead.ticketsystem.models.forms.AddTicketForm;
import com.houkstead.ticketsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isTech;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("tech tickets")
@RequestMapping("tech/{companyId}/tickets")
public class TicketsController {

    @Autowired
    private UserService userService;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String index(Model model, @PathVariable int companyId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        // Programatically verify that this is a tech
        if(!isTech(user, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        return "tech/tickets/index";
    }

    // Add ticket
    @RequestMapping(value="/add_ticket", method = RequestMethod.GET)
    public String addTicket(
            Model model,
            @PathVariable int companyId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        AddTicketForm addTicketForm = new AddTicketForm();

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId()) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("addTicketForm", addTicketForm);
        model.addAttribute("techCompany", techCompany);

        return "tech/tickets/add_ticket";
    }

    // This will display a specific site view and add offie from inline form
    @RequestMapping(value="/add_ticket", method = RequestMethod.POST)
    public String addTicket(
            @ModelAttribute @Valid AddTicketForm addTicketForm,
            Errors errors,
            Model model,
            @PathVariable int companyId
    ) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        // Programatically verify that this is a tech and not tech company
        if (!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("addTicketForm", addTicketForm);
            model.addAttribute("techCompany", techCompany);

            return "tech/tickets/add_tickets";
        } else {
            Ticket newTicket = new Ticket(addTicketForm, statusRepository.findByStatus("Open by Customer"));
            ticketRepository.save(newTicket);
            myCompany.addTicket(newTicket);
            companyRepository.save(myCompany);

            return "redirect:/tech/" + myCompany.getId() + "/tickets/" + newTicket.getId();
        }
    }

    // This will display a specific site view and add offie from inline form
    @RequestMapping(value="/{ticketId}", method = RequestMethod.GET)
    public String siteView(
            Model model,
            @PathVariable int companyId,
            @PathVariable int ticketId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Ticket myTicket = ticketRepository.findOne(ticketId);

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myTicket.getComputer().getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }


        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("ticket", myTicket);
        model.addAttribute("techCompany", techCompany);

        return "tech/tickets/view_ticket";
    }

}

