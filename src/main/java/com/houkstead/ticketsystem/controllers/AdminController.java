package com.houkstead.ticketsystem.controllers;

import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.*;
import com.houkstead.ticketsystem.models.forms.AddCompanyForm;
import com.houkstead.ticketsystem.models.test_models.TestAddress;
import com.houkstead.ticketsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;

import static com.houkstead.ticketsystem.utilities.SetupUtilities.*;
import static com.houkstead.ticketsystem.utilities.SetupUtilities.createUserInfo;

@Controller
@RequestMapping("admin")
public class AdminController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private CompanyInfoRepository companyInfoRepository;

    @Autowired
    private OfficeRepository officeRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserService userService;

    @RequestMapping(value = "add_company", method = RequestMethod.GET)
    public String addCompany(Model model) {
        //Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        //User user = userService.findUserByUsername(auth.getName());

        AddCompanyForm addCompanyForm = new AddCompanyForm();
        model.addAttribute("addCompanyForm", addCompanyForm);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("title", "Setup New Customer Company");
        model.addAttribute("action", "/admin/add_company");

        return "shared/add_company";
    }

    @RequestMapping(value = "add_company", method = RequestMethod.POST)
    public String addCompany(@ModelAttribute @Valid AddCompanyForm addCompanyForm,
                             Errors errors,
                             Model model) {

        if (errors.hasErrors()) {
            System.out.println("has errors");
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("title", "Setup Customer Company");
            model.addAttribute("action", "/admin/add_company");

            return "shared/add_company";
        } else {
            Company company = null;
            CompanyInfo companyInfo = null;
            User user = null;
            Site site = null;
            Office office = null;
            Address billingAddress = null;
            Address streetAddress = null;
            UserInfo userInfo = null;


            // Create company
            company = createCompany(new Company(), companyRepository);


            // Create  StreetAddress & Billing Addresses and verify different
            // duplicateAddressCheck checks all addresses in a company
            // and either returns the address that's already created or creates
            // a new address.
            billingAddress = duplicateAddressCheck(new TestAddress(
                            addCompanyForm.getBillingAddress1(),
                            addCompanyForm.getBillingAddress2(),
                            addCompanyForm.getBillingAddressCity(),
                            addCompanyForm.getBillingAddressState(),
                            addCompanyForm.getBillingAddressZip()),
                    company,
                    addressRepository,
                    companyRepository
            );

            streetAddress = duplicateAddressCheck(new TestAddress(
                            addCompanyForm.getStreetAddress1(),
                            addCompanyForm.getStreetAddress2(),
                            addCompanyForm.getStreetAddressCity(),
                            addCompanyForm.getStreetAddressState(),
                            addCompanyForm.getStreetAddressZip()),
                    company,
                    addressRepository,
                    companyRepository
            );

            // Create admin User
            userService.saveUser(new User(
                    addCompanyForm.getUsername(),
                    addCompanyForm.getPassword(),
                    company,
                    new HashSet<Role>(Arrays.asList(
                            roleRepository.findByRole("USER"),
                            roleRepository.findByRole("ADMIN")))
            ));
            user = userService.findUserByUsername(addCompanyForm.getUsername());


            // Create Site
            site = createSite(new Site(
                            addCompanyForm.getSite(),
                            company,
                            streetAddress,
                            addCompanyForm.getCompanyPhone(),
                            addCompanyForm.getFax(),
                            user),
                    siteRepository
            );


            // Office
            office = createOffice(new Office(site, addCompanyForm.getOffice()), officeRepository);


            // User_Info
            userInfo = createUserInfo(new UserInfo(
                    user,
                    addCompanyForm.getFname(),
                    addCompanyForm.getLname(),
                    addCompanyForm.getTitle(),
                    addCompanyForm.getEmail(),
                    addCompanyForm.getUserPhone(),
                    addCompanyForm.getCompanyUsername(),
                    addCompanyForm.getCellPhone(),
                    addCompanyForm.getCanText(),
                    office
            ), userInfoRepository);


            // Create CompanyInfo (Site, Billing Address)
            companyInfo = createCompanyInfo(new CompanyInfo(
                            addCompanyForm.getCompanyName(),
                            company,
                            site,
                            billingAddress,
                            addCompanyForm.getWebsite()
                    ),
                    companyInfoRepository);

            // Update Company (CompanyInfo)
            company.setCompanyInfo(companyInfo);
            companyRepository.save(company);

            // update User (User Info)
            user.setUserInfo(userInfo);
            user.setPassword(addCompanyForm.getPassword());
            userService.saveUser(user);

            //model.addAttribute("addCompanyForm", addCompanyForm);
            //model.addAttribute("title","Tech Company and Admin User Setup");
            //model.addAttribute("message", "Setup was successful");

            return "redirect:/tech";
        }
    }
}