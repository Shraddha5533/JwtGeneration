package com.loginsecurityexample.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import com.loginsecurityexample.config.AuthenticationRequest;
import com.loginsecurityexample.entity.UserInfo;
import com.loginsecurityexample.service.JwtService;
import com.loginsecurityexample.service.UserInfoServic;
import java.util.List;

@RestController
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private UserInfoServic service;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/addNewUser")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return service.addUser(userInfo);
    }

    @PostMapping("/generateToken")
    public String authenticateAndGetToken(@RequestBody AuthenticationRequest authRequest) {
        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authentication.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getUsername());
        } else {
            throw new UsernameNotFoundException("Invalid user request!");
        }
    }

    @GetMapping("/message")
    public String message() {
        return "This is controller class";
    }

    // CRUD operations

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserInfo> getAllUsers() {
        return service.getAllUsers();
    }

    @GetMapping("/users/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public UserInfo getUserById(@PathVariable int id) {
        return service.getUserById(id);
    }

    @PostMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public UserInfo createUser(@RequestBody UserInfo userInfo) {
        return service.createUser(userInfo);
    }

    @PutMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public UserInfo updateUser(@PathVariable int id, @RequestBody UserInfo userInfo) {
        return service.updateUser(id, userInfo);
    }

    @DeleteMapping("/users/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteUser(@PathVariable int id) {
        service.deleteUser(id);
    }
}
