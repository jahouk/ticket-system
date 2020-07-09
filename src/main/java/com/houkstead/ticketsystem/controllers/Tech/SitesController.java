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
import com.houkstead.ticketsystem.models.forms.AddSiteForm;
import com.houkstead.ticketsystem.models.test_models.TestAddress;
import com.houkstead.ticketsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isTech;
import static com.houkstead.ticketsystem.utilities.SetupUtilities.*;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("tech sites")
@RequestMapping("tech/{companyId}/sites")
public class SitesController {

    @Autowired
    private  AddressRepository addressRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String index(Model model, @PathVariable int companyId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findById(companyId).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        // Programatically verify that this is a tech and not tech company
        if(!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ) {
            return "redirect:/";
        }
        model.addAttribute("user",myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        return "tech/sites/index";
    }

    // This will display a specific site view
    @RequestMapping(value="/{mySiteId}", method = RequestMethod.GET)
    public String siteView(
            Model model,
            @PathVariable int companyId,
            @PathVariable int mySiteId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findById(companyId).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());
        Site mySite = siteRepository.findById(mySiteId).get();

        // Programatically verify that this is a tech and not tech company
        if(!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != mySite.getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("mySite", mySite);
        model.addAttribute("techCompany", techCompany);

        return "tech/sites/view_site";
    }

    // This will display a specific site view and add offie from inline form
    @RequestMapping(value="/{mySiteId}", method = RequestMethod.POST)
    public String siteView(
            Model model,
            @PathVariable int companyId,
            @PathVariable int mySiteId,
            @RequestParam String office_name){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findById(companyId).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());
        Site mySite = siteRepository.findById(mySiteId).get();

        // Programatically verify that this is a tech and not tech company
        if(!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != mySite.getCompany().getId()) {
            return "redirect:/";
        }

        Office newOffice = createOffice(
                new Office(mySite, office_name),
                officeRepository
        );

        mySite.addOffice(newOffice);
        siteRepository.save(mySite);

        model.addAttribute("user",myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("mySite", mySite);
        model.addAttribute("techCompany", techCompany);

        return "tech/sites/view_site";
    }

    // Add Site
    @RequestMapping(value="/add_site", method = RequestMethod.GET)
    public String addSite(
            Model model,
            @PathVariable int companyId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findById(companyId).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());
        AddSiteForm addSiteForm = new AddSiteForm();

        // Programatically verify that this is a tech and not tech company
        if(!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId()) {
            return "redirect:/";
        }

        model.addAttribute("user", myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("addSiteForm", addSiteForm);
        model.addAttribute("techCompany", techCompany);

        return "tech/sites/add_site";
    }

    // This will display a specific site view and add offie from inline form
    @RequestMapping(value="/add_site", method = RequestMethod.POST)
    public String addSite(
            @ModelAttribute @Valid AddSiteForm addSiteForm,
            Errors errors,
            Model model,
            @PathVariable int companyId
            ) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findById(companyId).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        // Programatically verify that this is a tech and not tech company
        if (!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user", myUser);
            model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("addSiteForm", addSiteForm);
            model.addAttribute("techCompany", techCompany);

            return "tech/sites/add_site";
        } else {
            User newSiteContact = userRepository.findById(addSiteForm.getSiteContact().getId()).get();
            Address newSiteAddress = duplicateAddressCheck( new TestAddress(
                            addSiteForm.getStreetAddress1(),
                            addSiteForm.getStreetAddress2(),
                            addSiteForm.getStreetAddressCity(),
                            addSiteForm.getStreetAddressState(),
                            addSiteForm.getStreetAddressZip()),
                    myCompany,
                    addressRepository,
                    companyRepository
            );
            Site newSite = createSite(new Site(
                            addSiteForm.getSite(),
                            myCompany,
                            newSiteAddress,
                            addSiteForm.getCompanyPhone(),
                            addSiteForm.getFax(),
                            newSiteContact),
                    siteRepository
            );



            myCompany.addSite(newSite);
            companyRepository.save(myCompany);

            return "redirect:/tech/" + myCompany.getId() + "/sites";
        }
    }



    // Edit Site
    @RequestMapping(value="/{siteId}/edit_site", method = RequestMethod.GET)
    public String editSite(
            Model model,
            @PathVariable int companyId,
            @PathVariable int siteId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findById(companyId).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());
        Site mySite = siteRepository.findById(siteId).get();
        AddSiteForm addSiteForm = new AddSiteForm(
                mySite.getAddress().getAddress1(),
                mySite.getAddress().getAddress2(),
                mySite.getAddress().getCity(),
                mySite.getAddress().getState(),
                mySite.getAddress().getZip(),
                mySite.getSite(),
                mySite.getPhone(),
                mySite.getFax(),
                mySite.getSiteContact());

        // Programatically verify that this is a tech and not tech company
        if(!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != mySite.getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user", myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("addSiteForm", addSiteForm);
        model.addAttribute("techCompany", techCompany);

        return "tech/sites/edit_site";
    }

    // This will display a specific site view and add office from inline form
    @RequestMapping(value="/{siteId}/edit_site", method = RequestMethod.POST)
    public String addSite(
            @ModelAttribute @Valid AddSiteForm addSiteForm,
            Errors errors,
            Model model,
            @PathVariable int companyId,
            @PathVariable int siteId
    ) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findById(companyId).get();
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());
        Site mySite = siteRepository.findById(siteId).get();

        // Programatically verify that this is a tech and not tech company
        if (!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != mySite.getCompany().getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user", myUser);
            model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("addSiteForm", addSiteForm);
            model.addAttribute("techCompany", techCompany);

            return "tech/sites/edit_site";
        } else {
            if(addSiteForm.getSiteContact().getId() != mySite.getSiteContact().getId()){
                mySite.setSiteContact(userRepository.findById(addSiteForm.getSiteContact().getId()).get());
            }

            if (!addSiteForm.getStreetAddress1().equals(mySite.getAddress().getAddress1())) {
                mySite.getAddress().setAddress1(addSiteForm.getStreetAddress1());
            }

            if (addSiteForm.getStreetAddress2() != null &&
                    mySite.getAddress().getAddress2() != null &&
                    !addSiteForm.getStreetAddress2().equals(mySite.getAddress().getAddress2()))
            {
                mySite.getAddress().setAddress2(addSiteForm.getStreetAddress2());
            }else if(addSiteForm.getStreetAddress2() == null &&
                    mySite.getAddress().getAddress2() != null){
                mySite.getAddress().setAddress2(addSiteForm.getStreetAddress2());
            }else if(addSiteForm.getStreetAddress2() != null &&
                    mySite.getAddress().getAddress2() == null){
                mySite.getAddress().setAddress2(addSiteForm.getStreetAddress2());
            }

            if(!addSiteForm.getStreetAddressCity().equals(mySite.getAddress().getCity())){
                mySite.getAddress().setCity(addSiteForm.getStreetAddressCity());
            }

            if(!addSiteForm.getStreetAddressState().equals(mySite.getAddress().getState())){
                mySite.getAddress().setCity(addSiteForm.getStreetAddressCity());
            }

            if(!addSiteForm.getStreetAddressZip().equals(mySite.getAddress().getZip())){
                mySite.getAddress().setZip(addSiteForm.getStreetAddressZip());
            }

            if(!addSiteForm.getSite().equals(mySite.getSite())){
                mySite.setSite(addSiteForm.getSite());
            }

            if(!addSiteForm.getCompanyPhone().equals(mySite.getPhone())){
                mySite.setPhone(addSiteForm.getCompanyPhone());
            }

            if(addSiteForm.getFax()!= null &&
                    mySite.getFax() != null &&
                    addSiteForm.getFax().equals(mySite.getFax())){
                mySite.setFax(addSiteForm.getFax());
            }else if(addSiteForm.getFax()== null &&
                    mySite.getFax() != null) {
                mySite.setFax(addSiteForm.getFax());
            }else if(addSiteForm.getFax()!= null &&
                    mySite.getFax() == null) {
                mySite.setFax(addSiteForm.getFax());
            }

            siteRepository.save(mySite);
            companyRepository.save(myCompany);

            return "redirect:/tech/" + myCompany.getId() + "/sites";
        }
    }


}

