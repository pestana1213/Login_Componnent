package com.example.demo.appuser;


import com.example.demo.registration.token.ConfirmationToken;
import com.example.demo.registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG =
            "user with email %s not found";

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {
        return appUserRepository.findByEmail(email)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                String.format(USER_NOT_FOUND_MSG, email)));
    }

    public String signUpUser(AppUser appUser) throws IllegalAccessException {
        if(appUserRepository
                .findByEmail(appUser.getEmail())
                .isPresent())
        {
            throw new IllegalAccessException("email already taken");
        }
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser);

        String tk = UUID.randomUUID().toString();
        ConfirmationToken token = new ConfirmationToken(
                tk,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(token);
        return tk;
    }

    public void enableAppUser(String email){
        appUserRepository.enableAppUser(email);
    }

    public boolean login(String email, String password){
        AppUser user = appUserRepository.findByEmail(email).orElseThrow(
                () -> new IllegalArgumentException("Email not registered on the app")
        );
        return passwordEncoder.matches(password, user.getPassword());
    }
}
