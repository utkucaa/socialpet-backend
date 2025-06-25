package com.example.social_pet.controller;

import com.example.social_pet.entities.Answer;
import com.example.social_pet.entities.Question;
import com.example.social_pet.entities.User;
import com.example.social_pet.service.AnswerService;
import com.example.social_pet.service.QuestionService;
import com.example.social_pet.service.UserService;
import com.example.social_pet.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/answers")
public class AnswerController {

    private final AnswerService answerService;
    private final UserService userService;
    private final QuestionService questionService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public AnswerController(AnswerService answerService, UserService userService, 
                          QuestionService questionService, JwtTokenProvider jwtTokenProvider) {
        this.answerService = answerService;
        this.userService = userService;
        this.questionService = questionService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @PostMapping("/question/{questionId}")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<?> createAnswer(@PathVariable Long questionId, 
                                        @RequestBody Map<String, String> request,
                                        HttpServletRequest httpRequest) {
        try {
            // JWT token'dan kullanıcı bilgisini al
            String token = getTokenFromRequest(httpRequest);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization token required"));
            }

            String userEmail = jwtTokenProvider.getUsernameFromToken(token);
            User user = userService.findByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
            }

            // Soruyu kontrol et
            Question question = questionService.getQuestionById(questionId);
            if (question == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Question not found"));
            }

            // Cevap içeriğini al
            String content = request.get("content");
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Answer content is required"));
            }

            // Yeni cevap oluştur
            Answer answer = new Answer();
            answer.setContent(content.trim());
            answer.setUser(user);
            answer.setQuestion(question);
            answer.setDatePosted(new Date());

            Answer createdAnswer = answerService.createAnswer(answer);
            
            // Response DTO oluştur
            Map<String, Object> response = Map.of(
                "id", createdAnswer.getId(),
                "content", createdAnswer.getContent(),
                "datePosted", createdAnswer.getDatePosted(),
                "userName", createdAnswer.getUser().getUserName(),
                "questionId", createdAnswer.getQuestion().getId()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to create answer: " + e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<Answer>> getAllAnswers() {
        List<Answer> answers = answerService.getAllAnswers();
        return ResponseEntity.ok(answers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Answer> getAnswerById(@PathVariable Long id) {
        return answerService.getAnswerById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/question/{questionId}")
    public ResponseEntity<List<Answer>> getAnswersByQuestionId(@PathVariable Long questionId) {
        try {
            List<Answer> answers = answerService.getAnswersByQuestionId(questionId);
            return ResponseEntity.ok(answers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<?> updateAnswer(@PathVariable Long id, 
                                        @RequestBody Map<String, String> request,
                                        HttpServletRequest httpRequest) {
        try {
            // JWT token'dan kullanıcı bilgisini al
            String token = getTokenFromRequest(httpRequest);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization token required"));
            }

            String userEmail = jwtTokenProvider.getUsernameFromToken(token);
            User user = userService.findByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
            }

            // Mevcut cevabı kontrol et
            Answer existingAnswer = answerService.getAnswerById(id).orElse(null);
            if (existingAnswer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Answer not found"));
            }

            // Sadece kendi cevabını düzenleyebilir (admin hariç)
            if (!existingAnswer.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You can only edit your own answers"));
            }

            String content = request.get("content");
            if (content == null || content.trim().isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Answer content is required"));
            }

            existingAnswer.setContent(content.trim());
            Answer updatedAnswer = answerService.updateAnswer(existingAnswer);
            
            Map<String, Object> response = Map.of(
                "id", updatedAnswer.getId(),
                "content", updatedAnswer.getContent(),
                "datePosted", updatedAnswer.getDatePosted(),
                "userName", updatedAnswer.getUser().getUserName(),
                "questionId", updatedAnswer.getQuestion().getId()
            );

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to update answer: " + e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MEMBER') or hasRole('ADMIN')")
    public ResponseEntity<?> deleteAnswer(@PathVariable Long id, HttpServletRequest httpRequest) {
        try {
            // JWT token'dan kullanıcı bilgisini al
            String token = getTokenFromRequest(httpRequest);
            if (token == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization token required"));
            }

            String userEmail = jwtTokenProvider.getUsernameFromToken(token);
            User user = userService.findByEmail(userEmail);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "User not found"));
            }

            // Mevcut cevabı kontrol et
            Answer existingAnswer = answerService.getAnswerById(id).orElse(null);
            if (existingAnswer == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Answer not found"));
            }

            // Sadece kendi cevabını silebilir (admin hariç)
            if (!existingAnswer.getUser().getId().equals(user.getId()) && !user.isAdmin()) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("error", "You can only delete your own answers"));
            }

            answerService.deleteAnswer(id);
            return ResponseEntity.ok(Map.of("message", "Answer deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("error", "Failed to delete answer: " + e.getMessage()));
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
} 