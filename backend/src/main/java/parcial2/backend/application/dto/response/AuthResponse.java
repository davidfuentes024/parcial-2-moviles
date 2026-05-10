package parcial2.backend.application.dto.response;

public class AuthResponse {

    public String token;
    public String email;
    public String fullname;
    public String photoUrl;

    public AuthResponse() {}

    public AuthResponse(String token, String email, String fullName, String photoUrl) {
        this.token = token;
        this.email = email;
        this.fullname = fullname;
        this.photoUrl = photoUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
