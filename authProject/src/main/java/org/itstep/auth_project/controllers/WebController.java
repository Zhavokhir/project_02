package org.itstep.auth_project.controllers;

import org.itstep.auth_project.config.StaticConfig;
import org.itstep.auth_project.entities.DbUser;
import org.itstep.auth_project.entities.Role;
import org.itstep.auth_project.models.UserModel;
import org.itstep.auth_project.services.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.ArrayList;

@Controller
public class WebController {

    private final UserService userService;

    public WebController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "login")
    public String loginPage() {
        return "login";
    }

    @GetMapping(value = "register")
    public String registerPage(Model model) {
        model.addAttribute("userModel", new UserModel());
        return "register";
    }

    @PostMapping(value = "register")
    public String registerUser(@ModelAttribute UserModel userModel) {
        if (userModel.getPassword().equals(userModel.getConfirmPassword())) {
            List<Role> roles = new ArrayList<>();
            roles.add(StaticConfig.ROLE_USER);
            DbUser dbUser = new DbUser(userModel.getEmail(), userModel.getPassword(), userModel.getFullName(), roles);
            userService.registerUser(dbUser);
            return "redirect:/login";
        }
        else {
            return "redirect:/register?error=1";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN','ROLE_MODERATOR')")
    @GetMapping(value = "/profile")
    public String profile(){
        return "profile";
    }

    @ModelAttribute
    public void addUser(Model model) {
        model.addAttribute("user", getUser());
    }

    private DbUser getUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            User securityUser = (User) authentication.getPrincipal();
            DbUser user = userService.getUser(securityUser.getUsername());
            return user;
        }
        return null;

    }
}
