package parcial2.backend.infraestructure.persistence.adapter;

import org.springframework.stereotype.Repository;
import parcial2.backend.domain.model.Message;
import parcial2.backend.domain.model.NotificationResult;
import parcial2.backend.domain.repository.MessageRepository;
import parcial2.backend.infraestructure.persistence.entity.MessageEntity;
import parcial2.backend.infraestructure.persistence.entity.NotificationResultEntity;
import parcial2.backend.infraestructure.persistence.entity.UserEntity;
import parcial2.backend.infraestructure.persistence.jpa.JpaMessageRepository;
import parcial2.backend.infraestructure.persistence.jpa.JpaUserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PostgresMessageRepository implements MessageRepository {

    private final JpaMessageRepository jpaMessageRepository;
    private final JpaUserRepository jpaUserRepository;

    public PostgresMessageRepository(JpaMessageRepository jpaMessageRepository,
                                     JpaUserRepository jpaUserRepository) {
        this.jpaMessageRepository = jpaMessageRepository;
        this.jpaUserRepository = jpaUserRepository;
    }

    @Override
    public Message save(Message message) {

        UserEntity sender = jpaUserRepository.findByEmail(message.getSender())
                .orElseThrow(() -> new IllegalArgumentException("Remitente no encontrado"));
        UserEntity recipient = jpaUserRepository.findByEmail(message.getReceiver())
                .orElseThrow(() -> new IllegalArgumentException("Destinatario no encontrado"));

        MessageEntity entity = new MessageEntity();
        entity.setTitle(message.getTitle());
        entity.setBody(message.getBody());
        entity.setSender(sender);
        entity.setRecipient(recipient);
        entity.setSentAt(message.getSentAt());

        MessageEntity saved = jpaMessageRepository.save(entity);

        List<NotificationResultEntity> results = message.getNotificationResults()
                .stream()
                .map(nr -> new NotificationResultEntity(saved, nr.getDeviceToken(), nr.getResult()))
                .collect(Collectors.toList());

        saved.setNotificationResults(results);
        MessageEntity withResults = jpaMessageRepository.save(saved);

        return toDomain(withResults);
    }

    @Override
    public List<Message> findByReceiverEmail(String recipientEmail) {
        return jpaMessageRepository
                .findByRecipientEmailOrderBySentAtDesc(recipientEmail)
                .stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Message toDomain(MessageEntity entity) {
        Message message = new Message();
        message.setId(String.valueOf(entity.getId()));
        message.setTitle(entity.getTitle());
        message.setBody(entity.getBody());
        message.setSender(entity.getSender().getEmail());
        message.setReceiver(entity.getRecipient().getEmail());
        message.setSentAt(entity.getSentAt());

        List<NotificationResult> results = entity.getNotificationResults()
                .stream()
                .map(nr -> new NotificationResult(nr.getDeviceToken(), nr.getResult()))
                .collect(Collectors.toList());
        message.setNotificationResults(results);

        return message;
    }
}
