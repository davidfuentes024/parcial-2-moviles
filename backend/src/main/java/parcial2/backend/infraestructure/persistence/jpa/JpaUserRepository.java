package parcial2.backend.infraestructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import parcial2.backend.infraestructure.persistence.entity.UserEntity;

import java.util.Optional;

public interface JpaUserRepository extends JpaRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

    boolean existsByEmail(String email);
}
