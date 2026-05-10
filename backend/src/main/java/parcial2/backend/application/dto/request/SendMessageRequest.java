package parcial2.backend.application.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class SendMessageRequest {

    @Email(message = "Email del destinatario inválido")
    @NotBlank(message = "El email del destinatario es obligatorio")
    private String receiverEmail;

    @NotBlank(message = "El título es obligatorio")
    private String title;

    @NotBlank(message = "El cuerpo del mensaje es obligatorio")
    private String body;

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
