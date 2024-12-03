package com.example.productorderingsystem.controller;


import com.example.productorderingsystem.dto.Response;
import com.example.productorderingsystem.service.interf.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * Get all users. Only accessible by admins.
     */
    @GetMapping("/get-all")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Response> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    /**
     * Get the user's information and order history.
     */
    // @GetMapping("/my-info")
    // public ResponseEntity<Response> getUserInfoAndOrderHistory() {
    //     return ResponseEntity.ok(userService.getUserInfoAndOrderHistory());
    // }
}
