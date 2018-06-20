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

package com.houkstead.ticketsystem.controllers.Customer;

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

@Controller("customer offices")
@RequestMapping("customer/offices")
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
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        Office myOffice = officeRepository.getOne(officeId);
        EditOfficeForm editOfficeForm = new EditOfficeForm(myOffice.getOffice());

        // Programatically verify that this is a user admin
        if(!isUserAdmin(myUser, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("editOfficeForm", editOfficeForm);

        return "customer/offices/edit_office";
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
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        Office myOffice = officeRepository.getOne(officeId);

        // Programatically verify that this is a user admin
        if(!isUserAdmin(myUser, roleRepository)) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user",myUser);
            model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("editOfficeForm", editOfficeForm);
            return "customer/offices/edit_office";
        } else {
            if(!editOfficeForm.getOffice().equals(myOffice.getOffice())){
                myOffice.setOffice(editOfficeForm.getOffice());
            }
            officeRepository.save(myOffice);
            companyRepository.save(myCompany);

            return "redirect:/customer/sites/" + myOffice.getSite().getId();
        }
    }
}
