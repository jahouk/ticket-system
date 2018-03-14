package com.houkstead.ticketsystem.controllers.Tech;


import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.*;
import com.houkstead.ticketsystem.models.forms.AddTicketForm;
import com.houkstead.ticketsystem.models.forms.AddTicketUpdateForm;
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

    @Autowired
    private TicketUpdateRepository ticketUpdateRepository;

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
    public String ticketView(
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
                myCompany.getId() != myTicket.getAsset().getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }


        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("ticket", myTicket);
        model.addAttribute("techCompany", techCompany);

        return "tech/tickets/view_ticket";
    }

    @RequestMapping(value="/{ticketId}/add_update", method = RequestMethod.GET)
    public String addUpdate(
            Model model,
            @PathVariable int companyId,
            @PathVariable int ticketId) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Ticket myTicket = ticketRepository.findOne(ticketId);
        AddTicketUpdateForm addTicketUpdateForm = new AddTicketUpdateForm();
        List<Status> statuses = statusRepository.findAll();

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myTicket.getAsset().getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("ticket", myTicket);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("statuses", statuses);
        model.addAttribute("addTicketUpdateForm", addTicketUpdateForm);

        return "tech/tickets/add_ticket_update";

    }

    @RequestMapping(value="/{ticketId}/add_update", method = RequestMethod.POST)
    public String addUpdate(
            @ModelAttribute @Valid AddTicketUpdateForm addTicketUpdateForm,
            Errors errors,
            Model model,
            @PathVariable int companyId,
            @PathVariable int ticketId) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Ticket myTicket = ticketRepository.findOne(ticketId);
        List<Status> statuses = statusRepository.findAll();

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myTicket.getAsset().getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user",user);
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("ticket", myTicket);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("statuses", statuses);
            model.addAttribute("addTicketUpdateForm", addTicketUpdateForm);

            return "tech/tickets/add_tickets";
        } else {
            TicketUpdate newTicketUpdate =
                    new TicketUpdate(myTicket, user, addTicketUpdateForm);
            ticketUpdateRepository.save(newTicketUpdate);
            myTicket.setStatus(addTicketUpdateForm.getStatus());
            myTicket.addUpdate(newTicketUpdate);
            ticketRepository.save(myTicket);

            return "redirect:/tech/" + myCompany.getId() + "/tickets/" + myTicket.getId();
        }

    }
}

