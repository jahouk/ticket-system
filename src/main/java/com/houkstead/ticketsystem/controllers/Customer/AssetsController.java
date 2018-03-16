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
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        // Programatically verify that this is a user admin
        if(!isUserAdmin(user, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);

        return "customer/assets/index";
    }


    // View Asset
    @RequestMapping(value="/{assetId}", method = RequestMethod.GET)
    public String viewasset(Model model,
                               @PathVariable int assetId){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        Asset myAsset = assetRepository.findOne(assetId);

        // Programatically verify that this is a user admin
        if(!isUserAdmin(user, roleRepository) ||
                user.getCompany().getId() !=
                        myAsset.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("asset", myAsset);
        return "customer/assets/view_asset";
    }


    // Add Asset
    @RequestMapping(value="/add_asset", method = RequestMethod.GET)
    public String addasset(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        AddAssetForm addAssetForm = new AddAssetForm();
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a user admin
        if(!isUserAdmin(user, roleRepository)) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("addAssetForm", addAssetForm);
        model.addAttribute("techCompany", techCompany);

        return "customer/assets/add_asset";
    }

    // Add asset
    @RequestMapping(value="/add_asset", method = RequestMethod.POST)
    public String addasset(
            @ModelAttribute @Valid AddAssetForm addAssetForm,
            Errors errors,
            Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a user admin
        if(!isUserAdmin(user, roleRepository)) {
            return "redirect:/";
        }else if (errors.hasErrors()) {
            model.addAttribute("user",user);
            model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("addAssetForm", addAssetForm);
            model.addAttribute("techCompany", techCompany);

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
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        Asset myAsset = assetRepository.findOne(assetId);
        AddAssetForm addAssetForm = new AddAssetForm(myAsset);
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a user admin
        if(!isUserAdmin(user, roleRepository) ||
                user.getCompany().getId() !=
                        myAsset.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
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
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        Asset myAsset = assetRepository.findOne(assetId);
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a user admin
        if(!isUserAdmin(user, roleRepository) ||
                user.getCompany().getId() !=
                        myAsset.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user",user);
            model.addAttribute("isUserAdmin", isUserAdmin(user, roleRepository));
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
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        Asset myAsset = assetRepository.findOne(assetId);

        // Programatically verify that this is a user admin
        if(!isUserAdmin(user, roleRepository) ||
                user.getCompany().getId() !=
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

