package parcial2.backend.application.service;


import org.springframework.stereotype.Service;
import parcial2.backend.application.dto.request.SendMessageRequest;
import parcial2.backend.application.dto.response.MessageResponse;
import parcial2.backend.domain.model.Message;
import parcial2.backend.domain.model.NotificationResult;
import parcial2.backend.domain.model.User;
import parcial2.backend.domain.port.NotificationPort;
import parcial2.backend.domain.repository.MessageRepository;
import parcial2.backend.domain.repository.UserRepository;

import java.util.List;

@Service
public class SendMessageService {

    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final NotificationPort notificationPort;

    public SendMessageService(UserRepository userRepository,
                              MessageRepository messageRepository,
                              NotificationPort notificationPort) {
        this.userRepository = userRepository;
        this.messageRepository = messageRepository;
        this.notificationPort = notificationPort;
    }

    public MessageResponse execute(SendMessageRequest messageRequest, String senderEmail) {
        User sender = userRepository.findByEmail(senderEmail)
                .orElseThrow(() -> new IllegalArgumentException("Remitente no encontrado"));

        User recipient = userRepository.findByEmail(messageRequest.getReceiverEmail())
                .orElseThrow(() -> new IllegalArgumentException("Destinatario no encontrado: " + messageRequest.getReceiverEmail()));

        Message message = new Message(messageRequest.getTitle(), messageRequest.getBody(), senderEmail, messageRequest.getReceiverEmail());

        if (!recipient.getFcmTokens().isEmpty()) {
            List<NotificationResult> results = notificationPort.sendToDevices(
                    recipient.getFcmTokens(),
                    messageRequest.getTitle(),
                    "De: " + sender.getFullname() + " - " + messageRequest.getBody()
            );
            message.setNotificationResults(results);
        }

        Message saved = messageRepository.save(message);

        MessageResponse response = new MessageResponse();
        response.setId(saved.getId());
        response.setTitle(saved.getTitle());
        response.setBody(saved.getBody());
        response.setSenderEmail(saved.getSender());
        response.setSenderName(sender.getFullname());
        response.setSenderPhotoUrl(sender.getImageUrl());
        response.setReceiverEmail(saved.getReceiver());
        response.setSentAt(saved.getSentAt());
        return response;

    }
}
