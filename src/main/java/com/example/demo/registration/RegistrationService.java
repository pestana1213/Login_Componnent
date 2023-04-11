package com.example.demo.registration;


import com.example.demo.appuser.AppUser;
import com.example.demo.appuser.AppUserRole;
import com.example.demo.appuser.AppUserService;
import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;


@Service
@AllArgsConstructor
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailValidator emailValidator;
    private final ConfirmationTokenService confirmationTokenService;

    public String register(RegistrationRequest request) throws IllegalAccessException {
        boolean isValidEmail = emailValidator.test(request.getEmail());

        if(isValidEmail){
            return appUserService.signUpUser(
                    new AppUser(
                            request.getFirstName(),
                            request.getLastName(),
                            request.getEmail(),
                            request.getPassword(),
                            AppUserRole.USER
                    )
            );
        }
        else{
            throw new IllegalAccessException("Email already taken");
        }
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken getToken = confirmationTokenService.
                getToken(token).
                orElseThrow(()->
                        new IllegalStateException("token not found"))
                ;

        if (getToken.getConfirmedAt()!=null){
            throw new IllegalStateException("Email already verified");
        }
        if(getToken.getExpiredAt().isBefore(LocalDateTime.now())){
            confirmationTokenService.delete(getToken);
            throw new IllegalStateException("Token expired");
        }
        getToken.setConfirmedAt(LocalDateTime.now());

        appUserService.enableAppUser(
                getToken.getAppUser().getEmail()
        );

        return "confirmed";
    }
}
