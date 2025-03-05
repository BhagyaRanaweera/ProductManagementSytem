package com.example.productorderingsystem.service.impl;

import com.example.productorderingsystem.dto.AddressDto;
import com.example.productorderingsystem.entity.Address;
import com.example.productorderingsystem.entity.User;
import com.example.productorderingsystem.repository.AddressRepo;
import com.example.productorderingsystem.repository.UserRepository;
import com.example.productorderingsystem.service.interf.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressRepo addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public AddressDto saveAddress(AddressDto addressDto, String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Address address = new Address();
        address.setStreet(addressDto.getStreet());
        address.setCity(addressDto.getCity());
        address.setState(addressDto.getState());
        address.setZipCode(addressDto.getZipCode());
        address.setCountry(addressDto.getCountry());
        address.setUser(user);

        Address savedAddress = addressRepository.save(address);

        return mapToDto(savedAddress);
    }

    @Override
    public List<AddressDto> getAddressesByUserId(String userId) {
        List<Address> addresses = addressRepository.findByUserId(userId);
        return addresses.stream().map(this::mapToDto).collect(Collectors.toList());
    }

    private AddressDto mapToDto(Address address) {
        AddressDto dto = new AddressDto();
        dto.setId(address.getId());
        dto.setStreet(address.getStreet());
        dto.setCity(address.getCity());
        dto.setState(address.getState());
        dto.setZipCode(address.getZipCode());
        dto.setCountry(address.getCountry());
        return dto;
    }
}
