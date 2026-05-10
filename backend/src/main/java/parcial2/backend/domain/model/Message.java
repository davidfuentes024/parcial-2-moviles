package parcial2.backend.domain.model;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Message {

    private String id;
    private String sender;
    private String receiver;
    private String body;
    private String title;
    private Instant sentAt;
    private List<NotificationResult> notificationResults;

    public Message() {
        this.notificationResults = new ArrayList<>();
        this.sentAt = Instant.now();
    }

    public Message(String title, String body, String sender, String receiver) {
        this.title = title;
        this.body = body;
        this.sender = sender;
        this.receiver = receiver;
        this.sentAt = Instant.now();
        this.notificationResults = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Instant getSentAt() {
        return sentAt;
    }

    public void setSentAt(Instant sentAt) {
        this.sentAt = sentAt;
    }

    public List<NotificationResult> getNotificationResults() {
        return notificationResults;
    }

    public void setNotificationResults(List<NotificationResult> notificationResults) {
        this.notificationResults = notificationResults;
    }
}
