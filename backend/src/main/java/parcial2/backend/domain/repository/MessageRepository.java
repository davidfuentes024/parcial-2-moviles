package parcial2.backend.domain.repository;

import parcial2.backend.domain.model.Message;

import java.util.List;

public interface MessageRepository {

    Message save(Message message);

    List<Message> findByReceiverEmail(String receiverEmail);

}
