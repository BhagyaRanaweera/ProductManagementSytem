package com.example.productorderingsystem.service.interf;

import com.example.productorderingsystem.dto.AddressDto;

import java.util.List;

public interface AddressService {
    AddressDto saveAddress(AddressDto addressDto, String userId);
    List<AddressDto> getAddressesByUserId(String userId);
}
