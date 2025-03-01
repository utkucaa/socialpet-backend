package com.example.social_pet.controller;

import com.example.social_pet.dto.MedicalRecordRequest;
import com.example.social_pet.entities.*;
import com.example.social_pet.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // Medical Record CRUD operations
    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecordRequest request) {
        MedicalRecord medicalRecord = medicalRecordService.createMedicalRecord(request.getPetId());
        return new ResponseEntity<>(medicalRecord, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecord(@PathVariable Long id) {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(id);
        return ResponseEntity.ok(medicalRecord);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPetId(@PathVariable Long petId) {
        List<MedicalRecord> medicalRecords = medicalRecordService.getMedicalRecordsByPetId(petId);
        return ResponseEntity.ok(medicalRecords);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.noContent().build();
    }

    // Vaccination operations
    @PostMapping("/{medicalRecordId}/vaccinations")
    public ResponseEntity<Vaccination> addVaccination(
            @PathVariable Long medicalRecordId,
            @RequestBody Vaccination vaccination) {
        Vaccination savedVaccination = medicalRecordService.addVaccination(medicalRecordId, vaccination);
        return new ResponseEntity<>(savedVaccination, HttpStatus.CREATED);
    }

    @GetMapping("/{medicalRecordId}/vaccinations")
    public ResponseEntity<List<Vaccination>> getVaccinations(@PathVariable Long medicalRecordId) {
        List<Vaccination> vaccinations = medicalRecordService.getVaccinations(medicalRecordId);
        return ResponseEntity.ok(vaccinations);
    }

    // Treatment operations
    @PostMapping("/{medicalRecordId}/treatments")
    public ResponseEntity<Treatment> addTreatment(
            @PathVariable Long medicalRecordId,
            @RequestBody Treatment treatment) {
        Treatment savedTreatment = medicalRecordService.addTreatment(medicalRecordId, treatment);
        return new ResponseEntity<>(savedTreatment, HttpStatus.CREATED);
    }

    @GetMapping("/{medicalRecordId}/treatments")
    public ResponseEntity<List<Treatment>> getTreatments(@PathVariable Long medicalRecordId) {
        List<Treatment> treatments = medicalRecordService.getTreatments(medicalRecordId);
        return ResponseEntity.ok(treatments);
    }

    // Appointment operations
    @PostMapping("/{medicalRecordId}/appointments")
    public ResponseEntity<Appointment> addAppointment(
            @PathVariable Long medicalRecordId,
            @RequestBody Appointment appointment) {
        Appointment savedAppointment = medicalRecordService.addAppointment(medicalRecordId, appointment);
        return new ResponseEntity<>(savedAppointment, HttpStatus.CREATED);
    }

    @GetMapping("/{medicalRecordId}/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable Long medicalRecordId) {
        List<Appointment> appointments = medicalRecordService.getAppointments(medicalRecordId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{medicalRecordId}/appointments/upcoming")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(@PathVariable Long medicalRecordId) {
        List<Appointment> upcomingAppointments = medicalRecordService.getUpcomingAppointments(medicalRecordId);
        return ResponseEntity.ok(upcomingAppointments);
    }

    // Medication operations
    @PostMapping("/{medicalRecordId}/medications")
    public ResponseEntity<Medication> addMedication(
            @PathVariable Long medicalRecordId,
            @RequestBody Medication medication) {
        Medication savedMedication = medicalRecordService.addMedication(medicalRecordId, medication);
        return new ResponseEntity<>(savedMedication, HttpStatus.CREATED);
    }

    @GetMapping("/{medicalRecordId}/medications")
    public ResponseEntity<List<Medication>> getMedications(@PathVariable Long medicalRecordId) {
        List<Medication> medications = medicalRecordService.getMedications(medicalRecordId);
        return ResponseEntity.ok(medications);
    }

    @GetMapping("/{medicalRecordId}/medications/current")
    public ResponseEntity<List<Medication>> getCurrentMedications(@PathVariable Long medicalRecordId) {
        List<Medication> currentMedications = medicalRecordService.getCurrentMedications(medicalRecordId);
        return ResponseEntity.ok(currentMedications);
    }

    // Allergy operations
    @PostMapping("/{medicalRecordId}/allergies")
    public ResponseEntity<Allergy> addAllergy(
            @PathVariable Long medicalRecordId,
            @RequestBody Allergy allergy) {
        Allergy savedAllergy = medicalRecordService.addAllergy(medicalRecordId, allergy);
        return new ResponseEntity<>(savedAllergy, HttpStatus.CREATED);
    }

    @GetMapping("/{medicalRecordId}/allergies")
    public ResponseEntity<List<Allergy>> getAllergies(@PathVariable Long medicalRecordId) {
        List<Allergy> allergies = medicalRecordService.getAllergies(medicalRecordId);
        return ResponseEntity.ok(allergies);
    }

    // Weight Record operations
    @PostMapping("/{medicalRecordId}/weight-records")
    public ResponseEntity<WeightRecord> addWeightRecord(
            @PathVariable Long medicalRecordId,
            @RequestBody WeightRecord weightRecord) {
        WeightRecord savedWeightRecord = medicalRecordService.addWeightRecord(medicalRecordId, weightRecord);
        return new ResponseEntity<>(savedWeightRecord, HttpStatus.CREATED);
    }

    @GetMapping("/{medicalRecordId}/weight-records")
    public ResponseEntity<List<WeightRecord>> getWeightRecords(@PathVariable Long medicalRecordId) {
        List<WeightRecord> weightRecords = medicalRecordService.getWeightRecords(medicalRecordId);
        return ResponseEntity.ok(weightRecords);
    }

    @GetMapping("/{medicalRecordId}/weight-records/latest")
    public ResponseEntity<WeightRecord> getLatestWeightRecord(@PathVariable Long medicalRecordId) {
        Optional<WeightRecord> latestWeightRecord = medicalRecordService.getLatestWeight(medicalRecordId);
        return latestWeightRecord
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
