package com.ucsal.arqsoftware.servicies;

import com.ucsal.arqsoftware.dto.EmailDTO;
import com.ucsal.arqsoftware.dto.NewPasswordDTO;
import com.ucsal.arqsoftware.entities.PasswordRecover;
import com.ucsal.arqsoftware.entities.User;
import com.ucsal.arqsoftware.repositories.PasswordRecoverRepository;
import com.ucsal.arqsoftware.repositories.UserRepository;
import com.ucsal.arqsoftware.servicies.exceptions.InvalidTokenException;
import com.ucsal.arqsoftware.servicies.exceptions.ResourceNotFoundException;
import com.ucsal.arqsoftware.servicies.exceptions.TokenExpiredException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class AuthService {

    @Value("${spring.mail.username}")
    private String defaultSender;

    @Value("${email.password-recover.uri}")
    private String recoverUri;

    @Value("${email.password-recover.token.minutes}")
    private Long tokenMinutes;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private PasswordRecoverRepository passwordRecoverRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Transactional
    public void createRecoverToken(EmailDTO body) {
        User user = userRepository.findByLogin(body.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("Email not found"));

        String token = UUID.randomUUID().toString();

        PasswordRecover entity = new PasswordRecover();
        entity.setToken(token);
        entity.setExpiration(Instant.now().plusSeconds(tokenMinutes * 60L));
        entity.setEmail(body.getEmail());
        passwordRecoverRepository.save(entity);

        String text = "Acesse o link para definir uma nova senha (válido por " + tokenMinutes + " minutos):\n\n"
                + recoverUri + token;

        emailService.sendEmail(body.getEmail(), "Recuperação de senha", text);
    }

    @Transactional
    public void saveNewPassword(NewPasswordDTO body) {
        List<PasswordRecover> list = passwordRecoverRepository.searchValidTokens(body.getToken(), Instant.now());

        if (list.size() == 0) {
            throw new ResourceNotFoundException("Invalid token");
        }

        String email = list.get(0).getEmail();

        User user = userRepository.findByLogin(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with login: " + email));;
        user.setPassword(passwordEncoder.encode(body.getPassword()));
        userRepository.save(user);
    }

    @Transactional
    public boolean isValidToken(String token) {
        PasswordRecover recover = passwordRecoverRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Token not found"));

        if (recover.getExpiration().isBefore(Instant.now())) {
            throw new TokenExpiredException("Token expired");
        }

        return true;
    }
}
