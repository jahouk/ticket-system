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
    @RequestMapping(value="/{userId}", method = RequestMethod.GET)
    public String userView(
            Model model,
            @PathVariable int companyId,
            @PathVariable int userId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());
        User user = userRepository.findOne(userId);

        // Programatically verify that this is a tech and not tech company
        if(!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != user.getCompany().getId()) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("myUser", user);
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
        User myUser = userService.findUserByUsername(auth.getName());
        AddUserForm addUserForm = new AddUserForm();

        // Programatically verify that this is a tech and not tech company
        if(!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ) {
            return "redirect:/";
        }

        model.addAttribute("user",myUser);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
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
                    myUser,
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
    @RequestMapping(value="/{serId}/edit_user", method = RequestMethod.GET)
    public String editUser(
            Model model,
            @PathVariable int companyId,
            @PathVariable int userId){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());
        User user = userRepository.findOne(userId);
        EditUserForm editUser = new EditUserForm(user);

        // Programatically verify that this is a tech and not tech company
        if(!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != user.getCompany().getId()) {
            return "redirect:/";
        }


        model.addAttribute("user",myUser);
        model.addAttribute("roles", roleRepository.findAll());
        model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
        model.addAttribute("company", myCompany);
        model.addAttribute("editUser", editUser);
        model.addAttribute("techCompany", techCompany);

        return "tech/users/edit_user";
    }

    // This will edit a user
    @RequestMapping(value="/{userId}/edit_user", method = RequestMethod.POST)
    public String editUser(@ModelAttribute @Valid EditUserForm editUser,
                           Errors errors,
                           Model model,
                           @PathVariable int companyId,
                           @PathVariable int userId,
                           @RequestParam String password){
        Company techCompany = getTechCompany(techCompanyRepository, companyRepository);
        Company myCompany = companyRepository.findOne(companyId);
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User myUser = userService.findUserByUsername(auth.getName());
        User user = userRepository.findOne(userId);

        // Programatically verify that this is a tech and not tech company
        if(!isTech(myUser, roleRepository) ||
                myCompany == null ||
                myCompany.getId() == techCompany.getId() ||
                myCompany.getId() != user.getCompany().getId()) {
            return "redirect:/";
        } else if (errors.hasErrors()) {
            model.addAttribute("user",myUser);
            model.addAttribute("roles", roleRepository.findAll());
            model.addAttribute("isAdmin", isAdmin(myUser, roleRepository));
            model.addAttribute("company", myCompany);
            model.addAttribute("editUser", editUser);
            model.addAttribute("techCompany", techCompany);

            return "tech/users/edit_user";
        }else{
            if(!editUser.getCanText().equals(user.getUserInfo().getCanText())){
                user.getUserInfo().setCanText(editUser.getCanText());
            }

            if(!editUser.getCellPhone().equals(user.getUserInfo().getCellphone())){
                user.getUserInfo().setCellphone(editUser.getCellPhone());
            }

            if(!editUser.getCompanyUsername().equals(user.getUserInfo().getCompanyUserName())){
                user.getUserInfo().setCompanyUserName(editUser.getCompanyUsername());
            }

            if(!editUser.getEmail().equals(user.getUserInfo().getEmail())){
                user.getUserInfo().setEmail(editUser.getEmail());
            }

            if(!editUser.getFname().equals(user.getUserInfo().getFname()))  {
                user.getUserInfo().setFname(editUser.getFname());
            }

            if(!editUser.getLname().equals(user.getUserInfo().getLname())){
                user.getUserInfo().setLname(editUser.getLname());
            }

            if(!editUser.getOffice().equals(user.getUserInfo().getOffice().getOffice())){
                user.getUserInfo().getOffice().setOffice(editUser.getOffice());
            }

            if(!editUser.getTitle().equals(user.getUserInfo().getTitle())) {
                user.getUserInfo().setTitle(editUser.getTitle());
            }

            if(!editUser.getUserPhone().equals(user.getUserInfo().getPhone())) {
                user.getUserInfo().setPhone(editUser.getUserPhone());
            }

            if(!user.getUsername().equals(user.getUserInfo().getEmail())){
                user.setUsername(user.getUserInfo().getEmail());
            }

            if(password!=null && !(password.isEmpty())){
                userService.updateUser(user, password);
            }else{
                userService.updateUser(user);
            }

            return "redirect:/tech/"+myCompany.getId()+"/users/"+user.getId();
        }
    }
}