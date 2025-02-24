package com.example.social_pet.controller;

import com.example.social_pet.dto.MedicalRecordRequest;
import com.example.social_pet.entities.*;
import com.example.social_pet.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/medical-records")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    // MedicalRecord endpoints
    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecordRequest request) {
        MedicalRecord medicalRecord = medicalRecordService.createMedicalRecord(request.getPetId());
        return ResponseEntity.ok(medicalRecord);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MedicalRecord> getMedicalRecord(@PathVariable Long id) {
        MedicalRecord medicalRecord = medicalRecordService.getMedicalRecord(id);
        return ResponseEntity.ok(medicalRecord);
    }

    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<MedicalRecord>> getMedicalRecordsByPetId(@PathVariable Long petId) {
        List<MedicalRecord> records = medicalRecordService.getMedicalRecordsByPetId(petId);
        return ResponseEntity.ok(records);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedicalRecord(@PathVariable Long id) {
        medicalRecordService.deleteMedicalRecord(id);
        return ResponseEntity.ok().build();
    }

    // Vaccination endpoints
    @PostMapping("/{id}/vaccinations")
    public ResponseEntity<Vaccination> addVaccination(@PathVariable Long id, @RequestBody Vaccination vaccination) {
        Vaccination savedVaccination = medicalRecordService.addVaccination(id, vaccination);
        return ResponseEntity.ok(savedVaccination);
    }

    @GetMapping("/{id}/vaccinations")
    public ResponseEntity<List<Vaccination>> getVaccinations(@PathVariable Long id) {
        List<Vaccination> vaccinations = medicalRecordService.getVaccinations(id);
        return ResponseEntity.ok(vaccinations);
    }

    // Treatment endpoints
    @PostMapping("/{id}/treatments")
    public ResponseEntity<Treatment> addTreatment(@PathVariable Long id, @RequestBody Treatment treatment) {
        Treatment savedTreatment = medicalRecordService.addTreatment(id, treatment);
        return ResponseEntity.ok(savedTreatment);
    }

    @GetMapping("/{id}/treatments")
    public ResponseEntity<List<Treatment>> getTreatments(@PathVariable Long id) {
        List<Treatment> treatments = medicalRecordService.getTreatments(id);
        return ResponseEntity.ok(treatments);
    }

    // Appointment endpoints
    @PostMapping("/{id}/appointments")
    public ResponseEntity<Appointment> addAppointment(@PathVariable Long id, @RequestBody Appointment appointment) {
        Appointment savedAppointment = medicalRecordService.addAppointment(id, appointment);
        return ResponseEntity.ok(savedAppointment);
    }

    @GetMapping("/{id}/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable Long id) {
        List<Appointment> appointments = medicalRecordService.getAppointments(id);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping("/{id}/appointments/upcoming")
    public ResponseEntity<List<Appointment>> getUpcomingAppointments(@PathVariable Long id) {
        List<Appointment> appointments = medicalRecordService.getUpcomingAppointments(id);
        return ResponseEntity.ok(appointments);
    }

    // Medication endpoints
    @PostMapping("/{id}/medications")
    public ResponseEntity<Medication> addMedication(@PathVariable Long id, @RequestBody Medication medication) {
        Medication savedMedication = medicalRecordService.addMedication(id, medication);
        return ResponseEntity.ok(savedMedication);
    }

    @GetMapping("/{id}/medications")
    public ResponseEntity<List<Medication>> getMedications(@PathVariable Long id) {
        List<Medication> medications = medicalRecordService.getMedications(id);
        return ResponseEntity.ok(medications);
    }

    @GetMapping("/{id}/medications/current")
    public ResponseEntity<List<Medication>> getCurrentMedications(@PathVariable Long id) {
        List<Medication> medications = medicalRecordService.getCurrentMedications(id);
        return ResponseEntity.ok(medications);
    }

    // Allergy endpoints
    @PostMapping("/{id}/allergies")
    public ResponseEntity<Allergy> addAllergy(@PathVariable Long id, @RequestBody Allergy allergy) {
        Allergy savedAllergy = medicalRecordService.addAllergy(id, allergy);
        return ResponseEntity.ok(savedAllergy);
    }

    @GetMapping("/{id}/allergies")
    public ResponseEntity<List<Allergy>> getAllergies(@PathVariable Long id) {
        List<Allergy> allergies = medicalRecordService.getAllergies(id);
        return ResponseEntity.ok(allergies);
    }

    // Weight record endpoints
    @PostMapping("/{id}/weight-records")
    public ResponseEntity<WeightRecord> addWeightRecord(@PathVariable Long id, @RequestBody WeightRecord weightRecord) {
        WeightRecord savedWeightRecord = medicalRecordService.addWeightRecord(id, weightRecord);
        return ResponseEntity.ok(savedWeightRecord);
    }

    @GetMapping("/{id}/weight-records")
    public ResponseEntity<List<WeightRecord>> getWeightRecords(@PathVariable Long id) {
        List<WeightRecord> weightRecords = medicalRecordService.getWeightRecords(id);
        return ResponseEntity.ok(weightRecords);
    }

    @GetMapping("/{id}/weight-records/latest")
    public ResponseEntity<WeightRecord> getLatestWeight(@PathVariable Long id) {
        Optional<WeightRecord> latestWeight = medicalRecordService.getLatestWeight(id);
        return latestWeight.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
