package com.example.social_pet.controller;

import com.example.social_pet.dto.*;
import com.example.social_pet.entities.*;
import com.example.social_pet.service.MedicalRecordService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.ArrayList;

@RestController
@RequestMapping("/api/medical-records")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    @Autowired
    public MedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> createMedicalRecord(@RequestBody MedicalRecordRequest request) {
        MedicalRecord medicalRecord = medicalRecordService.createMedicalRecord(request.getPetId());
        return ResponseEntity.status(HttpStatus.CREATED).body(medicalRecord);
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

    // Vaccination endpoints
    @PostMapping("/{medicalRecordId}/vaccinations")
    public ResponseEntity<?> addVaccination(
            @PathVariable Long medicalRecordId,
            @RequestBody VaccinationRequest vaccinationRequest) {
        try {
            // Önce medical record'ın var olup olmadığını kontrol et
            try {
                medicalRecordService.getMedicalRecord(medicalRecordId);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medical record not found with id: " + medicalRecordId + ". Please create a medical record first.");
            }
            
            Vaccination vaccination = new Vaccination();
            vaccination.setVaccineName(vaccinationRequest.getVaccineName());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate vaccinationDate;
            try {
                vaccinationDate = LocalDate.parse(vaccinationRequest.getVaccinationDate(), formatter);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Please use YYYY-MM-DD format.");
            }
            vaccination.setVaccinationDate(vaccinationDate);
            
            vaccination.setVeterinarian(vaccinationRequest.getVeterinarian());
            
            Vaccination savedVaccination = medicalRecordService.addVaccination(medicalRecordId, vaccination);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedVaccination);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Failed to add vaccination: " + e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/vaccinations")
    public ResponseEntity<?> getVaccinations(@PathVariable Long medicalRecordId) {
        try {
            List<Vaccination> vaccinations = medicalRecordService.getVaccinations(medicalRecordId);
            return ResponseEntity.ok(vaccinations);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Treatment endpoints
    @PostMapping("/{medicalRecordId}/treatments")
    public ResponseEntity<?> addTreatment(
            @PathVariable Long medicalRecordId,
            @RequestBody TreatmentRequest treatmentRequest) {
        try {
            // Önce medical record'ın var olup olmadığını kontrol et
            try {
                medicalRecordService.getMedicalRecord(medicalRecordId);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medical record not found with id: " + medicalRecordId + ". Please create a medical record first.");
            }
            
            Treatment treatment = new Treatment();
            
            // Frontend'den gelen alan isimlerini kontrol et ve uygun şekilde eşleştir
            // treatmentName -> treatmentType
            if (treatmentRequest.getTreatmentType() != null) {
                treatment.setTreatmentType(treatmentRequest.getTreatmentType());
            } else if (treatmentRequest.getTreatmentName() != null) {
                treatment.setTreatmentType(treatmentRequest.getTreatmentName());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Treatment type/name is required");
            }
            
            // notes -> description
            if (treatmentRequest.getDescription() != null) {
                treatment.setDescription(treatmentRequest.getDescription());
            } else if (treatmentRequest.getNotes() != null) {
                treatment.setDescription(treatmentRequest.getNotes());
            }
            
            // Veterinarian alanını ayarla
            if (treatmentRequest.getVeterinarian() != null) {
                treatment.setVeterinarian(treatmentRequest.getVeterinarian());
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate treatmentDate;
            try {
                treatmentDate = LocalDate.parse(treatmentRequest.getTreatmentDate(), formatter);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Please use YYYY-MM-DD format.");
            }
            treatment.setTreatmentDate(treatmentDate);
            
            Treatment savedTreatment = medicalRecordService.addTreatment(medicalRecordId, treatment);
            
            // Frontend'in beklediği formatta yanıt döndür
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedTreatment.getId());
            response.put("treatmentName", savedTreatment.getTreatmentType());
            response.put("treatmentDate", savedTreatment.getTreatmentDate().toString());
            response.put("notes", savedTreatment.getDescription());
            response.put("veterinarian", savedTreatment.getVeterinarian());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Failed to add treatment: " + e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/treatments")
    public ResponseEntity<?> getTreatments(@PathVariable Long medicalRecordId) {
        try {
            List<Treatment> treatments = medicalRecordService.getTreatments(medicalRecordId);
            
            // Frontend'in beklediği formatta yanıt döndür
            List<Map<String, Object>> response = new ArrayList<>();
            for (Treatment treatment : treatments) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", treatment.getId());
                item.put("treatmentName", treatment.getTreatmentType());
                item.put("treatmentDate", treatment.getTreatmentDate().toString());
                item.put("notes", treatment.getDescription());
                item.put("veterinarian", treatment.getVeterinarian());
                response.add(item);
            }
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Appointment endpoints
    @PostMapping("/{medicalRecordId}/appointments")
    public ResponseEntity<?> addAppointment(
            @PathVariable Long medicalRecordId,
            @RequestBody AppointmentRequest appointmentRequest) {
        try {
            // Önce medical record'ın var olup olmadığını kontrol et
            try {
                medicalRecordService.getMedicalRecord(medicalRecordId);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medical record not found with id: " + medicalRecordId + ". Please create a medical record first.");
            }
            
            Appointment appointment = new Appointment();
            appointment.setVeterinarian(appointmentRequest.getVeterinarian());
            appointment.setReason(appointmentRequest.getReason());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate appointmentDate;
            try {
                appointmentDate = LocalDate.parse(appointmentRequest.getAppointmentDate(), formatter);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Please use YYYY-MM-DD format.");
            }
            appointment.setAppointmentDate(appointmentDate);
            
            Appointment savedAppointment = medicalRecordService.addAppointment(medicalRecordId, appointment);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Failed to add appointment: " + e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/appointments")
    public ResponseEntity<?> getAppointments(@PathVariable Long medicalRecordId) {
        try {
            List<Appointment> appointments = medicalRecordService.getAppointments(medicalRecordId);
            return ResponseEntity.ok(appointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/appointments/upcoming")
    public ResponseEntity<?> getUpcomingAppointments(@PathVariable Long medicalRecordId) {
        try {
            List<Appointment> upcomingAppointments = medicalRecordService.getUpcomingAppointments(medicalRecordId);
            return ResponseEntity.ok(upcomingAppointments);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Medication endpoints
    @PostMapping("/{medicalRecordId}/medications")
    public ResponseEntity<?> addMedication(
            @PathVariable Long medicalRecordId,
            @RequestBody MedicationRequest medicationRequest) {
        try {
            // Önce medical record'ın var olup olmadığını kontrol et
            try {
                medicalRecordService.getMedicalRecord(medicalRecordId);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Medical record not found with id: " + medicalRecordId + ". Please create a medical record first.");
            }
            
            Medication medication = new Medication();
            medication.setMedicationName(medicationRequest.getMedicationName());
            medication.setDosage(medicationRequest.getDosage());
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate startDate;
            LocalDate endDate;
            try {
                startDate = LocalDate.parse(medicationRequest.getStartDate(), formatter);
                endDate = LocalDate.parse(medicationRequest.getEndDate(), formatter);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Invalid date format. Please use YYYY-MM-DD format.");
            }
            medication.setStartDate(startDate);
            medication.setEndDate(endDate);
            
            Medication savedMedication = medicalRecordService.addMedication(medicalRecordId, medication);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedMedication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Failed to add medication: " + e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/medications")
    public ResponseEntity<?> getMedications(@PathVariable Long medicalRecordId) {
        try {
            List<Medication> medications = medicalRecordService.getMedications(medicalRecordId);
            return ResponseEntity.ok(medications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/{medicalRecordId}/medications/current")
    public ResponseEntity<?> getCurrentMedications(@PathVariable Long medicalRecordId) {
        try {
            List<Medication> currentMedications = medicalRecordService.getCurrentMedications(medicalRecordId);
            return ResponseEntity.ok(currentMedications);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    // Allergy endpoints
    @PostMapping("/{medicalRecordId}/allergies")
    public ResponseEntity<?> addAllergy(
            @PathVariable Long medicalRecordId,
            @RequestBody AllergyRequest allergyRequest) {
        try {
            // Önce medical record'ın var olup olmadığını kontrol et
            try {
                medicalRecordService.getMedicalRecord(medicalRecordId);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Medical record not found with id: " + medicalRecordId + ". Please create a medical record first."));
            }
            
            Allergy allergy = new Allergy();
            
            // Frontend'den gelen alan isimlerini kontrol et
            if (allergyRequest.getAllergen() != null) {
                allergy.setAllergen(allergyRequest.getAllergen());
            } else if (allergyRequest.getAllergyName() != null) {
                allergy.setAllergen(allergyRequest.getAllergyName());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Allergen/allergyName is required"));
            }
            
            // Reaction alanını ayarla
            if (allergyRequest.getReaction() != null) {
                allergy.setReaction(allergyRequest.getReaction());
            } else if (allergyRequest.getSymptoms() != null) {
                allergy.setReaction(allergyRequest.getSymptoms());
            }
            
            // Severity alanını ayarla
            if (allergyRequest.getSeverity() != null) {
                allergy.setSeverity(allergyRequest.getSeverity());
            }
            
            // Notes alanını ayarla
            if (allergyRequest.getNotes() != null) {
                allergy.setNotes(allergyRequest.getNotes());
            }
            
            Allergy savedAllergy = medicalRecordService.addAllergy(medicalRecordId, allergy);
            
            // Frontend'in beklediği formatta yanıt döndür
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedAllergy.getId());
            response.put("allergyName", savedAllergy.getAllergen());
            response.put("symptoms", savedAllergy.getReaction());
            response.put("severity", savedAllergy.getSeverity());
            response.put("notes", savedAllergy.getNotes());
            response.put("success", true);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını logla
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Failed to add allergy: " + e.getMessage(), "success", false));
        }
    }

    @GetMapping("/{medicalRecordId}/allergies")
    public ResponseEntity<?> getAllergies(@PathVariable Long medicalRecordId) {
        try {
            List<Allergy> allergies = medicalRecordService.getAllergies(medicalRecordId);
            
            // Frontend'in beklediği formatta yanıt döndür
            List<Map<String, Object>> allergyList = new ArrayList<>();
            for (Allergy allergy : allergies) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", allergy.getId());
                item.put("allergyName", allergy.getAllergen());
                item.put("symptoms", allergy.getReaction());
                item.put("severity", allergy.getSeverity());
                item.put("notes", allergy.getNotes());
                allergyList.add(item);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("allergies", allergyList);
            response.put("success", true);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını logla
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Failed to get allergies: " + e.getMessage(), "success", false));
        }
    }

    // Weight Record endpoints
    @PostMapping("/{medicalRecordId}/weight-records")
    public ResponseEntity<?> addWeightRecord(
            @PathVariable Long medicalRecordId,
            @RequestBody WeightRecordRequest weightRecordRequest) {
        try {
            // Önce medical record'ın var olup olmadığını kontrol et
            try {
                medicalRecordService.getMedicalRecord(medicalRecordId);
            } catch (EntityNotFoundException e) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Medical record not found with id: " + medicalRecordId + ". Please create a medical record first."));
            }
            
            WeightRecord weightRecord = new WeightRecord();
            
            // Weight alanını ayarla
            if (weightRecordRequest.getWeight() != null) {
                weightRecord.setWeight(weightRecordRequest.getWeight());
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Weight is required"));
            }
            
            // Unit alanını ayarla
            if (weightRecordRequest.getUnit() != null) {
                weightRecord.setUnit(weightRecordRequest.getUnit());
            } else {
                // Default unit olarak kg kullan
                weightRecord.setUnit("kg");
            }
            
            // Notes alanını ayarla
            if (weightRecordRequest.getNotes() != null) {
                weightRecord.setNotes(weightRecordRequest.getNotes());
            }
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate recordDate;
            try {
                if (weightRecordRequest.getRecordDate() != null) {
                    recordDate = LocalDate.parse(weightRecordRequest.getRecordDate(), formatter);
                } else if (weightRecordRequest.getDate() != null) {
                    recordDate = LocalDate.parse(weightRecordRequest.getDate(), formatter);
                } else {
                    // Eğer tarih belirtilmemişse bugünün tarihini kullan
                    recordDate = LocalDate.now();
                }
                weightRecord.setRecordDate(recordDate);
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Invalid date format. Please use YYYY-MM-DD format."));
            }
            
            WeightRecord savedWeightRecord = medicalRecordService.addWeightRecord(medicalRecordId, weightRecord);
            
            // Frontend'in beklediği formatta yanıt döndür
            Map<String, Object> response = new HashMap<>();
            response.put("id", savedWeightRecord.getId());
            response.put("weight", savedWeightRecord.getWeight());
            response.put("unit", savedWeightRecord.getUnit());
            response.put("date", savedWeightRecord.getRecordDate().toString());
            response.put("notes", savedWeightRecord.getNotes());
            response.put("success", true);
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını logla
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Failed to add weight record: " + e.getMessage(), "success", false));
        }
    }

    @GetMapping("/{medicalRecordId}/weight-records")
    public ResponseEntity<?> getWeightRecords(@PathVariable Long medicalRecordId) {
        try {
            List<WeightRecord> weightRecords = medicalRecordService.getWeightRecords(medicalRecordId);
            
            // Frontend'in beklediği formatta yanıt döndür
            List<Map<String, Object>> weightList = new ArrayList<>();
            for (WeightRecord weightRecord : weightRecords) {
                Map<String, Object> item = new HashMap<>();
                item.put("id", weightRecord.getId());
                item.put("weight", weightRecord.getWeight());
                item.put("unit", weightRecord.getUnit());
                item.put("date", weightRecord.getRecordDate().toString());
                item.put("notes", weightRecord.getNotes());
                weightList.add(item);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("weightRecords", weightList);
            response.put("success", true);
            
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını logla
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Failed to get weight records: " + e.getMessage(), "success", false));
        }
    }

    @GetMapping("/{medicalRecordId}/weight-records/latest")
    public ResponseEntity<?> getLatestWeightRecord(@PathVariable Long medicalRecordId) {
        try {
            Optional<WeightRecord> latestWeightRecord = medicalRecordService.getLatestWeight(medicalRecordId);
            
            if (latestWeightRecord.isPresent()) {
                // Frontend'in beklediği formatta yanıt döndür
                Map<String, Object> weightData = new HashMap<>();
                weightData.put("id", latestWeightRecord.get().getId());
                weightData.put("weight", latestWeightRecord.get().getWeight());
                weightData.put("unit", latestWeightRecord.get().getUnit());
                weightData.put("date", latestWeightRecord.get().getRecordDate().toString());
                weightData.put("notes", latestWeightRecord.get().getNotes());
                
                Map<String, Object> response = new HashMap<>();
                response.put("weightRecord", weightData);
                response.put("success", true);
                
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "No weight records found for medical record with id: " + medicalRecordId, "success", false));
            }
        } catch (Exception e) {
            e.printStackTrace(); // Hata detaylarını logla
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", "Failed to get latest weight record: " + e.getMessage(), "success", false));
        }
    }
}
