package com.example.library_management_service.Controller;

import com.example.library_management_service.DTO.UserDTO;
import com.example.library_management_service.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/users")
@CrossOrigin(
    origins = "https://library-management-frontend-3nyy.onrender.com",
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // --- OPTIONS handler cho preflight ---
    @RequestMapping(method = RequestMethod.OPTIONS, path = "/**")
    public ResponseEntity<?> handleOptions() {
        return ResponseEntity.ok().build(); // trả về 200 OK cho preflight
    }

    @GetMapping
    public List<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public UserDTO getUserById(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PutMapping("/{id}/hide")
    public ResponseEntity<String> hideUser(@PathVariable Long id) {
        boolean result = userService.hideUserById(id);
        if (result) {
            return ResponseEntity.ok("User " + id + " đã bị ẩn.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/{id}/unhide")
    public ResponseEntity<String> unhideUser(@PathVariable Long id) {
        boolean result = userService.unhideUserById(id);
        if (result) {
            return ResponseEntity.ok("User " + id + " đã được hiển thị lại.");
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
