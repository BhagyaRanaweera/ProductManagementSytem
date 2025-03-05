package com.example.productorderingsystem.controller;

import com.example.productorderingsystem.dto.AddressDto;
import com.example.productorderingsystem.service.interf.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/addresses")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @PostMapping("/{userId}")
    public AddressDto saveAddress(@RequestBody AddressDto addressDto, @PathVariable String userId) {
        return addressService.saveAddress(addressDto, userId);
    }

    @GetMapping("/user/{userId}")
    public List<AddressDto> getAddressesByUser(@PathVariable String userId) {
        return addressService.getAddressesByUserId(userId);
    }
}
