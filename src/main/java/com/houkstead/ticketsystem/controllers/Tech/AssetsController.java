package com.houkstead.ticketsystem.controllers.Tech;


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
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("tech assets")
@RequestMapping("tech/{companyId}/assets")
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
        return "tech/assets/index";
    }


    //
    @RequestMapping(value="/{assetId}", method = RequestMethod.GET)
    public String viewasset(Model model,
                               @PathVariable int companyId,
                               @PathVariable int assetId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Asset myAsset = assetRepository.findOne(assetId);

        // Programatically verify that this is a tech
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myAsset.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("asset", myAsset);
        return "tech/assets/view_asset";
    }


    // Add Asset
    @RequestMapping(value="/add_asset", method = RequestMethod.GET)
    public String addasset(
            Model model,
            @PathVariable int companyId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        AddAssetForm addAssetForm = new AddAssetForm();
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId()) {
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("offices", myOffices);
        model.addAttribute("addAssetForm", addAssetForm);
        model.addAttribute("techCompany", techCompany);

        return "tech/assets/add_asset";
    }

    // This will display a specific site view and add office from inline form
    @RequestMapping(value="/add_asset", method = RequestMethod.POST)
    public String addasset(
            @ModelAttribute @Valid AddAssetForm addAssetForm,
            Errors errors,
            Model model,
            @PathVariable int companyId
    ) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a tech and not tech company
        if (!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("offices", myOffices);
            model.addAttribute("addAssetForm", addAssetForm);
            model.addAttribute("techCompany", techCompany);

            return "tech/assets/add_asset";
        } else {
            Asset addAsset = new Asset(addAssetForm);
            assetRepository.save(addAsset);
            myCompany.addAsset(addAsset);
            companyRepository.save(myCompany);

            return "redirect:/tech/" + myCompany.getId() + "/assets";
        }
    }

    // Edit Asset
    @RequestMapping(value="{assetId}/edit_asset", method = RequestMethod.GET)
    public String editasset(
            Model model,
            @PathVariable int companyId,
            @PathVariable int assetId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Asset myAsset = assetRepository.findOne(assetId);
        AddAssetForm addAssetForm = new AddAssetForm(myAsset);
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myAsset.getOffice().getSite().getCompany().getId() != myCompany.getId()){
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("offices", myOffices);
        model.addAttribute("addAssetForm", addAssetForm);
        model.addAttribute("techCompany", techCompany);

        return "tech/assets/edit_asset";
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
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Asset myAsset = assetRepository.findOne(assetId);
        List<Office> myOffices = new ArrayList<Office>();

        for (Site site: myCompany.getSites()) {
            for (Office office: site.getOffices()){
                myOffices.add(office);
            }
        }

        // Programatically verify that this is a tech and not tech company
        if (!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myAsset.getOffice().getSite().getCompany().getId() != myCompany.getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("offices", myOffices);
            model.addAttribute("addAssetForm", addAssetForm);
            model.addAttribute("techCompany", techCompany);

            return "tech/assets/edit_asset";
        } else {
            if(!addAssetForm.getName().equals(myAsset.getName())){
                myAsset.setName(addAssetForm.getName());
            }

            if(addAssetForm.getOffice().getId() != myAsset.getOffice().getId()){
                myAsset.setOffice(officeRepository.findOne(addAssetForm.getOffice().getId()));
            }

            assetRepository.save(myAsset);
            companyRepository.save(myCompany);

            return "redirect:/tech/" + myCompany.getId() + "/assets";
        }
    }


    @RequestMapping(value="/{assetId}", method = RequestMethod.POST)
    public String viewasset(Model model,
                               @PathVariable int companyId,
                               @PathVariable int assetId,
                               @RequestParam String specName,
                               @RequestParam String specValue) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Asset myAsset = assetRepository.findOne(assetId);

        // Programatically verify that this is a tech
        if (!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myAsset.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        AssetSpec myAssetSpec = new AssetSpec(myAsset, specName, specValue);
        assetSpecRepository.save(myAssetSpec);
        myAsset.addAssetSpec(myAssetSpec);
        assetRepository.save(myAsset);
        companyRepository.save(myCompany);

        return "redirect:/tech/" + myCompany.getId() + "/assets/"+ myAsset.getId();
    }



}

