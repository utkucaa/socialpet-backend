package com.example.social_pet.service;

import com.example.social_pet.entities.*;
import com.example.social_pet.repository.MedicalRecordRepository;
import com.example.social_pet.repository.PetRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Comparator;

@Service
@Transactional
public class MedicalRecordService {

    private final MedicalRecordRepository medicalRecordRepository;
    private final PetRepository petRepository;

    @Autowired
    public MedicalRecordService(MedicalRecordRepository medicalRecordRepository, PetRepository petRepository) {
        this.medicalRecordRepository = medicalRecordRepository;
        this.petRepository = petRepository;
    }

    // Medical Record CRUD operations
    public MedicalRecord createMedicalRecord(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new EntityNotFoundException("Pet not found with id: " + petId));

        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPet(pet);
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord getMedicalRecord(Long id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Medical record not found with id: " + id));
    }

    public List<MedicalRecord> getMedicalRecordsByPetId(Long petId) {
        return medicalRecordRepository.findByPetId(petId);
    }

    public void deleteMedicalRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }

    // Vaccination operations
    public Vaccination addVaccination(Long medicalRecordId, Vaccination vaccination) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        vaccination.setMedicalRecord(medicalRecord);
        vaccination.setPet(medicalRecord.getPet());
        
        if (medicalRecord.getVaccinations() == null) {
            medicalRecord.setVaccinations(new ArrayList<>());
        }
        
        medicalRecord.getVaccinations().add(vaccination);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        
        // Kaydedilen aşıyı bul ve döndür
        return savedRecord.getVaccinations()
            .stream()
            .filter(v -> v.getVaccineName().equals(vaccination.getVaccineName()) && 
                   v.getVaccinationDate().equals(vaccination.getVaccinationDate()))
            .findFirst()
            .orElse(vaccination);
    }

    public List<Vaccination> getVaccinations(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getVaccinations();
    }

    // Treatment operations
    public Treatment addTreatment(Long medicalRecordId, Treatment treatment) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        treatment.setMedicalRecord(medicalRecord);
        
        if (medicalRecord.getTreatments() == null) {
            medicalRecord.setTreatments(new ArrayList<>());
        }
        
        medicalRecord.getTreatments().add(treatment);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        
        // Kaydedilen tedaviyi bul ve döndür
        return savedRecord.getTreatments()
            .stream()
            .filter(t -> t.getTreatmentType().equals(treatment.getTreatmentType()) && 
                   t.getTreatmentDate().equals(treatment.getTreatmentDate()))
            .findFirst()
            .orElse(treatment);
    }

    public List<Treatment> getTreatments(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getTreatments();
    }

    // Appointment operations
    public Appointment addAppointment(Long medicalRecordId, Appointment appointment) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        appointment.setMedicalRecord(medicalRecord);
        
        if (medicalRecord.getAppointments() == null) {
            medicalRecord.setAppointments(new ArrayList<>());
        }
        
        medicalRecord.getAppointments().add(appointment);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        
        // Kaydedilen randevuyu bul ve döndür
        return savedRecord.getAppointments()
            .stream()
            .filter(a -> a.getAppointmentDate().equals(appointment.getAppointmentDate()) && 
                   a.getReason().equals(appointment.getReason()))
            .findFirst()
            .orElse(appointment);
    }

    public List<Appointment> getAppointments(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getAppointments();
    }

    public List<Appointment> getUpcomingAppointments(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        LocalDate today = LocalDate.now();
        
        return medicalRecord.getAppointments().stream()
                .filter(appointment -> appointment.getAppointmentDate().isAfter(today) || 
                                      appointment.getAppointmentDate().isEqual(today))
                .collect(Collectors.toList());
    }

    // Medication operations
    public Medication addMedication(Long medicalRecordId, Medication medication) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        medication.setMedicalRecord(medicalRecord);
        
        if (medicalRecord.getMedications() == null) {
            medicalRecord.setMedications(new ArrayList<>());
        }
        
        medicalRecord.getMedications().add(medication);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        
        // Kaydedilen ilacı bul ve döndür
        return savedRecord.getMedications()
            .stream()
            .filter(m -> m.getMedicationName().equals(medication.getMedicationName()) && 
                   m.getStartDate().equals(medication.getStartDate()))
            .findFirst()
            .orElse(medication);
    }

    public List<Medication> getMedications(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getMedications();
    }

    public List<Medication> getCurrentMedications(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        LocalDate today = LocalDate.now();
        
        return medicalRecord.getMedications().stream()
                .filter(medication -> medication.getEndDate().isAfter(today) || 
                                     medication.getEndDate().isEqual(today))
                .collect(Collectors.toList());
    }

    // Allergy operations
    public Allergy addAllergy(Long medicalRecordId, Allergy allergy) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        allergy.setMedicalRecord(medicalRecord);
        
        if (medicalRecord.getAllergies() == null) {
            medicalRecord.setAllergies(new ArrayList<>());
        }
        
        medicalRecord.getAllergies().add(allergy);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        
        // Kaydedilen alerjiyi bul ve döndür
        return savedRecord.getAllergies()
            .stream()
            .filter(a -> a.getAllergen().equals(allergy.getAllergen()))
            .findFirst()
            .orElse(allergy);
    }

    public List<Allergy> getAllergies(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getAllergies();
    }

    // Weight Record operations
    public WeightRecord addWeightRecord(Long medicalRecordId, WeightRecord weightRecord) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        weightRecord.setMedicalRecord(medicalRecord);
        
        if (medicalRecord.getWeightRecords() == null) {
            medicalRecord.setWeightRecords(new ArrayList<>());
        }
        
        medicalRecord.getWeightRecords().add(weightRecord);
        MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
        
        // Kaydedilen kilo kaydını bul ve döndür
        return savedRecord.getWeightRecords()
            .stream()
            .filter(w -> w.getRecordDate().equals(weightRecord.getRecordDate()) && 
                   Math.abs(w.getWeight() - weightRecord.getWeight()) < 0.001)
            .findFirst()
            .orElse(weightRecord);
    }

    public List<WeightRecord> getWeightRecords(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getWeightRecords();
    }

    public Optional<WeightRecord> getLatestWeight(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        
        return medicalRecord.getWeightRecords().stream()
                .sorted((w1, w2) -> w2.getRecordDate().compareTo(w1.getRecordDate()))
                .findFirst();
    }
}
