package parcial2.backend.presentation.contorller;

import jakarta.validation.Valid;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import parcial2.backend.application.dto.request.LoginRequest;
import parcial2.backend.application.dto.request.RegisterRequest;
import parcial2.backend.application.dto.response.AuthResponse;
import parcial2.backend.application.service.LoginUserService;
import parcial2.backend.application.service.RegisterUserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final RegisterUserService registerUserService;
    private final LoginUserService loginUserService;

    public AuthController(RegisterUserService registerUserService,
                          LoginUserService loginUserService) {
        this.registerUserService = registerUserService;
        this.loginUserService = loginUserService;
    }

    @PostMapping(value = "/register", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AuthResponse> register(
            @RequestPart("email") String email,
            @RequestPart("password") String password,
            @RequestPart("fullName") String fullName,
            @RequestPart("phoneNumber") String phoneNumber,
            @RequestPart("role") String role,
            @RequestPart("fcmToken") String fcmToken,
            @RequestPart(value = "photo", required = false) MultipartFile photo) throws Exception {

        RegisterRequest dto = new RegisterRequest();
        dto.setEmail(email);
        dto.setPassword(password);
        dto.setFullName(fullName);
        dto.setPhoneNumber(phoneNumber);
        dto.setRole(role);
        dto.setFcmToken(fcmToken);

        AuthResponse response = registerUserService.executeRegisterUser(dto, photo);
        return ResponseEntity.ok(response);
    }

    /**
     * Inicia sesión con email y contraseña.
     * También recibe el token FCM del dispositivo.
     */
    @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest dto) throws Exception {
        AuthResponse response = loginUserService.executeLogin(dto);
        return ResponseEntity.ok(response);
    }
}
