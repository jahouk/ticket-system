package com.houkstead.ticketsystem.controllers;

import javax.validation.Valid;

import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.*;
import com.houkstead.ticketsystem.models.forms.AddCompanyForm;
import com.houkstead.ticketsystem.models.test_models.TestAddress;
import com.houkstead.ticketsystem.repository.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import java.util.*;

import static com.houkstead.ticketsystem.utilities.SetupUtilities.*;


@Controller
@RequestMapping("setup")
public class SetupController {

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
    private  SiteRepository siteRepository;

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String setup(Model model){
        /*
        This function checks to see if there are any companies or users in the database,
        if not it will return the setup form and seed the database entries for roles and statuses.
         */

        // If no companies at all and no users at all
        if(companyRepository.findAll().isEmpty() && userService.findAll().isEmpty()){

            // Seed the ROLES
            roleRepository.deleteAll();
            seedUserRole(new Role("ADMIN","Site Admin",99), roleRepository);
            seedUserRole(new Role("TECH", "Tech User", 10), roleRepository);
            seedUserRole(new Role("USER", "Customer / End-User", 1), roleRepository);
            seedUserRole(new Role("USER-ADMIN", "Customer / End-User with Additional Rights", 2), roleRepository);


            // Seed TICKET_STATUSES
            statusRepository.deleteAll();
            seedTicketStatus(new Status("Open by Customer", 10), statusRepository);
            seedTicketStatus(new Status("Assigned to Tech", 20), statusRepository);
            seedTicketStatus(new Status("Waiting on Customer", 30), statusRepository);
            seedTicketStatus(new Status("Escalated to Vendor", 40), statusRepository);
            seedTicketStatus(new Status("Closed Pending Customer", 50), statusRepository);
            seedTicketStatus(new Status("Closed by Customer", 60), statusRepository);

            AddCompanyForm addCompanyForm = new AddCompanyForm();
            model.addAttribute("addCompanyForm", addCompanyForm);
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("title","Tech Company and Admin User Setup");
            model.addAttribute("action", "/setup/");

            return "setup/add_company";
        }else{
            // if setup redirect to home
            return "redirect:/";
        }
    }

    @RequestMapping(value="", method = RequestMethod.POST)
    public  String setup( @ModelAttribute @Valid AddCompanyForm addCompanyForm,
                          Errors errors,
                          Model model){
        Company company = null;

        Address billingAddress = null;
        Address streetAddress = null;

        CompanyInfo companyInfo = null;
        User user = null;
        Site site = null;
        Office office = null;

        UserInfo userInfo = null;



        // Still verifying that the database has not been configured for tech company
        if(companyRepository.findAll().isEmpty() && userService.findAll().isEmpty()){
            if (errors.hasErrors()) {
                model.addAttribute("roles", roleRepository.findAll());
                model.addAttribute("title","Tech Company and Admin User Setup");
                model.addAttribute("action", "/setup/");

                return "setup/add_company";
            }
            else {
                /*
                The creation order
                Add Tech Company
                 - Add addresses
                 - Add admin users
                 - Add site
                 - Add office
                 - Add user_info
                Update Tech  Company
                */

                // Create company
                company = createCompany(new Company(), companyRepository);


                // Create  StreetAddress & Billing Addresses and verify different
                // duplicateAddressCheck checks all addresses in a company
                // and either returns the address that's already created or creates
                // a new address.
                billingAddress =duplicateAddressCheck( new TestAddress(
                                addCompanyForm.getBillingAddress1(),
                                addCompanyForm.getBillingAddress2(),
                                addCompanyForm.getBillingAddressCity(),
                                addCompanyForm.getBillingAddressState(),
                                addCompanyForm.getBillingAddressZip()),
                        company,
                        addressRepository,
                        companyRepository
                );

                streetAddress = duplicateAddressCheck( new TestAddress(
                        addCompanyForm.getStreetAddress1(),
                        addCompanyForm.getStreetAddress2(),
                        addCompanyForm.getStreetAddressCity(),
                        addCompanyForm.getStreetAddressState(),
                        addCompanyForm.getStreetAddressZip()),
                        company,
                        addressRepository,
                        companyRepository
                );

                company.addAddress(billingAddress);
                if(billingAddress.getId() != streetAddress.getId()){
                    company.addAddress(streetAddress);
                }



                // Create admin User
                userService.saveUser(new User(
                        addCompanyForm.getUsername(),
                        addCompanyForm.getPassword(),
                        company,
                        new HashSet<Role>(Arrays.asList(
                                roleRepository.findByRole("TECH"),
                                roleRepository.findByRole("ADMIN")))
                ));
                user = userService.findUserByUsername(addCompanyForm.getUsername());



                // Create SitesController & Office
                site = createSite(new Site(
                        addCompanyForm.getSite(),
                        company,
                        streetAddress,
                        addCompanyForm.getCompanyPhone(),
                        addCompanyForm.getFax(),
                        user),
                        siteRepository
                );

                office = createOffice(new Office(site, addCompanyForm.getOffice()), officeRepository);

                site.addOffice(office);
                company.addSite(site);


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



                // Create CompanyInfo (SitesController, Billing Address)
                companyInfo = createCompanyInfo(new CompanyInfo(
                                addCompanyForm.getCompanyName(),
                                company,
                                site,
                                billingAddress,
                                addCompanyForm.getWebsite()
                        ),
                        companyInfoRepository);
                company.setCompanyInfo(companyInfo);


                // update User (User Info)
                user.setUserInfo(userInfo);
                user.setPassword(addCompanyForm.getPassword());
                userService.saveUser(user);
                company.addUser(user);

                companyRepository.save(company);

                // Update techCompany
                techCompanyRepository.save(new TechCompany(company));

                model.addAttribute("addCompanyForm", addCompanyForm);
                model.addAttribute("title","Tech Company and Admin User Setup");
                model.addAttribute("message", "Setup was successful");

                return "setup/index";

            } // End Else for no errors
        } // End if for no companies
        else{
            return "redirect:/";
        }
    }
}