package com.tarea.tarea.service;

import com.tarea.tarea.entity.Address;
import com.tarea.tarea.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressService {
    @Autowired
    AddressRepository addressRepository;
    public Address createAddress(Address address){
        addressRepository.save(address);
        return address;
    }
    public Address findById(Long id){
        return addressRepository.findById(id).orElse(null);
    }
}
