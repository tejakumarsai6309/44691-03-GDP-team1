package com.GDP.TaskMasterDemo.Controller;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.GDP.TaskMasterDemo.Model.Role;
import com.GDP.TaskMasterDemo.Model.User;
import com.GDP.TaskMasterDemo.Repository.RoleRepository;
import com.GDP.TaskMasterDemo.Services.UserService;

@Controller
public class RegisterController {

    private UserService userService;
    
    @Autowired
    private RoleRepository roleRepository;

    
    @Autowired
    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new User());
        return "forms/register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "forms/register";
        }

        if (userService.isUserEmailPresent(user.getEmail())) {
            model.addAttribute("exist", true);
            return "register";
        }
        
     // Fetch the role based on user selection in the form
        String selectedRole = user.getRoles().get(0).getRole(); 
        System.out.println("selectedRole : "+selectedRole);

        // assuming only one role for simplicity
        Role userRole = roleRepository.findByRole(selectedRole);
        System.out.println("userRole : "+userRole);

        // If role doesn't exist, create a new one
        if (userRole == null) {
            userRole = new Role();
            userRole.setRole(selectedRole);
            roleRepository.save(userRole);
        }
        
        user.setRoles(Collections.singletonList(userRole));

        
        System.out.println(user.toString());
        System.out.println("Roles : "+user.getRoles());

        userService.createUser(user);
        return "views/success";
    }


}
