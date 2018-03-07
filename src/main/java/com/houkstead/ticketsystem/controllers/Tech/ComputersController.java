package com.houkstead.ticketsystem.controllers.Tech;


import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.*;
import com.houkstead.ticketsystem.models.forms.AddComputerForm;
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

@Controller("tech computers")
@RequestMapping("tech/{companyId}/computers")
public class ComputersController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private ComputerRepository computerRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private ComputerSpecRepository computerSpecRepository;

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
        return "tech/computers/index";
    }


    //
    @RequestMapping(value="/{computerId}", method = RequestMethod.GET)
    public String viewComputer(Model model,
                               @PathVariable int companyId,
                               @PathVariable int computerId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Computer myComputer = computerRepository.findOne(computerId);

        // Programatically verify that this is a tech
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myComputer.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("computer", myComputer);
        return "tech/computers/view_computer";
    }


    // Add Computer
    @RequestMapping(value="/add_computer", method = RequestMethod.GET)
    public String addComputer(
            Model model,
            @PathVariable int companyId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        AddComputerForm addComputerForm = new AddComputerForm();
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
        model.addAttribute("addComputerForm", addComputerForm);
        model.addAttribute("techCompany", techCompany);

        return "tech/computers/add_computer";
    }

    // This will display a specific site view and add office from inline form
    @RequestMapping(value="/add_computer", method = RequestMethod.POST)
    public String addComputer(
            @ModelAttribute @Valid AddComputerForm addComputerForm,
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
            model.addAttribute("addComputerForm", addComputerForm);
            model.addAttribute("techCompany", techCompany);

            return "tech/computers/add_computer";
        } else {
            Computer addComputer = new Computer(addComputerForm);
            computerRepository.save(addComputer);
            myCompany.addComputer(addComputer);
            companyRepository.save(myCompany);

            return "redirect:/tech/" + myCompany.getId() + "/computers";
        }
    }


    // Edit Computer
    @RequestMapping(value="{computerId}/edit_computer", method = RequestMethod.GET)
    public String editComputer(
            Model model,
            @PathVariable int companyId,
            @PathVariable int computerId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Computer myComputer = computerRepository.findOne(computerId);
        AddComputerForm addComputerForm = new AddComputerForm(myComputer);
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
                myComputer.getOffice().getSite().getCompany().getId() != myCompany.getId()){
            return "redirect:/";
        }

        model.addAttribute("user", user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("offices", myOffices);
        model.addAttribute("addComputerForm", addComputerForm);
        model.addAttribute("techCompany", techCompany);

        return "tech/computers/edit_computer";
    }

    // This will display a specific site view and add offie from inline form
    @RequestMapping(value="{computerId}/edit_computer", method = RequestMethod.POST)
    public String editComputer(
            @ModelAttribute @Valid AddComputerForm addComputerForm,
            Errors errors,
            Model model,
            @PathVariable int companyId,
            @PathVariable int computerId
    ) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Computer myComputer = computerRepository.findOne(computerId);
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
                myComputer.getOffice().getSite().getCompany().getId() != myCompany.getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("offices", myOffices);
            model.addAttribute("addComputerForm", addComputerForm);
            model.addAttribute("techCompany", techCompany);

            return "tech/computers/edit_computer";
        } else {
            if(!addComputerForm.getName().equals(myComputer.getName())){
                myComputer.setName(addComputerForm.getName());
            }

            if(addComputerForm.getOffice().getId() != myComputer.getOffice().getId()){
                myComputer.setOffice(officeRepository.findOne(addComputerForm.getOffice().getId()));
            }

            computerRepository.save(myComputer);
            companyRepository.save(myCompany);

            return "redirect:/tech/" + myCompany.getId() + "/computers";
        }
    }


    @RequestMapping(value="/{computerId}", method = RequestMethod.POST)
    public String viewComputer(Model model,
                               @PathVariable int companyId,
                               @PathVariable int computerId,
                               @RequestParam String specName,
                               @RequestParam String specValue) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        Computer myComputer = computerRepository.findOne(computerId);

        // Programatically verify that this is a tech
        if (!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myComputer.getOffice().getSite().getCompany().getId()) {
            return "redirect:/";
        }

        ComputerSpec myComputerSpec = new ComputerSpec(myComputer, specName, specValue);
        computerSpecRepository.save(myComputerSpec);
        myComputer.addComputerSpec(myComputerSpec);
        computerRepository.save(myComputer);
        companyRepository.save(myCompany);

        return "redirect:/tech/" + myCompany.getId() + "/computers/"+myComputer.getId();
    }



}

