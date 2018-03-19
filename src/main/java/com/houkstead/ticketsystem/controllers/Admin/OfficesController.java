package com.houkstead.ticketsystem.controllers.Admin;

import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.Company;
import com.houkstead.ticketsystem.models.Office;
import com.houkstead.ticketsystem.models.User;
import com.houkstead.ticketsystem.models.forms.EditOfficeForm;
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

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.*;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("admin offices")
@RequestMapping("admin/offices")
public class OfficesController {

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OfficeRepository officeRepository;

    // Edit Site
    @RequestMapping(value="/{officeId}/edit_office", method = RequestMethod.GET)
    public String editSite(
            Model model,
            @PathVariable int officeId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        Office myOffice = officeRepository.getOne(officeId);
        EditOfficeForm editOfficeForm = new EditOfficeForm(myOffice.getOffice());

        // Programatically verify that this is a user admin
        if(!isAdmin(user, roleRepository)) {
            return "redirect:/admin";
        }

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("editOfficeForm", editOfficeForm);

        return "admin/offices/edit_office";
    }

    // This will display a specific site view and add office from inline form
    @RequestMapping(value="/{officeId}/edit_office", method = RequestMethod.POST)
    public String addSite(
            @ModelAttribute @Valid EditOfficeForm editOfficeForm,
            Errors errors,
            Model model,
            @PathVariable int officeId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        Office myOffice = officeRepository.getOne(officeId);

        // Programatically verify that this is a user admin
        if(!isAdmin(user, roleRepository)) {
            return "redirect:/admin";
        } else if (errors.hasErrors()) {
            model.addAttribute("user",user);
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("editOfficeForm", editOfficeForm);
            return "admin/offices/edit_office";
        } else {
            if(!editOfficeForm.getOffice().equals(myOffice.getOffice())){
                myOffice.setOffice(editOfficeForm.getOffice());
            }
            officeRepository.save(myOffice);
            companyRepository.save(myCompany);

            return "redirect:/admin/sites/" + myOffice.getSite().getId();
        }
    }
}
