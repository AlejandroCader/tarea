package com.tarea.tarea.service;

import com.tarea.tarea.entity.Address;
import com.tarea.tarea.entity.Patient;
import com.tarea.tarea.repository.AddressRepository;
import com.tarea.tarea.repository.PatientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class PatientService {
    @Autowired
    PatientRepository patientRepository;
    public Patient createPatient(Patient patient){
        patientRepository.save(patient);
        return patient;
    }

    @Transactional(readOnly = true)
    public List<Patient> findAll(){
        return (List<Patient>) patientRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<Patient> findAll(Pageable pageable) {
        return patientRepository.findAll(pageable);
    }

    @Transactional
    public void save(Patient patient) {
        patientRepository.save(patient);
    }

    @Transactional(readOnly = true)
    public Patient findById(Long id) {
        return patientRepository.findById(id).orElse(null);
    }

    @Transactional
    public void delete(Long id) {
        patientRepository.deleteById(id);
    }

}
