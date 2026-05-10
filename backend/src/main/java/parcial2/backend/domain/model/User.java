package parcial2.backend.domain.model;

import java.util.ArrayList;
import java.util.List;

public class User {

    private String email;
    private String passwordHash;
    private String fullname;
    private String role;
    private String phoneNumber;
    private String imageUrl;
    private List<String> fcmTokens;

    public User () {
        fcmTokens = new ArrayList<>();
    }

    public User(
            String email, String passwordHash, String fullname, String role,
            String phoneNumber, String imageUrl) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.fullname = fullname;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.imageUrl = imageUrl;
        this.fcmTokens = new ArrayList<>();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public List<String> getFcmTokens() {
        return fcmTokens;
    }

    public void setFcmTokens(List<String> fcmTokens) {
        this.fcmTokens = fcmTokens;
    }
}
