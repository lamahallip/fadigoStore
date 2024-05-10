package fr.fadigoStore.user;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping(value = "api/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping(value = "/activate-account")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void activateAccount(@RequestParam String token) throws MessagingException {
        userService.activateAccount(token);
    }



}
