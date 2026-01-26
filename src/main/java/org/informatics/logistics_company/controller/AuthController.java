package org.informatics.logistics_company.controller;

import org.informatics.logistics_company.exception.EmailAlreadyExistsException;
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
            @RequestParam String firstName,
            @RequestParam String middleName,
            @RequestParam String lastName,
            @RequestParam String phoneNumber,
            @RequestParam String email,
            @RequestParam String password,
            @RequestParam String confirmPassword
    ) {
        if (!password.equals(confirmPassword)) {
            return "redirect:/register?error=password_mismatch";
        }

        try {
            userService.register(firstName, middleName, lastName, phoneNumber, email, password);
            return "redirect:/login-page?registered";
        } catch (EmailAlreadyExistsException e) {
            return "redirect:/register?error=email_failed";
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/register?error=register_failed";
        }
    }

    @GetMapping("/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }
}