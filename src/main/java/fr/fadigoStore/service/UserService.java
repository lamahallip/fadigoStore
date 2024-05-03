package fr.fadigoStore.service;


import fr.fadigoStore.entities.Role;
import fr.fadigoStore.entities.Token;
import fr.fadigoStore.entities.User;
import fr.fadigoStore.repositories.RoleRepository;
import fr.fadigoStore.repositories.TokenRepository;
import fr.fadigoStore.repositories.UserRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenRepository tokenRepository;
    private final EmailService emailService;
    private final String activationUrl = "http://localhost:4200/activate_account";


    private final String confirmationUrl = "";

    public void register(User user) throws MessagingException {


        if(roleRepository.findByName("USER").isEmpty()) {
            Role role_user = Role.builder()
                    .name("USER")
                    .build();
            roleRepository.save(role_user);
        }

        // This is the default role
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("This role is not initialized"));

        User newUser = User.builder()
                .firstname(user.getFirstname())
                .lastname(user.getLastname())
                .email(user.getEmail())
                .roles(List.of(userRole))
                .password(passwordEncoder.encode(user.getPassword()))
                .isAccountLocked(false)
                .isEnable(false)
                .build();

        this.userRepository.save(newUser);
        sendValidationEmail(newUser);

    }

    // Method to send notification for activate account
    private void sendValidationEmail(User user) throws MessagingException {

        String newToken = generateAndSaveToken(user);
        emailService.sendMail(
                user.getEmail(),
                "Activation de Compte",
                "Bonjour "+user.getFirstname()+
                        ", Voici votre code : "+newToken
                        +" Veuillez activer votre compte. Vous avez un délai de 15 minutes | "
                        +"Lien d'activation : "+activationUrl
        );

    }

    // Method to generate and save Token
    private String generateAndSaveToken(User user) {
        String code = generateCode(6);
        Token token = Token.builder()
                .token(code)
                .createdAt(LocalDateTime.now())
                .expiredAt(LocalDateTime.now().plusMinutes(15))
                .user(user)
                .build();
        tokenRepository.save(token);

        return code;
    }

    // Method to generate Token with length parameter
    private String generateCode(int length) {
        SecureRandom secureRandom = new SecureRandom();
        String characters = "0123456789";
        StringBuilder codeBuilder = new StringBuilder();

        for(int i = 0; i < 6; i++) {
            int randomIndex = secureRandom.nextInt(characters.length());
            codeBuilder.append(characters.charAt(randomIndex));
        }

        return codeBuilder.toString();
    }
}
