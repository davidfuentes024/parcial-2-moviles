package parcial2.backend.domain.port;

public interface StoragePort {

    String uploadPhoto(byte[] fileBytes, String fileName, String contentType);
}
