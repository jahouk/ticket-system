package com.houkstead.ticketsystem.controllers;

import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.repository.CompanyRepository;
import com.houkstead.ticketsystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value={""}, method = RequestMethod.GET)
    public String login(Model model){
        model.addAttribute("title", "Login");
        model.addAttribute("loginRequired","This site requires authentication to use");
        return "login/index";
    }
}