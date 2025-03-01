package com.example.social_pet.service;

import com.example.social_pet.entities.Question;
import com.example.social_pet.repository.QuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;


@Service
public class QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    public List<Question> getAllQuestions() {
        return questionRepository.findAll();
    }

    public Question getQuestionById(Long id) {
        return questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Soru bulunamadı - id: " + id));
    }

    public Question createQuestion(Question question) {
        question.setDatePosted(new Date());
        return questionRepository.save(question);
    }

    public Question updateQuestion(Long id, Question questionDetails) {
        Question question = getQuestionById(id);
        
        question.setTitle(questionDetails.getTitle());
        question.setContent(questionDetails.getContent());
        
        return questionRepository.save(question);
    }

    public void deleteQuestion(Long id) {
        Question question = getQuestionById(id);
        questionRepository.delete(question);
    }
} 