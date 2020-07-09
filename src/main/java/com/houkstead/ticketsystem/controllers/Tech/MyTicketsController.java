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

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isTech;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("tech mytickets")
@RequestMapping("tech/mytickets")
public class MyTicketsController {
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
    public String index(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();


        // Programatically verify that this is a user
        if(!isTech(myUser, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        return "tech/mytickets/index";
    }

    // Add ticket
    @RequestMapping(value="/add_ticket", method = RequestMethod.GET)
    public String addTicket(
            Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        AddTicketForm addTicketForm = new AddTicketForm();

        // Programatically verify that this is a user
        if(!isTech(myUser, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("addTicketForm", addTicketForm);

        return "tech/mytickets/add_ticket";
    }

    // This will display a specific site view and add offie from inline form
    @RequestMapping(value="/add_ticket", method = RequestMethod.POST)
    public String addTicket(
            @ModelAttribute @Valid AddTicketForm addTicketForm,
            Errors errors,
            Model model
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        // Programatically verify that this is a user
        if(!isTech(myUser, roleRepository)) {
            return "redirect:/";
        }else if (errors.hasErrors()) {
            model.addAttribute("user",myUser);
            model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("addTicketForm", addTicketForm);
            return "tech/mytickets/add_ticket";
        } else {
            Ticket newTicket = new Ticket(addTicketForm, statusRepository.findByStatus("Open by Customer"));
            ticketRepository.save(newTicket);
            myCompany.addTicket(newTicket);
            companyRepository.save(myCompany);

            return "redirect:/tech/mytickets/" + newTicket.getId();
        }
    }

    // This will display a specific site view and add offie from inline form
    @RequestMapping(value="/{ticketId}", method = RequestMethod.GET)
    public String ticketView(
            Model model,
            @PathVariable int ticketId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        Ticket myTicket = ticketRepository.findById(ticketId).get();

        // Programatically verify that this is a user
        if(!isTech(myUser, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("ticket", myTicket);

        return "tech/mytickets/view_ticket";
    }

    @RequestMapping(value="/{ticketId}/add_update", method = RequestMethod.GET)
    public String addUpdate(
            Model model,
            @PathVariable int ticketId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        Ticket myTicket = ticketRepository.findById(ticketId).get();
        AddTicketUpdateForm addTicketUpdateForm = new AddTicketUpdateForm();
        List<Status> statuses = statusRepository.findAll();

        // Programatically verify that this is a user
        if(!isTech(myUser, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("statuses", statuses);
        model.addAttribute("ticket", myTicket);
        model.addAttribute("addTicketUpdateForm", addTicketUpdateForm);

        return "tech/mytickets/add_ticket_update";
    }

    @RequestMapping(value="/{ticketId}/add_update", method = RequestMethod.POST)
    public String addUpdate(
            @ModelAttribute @Valid AddTicketUpdateForm addTicketUpdateForm,
            Errors errors,
            Model model,
            @PathVariable int ticketId) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        Ticket myTicket = ticketRepository.findById(ticketId).get();
        List<Status> statuses = statusRepository.findAll();


        // Programatically verify that this is a user
        if(!isTech(myUser, roleRepository)) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user",myUser);
            model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("statuses", statuses);
            model.addAttribute("ticket", myTicket);
            model.addAttribute("addTicketUpdateForm", addTicketUpdateForm);

            return "tech/mytickets/add_ticket_update";
        } else {
            TicketUpdate newTicketUpdate =
                    new TicketUpdate(myTicket, myUser, addTicketUpdateForm);
            ticketUpdateRepository.save(newTicketUpdate);
            myTicket.setStatus(addTicketUpdateForm.getStatus());
            myTicket.addUpdate(newTicketUpdate);
            ticketRepository.save(myTicket);

            return "redirect:/tech/mytickets/" + myTicket.getId();
        }
    }
}
