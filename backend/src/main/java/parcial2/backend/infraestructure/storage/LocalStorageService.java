package parcial2.backend.infraestructure.storage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import parcial2.backend.domain.port.StoragePort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class LocalStorageService implements StoragePort {

    @Value("${app.uploads.dir:uploads/photos}")
    private String uploadsDir;

    @Value("${server.port:8080}")
    private String serverPort;

    @Override
    public String uploadPhoto(byte[] fileBytes, String fileName, String contentType) {
        try {
            Path uploadPath = Paths.get(uploadsDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String justFileName = Paths.get(fileName).getFileName().toString();

            Path filePath = uploadPath.resolve(justFileName);
            Files.write(filePath, fileBytes);

            return "http://10.0.2.2:" + serverPort + "/uploads/photos/" + justFileName;

        } catch (IOException e) {
            throw new RuntimeException("Error guardando foto en disco: " + e.getMessage(), e);
        }
    }
}

