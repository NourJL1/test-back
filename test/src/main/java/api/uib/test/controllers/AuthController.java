package api.uib.test.controllers;

import api.uib.test.entities.User;
import api.uib.test.repositories.UserRepository;
import api.uib.test.services.EmailService;  // Service to send email
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;  // Make sure to implement this service
    
    
    

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");

        Optional<User> userOptional = userRepository.findByEmail(email);
        if (!userOptional.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No user found with this email.");
        }

        User user = userOptional.get();

        try {
            // Generate the reset token
            String token = UUID.randomUUID().toString();
            // Save the token to the database (you should have a column for reset token in your User entity)
            user.setResetToken(token);
            userRepository.save(user);

            // Send the email
            String resetLink = "http://localhost:4200/reset-password?token=" + token;
            emailService.sendPasswordResetEmail(email, resetLink);

            return ResponseEntity.ok("If this email is registered, a reset link has been sent.");
        } catch (Exception e) {
            // Log the error and return a generic error message
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending reset link. Try again later.");
        }
    }

}
