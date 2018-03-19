package com.houkstead.ticketsystem.controllers.Admin;

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

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SetupUtilities.*;
import static com.houkstead.ticketsystem.utilities.SetupUtilities.createCompanyInfo;
import static com.houkstead.ticketsystem.utilities.SetupUtilities.createUserInfo;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("admin company")
@RequestMapping("admin/companies")
public class CompaniesController {
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @RequestMapping(value = "add_company", method = RequestMethod.GET)
    public String addCompany(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        // Programatically verify that this is a user admin
        if(!isAdmin(user, roleRepository)) {
            return "redirect:/admin";
        }

        AddCompanyForm addCompanyForm = new AddCompanyForm();

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("addCompanyForm", addCompanyForm);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("title", "Setup New Customer Company");

        return "admin/companies/add_company";
    }

    @RequestMapping(value = "add_company", method = RequestMethod.POST)
    public String addCompany(@ModelAttribute @Valid AddCompanyForm addCompanyForm,
                             Errors errors,
                             Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = user.getCompany();

        // Programatically verify that this is a user admin
        if(!isAdmin(user, roleRepository)) {
            return "redirect:/admin";
        }else if (errors.hasErrors()) {
            model.addAttribute("user",user);
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("techCompany", techCompany);
            model.addAttribute("addCompanyForm", addCompanyForm);
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("title", "Setup New Customer Company");

            return "admin/companies/add_company";
        } else {
            Company company = null;
            CompanyInfo companyInfo = null;
            User myUser = null;
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

            company.addAddress(billingAddress);
            if(billingAddress.getId() != streetAddress.getId()){
                company.addAddress(streetAddress);
            }


            // Create admin User
            userService.saveUser(new User(
                    addCompanyForm.getEmail(),
                    addCompanyForm.getPassword(),
                    company,
                    new HashSet<Role>(Arrays.asList(
                            roleRepository.findByRole("USER"),
                            roleRepository.findByRole("USER-ADMIN")))
            ));
            myUser = userService.findUserByUsername(addCompanyForm.getEmail());


            // Create SitesController
            site = createSite(new Site(
                            addCompanyForm.getSite(),
                            company,
                            streetAddress,
                            addCompanyForm.getCompanyPhone(),
                            addCompanyForm.getFax(),
                            myUser),
                    siteRepository
            );

            office = createOffice(new Office(site, addCompanyForm.getOffice()), officeRepository);

            site.addOffice(office);
            company.addSite(site);


            // User_Info
            userInfo = createUserInfo(new UserInfo(
                    myUser,
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
            myUser.setUserInfo(userInfo);
            myUser.setPassword(addCompanyForm.getPassword());
            userService.saveUser(myUser);
            company.addUser(myUser);

            companyRepository.save(company);

            return "redirect:/tech/"+company.getId();
        }
    }
}