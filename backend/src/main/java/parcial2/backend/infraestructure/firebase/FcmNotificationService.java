package parcial2.backend.infraestructure.firebase;

import com.google.firebase.messaging.*;
import org.springframework.stereotype.Service;
import parcial2.backend.domain.model.NotificationResult;
import parcial2.backend.domain.port.NotificationPort;

import java.util.ArrayList;
import java.util.List;

@Service
public class FcmNotificationService implements NotificationPort {

    @Override
    public List<NotificationResult> sendToDevices(List<String> fcmTokens, String title, String body) {
        List<NotificationResult> results = new ArrayList<>();
        for (String token : fcmTokens) {
            results.add(sendToDevice(token, title, body));
        }
        return results;
    }

    private NotificationResult sendToDevice(String fcmToken, String title, String body) {
        try {
            AndroidConfig androidConfig = AndroidConfig.builder()
                    .setPriority(AndroidConfig.Priority.HIGH)
                    .setNotification(AndroidNotification.builder()
                            .setSound("default")
                            .setChannelId("messaging_channel")
                            .build())
                    .build();

            Message message = Message.builder()
                    .setToken(fcmToken)
                    .setNotification(Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .setAndroidConfig(androidConfig)
                    .build();

            String response = FirebaseMessaging.getInstance().send(message);
            return new NotificationResult(fcmToken, response);

        } catch (FirebaseMessagingException e) {
            return new NotificationResult(fcmToken,
                    "ERROR: " + e.getMessagingErrorCode() + " - " + e.getMessage());
        }
    }
}
