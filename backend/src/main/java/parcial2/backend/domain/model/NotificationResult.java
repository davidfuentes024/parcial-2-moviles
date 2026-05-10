package parcial2.backend.domain.model;

public class NotificationResult {

    private String deviceToken;
    private String result;

    public NotificationResult() {}

    public NotificationResult(String deviceToken, String result) {
        this.deviceToken = deviceToken;
        this.result = result;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public String getResult() {
        return result;
    }
}
