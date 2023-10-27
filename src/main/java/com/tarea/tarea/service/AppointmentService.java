package com.tarea.tarea.service;

import com.tarea.tarea.entity.Address;
import com.tarea.tarea.entity.Appointment;
import com.tarea.tarea.repository.AddressRepository;
import com.tarea.tarea.repository.AppointmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AppointmentService {

    @Autowired
    AppointmentRepository appointmentRepository;
    public Appointment createAppointment(Appointment appointment){
        appointmentRepository.save(appointment);
        return appointment;
    }
    public Appointment findById(Long id){
        return appointmentRepository.findById(id).orElse(null);
    }
}
