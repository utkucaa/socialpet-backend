package com.example.social_pet.repository;

import com.example.social_pet.entities.Answer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    // Belirli bir soruya ait cevapları getir
    List<Answer> findByQuestionId(Long questionId);
    
    // Belirli bir kullanıcının cevaplarını getir
    List<Answer> findByUserId(Long userId);
}
