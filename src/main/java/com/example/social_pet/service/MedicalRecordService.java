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
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        vaccination.setMedicalRecord(medicalRecord);
        medicalRecord.getVaccinations().add(vaccination);
        medicalRecordRepository.save(medicalRecord);
        return vaccination;
    }

    public List<Vaccination> getVaccinations(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getVaccinations();
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
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getTreatments();
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
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getAppointments();
    }

    public List<Appointment> getUpcomingAppointments(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        LocalDate now = LocalDate.now();
        return medicalRecord.getAppointments().stream()
                .filter(appointment -> appointment.getAppointmentDate().isAfter(now))
                .toList();
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
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getMedications();
    }

    public List<Medication> getCurrentMedications(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        LocalDate now = LocalDate.now();
        return medicalRecord.getMedications().stream()
                .filter(medication -> 
                    medication.getStartDate().isBefore(now) && 
                    (medication.getEndDate() == null || medication.getEndDate().isAfter(now)))
                .toList();
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
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getAllergies();
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
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getWeightRecords();
    }

    public Optional<WeightRecord> getLatestWeight(Long medicalRecordId) {
        MedicalRecord medicalRecord = getMedicalRecord(medicalRecordId);
        return medicalRecord.getWeightRecords().stream()
                .max((w1, w2) -> w1.getRecordDate().compareTo(w2.getRecordDate()));
    }
}
