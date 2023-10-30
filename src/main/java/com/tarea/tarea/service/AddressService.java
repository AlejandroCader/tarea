package com.tarea.tarea.service;

import com.tarea.tarea.entity.Address;
import com.tarea.tarea.entity.Appointment;
import com.tarea.tarea.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    @Transactional
    public void save(Address address) {
        addressRepository.save(address);
    }
    @Transactional
    public void delete(Long id) {
        addressRepository.deleteById(id);
    }

    @Transactional
    public void deleteAddressesByPatientId(Long patientId) {
        List<Address> appointmentsToDelete = addressRepository.findByPatientId(patientId);
        addressRepository.deleteAll(appointmentsToDelete);
    }
}
