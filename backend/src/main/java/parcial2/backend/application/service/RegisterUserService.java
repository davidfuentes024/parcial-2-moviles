package parcial2.backend.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import parcial2.backend.application.dto.request.RegisterRequest;
import parcial2.backend.application.dto.response.AuthResponse;
import parcial2.backend.domain.model.User;
import parcial2.backend.domain.port.StoragePort;
import parcial2.backend.domain.repository.UserRepository;
import parcial2.backend.infraestructure.jwt.JwtProvider;

import java.io.IOException;
import java.util.UUID;

@Service
public class RegisterUserService {

    private final UserRepository userRepository;
    private final StoragePort storagePort;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public RegisterUserService(UserRepository userRepository,
                               StoragePort storagePort,
                               PasswordEncoder passwordEncoder,
                               JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.storagePort = storagePort;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    public AuthResponse executeRegisterUser(RegisterRequest registerRequest, MultipartFile photo) throws Exception {

        // verifica si email existe
        if (userRepository.existsByEmail(registerRequest.getEmail())){
            throw new IllegalArgumentException("Ya existe un usuario con el email: " + registerRequest.getEmail());
        }

        // subir foto a firebase storage
        String photoUrl = null;
        if (photo != null && !photo.isEmpty()) {
            try {
                String fileName = "photos/" + UUID.randomUUID() + "_" + photo.getOriginalFilename();
                photoUrl = storagePort.uploadPhoto(photo.getBytes(), fileName, photo.getContentType());
            } catch (IOException e) {
                throw new RuntimeException("Error al subir la foto: " + e.getMessage(), e);
            }
        }

        User user = new User(
                registerRequest.getEmail(),
                passwordEncoder.encode(registerRequest.getPassword()),
                registerRequest.getFullName(),
                registerRequest.getRole(),
                registerRequest.getPhoneNumber(),
                photoUrl
        );

        user.getFcmTokens().add(registerRequest.getFcmToken());

        userRepository.save(user);

        String token = jwtProvider.generateToken(registerRequest.getEmail());

        return new AuthResponse(token, user.getEmail(), user.getFullname(), user.getImageUrl());

    }


}
