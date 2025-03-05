package com.example.social_pet.controller;

import com.example.social_pet.dto.MedicalRecordRequest;
import com.example.social_pet.dto.VaccinationRequest;
import com.example.social_pet.entities.*;
import com.example.social_pet.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
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
    public ResponseEntity<?> addVaccination(
            @PathVariable Long medicalRecordId,
            @RequestBody VaccinationRequest vaccinationRequest) {
        try {
            // String formatındaki tarihi LocalDate'e dönüştürüyoruz
            LocalDate vaccinationDate = LocalDate.parse(vaccinationRequest.getVaccinationDate());
            
            // Vaccination nesnesini oluşturuyoruz
            Vaccination vaccination = new Vaccination();
            vaccination.setVaccineName(vaccinationRequest.getVaccineName());
            vaccination.setVaccinationDate(vaccinationDate);
            vaccination.setVeterinarian(vaccinationRequest.getVeterinarian());
            
            // Vaccination'ı ekliyoruz
            Vaccination savedVaccination = medicalRecordService.addVaccination(medicalRecordId, vaccination);
            return new ResponseEntity<>(savedVaccination, HttpStatus.CREATED);
        } catch (DateTimeParseException e) {
            // Tarih formatı hatalıysa
            return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body("Invalid date format. Please use ISO format (YYYY-MM-DD): " + e.getMessage());
        } catch (Exception e) {
            // Diğer hatalar için
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to add vaccination: " + e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/vaccinations")
    public ResponseEntity<?> getVaccinations(@PathVariable Long medicalRecordId) {
        try {
            List<Vaccination> vaccinations = medicalRecordService.getVaccinations(medicalRecordId);
            return ResponseEntity.ok(vaccinations);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get vaccinations: " + e.getMessage());
        }
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
    public ResponseEntity<?> getTreatments(@PathVariable Long medicalRecordId) {
        try {
            List<Treatment> treatments = medicalRecordService.getTreatments(medicalRecordId);
            return ResponseEntity.ok(treatments);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get treatments: " + e.getMessage());
        }
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
    public ResponseEntity<?> getAppointments(@PathVariable Long medicalRecordId) {
        try {
            List<Appointment> appointments = medicalRecordService.getAppointments(medicalRecordId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get appointments: " + e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/appointments/upcoming")
    public ResponseEntity<?> getUpcomingAppointments(@PathVariable Long medicalRecordId) {
        try {
            List<Appointment> appointments = medicalRecordService.getUpcomingAppointments(medicalRecordId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get upcoming appointments: " + e.getMessage());
        }
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
    public ResponseEntity<?> getMedications(@PathVariable Long medicalRecordId) {
        try {
            List<Medication> medications = medicalRecordService.getMedications(medicalRecordId);
            return ResponseEntity.ok(medications);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get medications: " + e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/medications/current")
    public ResponseEntity<?> getCurrentMedications(@PathVariable Long medicalRecordId) {
        try {
            List<Medication> medications = medicalRecordService.getCurrentMedications(medicalRecordId);
            return ResponseEntity.ok(medications);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get current medications: " + e.getMessage());
        }
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
    public ResponseEntity<?> getAllergies(@PathVariable Long medicalRecordId) {
        try {
            List<Allergy> allergies = medicalRecordService.getAllergies(medicalRecordId);
            return ResponseEntity.ok(allergies);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get allergies: " + e.getMessage());
        }
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
    public ResponseEntity<?> getWeightRecords(@PathVariable Long medicalRecordId) {
        try {
            List<WeightRecord> weightRecords = medicalRecordService.getWeightRecords(medicalRecordId);
            return ResponseEntity.ok(weightRecords);
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get weight records: " + e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/weight-records/latest")
    public ResponseEntity<?> getLatestWeightRecord(@PathVariable Long medicalRecordId) {
        try {
            Optional<WeightRecord> weightRecord = medicalRecordService.getLatestWeight(medicalRecordId);
            return weightRecord.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Failed to get latest weight record: " + e.getMessage());
        }
    }
}
