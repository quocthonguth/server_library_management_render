package com.example.library_management_service.Controller;

import com.example.library_management_service.DTO.UserDTO;
import com.example.library_management_service.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(
    origins = "https://library-management-frontend-3nyy.onrender.com",
    allowedHeaders = "*",
    methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE, RequestMethod.OPTIONS}
)
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Bắt OPTIONS cho tất cả subpath
    @RequestMapping(method = RequestMethod.OPTIONS, path = "/**")
    public ResponseEntity<Void> handleOptions() {
        return ResponseEntity.ok().build();
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
        return result ? ResponseEntity.ok("User " + id + " đã bị ẩn.") : ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/unhide")
    public ResponseEntity<String> unhideUser(@PathVariable Long id) {
        boolean result = userService.unhideUserById(id);
        return result ? ResponseEntity.ok("User " + id + " đã được hiển thị lại.") : ResponseEntity.notFound().build();
    }
}
