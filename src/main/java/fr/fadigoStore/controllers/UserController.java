package fr.fadigoStore.controllers;

import fr.fadigoStore.entities.User;
import fr.fadigoStore.service.UserService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(value = "/register")
    @ResponseStatus(HttpStatus.CREATED)
    public void register(
            @RequestBody @Valid User user
            ) throws MessagingException {
        this.userService.register(user);
    }

}
