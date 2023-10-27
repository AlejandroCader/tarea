package com.tarea.tarea.controller;

import com.tarea.tarea.entity.Address;
import com.tarea.tarea.entity.Appointment;
import com.tarea.tarea.entity.Patient;
import com.tarea.tarea.service.AddressService;
import com.tarea.tarea.service.AppointmentService;
import com.tarea.tarea.service.PatientService;
import com.tarea.tarea.util.PageRender;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class AppointmentController {

    @Autowired
    private AddressService addressService;
    @Autowired
    private AppointmentService appointmentService;
    @Autowired
    private PatientService patientService;

    @GetMapping(value = "/view/{id}")
    public String ver(@PathVariable(value = "id") Long id, Map<String, Object> model,
                      RedirectAttributes flash){
        Patient patient = patientService.findOne(id);
        if (patient == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la BD");
            return "redirect:/appointmentList";
        }
        model.put("patient", patient);
        model.put("title", "Patient Details: " + patient.getName());
        return "appointmentView";
    }

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(@RequestParam(name = "page", defaultValue = "0") int page,
                         Model model){
        Pageable pageRequest = PageRequest.of(page, 4);
        Page<Patient> patients = patientService.findAll(pageRequest);


        PageRender<Patient> pageRender = new PageRender<>("/appointmentList", patients);

        model.addAttribute("title", "Appointments list");
        model.addAttribute("patients", patients) ;
        model.addAttribute("page", pageRender);
        return "appointmentList";
    }

    @RequestMapping(value = "/appointment")
    public String createAppointment(Map<String, Object> model){
        Appointment appointment = new Appointment();
        Address address = new Address();
        Patient patient = new Patient();

        model.put("appointment", appointment);
        model.put("address", address);
        model.put("patient", patient);
        model.put("title", "Appointment Form");
        return "appointmentForm";
    }

    @PostMapping("/appointment")
    public String saveAppointment(@Valid Address address, BindingResult addressResult,
                                  @Valid Appointment appointment, BindingResult appointmentResult,
                                  @Valid Patient patient,BindingResult patientResult, Model model){

        if (addressResult.hasErrors() | appointmentResult.hasErrors() | patientResult.hasErrors()){
            return "appointmentForm";
        }

        patientService.createPatient(patient);
        addressService.createAddress(address);
        appointmentService.createAppointment(appointment);

        address.setPatient(patient);
        appointment.setPatient(patient);

        addressService.createAddress(address);
        appointmentService.createAppointment(appointment);

        model.addAttribute("patient", patient);
        model.addAttribute("appointment", appointment);
        model.addAttribute("address", address);

        return "appointmentView";
    }
}
