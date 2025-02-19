package com.example.social_pet.service;

import com.example.social_pet.entities.LostPet;
import com.example.social_pet.entities.User;
import com.example.social_pet.repository.LostPetRepository;
import com.example.social_pet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LostPetService {

    private final LostPetRepository lostPetRepository;
    private final UserRepository userRepository;

    // Yeni ilan ekleme işlemi (giriş yapmış kullanıcıya bağlı)
    public LostPet addLostPet(LostPet lostPet, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));

        validateLostPet(lostPet); // Geçerlilik kontrolü

        if (lostPet.getImageUrl() == null || lostPet.getImageUrl().isEmpty()) {
            lostPet.setImageUrl("default-image.jpg"); // Varsayılan resim
        }

        lostPet.setUser(user); // İlanı kullanıcıya bağla
        return lostPetRepository.save(lostPet);
    }

    // Tüm ilanları listeleme işlemi
    public List<LostPet> getAllLostPets() {
        return lostPetRepository.findAll();
    }

    // Belirli bir id'ye sahip ilanı getirme
    public LostPet getLostPetById(Long id) {
        return lostPetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("İlan bulunamadı!"));
    }

    // Belirli bir ilanı silme işlemi (sadece ilan sahibi silebilir)
    public void deleteLostPet(Long id, Long userId) {
        LostPet lostPet = lostPetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Silinmek istenen ilan bulunamadı!"));

        if (!lostPet.getUser().getId().equals(userId)) {
            throw new RuntimeException("Bu ilanı silme yetkiniz yok!");
        }

        lostPetRepository.deleteById(id);
    }

    // Validasyon metodumuz
    private void validateLostPet(LostPet lostPet) {
        if (lostPet.getTitle() == null || lostPet.getTitle().isEmpty()) {
            throw new IllegalArgumentException("İlan başlığı zorunludur.");
        }
        if (lostPet.getDetails() == null || lostPet.getDetails().isEmpty()) {
            throw new IllegalArgumentException("İlan detayları zorunludur.");
        }
        if (lostPet.getCategory() == null || lostPet.getCategory().isEmpty()) {
            throw new IllegalArgumentException("Kategori zorunludur.");
        }
        if (!List.of("Kedi", "Köpek", "Muhabbet Kuşu", "Papağan").contains(lostPet.getCategory())) {
            throw new IllegalArgumentException("Geçersiz kategori! (Kedi, Köpek, Muhabbet Kuşu, Papağan olmalı)");
        }
        if (lostPet.getStatus() == null || lostPet.getStatus().isEmpty()) {
            throw new IllegalArgumentException("İlan durumu zorunludur.");
        }
        if (!List.of("Kayıp", "Bulundu").contains(lostPet.getStatus())) {
            throw new IllegalArgumentException("Geçersiz ilan durumu! (Kayıp, Bulundu olmalı)");
        }
        if (lostPet.getContactInfo() == null || lostPet.getContactInfo().isEmpty()) {
            throw new IllegalArgumentException("İletişim bilgisi zorunludur.");
        }
    }
}