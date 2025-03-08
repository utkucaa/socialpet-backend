package com.example.social_pet.controller;

import com.example.social_pet.dto.*;
import com.example.social_pet.entities.*;
import com.example.social_pet.service.MedicalRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/pets/{petId}/medical-records")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    // Get all medical records for a pet
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllMedicalRecords(@PathVariable Long petId) {
        Map<String, Object> allRecords = medicalRecordService.getAllMedicalRecords(petId);
        return ResponseEntity.ok(allRecords);
    }

    // Treatment endpoints
    @GetMapping("/treatments")
    public ResponseEntity<List<Treatment>> getTreatments(@PathVariable Long petId) {
        List<Treatment> treatments = medicalRecordService.getTreatments(petId);
        return ResponseEntity.ok(treatments);
    }

    @GetMapping("/treatments/{id}")
    public ResponseEntity<Treatment> getTreatment(@PathVariable Long id) {
        Treatment treatment = medicalRecordService.getTreatment(id);
        return ResponseEntity.ok(treatment);
    }

    @PostMapping("/treatments")
    public ResponseEntity<Treatment> addTreatment(
            @PathVariable Long petId,
            @RequestBody TreatmentRequest request) {
        Treatment treatment = medicalRecordService.addTreatment(petId, request);
        return new ResponseEntity<>(treatment, HttpStatus.CREATED);
    }

    @PutMapping("/treatments/{id}")
    public ResponseEntity<Treatment> updateTreatment(
            @PathVariable Long id,
            @RequestBody TreatmentRequest request) {
        Treatment treatment = medicalRecordService.updateTreatment(id, request);
        return ResponseEntity.ok(treatment);
    }

    @DeleteMapping("/treatments/{id}")
    public ResponseEntity<Void> deleteTreatment(@PathVariable Long id) {
        medicalRecordService.deleteTreatment(id);
        return ResponseEntity.noContent().build();
    }

    // Vaccination endpoints
    @GetMapping("/vaccinations")
    public ResponseEntity<List<Vaccination>> getVaccinations(@PathVariable Long petId) {
        List<Vaccination> vaccinations = medicalRecordService.getVaccinations(petId);
        return ResponseEntity.ok(vaccinations);
    }

    @GetMapping("/vaccinations/{id}")
    public ResponseEntity<Vaccination> getVaccination(@PathVariable Long id) {
        Vaccination vaccination = medicalRecordService.getVaccination(id);
        return ResponseEntity.ok(vaccination);
    }

    @PostMapping("/vaccinations")
    public ResponseEntity<Vaccination> addVaccination(
            @PathVariable Long petId,
            @RequestBody VaccinationRequest request) {
        Vaccination vaccination = medicalRecordService.addVaccination(petId, request);
        return new ResponseEntity<>(vaccination, HttpStatus.CREATED);
    }

    @PutMapping("/vaccinations/{id}")
    public ResponseEntity<Vaccination> updateVaccination(
            @PathVariable Long id,
            @RequestBody VaccinationRequest request) {
        Vaccination vaccination = medicalRecordService.updateVaccination(id, request);
        return ResponseEntity.ok(vaccination);
    }

    @DeleteMapping("/vaccinations/{id}")
    public ResponseEntity<Void> deleteVaccination(@PathVariable Long id) {
        medicalRecordService.deleteVaccination(id);
        return ResponseEntity.noContent().build();
    }

    // Allergy endpoints
    @GetMapping("/allergies")
    public ResponseEntity<List<Allergy>> getAllergies(@PathVariable Long petId) {
        List<Allergy> allergies = medicalRecordService.getAllergies(petId);
        return ResponseEntity.ok(allergies);
    }

    @GetMapping("/allergies/{id}")
    public ResponseEntity<Allergy> getAllergy(@PathVariable Long id) {
        Allergy allergy = medicalRecordService.getAllergy(id);
        return ResponseEntity.ok(allergy);
    }

    @PostMapping("/allergies")
    public ResponseEntity<Allergy> addAllergy(
            @PathVariable Long petId,
            @RequestBody AllergyRequest request) {
        Allergy allergy = medicalRecordService.addAllergy(petId, request);
        return new ResponseEntity<>(allergy, HttpStatus.CREATED);
    }

    @PutMapping("/allergies/{id}")
    public ResponseEntity<Allergy> updateAllergy(
            @PathVariable Long id,
            @RequestBody AllergyRequest request) {
        Allergy allergy = medicalRecordService.updateAllergy(id, request);
        return ResponseEntity.ok(allergy);
    }

    @DeleteMapping("/allergies/{id}")
    public ResponseEntity<Void> deleteAllergy(@PathVariable Long id) {
        medicalRecordService.deleteAllergy(id);
        return ResponseEntity.noContent().build();
    }

    // Appointment endpoints
    @GetMapping("/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable Long petId) {
        List<Appointment> appointments = medicalRecordService.getAppointments(petId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/appointments/{id}")
    public ResponseEntity<Appointment> getAppointment(@PathVariable Long id) {
        Appointment appointment = medicalRecordService.getAppointment(id);
        return ResponseEntity.ok(appointment);
    }

    @PostMapping("/appointments")
    public ResponseEntity<Appointment> addAppointment(
            @PathVariable Long petId,
            @RequestBody AppointmentRequest request) {
        Appointment appointment = medicalRecordService.addAppointment(petId, request);
        return new ResponseEntity<>(appointment, HttpStatus.CREATED);
    }

    @PutMapping("/appointments/{id}")
    public ResponseEntity<Appointment> updateAppointment(
            @PathVariable Long id,
            @RequestBody AppointmentRequest request) {
        Appointment appointment = medicalRecordService.updateAppointment(id, request);
        return ResponseEntity.ok(appointment);
    }

    @DeleteMapping("/appointments/{id}")
    public ResponseEntity<Void> deleteAppointment(@PathVariable Long id) {
        medicalRecordService.deleteAppointment(id);
        return ResponseEntity.noContent().build();
    }

    // WeightRecord endpoints
    @GetMapping("/weight-records")
    public ResponseEntity<List<WeightRecord>> getWeightRecords(@PathVariable Long petId) {
        List<WeightRecord> weightRecords = medicalRecordService.getWeightRecords(petId);
        return ResponseEntity.ok(weightRecords);
    }

    @GetMapping("/weight-records/{id}")
    public ResponseEntity<WeightRecord> getWeightRecord(@PathVariable Long id) {
        WeightRecord weightRecord = medicalRecordService.getWeightRecord(id);
        return ResponseEntity.ok(weightRecord);
    }

    @PostMapping("/weight-records")
    public ResponseEntity<WeightRecord> addWeightRecord(
            @PathVariable Long petId,
            @RequestBody WeightRecordRequest request) {
        WeightRecord weightRecord = medicalRecordService.addWeightRecord(petId, request);
        return new ResponseEntity<>(weightRecord, HttpStatus.CREATED);
    }

    @PutMapping("/weight-records/{id}")
    public ResponseEntity<WeightRecord> updateWeightRecord(
            @PathVariable Long id,
            @RequestBody WeightRecordRequest request) {
        WeightRecord weightRecord = medicalRecordService.updateWeightRecord(id, request);
        return ResponseEntity.ok(weightRecord);
    }

    @DeleteMapping("/weight-records/{id}")
    public ResponseEntity<Void> deleteWeightRecord(@PathVariable Long id) {
        medicalRecordService.deleteWeightRecord(id);
        return ResponseEntity.noContent().build();
    }

    // Medication endpoints
    @GetMapping("/medications")
    public ResponseEntity<List<Medication>> getMedications(@PathVariable Long petId) {
        List<Medication> medications = medicalRecordService.getMedications(petId);
        return ResponseEntity.ok(medications);
    }

    @GetMapping("/medications/{id}")
    public ResponseEntity<Medication> getMedication(@PathVariable Long id) {
        Medication medication = medicalRecordService.getMedication(id);
        return ResponseEntity.ok(medication);
    }

    @PostMapping("/medications")
    public ResponseEntity<Medication> addMedication(
            @PathVariable Long petId,
            @RequestBody MedicationRequest request) {
        Medication medication = medicalRecordService.addMedication(petId, request);
        return new ResponseEntity<>(medication, HttpStatus.CREATED);
    }

    @PutMapping("/medications/{id}")
    public ResponseEntity<Medication> updateMedication(
            @PathVariable Long id,
            @RequestBody MedicationRequest request) {
        Medication medication = medicalRecordService.updateMedication(id, request);
        return ResponseEntity.ok(medication);
    }

    @DeleteMapping("/medications/{id}")
    public ResponseEntity<Void> deleteMedication(@PathVariable Long id) {
        medicalRecordService.deleteMedication(id);
        return ResponseEntity.noContent().build();
    }
} 