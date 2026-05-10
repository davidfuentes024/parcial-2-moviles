package parcial2.backend.presentation.contorller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import parcial2.backend.application.dto.request.SendMessageRequest;
import parcial2.backend.application.dto.response.MessageResponse;
import parcial2.backend.application.service.GetReceivedMessagesService;
import parcial2.backend.application.service.SendMessageService;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final SendMessageService sendMessageService;
    private final GetReceivedMessagesService getReceivedMessagesService;

    public MessageController(SendMessageService sendMessageService,
                             GetReceivedMessagesService getReceivedMessagesService) {
        this.sendMessageService = sendMessageService;
        this.getReceivedMessagesService = getReceivedMessagesService;
    }

    @PostMapping("/send")
    public ResponseEntity<MessageResponse> sendMessage(
            @Valid @RequestBody SendMessageRequest dto,
            Authentication authentication) {
        // El email del remitente viene del JWT (no del body)
        String senderEmail = authentication.getName();
        MessageResponse response = sendMessageService.execute(dto, senderEmail);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/received")
    public ResponseEntity<List<MessageResponse>> getReceivedMessages(Authentication authentication) {
        String recipientEmail = authentication.getName();
        return ResponseEntity.ok(getReceivedMessagesService.execute(recipientEmail));
    }
}
