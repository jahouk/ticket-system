package com.houkstead.ticketsystem.controllers;


import com.houkstead.ticketsystem.UserService;
        import com.houkstead.ticketsystem.models.User;
import com.houkstead.ticketsystem.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
        import org.springframework.security.core.Authentication;
        import org.springframework.security.core.context.SecurityContextHolder;
        import org.springframework.stereotype.Controller;
        import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
        import org.springframework.web.bind.annotation.RequestMethod;




@Controller("HomeController")
@RequestMapping("/")
public class HomeController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    public String home(Model model){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = userService.findUserByUsername(auth.getName());

        if(user.getRoles().contains(roleRepository.findByRole("USER")))
        {
            // Direct to customer portal
            return "redirect:customer";

        }
        else if(user.getRoles().contains(roleRepository.findByRole("TECH")))
        {
            // Direct to tech portal which links to admin panel
            return "redirect:tech";
        }
        else{
            // There's a problem, and we should logout the users and direct to login
            return "redirect:logout";
        }
    }
}