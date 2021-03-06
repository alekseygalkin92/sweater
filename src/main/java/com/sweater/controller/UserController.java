package com.sweater.controller;

import com.sweater.domain.Role;
import com.sweater.domain.User;
import com.sweater.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userList(Model model) {
        model.addAttribute("users", userService.findAll());

        return "userList";
    }

    @GetMapping("{user}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user,
            Model model
    ) {

        if (StringUtils.isEmpty(username)) {
            model.addAttribute("message", "Username is empty");
            model.addAttribute("user", user);
            model.addAttribute("roles", Role.values());
            return "userEdit";
        }

        userService.saveUser(user, username, form);

        return "redirect:/user";
    }

    @GetMapping("profile")
    public String getProfile(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("username", user.getUsername());
        model.addAttribute("email", user.getEmail());

        return "profile";
    }

    @PostMapping("profile")
    public String updateProfile(
            @AuthenticationPrincipal User user,
            @RequestParam String password,
            @RequestParam String email,
            Model model) {

        if (StringUtils.isEmpty(password) && email.equals(user.getEmail())) {
            model.addAttribute("message", "Password is empty");
            model.addAttribute("username", user.getUsername());
            model.addAttribute("email", user.getEmail());
            return "profile";
        }

        userService.updateProfile(user, password, email);

        return "redirect:/user/profile";
    }
}
