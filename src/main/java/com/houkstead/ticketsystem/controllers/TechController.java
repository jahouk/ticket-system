package com.houkstead.ticketsystem.controllers;

import com.houkstead.ticketsystem.UserService;
import com.houkstead.ticketsystem.models.User;
import com.houkstead.ticketsystem.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("tech")
public class TechController {

    @Autowired
    private UserService userService;

    @Autowired
    private CompanyRepository companyRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String tech(Model model){

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        model.addAttribute("user",user);
        model.addAttribute("companies", companyRepository.findAll());

        return "tech/index";
    }

}