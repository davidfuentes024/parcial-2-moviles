package parcial2.backend.infraestructure.persistence.adapter;

import org.springframework.stereotype.Repository;
import parcial2.backend.domain.model.User;
import parcial2.backend.domain.repository.UserRepository;
import parcial2.backend.infraestructure.persistence.entity.FcmTokenEntity;
import parcial2.backend.infraestructure.persistence.entity.UserEntity;
import parcial2.backend.infraestructure.persistence.jpa.JpaFcmTokenRepository;
import parcial2.backend.infraestructure.persistence.jpa.JpaUserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class PostgresUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final JpaFcmTokenRepository jpaFcmTokenRepository;

    public PostgresUserRepository(JpaUserRepository jpaUserRepository,
                                  JpaFcmTokenRepository jpaFcmTokenRepository) {
        this.jpaUserRepository = jpaUserRepository;
        this.jpaFcmTokenRepository = jpaFcmTokenRepository;
    }

    @Override
    public User save(User user) {
        UserEntity entity = toEntity(user);
        UserEntity saved = jpaUserRepository.save(entity);
        return toDomain(saved);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return jpaUserRepository.findByEmail(email).map(this::toDomain);
    }

    @Override
    public List<User> findAll() {
        return jpaUserRepository.findAll()
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    @Override
    public void addFcmToken(String email, String fcmToken) {
        // Solo agregar si el token no existe ya en la BD
        if (!jpaFcmTokenRepository.existsByToken(fcmToken)) {
            UserEntity user = jpaUserRepository.findByEmail(email)
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + email));
            FcmTokenEntity tokenEntity = new FcmTokenEntity(fcmToken, user);
            jpaFcmTokenRepository.save(tokenEntity);
        }
    }

    @Override
    public boolean existsByEmail(String email) {
        return jpaUserRepository.existsByEmail(email);
    }

    private UserEntity toEntity(User user) {
        UserEntity entity = new UserEntity();
        entity.setEmail(user.getEmail());
        entity.setPasswordHash(user.getPasswordHash());
        entity.setFullName(user.getFullname());
        entity.setPhoneNumber(user.getPhoneNumber());
        entity.setRole(user.getRole());
        entity.setPhotoUrl(user.getImageUrl());
        return entity;
    }

    private User toDomain(UserEntity entity) {
        User user = new User();
        user.setEmail(entity.getEmail());
        user.setPasswordHash(entity.getPasswordHash());
        user.setFullname(entity.getFullName());
        user.setPhoneNumber(entity.getPhoneNumber());
        user.setRole(entity.getRole());
        user.setImageUrl(entity.getPhotoUrl());

        List<String> tokens = entity.getFcmTokens()
                .stream()
                .map(FcmTokenEntity::getToken)
                .collect(Collectors.toList());
        user.setFcmTokens(tokens);

        return user;
    }
}
