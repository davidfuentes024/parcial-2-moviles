package parcial2.backend.infraestructure.persistence.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import parcial2.backend.infraestructure.persistence.entity.MessageEntity;

import java.util.List;

public interface JpaMessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findByRecipientEmailOrderBySentAtDesc(String recipientEmail);
}


