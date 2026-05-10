package parcial2.backend.application.dto.response;

public class UserResponse {

    public String email;
    public String fullname;
    public String role;
    public String phoneNumber;
    public String photoUrl;

    public UserResponse(String email, String fullname, String role, String phoneNumber, String photoUrl) {
        this.email = email;
        this.fullname = fullname;
        this.role = role;
        this.phoneNumber = phoneNumber;
        this.photoUrl = photoUrl;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
