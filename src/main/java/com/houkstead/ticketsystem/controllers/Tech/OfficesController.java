package com.houkstead.ticketsystem.controllers.Tech;

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

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isTech;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("tech offices")
@RequestMapping("tech/{companyId}/offices")
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
            @PathVariable int companyId,
            @PathVariable int officeId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Office myOffice = officeRepository.getOne(officeId);
        EditOfficeForm editOfficeForm = new EditOfficeForm(myOffice.getOffice());

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myOffice.getSite().getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("editOfficeForm", editOfficeForm);

        return "tech/offices/edit_office";
    }

    // This will display a specific site view and add office from inline form
    @RequestMapping(value="/{officeId}/edit_office", method = RequestMethod.POST)
    public String addSite(
            @ModelAttribute @Valid EditOfficeForm editOfficeForm,
            Errors errors,
            Model model,
            @PathVariable int companyId,
            @PathVariable int officeId
    ) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Office myOffice = officeRepository.getOne(officeId);

        // Programatically verify that this is a tech and not tech company
        if (!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myOffice.getSite().getCompany().getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("editOfficeForm", editOfficeForm);
            return "tech/offices/edit_office";
        } else {
            if(!editOfficeForm.getOffice().equals(myOffice.getOffice())){
                myOffice.setOffice(editOfficeForm.getOffice());
            }
            officeRepository.save(myOffice);
            companyRepository.save(myCompany);

            return "redirect:/tech/" + myCompany.getId() + "/sites/" + myOffice.getSite().getId();
        }
    }
}
