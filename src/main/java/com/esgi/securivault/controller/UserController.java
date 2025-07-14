package com.esgi.securivault.controller;


import com.esgi.securivault.entity.User;
import com.esgi.securivault.services.UserServices;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserServices userServices;

    public UserController(UserServices userServices) {
        this.userServices = userServices;
    }

    @GetMapping("/by-email")
    public User getUserByEmail(@RequestParam String email) {
        return userServices.findByEmail(email);
    }

}


