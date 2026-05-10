package parcial2.backend.application.service;

import org.springframework.stereotype.Service;
import parcial2.backend.application.dto.response.UserResponse;
import parcial2.backend.domain.model.User;
import parcial2.backend.domain.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GetUsersService {

    private final UserRepository userRepository;

    public GetUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public List<UserResponse> execute() {
        return userRepository.findAll()
                .stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    public UserResponse executeForOne(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + email));
        return toDto(user);
    }

    private UserResponse toDto(User user) {
        return new UserResponse(
                user.getEmail(),
                user.getFullname(),
                user.getRole(),
                user.getPhoneNumber(),
                user.getImageUrl()
        );
    }
}
