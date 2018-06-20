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
import com.houkstead.ticketsystem.models.*;
import com.houkstead.ticketsystem.models.forms.AddAssetForm;
import com.houkstead.ticketsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isTech;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isUserAdmin;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("customer assets")
@RequestMapping("customer/assets")
public class AssetsController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AssetRepository assetRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private AssetSpecRepository assetSpecRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String index(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        // Programatically verify that this is a user admin
        if(!isUserAdmin(myUser, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);

        return "customer/assets/index";
    }


    // View Asset
    @RequestMapping(value="/{assetId}", method = RequestMethod.GET)
    public String viewasset(Model model,
                               @PathVariable int assetId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        Asset myAsset = assetRepository.findOne(assetId);

        // Programatically verify that this is a user admin
        if(!isUserAdmin(myUser, roleRepository) ||
                myUser.getCompany().getId() !=
                        myAsset.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("asset", myAsset);
        return "customer/assets/view_asset";
    }


    // Add Asset
    @RequestMapping(value="/add_asset", method = RequestMethod.GET)
    public String addasset(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        AddAssetForm addAssetForm = new AddAssetForm();
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a user admin
        if(!isUserAdmin(myUser, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("addAssetForm", addAssetForm);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("offices", myOffices);

        return "customer/assets/add_asset";
    }

    // Add asset
    @RequestMapping(value="/add_asset", method = RequestMethod.POST)
    public String addasset(
            @ModelAttribute @Valid AddAssetForm addAssetForm,
            Errors errors,
            Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        List<Office> myOffices = new ArrayList<>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a user admin
        if(!isUserAdmin(myUser, roleRepository)) {
            return "redirect:/";
        }else if (errors.hasErrors()) {
            model.addAttribute("user",myUser);
            model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("addAssetForm", addAssetForm);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("offices", myOffices);

            return "customer/assets/add_asset";
        } else {
            Asset addAsset = new Asset(addAssetForm);
            assetRepository.save(addAsset);
            myCompany.addAsset(addAsset);
            companyRepository.save(myCompany);

            return "redirect:/customer/assets";
        }
    }

    // Edit Asset
    @RequestMapping(value="{assetId}/edit_asset", method = RequestMethod.GET)
    public String editasset(
            Model model,
            @PathVariable int assetId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        Asset myAsset = assetRepository.findOne(assetId);
        AddAssetForm addAssetForm = new AddAssetForm(myAsset);
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a user admin
        if(!isUserAdmin(myUser, roleRepository) ||
                myUser.getCompany().getId() !=
                        myAsset.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("offices", myOffices);
        model.addAttribute("addAssetForm", addAssetForm);

        return "customer/assets/edit_asset";
    }

    // This will display a specific site view and add offie from inline form
    @RequestMapping(value="{assetId}/edit_asset", method = RequestMethod.POST)
    public String editasset(
            @ModelAttribute @Valid AddAssetForm addAssetForm,
            Errors errors,
            Model model,
            @PathVariable int companyId,
            @PathVariable int assetId
    ) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        Asset myAsset = assetRepository.findOne(assetId);
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a user admin
        if(!isUserAdmin(myUser, roleRepository) ||
                myUser.getCompany().getId() !=
                        myAsset.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user",myUser);
            model.addAttribute("isUserAdmin", isUserAdmin(myUser, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("offices", myOffices);
            model.addAttribute("addAssetForm", addAssetForm);

            return "customer/assets/edit_asset";
        } else {
            if(!addAssetForm.getName().equals(myAsset.getName())){
                myAsset.setName(addAssetForm.getName());
            }

            if(addAssetForm.getOffice().getId() != myAsset.getOffice().getId()){
                myAsset.setOffice(officeRepository.findOne(addAssetForm.getOffice().getId()));
            }

            assetRepository.save(myAsset);
            companyRepository.save(myCompany);

            return "redirect:/customer/assets";
        }
    }


    @RequestMapping(value="/{assetId}", method = RequestMethod.POST)
    public String viewAsset(Model model,
                               @PathVariable int assetId,
                               @RequestParam String specName,
                               @RequestParam String specValue) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = myUser.getCompany();

        Asset myAsset = assetRepository.findOne(assetId);

        // Programatically verify that this is a user admin
        if(!isUserAdmin(myUser, roleRepository) ||
                myUser.getCompany().getId() !=
                        myAsset.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        AssetSpec myAssetSpec = new AssetSpec(myAsset, specName, specValue);
        assetSpecRepository.save(myAssetSpec);
        myAsset.addAssetSpec(myAssetSpec);
        assetRepository.save(myAsset);
        companyRepository.save(myCompany);

        return "redirect:/customer/assets/"+ myAsset.getId();
    }



}

