package com.example.social_pet.service;

import com.example.social_pet.entities.*;
import com.example.social_pet.repository.MedicalRecordRepository;
import com.example.social_pet.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

    // Temel CRUD operasyonları
    public MedicalRecord createMedicalRecord(Long petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new RuntimeException("Pet not found with id: " + petId));
        
        MedicalRecord medicalRecord = new MedicalRecord();
        medicalRecord.setPet(pet);
        return medicalRecordRepository.save(medicalRecord);
    }

    public MedicalRecord getMedicalRecord(Long id) {
        return medicalRecordRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Medical record not found with id: " + id));
    }

    public List<MedicalRecord> getMedicalRecordsByPetId(Long petId) {
        return medicalRecordRepository.findByPetId(petId);
    }

    public void deleteMedicalRecord(Long id) {
        medicalRecordRepository.deleteById(id);
    }

    // Aşı kayıtları ile ilgili metodlar
    public Vaccination addVaccination(Long medicalRecordId, Vaccination vaccination) {
        try {
            // Validasyon kontrolleri
            if (vaccination.getVaccineName() == null || vaccination.getVaccineName().trim().isEmpty()) {
                throw new IllegalArgumentException("Vaccine name cannot be empty");
            }
            
            if (vaccination.getVaccinationDate() == null) {
                throw new IllegalArgumentException("Vaccination date cannot be null");
            }
            
            // MedicalRecord'u bul
            MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
            
            // İlişkileri ayarla
            vaccination.setMedicalRecord(medicalRecord);
            vaccination.setPet(medicalRecord.getPet());
            
            // Aşıyı listeye ekle
            medicalRecord.getVaccinations().add(vaccination);
            
            // Kaydet ve döndür
            MedicalRecord savedRecord = medicalRecordRepository.save(medicalRecord);
            
            // Kaydedilen aşıyı bul ve döndür
            return savedRecord.getVaccinations()
                .stream()
                .filter(v -> v.getVaccineName().equals(vaccination.getVaccineName()) && 
                       v.getVaccinationDate().equals(vaccination.getVaccinationDate()))
                .findFirst()
                .orElse(vaccination);
        } catch (Exception e) {
            // Hata durumunda detaylı log çıktısı
            System.err.println("Error adding vaccination: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to add vaccination: " + e.getMessage(), e);
        }
    }

    public List<Vaccination> getVaccinations(Long medicalRecordId) {
        try {
            MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
            // Hibernate session'ın açık olduğundan emin olmak için koleksiyonu zorla yükle
            List<Vaccination> vaccinations = medicalRecord.getVaccinations();
            // Koleksiyonu başlatmak için size() metodunu çağır
            vaccinations.size();
            return vaccinations;
        } catch (Exception e) {
            System.err.println("Error getting vaccinations: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get vaccinations: " + e.getMessage(), e);
        }
    }

    // Tedavi kayıtları ile ilgili metodlar
    public Treatment addTreatment(Long medicalRecordId, Treatment treatment) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        treatment.setMedicalRecord(medicalRecord);
        medicalRecord.getTreatments().add(treatment);
        medicalRecordRepository.save(medicalRecord);
        return treatment;
    }

    public List<Treatment> getTreatments(Long medicalRecordId) {
        try {
            MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
            List<Treatment> treatments = medicalRecord.getTreatments();
            treatments.size(); // Koleksiyonu başlatmak için
            return treatments;
        } catch (Exception e) {
            System.err.println("Error getting treatments: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get treatments: " + e.getMessage(), e);
        }
    }

    // Randevu kayıtları ile ilgili metodlar
    public Appointment addAppointment(Long medicalRecordId, Appointment appointment) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        appointment.setMedicalRecord(medicalRecord);
        medicalRecord.getAppointments().add(appointment);
        medicalRecordRepository.save(medicalRecord);
        return appointment;
    }

    public List<Appointment> getAppointments(Long medicalRecordId) {
        try {
            MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
            List<Appointment> appointments = medicalRecord.getAppointments();
            appointments.size(); // Koleksiyonu başlatmak için
            return appointments;
        } catch (Exception e) {
            System.err.println("Error getting appointments: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get appointments: " + e.getMessage(), e);
        }
    }

    public List<Appointment> getUpcomingAppointments(Long medicalRecordId) {
        try {
            List<Appointment> allAppointments = getAppointments(medicalRecordId);
            LocalDate today = LocalDate.now();
            return allAppointments.stream()
                    .filter(appointment -> appointment.getAppointmentDate().isAfter(today))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting upcoming appointments: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get upcoming appointments: " + e.getMessage(), e);
        }
    }

    // İlaç kayıtları ile ilgili metodlar
    public Medication addMedication(Long medicalRecordId, Medication medication) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        medication.setMedicalRecord(medicalRecord);
        medicalRecord.getMedications().add(medication);
        medicalRecordRepository.save(medicalRecord);
        return medication;
    }

    public List<Medication> getMedications(Long medicalRecordId) {
        try {
            MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
            List<Medication> medications = medicalRecord.getMedications();
            medications.size(); // Koleksiyonu başlatmak için
            return medications;
        } catch (Exception e) {
            System.err.println("Error getting medications: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get medications: " + e.getMessage(), e);
        }
    }

    public List<Medication> getCurrentMedications(Long medicalRecordId) {
        try {
            List<Medication> allMedications = getMedications(medicalRecordId);
            LocalDate today = LocalDate.now();
            return allMedications.stream()
                    .filter(medication -> medication.getEndDate() == null || medication.getEndDate().isAfter(today))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            System.err.println("Error getting current medications: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get current medications: " + e.getMessage(), e);
        }
    }

    // Alerji kayıtları ile ilgili metodlar
    public Allergy addAllergy(Long medicalRecordId, Allergy allergy) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        allergy.setMedicalRecord(medicalRecord);
        medicalRecord.getAllergies().add(allergy);
        medicalRecordRepository.save(medicalRecord);
        return allergy;
    }

    public List<Allergy> getAllergies(Long medicalRecordId) {
        try {
            MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
            List<Allergy> allergies = medicalRecord.getAllergies();
            allergies.size(); // Koleksiyonu başlatmak için
            return allergies;
        } catch (Exception e) {
            System.err.println("Error getting allergies: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get allergies: " + e.getMessage(), e);
        }
    }

    // Kilo kayıtları ile ilgili metodlar
    public WeightRecord addWeightRecord(Long medicalRecordId, WeightRecord weightRecord) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        weightRecord.setMedicalRecord(medicalRecord);
        medicalRecord.getWeightRecords().add(weightRecord);
        medicalRecordRepository.save(medicalRecord);
        return weightRecord;
    }

    public List<WeightRecord> getWeightRecords(Long medicalRecordId) {
        try {
            MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
            List<WeightRecord> weightRecords = medicalRecord.getWeightRecords();
            weightRecords.size(); // Koleksiyonu başlatmak için
            return weightRecords;
        } catch (Exception e) {
            System.err.println("Error getting weight records: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get weight records: " + e.getMessage(), e);
        }
    }

    public Optional<WeightRecord> getLatestWeight(Long medicalRecordId) {
        try {
            List<WeightRecord> weightRecords = getWeightRecords(medicalRecordId);
            return weightRecords.stream()
                    .max(Comparator.comparing(WeightRecord::getRecordDate));
        } catch (Exception e) {
            System.err.println("Error getting latest weight: " + e.getMessage());
            e.printStackTrace();
            throw new RuntimeException("Failed to get latest weight: " + e.getMessage(), e);
        }
    }
}
