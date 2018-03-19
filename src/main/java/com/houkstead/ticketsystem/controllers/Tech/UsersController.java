package com.houkstead.ticketsystem.controllers.Tech;

import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.*;
import com.houkstead.ticketsystem.models.forms.AddUserForm;
import com.houkstead.ticketsystem.models.forms.EditUserForm;
import com.houkstead.ticketsystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isAdmin;
import static com.houkstead.ticketsystem.utilities.SecurityUtilities.isTech;
import static com.houkstead.ticketsystem.utilities.SetupUtilities.createOffice;
import static com.houkstead.ticketsystem.utilities.SetupUtilities.createUserInfo;
import static com.houkstead.ticketsystem.utilities.SiteUtilities.getTechCompany;

@Controller("tech users")
@RequestMapping("tech/{companyId}/users")
public class UsersController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private TechCompanyRepository techCompanyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private OfficeRepository officeRepository;

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
        return "tech/users/index";
    }


    // This will display a specific user view
    @RequestMapping(value="/{myUserId}", method = RequestMethod.GET)
    public String userView(
            Model model,
            @PathVariable int companyId,
            @PathVariable int myUserId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        User myUser = userRepository.findOne(myUserId);

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myUser.getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("myUser", myUser);
        model.addAttribute("techCompany", techCompany);

        return "tech/users/view_user";
    }

    // This will edit a user
    @RequestMapping(value="/add_user", method = RequestMethod.GET)
    public String addUser(
            Model model,
            @PathVariable int companyId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        AddUserForm addUserForm = new AddUserForm();

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ) {
            return "redirect:/";
        }

        model.addAttribute("user",user);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("techCompany", techCompany);
        model.addAttribute("addUserForm", addUserForm);

        return "tech/users/add_user";
    }

    // This will edit a user
    @RequestMapping(value="/add_user", method = RequestMethod.POST)
    public String addUser(@ModelAttribute @Valid AddUserForm addUserForm,
                           Errors errors,
                           Model model,
                           @PathVariable int companyId) {
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        // Programatically verify that this is a tech and not tech company
        if (!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user", user);
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("addUserForm", addUserForm);
            model.addAttribute("techCompany", techCompany);

            return "tech/users/add_user";
        } else {
            User newUser = null;

            userService.saveUser(new User(
                            addUserForm.getEmail(),
                            addUserForm.getPassword(),
                            myCompany,
                            new HashSet<Role>(Arrays.asList(
                                    roleRepository.findByRole("USER")))
                    ));
            newUser = userService.findUserByUsername(addUserForm.getEmail());

            Office newOffice = createOffice(new Office(myCompany.getCompanyInfo().getPrimarySite(),
                    addUserForm.getOffice()), officeRepository);

            myCompany.getCompanyInfo().getPrimarySite().addOffice(newOffice);

            // User_Info
            UserInfo userInfo = createUserInfo(new UserInfo(
                    user,
                    addUserForm.getFname(),
                    addUserForm.getLname(),
                    addUserForm.getTitle(),
                    addUserForm.getEmail(),
                    addUserForm.getUserPhone(),
                    addUserForm.getCompanyUsername(),
                    addUserForm.getCellPhone(),
                    addUserForm.getCanText(),
                    newOffice
            ), userInfoRepository);

            // update User (User Info)
            newUser.setUserInfo(userInfo);
            newUser.setPassword(addUserForm.getPassword());
            userService.saveUser(newUser);
            myCompany.addUser(newUser);

            companyRepository.save(myCompany);

            return "redirect:/tech/" + myCompany.getId() + "/users";
        }
    }

    // This will edit a user
    @RequestMapping(value="/{myUserId}/edit_user", method = RequestMethod.GET)
    public String editUser(
            Model model,
            @PathVariable int companyId,
            @PathVariable int myUserId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        User myUser = userRepository.findOne(myUserId);
        EditUserForm editUser = new EditUserForm(myUser);

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myUser.getCompany().getId()) {
            return "redirect:/";
        }


        model.addAttribute("user",user);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("isAdmin", isAdmin(user, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("editUser", editUser);
        model.addAttribute("techCompany", techCompany);

        return "tech/users/edit_user";
    }

    // This will edit a user
    @RequestMapping(value="/{myUserId}/edit_user", method = RequestMethod.POST)
    public String editUser(@ModelAttribute @Valid EditUserForm editUser,
                           Errors errors,
                           Model model,
                           @PathVariable int companyId,
                           @PathVariable int myUserId,
                           @RequestParam String password){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());
        User myUser = userRepository.findOne(myUserId);

        // Programatically verify that this is a tech and not tech company
        if(!isTech(user, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != myUser.getCompany().getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user",user);
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("isAdmin", isAdmin(user, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("editUser", editUser);
            model.addAttribute("techCompany", techCompany);

            return "tech/users/edit_user";
        }else{
            if(!editUser.getCanText().equals(myUser.getUserInfo().getCanText())){
                myUser.getUserInfo().setCanText(editUser.getCanText());
            }

            if(!editUser.getCellPhone().equals(myUser.getUserInfo().getCellphone())){
                myUser.getUserInfo().setCellphone(editUser.getCellPhone());
            }

            if(!editUser.getCompanyUsername().equals(myUser.getUserInfo().getCompanyUserName())){
                myUser.getUserInfo().setCompanyUserName(editUser.getCompanyUsername());
            }

            if(!editUser.getEmail().equals(myUser.getUserInfo().getEmail())){
                myUser.getUserInfo().setEmail(editUser.getEmail());
            }

            if(!editUser.getFname().equals(myUser.getUserInfo().getFname()))  {
                myUser.getUserInfo().setFname(editUser.getFname());
            }

            if(!editUser.getLname().equals(myUser.getUserInfo().getLname())){
                myUser.getUserInfo().setLname(editUser.getLname());
            }

            if(!editUser.getOffice().equals(myUser.getUserInfo().getOffice().getOffice())){
                myUser.getUserInfo().getOffice().setOffice(editUser.getOffice());
            }

            if(!editUser.getTitle().equals(myUser.getUserInfo().getTitle())) {
                myUser.getUserInfo().setTitle(editUser.getTitle());
            }

            if(!editUser.getUserPhone().equals(myUser.getUserInfo().getPhone())) {
                myUser.getUserInfo().setPhone(editUser.getUserPhone());
            }

            if(!myUser.getUsername().equals(myUser.getUserInfo().getEmail())){
                myUser.setUsername(myUser.getUserInfo().getEmail());
            }

            if(password!=null && !(password.isEmpty())){
                userService.updateUser(myUser, password);
            }else{
                userService.updateUser(myUser);
            }

            return "redirect:/tech/"+myCompany.getId()+"/users/"+myUser.getId();
        }
    }
}