package parcial2.backend.presentation.contorller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import parcial2.backend.application.dto.response.UserResponse;
import parcial2.backend.application.service.GetUsersService;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final GetUsersService getUsersService;

    public UserController(GetUsersService getUsersService) {
        this.getUsersService = getUsersService;
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(getUsersService.execute());
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserResponse> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(getUsersService.executeForOne(email));
    }
}
