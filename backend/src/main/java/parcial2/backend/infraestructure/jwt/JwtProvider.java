package parcial2.backend.infraestructure.jwt;

import org.springframework.stereotype.Component;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

@Component
public class JwtProvider {

    private String jwtSecret;

    private Long jwtExpiration;

    public String generateToken(String email) throws Exception {
        try {
            long exp = Instant.now()
                    .plusMillis(jwtExpiration)
                    .getEpochSecond();

            String header = """
                {"alg":"HS256","typ":"JWT"}
            """;

            String payload = String.format("""
                {"sub":"%s","exp":%d}
            """, email, exp);

            String encodedHeader = base64Url(header);
            String encodedPayload = base64Url(payload);
            String unsignedToken = encodedHeader + "." + encodedPayload;
            String signature =  sign(unsignedToken);

            return unsignedToken + "." + signature;

        } catch (Exception e){
            throw new RuntimeException(
                    "Failed to generate JWT token", e
            );
        }
    }

    public boolean validateToken(String token){

        try {
            String[] parts = token.split("\\.");

            if (parts.length != 3) {
                return false;
            }

            String unsignedToken = parts[0] + "." + parts[1];
            String receivedSignature = parts[2];
            String expectedSignature = sign(unsignedToken);

            return expectedSignature.equals(receivedSignature);

        } catch (Exception e) {
            return false;
        }
    }

    public String getEmailFromToken(String token) {

        String[] parts = token.split("\\.");
        String payloadJson = new String(
                        Base64.getUrlDecoder().decode(parts[1]),
                        StandardCharsets.UTF_8
                );

        return payloadJson
                .split("\"sub\":\"")[1]
                .split("\"")[0];
    }

    private String sign(String data) throws Exception{

        Mac hmac = Mac.getInstance("HmacSHA256");

        SecretKeySpec key = new SecretKeySpec(
                jwtSecret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        hmac.init(key);

        byte[] signatureBytes = hmac.doFinal(data.getBytes(StandardCharsets.UTF_8));

        return Base64.getUrlEncoder()
                .withoutPadding()
                .encodeToString(signatureBytes);
    }

    private String base64Url(String value){

        return Base64.getUrlEncoder()
                .encodeToString(
                        value.getBytes(StandardCharsets.UTF_8
        ));
    }
}
