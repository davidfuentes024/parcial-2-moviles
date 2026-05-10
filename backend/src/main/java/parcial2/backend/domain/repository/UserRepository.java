package parcial2.backend.domain.repository;

import parcial2.backend.domain.model.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    User save(User user);

    Optional<User> findByEmail(String email);

    void addFcmToken(String email, String fcmToken);

    boolean existsByEmail(String email);

    List<User> findAll();
}
