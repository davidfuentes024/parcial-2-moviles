package parcial2.backend.application.service;

import org.springframework.stereotype.Service;
import parcial2.backend.application.dto.response.MessageResponse;
import parcial2.backend.domain.model.Message;
import parcial2.backend.domain.model.User;
import parcial2.backend.domain.repository.MessageRepository;
import parcial2.backend.domain.repository.UserRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class GetReceivedMessagesService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public GetReceivedMessagesService(MessageRepository messageRepository,
                                      UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    public List<MessageResponse> execute(String recipientEmail) {
        return messageRepository.findByReceiverEmail(recipientEmail)
                .stream()
                .map(msg -> toDto(msg))
                .collect(Collectors.toList());
    }

    private MessageResponse toDto(Message msg) {
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setId(msg.getId());
        messageResponse.setTitle(msg.getTitle());
        messageResponse.setBody(msg.getBody());
        messageResponse.setSenderEmail(msg.getSender());
        messageResponse.setReceiverEmail(msg.getReceiver());
        messageResponse.setSentAt(msg.getSentAt());

        Optional<User> sender = userRepository.findByEmail(msg.getSender());
        sender.ifPresent(u -> {
            messageResponse.setSenderName(u.getFullname());
            messageResponse.setSenderPhotoUrl(u.getImageUrl());
        });

        return messageResponse;
    }
}
