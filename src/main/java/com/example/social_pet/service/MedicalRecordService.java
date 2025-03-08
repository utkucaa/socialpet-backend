package com.example.social_pet.service;

import com.example.social_pet.entities.*;
import com.example.social_pet.repository.*;
import com.example.social_pet.dto.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class MedicalRecordService {

    @Autowired
    private TreatmentRepository treatmentRepository;
    
    @Autowired
    private VaccinationRepository vaccinationRepository;
    
    @Autowired
    private AllergyRepository allergyRepository;
    
    @Autowired
    private AppointmentRepository appointmentRepository;
    
    @Autowired
    private WeightRecordRepository weightRecordRepository;
    
    @Autowired
    private MedicationRepository medicationRepository;
    
    @Autowired
    private PetRepository petRepository;

    // Get all medical records for a pet
    public Map<String, Object> getAllMedicalRecords(Long petId) {
        Map<String, Object> allRecords = new HashMap<>();
        
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
                
        allRecords.put("treatments", treatmentRepository.findByPetId(petId));
        allRecords.put("vaccinations", vaccinationRepository.findByPetId(petId));
        allRecords.put("allergies", allergyRepository.findByPetId(petId));
        allRecords.put("appointments", appointmentRepository.findByPetId(petId));
        allRecords.put("weightRecords", weightRecordRepository.findByPetId(petId));
        allRecords.put("medications", medicationRepository.findByPetId(petId));
        
        return allRecords;
    }
    
    // Treatment methods
    public List<Treatment> getTreatments(Long petId) {
        return treatmentRepository.findByPetId(petId);
    }
    
    public Treatment getTreatment(Long id) {
        return treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + id));
    }
    
    @Transactional
    public Treatment addTreatment(Long petId, TreatmentRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
        
        Treatment treatment = new Treatment();
        treatment.setPet(pet);
        
        // Use treatmentType from request, or fallback to treatmentName if available
        if (request.getTreatmentType() != null) {
            treatment.setTreatmentType(request.getTreatmentType());
        } else if (request.getTreatmentName() != null) {
            treatment.setTreatmentType(request.getTreatmentName());
        }
        
        treatment.setDescription(request.getDescription() != null ? 
                request.getDescription() : request.getNotes());
        
        if (request.getTreatmentDate() != null) {
            treatment.setTreatmentDate(LocalDate.parse(request.getTreatmentDate(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        treatment.setVeterinarian(request.getVeterinarian());
        
        return treatmentRepository.save(treatment);
    }
    
    @Transactional
    public Treatment updateTreatment(Long id, TreatmentRequest request) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + id));
        
        // Use treatmentType from request, or fallback to treatmentName if available
        if (request.getTreatmentType() != null) {
            treatment.setTreatmentType(request.getTreatmentType());
        } else if (request.getTreatmentName() != null) {
            treatment.setTreatmentType(request.getTreatmentName());
        }
        
        if (request.getDescription() != null) {
            treatment.setDescription(request.getDescription());
        } else if (request.getNotes() != null) {
            treatment.setDescription(request.getNotes());
        }
        
        if (request.getTreatmentDate() != null) {
            treatment.setTreatmentDate(LocalDate.parse(request.getTreatmentDate(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        if (request.getVeterinarian() != null) {
            treatment.setVeterinarian(request.getVeterinarian());
        }
        
        return treatmentRepository.save(treatment);
    }
    
    public void deleteTreatment(Long id) {
        Treatment treatment = treatmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Treatment not found with id: " + id));
        treatmentRepository.delete(treatment);
    }
    
    // Vaccination methods
    public List<Vaccination> getVaccinations(Long petId) {
        return vaccinationRepository.findByPetId(petId);
    }
    
    public Vaccination getVaccination(Long id) {
        return vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
    }
    
    @Transactional
    public Vaccination addVaccination(Long petId, VaccinationRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
        
        Vaccination vaccination = new Vaccination();
        vaccination.setPet(pet);
        vaccination.setVaccineName(request.getVaccineName());
        
        if (request.getVaccinationDate() != null) {
            vaccination.setVaccinationDate(LocalDate.parse(request.getVaccinationDate(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        vaccination.setVeterinarian(request.getVeterinarian());
        
        return vaccinationRepository.save(vaccination);
    }
    
    @Transactional
    public Vaccination updateVaccination(Long id, VaccinationRequest request) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
        
        if (request.getVaccineName() != null) {
            vaccination.setVaccineName(request.getVaccineName());
        }
        
        if (request.getVaccinationDate() != null) {
            vaccination.setVaccinationDate(LocalDate.parse(request.getVaccinationDate(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        if (request.getVeterinarian() != null) {
            vaccination.setVeterinarian(request.getVeterinarian());
        }
        
        return vaccinationRepository.save(vaccination);
    }
    
    public void deleteVaccination(Long id) {
        Vaccination vaccination = vaccinationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Vaccination not found with id: " + id));
        vaccinationRepository.delete(vaccination);
    }
    
    // Allergy methods
    public List<Allergy> getAllergies(Long petId) {
        return allergyRepository.findByPetId(petId);
    }
    
    public Allergy getAllergy(Long id) {
        return allergyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Allergy not found with id: " + id));
    }
    
    @Transactional
    public Allergy addAllergy(Long petId, AllergyRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
        
        Allergy allergy = new Allergy();
        allergy.setPet(pet);
        allergy.setAllergen(request.getAllergen());
        allergy.setReaction(request.getReaction());
        allergy.setSeverity(request.getSeverity());
        allergy.setNotes(request.getNotes());
        
        return allergyRepository.save(allergy);
    }
    
    @Transactional
    public Allergy updateAllergy(Long id, AllergyRequest request) {
        Allergy allergy = allergyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Allergy not found with id: " + id));
        
        if (request.getAllergen() != null) {
            allergy.setAllergen(request.getAllergen());
        }
        
        if (request.getReaction() != null) {
            allergy.setReaction(request.getReaction());
        }
        
        if (request.getSeverity() != null) {
            allergy.setSeverity(request.getSeverity());
        }
        
        if (request.getNotes() != null) {
            allergy.setNotes(request.getNotes());
        }
        
        return allergyRepository.save(allergy);
    }
    
    public void deleteAllergy(Long id) {
        Allergy allergy = allergyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Allergy not found with id: " + id));
        allergyRepository.delete(allergy);
    }
    
    // Appointment methods
    public List<Appointment> getAppointments(Long petId) {
        return appointmentRepository.findByPetId(petId);
    }
    
    public Appointment getAppointment(Long id) {
        return appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
    }
    
    @Transactional
    public Appointment addAppointment(Long petId, AppointmentRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
        
        Appointment appointment = new Appointment();
        appointment.setPet(pet);
        
        if (request.getAppointmentDate() != null) {
            appointment.setAppointmentDate(LocalDateTime.parse(request.getAppointmentDate()));
        }
        
        appointment.setVeterinarian(request.getVeterinarian());
        appointment.setReason(request.getReason());
        
        return appointmentRepository.save(appointment);
    }
    
    @Transactional
    public Appointment updateAppointment(Long id, AppointmentRequest request) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        
        if (request.getAppointmentDate() != null) {
            appointment.setAppointmentDate(LocalDateTime.parse(request.getAppointmentDate()));
        }
        
        if (request.getVeterinarian() != null) {
            appointment.setVeterinarian(request.getVeterinarian());
        }
        
        if (request.getReason() != null) {
            appointment.setReason(request.getReason());
        }
        
        return appointmentRepository.save(appointment);
    }
    
    public void deleteAppointment(Long id) {
        Appointment appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Appointment not found with id: " + id));
        appointmentRepository.delete(appointment);
    }
    
    // WeightRecord methods
    public List<WeightRecord> getWeightRecords(Long petId) {
        return weightRecordRepository.findByPetId(petId);
    }
    
    public WeightRecord getWeightRecord(Long id) {
        return weightRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeightRecord not found with id: " + id));
    }
    
    @Transactional
    public WeightRecord addWeightRecord(Long petId, WeightRecordRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
        
        WeightRecord weightRecord = new WeightRecord();
        weightRecord.setPet(pet);
        
        String dateString = request.getRecordDate() != null ? 
                request.getRecordDate() : request.getDate();
        
        if (dateString != null) {
            weightRecord.setRecordDate(LocalDate.parse(dateString, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        weightRecord.setWeight(request.getWeight());
        weightRecord.setUnit(request.getUnit());
        weightRecord.setNotes(request.getNotes());
        
        return weightRecordRepository.save(weightRecord);
    }
    
    @Transactional
    public WeightRecord updateWeightRecord(Long id, WeightRecordRequest request) {
        WeightRecord weightRecord = weightRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("WeightRecord not found with id: " + id));
        
        String dateString = request.getRecordDate() != null ? 
                request.getRecordDate() : request.getDate();
        
        if (dateString != null) {
            weightRecord.setRecordDate(LocalDate.parse(dateString, 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        if (request.getWeight() != null) {
            weightRecord.setWeight(request.getWeight());
        }
        
        if (request.getUnit() != null) {
            weightRecord.setUnit(request.getUnit());
        }
        
        if (request.getNotes() != null) {
            weightRecord.setNotes(request.getNotes());
        }
        
        return weightRecordRepository.save(weightRecord);
    }
    
    public void deleteWeightRecord(Long id) {
        WeightRecord weightRecord = weightRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Weight record not found with id: " + id));
        weightRecordRepository.delete(weightRecord);
    }
    
    // Medication methods
    public List<Medication> getMedications(Long petId) {
        return medicationRepository.findByPetId(petId);
    }
    
    public Medication getMedication(Long id) {
        return medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));
    }
    
    @Transactional
    public Medication addMedication(Long petId, MedicationRequest request) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
        
        Medication medication = new Medication();
        medication.setPet(pet);
        medication.setMedicationName(request.getMedicationName());
        medication.setDosage(request.getDosage());
        
        if (request.getStartDate() != null) {
            medication.setStartDate(LocalDate.parse(request.getStartDate(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        if (request.getEndDate() != null) {
            medication.setEndDate(LocalDate.parse(request.getEndDate(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        return medicationRepository.save(medication);
    }
    
    @Transactional
    public Medication updateMedication(Long id, MedicationRequest request) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));
        
        if (request.getMedicationName() != null) {
            medication.setMedicationName(request.getMedicationName());
        }
        
        if (request.getDosage() != null) {
            medication.setDosage(request.getDosage());
        }
        
        if (request.getStartDate() != null) {
            medication.setStartDate(LocalDate.parse(request.getStartDate(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        if (request.getEndDate() != null) {
            medication.setEndDate(LocalDate.parse(request.getEndDate(), 
                    DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
        
        return medicationRepository.save(medication);
    }
    
    public void deleteMedication(Long id) {
        Medication medication = medicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medication not found with id: " + id));
        medicationRepository.delete(medication);
    }
} 