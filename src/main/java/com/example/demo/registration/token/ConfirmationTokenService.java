package com.example.demo.registration.token;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ConfirmationTokenService {
    private final ConfirmationTokenRepo confirmationTokenRepo;

    public void saveConfirmationToken(ConfirmationToken token){
        confirmationTokenRepo.save(token);
    }

    public Optional<ConfirmationToken> getToken(String token){
        return confirmationTokenRepo.findByToken(token);
    }

    public void delete(ConfirmationToken token){
        confirmationTokenRepo.deleteById(token.getId());
    }

}
