package parcial2.backend.domain.port;

import parcial2.backend.domain.model.NotificationResult;

import java.util.List;

public interface NotificationPort {
    List<NotificationResult> sendToDevices(List<String> fcmTokens, String title, String body);
}
