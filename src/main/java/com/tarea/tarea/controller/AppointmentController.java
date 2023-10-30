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
    public String view(@PathVariable(value = "id") Long id, Map<String, Object> model,
                      RedirectAttributes flash){
        Patient patient = patientService.findOne(id);
        Address address = addressService.findById(id);
        Appointment appointment = appointmentService.findById(id);

        if (patient == null) {
            flash.addFlashAttribute("error", "El cliente no existe en la BD");
            return "redirect:/appointmentList";
        }

        model.put("address", address);
        model.put("appointment", appointment);
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

        return "redirect:/list";
    }

    @GetMapping(value = "/edit/{id}")
    public String edit(@PathVariable(value = "id") Long id, Model model, RedirectAttributes flash) {
        Patient patient = patientService.findOne(id);

        if (patient != null) {
            // Obtener la cita relacionada con el paciente
            Appointment appointment = appointmentService.findById(id);
            Address address = addressService.findById(id);

            // Cargar los datos del paciente, la dirección y la cita en el modelo
            model.addAttribute("patient", patient);
            model.addAttribute("appointment", appointment);
            model.addAttribute("address", address);

            // Asegurarse de que el formulario de edición en la vista sepa si se está creando una nueva cita o editando una existente
            model.addAttribute("editMode", true);


            return "editAppointmentView"; // Vista de edición
        } else {
            flash.addFlashAttribute("error", "El paciente no existe en la BD.");
            return "redirect:/list";
        }
    }

    @PostMapping(value = "/edit/{id}")
    public String update(@PathVariable(value = "id") Long id, Patient updatedPatient
            , Appointment updatedAppointment, Address updatedAddress
            , RedirectAttributes flash) {

        // Obtener el paciente original
        Patient originalPatient = patientService.findOne(id);
        Appointment originalAppointment = appointmentService.findById(id);
        Address originalAddress = addressService.findById(id);

        if (originalPatient != null) {
            // Actualiza los datos del paciente con los datos del formulario
            originalPatient.setName(updatedPatient.getName());
            originalPatient.setLastName(updatedPatient.getLastName());
            originalPatient.setBirthdate(updatedPatient.getBirthdate());

            // Actualiza los datos de la cita con los datos del formulario
            originalAppointment.setDay(updatedAppointment.getDay());
            originalAppointment.setTime(updatedAppointment.getTime());

            // Actualiza los datos de Address si es necesario
            originalAddress.setAddress1(updatedAddress.getAddress1());
            originalAddress.setAddress2(updatedAddress.getAddress2());
            originalAddress.setCity(updatedAddress.getCity());
            originalAddress.setRegion(updatedAddress.getRegion());
            originalAddress.setZipCode(updatedAddress.getZipCode());

            // Guarda los cambios en la base de datos
            patientService.save(originalPatient);
            appointmentService.save(originalAppointment);
            addressService.save(originalAddress);

            flash.addFlashAttribute("success", "Data has been successfully changed.");
            return "redirect:/list";
        } else {
            flash.addFlashAttribute("error", "The patient doesn't exist in the DB");
            return "redirect:/list";
        }
    }



    @RequestMapping(value = "/delete/{id}")
    public String deleteAppointment(@PathVariable(value = "id") Long id, RedirectAttributes flash){
        if (id > 0) {
            Patient patient = patientService.findOne(id);

            if (patient != null) {
                addressService.deleteAddressesByPatientId(id);
                appointmentService.deleteAppointmentsByPatientId(id);
                patientService.delete(id);
                flash.addFlashAttribute("success", "The patient, and his addresses and appointments have been deleted successfully.");
            } else {
                flash.addFlashAttribute("error", "The patient doesn't exist in the DB");
            }
        }
        return "redirect:/list";
    }
}
