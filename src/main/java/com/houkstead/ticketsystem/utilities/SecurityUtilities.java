package com.houkstead.ticketsystem.utilities;

import com.houkstead.ticketsystem.models.User;
import com.houkstead.ticketsystem.repository.RoleRepository;

public class SecurityUtilities {

    public static boolean isAdmin(User user, RoleRepository roleRepository){
        boolean returnValue = false;
        if(user.getRoles().contains(roleRepository.findByRole("ADMIN")))
        {
            returnValue = true;
        }

        return returnValue;
    }

    public static boolean isTech(User user, RoleRepository roleRepository){
        boolean returnValue = false;
        if(user.getRoles().contains(roleRepository.findByRole("TECH")))
        {
            returnValue = true;
        }

        return returnValue;
    }

    public static boolean isUser(User user, RoleRepository roleRepository){
        boolean returnValue = false;
        if(user.getRoles().contains(roleRepository.findByRole("USER")))
        {
            returnValue = true;
        }

        return returnValue;
    }


    public static boolean isUserAdmin(User user, RoleRepository roleRepository){
        boolean returnValue = false;
        if(user.getRoles().contains(roleRepository.findByRole("USER-ADMIN")))
        {
            returnValue = true;
        }

        return returnValue;
    }


}
