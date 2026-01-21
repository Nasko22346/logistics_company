package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public String index() {
        return "home";
    }

    @PostMapping("/register")
    public String register(
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword
    ) {
        if (!password.equals(confirmPassword)) {
            return "redirect:/?error=password_mismatch";
        }

        try {
            userService.register(email, password);
            return "redirect:/?success";
        } catch (Exception e) {
            return "redirect:/?error=register_failed";
        }
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

//    @PostMapping("/login")
//    public String login(
//            @RequestParam String email,
//            @RequestParam String password
//    ) {
//        try {
//            userService.login(email, password);
//            return "redirect:/home";
//        } catch (Exception e) {
//            return "redirect:/?error=login_failed";
//        }
//    }
}
