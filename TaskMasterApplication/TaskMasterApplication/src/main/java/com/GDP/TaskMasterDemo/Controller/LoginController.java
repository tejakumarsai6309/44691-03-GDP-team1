package com.GDP.TaskMasterDemo.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.GDP.TaskMasterDemo.Services.UserService;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;

    @GetMapping("/")
    public String showLoginForm() {
        //login form is submitted using POST method <form th:action="@{/login}" method="post">
        return "forms/login";
    }
    
    @GetMapping("/reset-password")
    public String showResetPasswordForm() {
        // Display the reset password form
        return "forms/reset-password";
    }
    
    @PostMapping("/reset-password")
    public String changePassword(@RequestParam("email") String email, @RequestParam("password") String newPassword) {
        if (userService.isUserEmailPresent(email)) {
            userService.changePassword(email, newPassword);
            System.out.println("Password : "+ newPassword);
            return "forms/login";
        } else {
            System.out.println("email : "+ email);
            return "forms/reset-password";
        }
    }
}