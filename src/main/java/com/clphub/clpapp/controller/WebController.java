package com.clphub.clpapp.controller;


import javax.servlet.http.HttpServletRequest;

import com.clphub.clpapp.model.User;
import com.clphub.clpapp.service.SecurityService;
import com.clphub.clpapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
public class WebController {
    @Autowired
    UserService userService;

    @Autowired
    private SecurityService securityService;

    @RequestMapping(value="/main")
    public String main(){
        return "main";
    }

    @RequestMapping("/login")
    public String login(Model model, String error, String logout, HttpServletRequest request ){
        if (logout != null){
            model.addAttribute("logout", "You have been logged out successfully.");
        }
        return "login";
    }

    @RequestMapping(value="/loginError")
    public String loginError(Model model, String username ){
        model.addAttribute("error", "Your username and password is invalid.");
        model.addAttribute("username",username);
        return "login";
    }

    @GetMapping("/registration")
    public String registration(Model model){
        model.addAttribute("userForm", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String registration(@ModelAttribute("userForm") User userForm, BindingResult bindingResult,
                               Model model , String[] roles ){
        String password = userForm.getPassword();
        userService.saveUser(userForm,roles);
        securityService.autologin(userForm.getUsername(),password);
        return "redirect:/main";
    }

}

