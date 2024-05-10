package fr.fadigoStore.user;


import fr.fadigoStore.auth.RegistrationRequest;
import fr.fadigoStore.security.JwtService;
import fr.fadigoStore.service.EmailService;

import fr.fadigoStore.auth.AuthenticationRequest;
import fr.fadigoStore.auth.AuthenticationResponse;
import fr.fadigoStore.repositories.RoleRepository;
import fr.fadigoStore.repositories.TokenRepository;
import fr.fadigoStore.repositories.UserRepository;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;

import java.util.List;


@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtService jwtService;

    private final String activationUrl = "http://localhost:4200/activate_account";


    private final String confirmationUrl = "";

    public AuthenticationResponse register(RegistrationRequest request) throws MessagingException {

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
                .firstname(request.getFirstname())
                .lastname(request.getLastname())
                .email(request.getEmail())
                .roles(List.of(userRole))
                .password(passwordEncoder.encode(request.getPassword()))
                .isAccountLocked(false)
                .isEnable(false)
                .build();

        this.userRepository.save(newUser);

        log.info("User created !!!");
        sendValidationEmail(newUser);
        log.info("User receive message !!!");

        UserDetails userDetails = userRepository.findByEmail(newUser.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        String token = jwtService.generateToken(userDetails);

        return AuthenticationResponse.builder()
                .token(token)
                .build();
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
                        +"Lien d'activation : " + activationUrl
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

    // Method to activate account
    public void activateAccount(String token) throws MessagingException {
        Token savedToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid Token"));

        if(LocalDateTime.now().isAfter(savedToken.getExpiredAt())) {
            sendValidationEmail(savedToken.getUser());
            throw new RuntimeException("This token is expired ! A new token has been sent to your address email");
        }

        User user = userRepository.findById(savedToken.getUser().getId())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        user.setIsEnable(true);
        log.info("User account is activate !!!");
        userRepository.save(user);
        savedToken.setValidateAt(LocalDateTime.now());
        tokenRepository.save(savedToken);
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        if (auth.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getEmail());
            String token = jwtService.generateToken(userDetails);
            return AuthenticationResponse.builder().token(token).build();
        }
        return AuthenticationResponse.builder()
                .token("Token not generate...")
                .build();
    }
}
