package parcial2.backend.infraestructure.persistence.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "notification_results")
public class NotificationResultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private MessageEntity message;

    /** Token FCM del dispositivo al que se envió la notificación */
    @Column(name = "device_token", nullable = false, length = 500)
    private String deviceToken;

    @Column(name = "result", nullable = false, length = 1000)
    private String result;

    public NotificationResultEntity() {}

    public NotificationResultEntity(MessageEntity message, String deviceToken, String result) {
        this.message = message;
        this.deviceToken = deviceToken;
        this.result = result;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public MessageEntity getMessage() { return message; }
    public void setMessage(MessageEntity message) { this.message = message; }

    public String getDeviceToken() { return deviceToken; }
    public void setDeviceToken(String deviceToken) { this.deviceToken = deviceToken; }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }
}

