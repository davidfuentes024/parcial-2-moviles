package parcial2.backend.infraestructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import parcial2.backend.infraestructure.persistence.entity.FcmTokenEntity;

import java.util.Optional;

public interface JpaFcmTokenRepository extends JpaRepository<FcmTokenEntity, Long> {

    boolean existsByToken(String token);

    Optional<FcmTokenEntity> findByToken(String token);
}