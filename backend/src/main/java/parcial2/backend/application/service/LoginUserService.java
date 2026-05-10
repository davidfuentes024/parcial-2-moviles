package parcial2.backend.application.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import parcial2.backend.application.dto.request.LoginRequest;
import parcial2.backend.application.dto.response.AuthResponse;
import parcial2.backend.domain.model.User;
import parcial2.backend.domain.repository.UserRepository;
import parcial2.backend.infraestructure.jwt.JwtProvider;


@Service
public class LoginUserService {

    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private JwtProvider jwtProvider;

    public LoginUserService(UserRepository userRepository,
                            PasswordEncoder passwordEncoder,
                            JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;

    }

    public AuthResponse executeLogin(LoginRequest loginRequest) throws Exception {

        //1 buscar user
        User user = userRepository.findByEmail((loginRequest.getEmail()))
                .orElseThrow(()-> new IllegalArgumentException("invalid email or password"));

        //2 validar user
        if (!passwordEncoder.matches(user.getPasswordHash(), loginRequest.getPassword())){
            throw new IllegalArgumentException("invalid email or password");
        }

        //validar fcm token
        if (!user.getFcmTokens().contains(loginRequest.getFcmToken())){
            userRepository.addFcmToken(loginRequest.getEmail(), loginRequest.getFcmToken());
        }

        //crear y devolver jwt
        String token = jwtProvider.generateToken(user.getEmail());
        return  new AuthResponse(
                token,
                user.getEmail(),
                user.getFullname(),
                user.getImageUrl());
    }

}
