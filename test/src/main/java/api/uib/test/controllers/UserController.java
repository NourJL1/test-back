package api.uib.test.controllers;

import api.uib.test.entities.User;
import api.uib.test.entities.Wallet;
import api.uib.test.controllers.UserController.LoginRequest;
import api.uib.test.controllers.UserController.ResponseDTO;
import api.uib.test.entities.IdentificationType;
import api.uib.test.entities.Role;
import api.uib.test.repositories.UserRepository;
import api.uib.test.repositories.RoleRepository;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.HashSet;
import java.util.Optional;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    // DTO class for login
    public static class LoginRequest {
        private String username;
        private String password;

        // Getters and setters
        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }

    // DTO de réponse
    public static class ResponseDTO {
        private String message;
        private String userId;
        private String username;
        private String fullname;
        private String phone_nbr;
        private String email;
        private IdentificationType identificationType;
        private Set<Role> role;

        public ResponseDTO(String message, String userId, String username, String fullname, Set<Role> role, String email, IdentificationType identificationType, String phone_nbr) {
            this.message = message;
            this.userId = userId;
            this.username = username;
            this.fullname = fullname;
            this.role = role;
            this.email = email;
            this.phone_nbr = phone_nbr;
            this.setIdentificationType(identificationType);
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getFullname() {
            return fullname;
        }

        public void setFullname(String fullname) {
            this.fullname = fullname;
        }

        public String getPhone_nbr() {
            return phone_nbr;
        }

        public void setPhone_nbr(String phone_nbr) {
            this.phone_nbr = phone_nbr;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Set<Role> getRole() {
            return role;
        }

        public void setRole(Set<Role> role) {
            this.role = role;
        }

		public IdentificationType getIdentificationType() {
			return identificationType;
		}

		public void setIdentificationType(IdentificationType identificationType) {
			this.identificationType = identificationType;
		}
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Validated @RequestBody User user) {
        Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken.");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Optional<Role> userRoleOpt = roleRepository.findById(1L);
        if (userRoleOpt.isEmpty()) {
            return ResponseEntity.status(500).body("Default role not found. Please ensure the role exists in the database.");
        }

        Role userRole = userRoleOpt.get();
        Set<Role> roles = new HashSet<>();
        roles.add(userRole);
        user.setRoles(roles);
        
        user.setWallet(new Wallet(user));

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(new ResponseDTO(
                "User registered successfully",
                savedUser.getId().toString(),
                savedUser.getUsername(),
                savedUser.getFullname(),
                savedUser.getRoles(),
                savedUser.getEmail(),
                savedUser.getIdentificationType(),
                savedUser.getPhoneNbr()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            Optional<User> userOpt = userRepository.findByUsername(loginRequest.getUsername());

            if (userOpt.isPresent()) {
                User user = userOpt.get();
                return ResponseEntity.ok(new ResponseDTO(
                        "Login successful",
                        user.getId().toString(),
                        user.getUsername(),
                        user.getFullname(),
                        user.getRoles(),
                        user.getEmail(),
                        user.getIdentificationType(),
                        user.getPhoneNbr()
                ));
            } else {
                return ResponseEntity.status(404).body(new ResponseDTO("User not found", null, null, null, null, null, null, null));
            }

        } catch (BadCredentialsException e) {
            return ResponseEntity.status(401).body(new ResponseDTO("Invalid credentials", null, null, null, null, null, null, null));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ResponseDTO("An error occurred during login", null, null, null, null, null, null,null));
        }
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllUsers() {
        try {
            Iterable<User> users = userRepository.findAll();
            return ResponseEntity.ok(users);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("An error occurred while retrieving users.");
        }
    }

    @GetMapping("/{username}")
    public ResponseEntity<?> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(user.get());
    }

    @PutMapping("/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @RequestBody User updatedUser) {
        Optional<User> existingUserOpt = userRepository.findByUsername(username);
        if (existingUserOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        User existingUser = existingUserOpt.get();
        existingUser.setFullname(updatedUser.getFullname());
        existingUser.setEmail(updatedUser.getEmail());

        User savedUser = userRepository.save(existingUser);
        return ResponseEntity.ok(savedUser);
    }

    @PutMapping("/{username}/assignRoles")
    public ResponseEntity<?> assignRolesToUser(@PathVariable String username, @RequestBody Set<Long> roleIds) {
        if (roleIds == null || roleIds.isEmpty()) {
            return ResponseEntity.badRequest().body("Role IDs are missing or empty.");
        }

        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found.");
        }

        User user = userOpt.get();
        Set<Role> roles = new HashSet<>();

        for (Long roleId : roleIds) {
            Optional<Role> roleOpt = roleRepository.findById(roleId);
            if (roleOpt.isPresent()) {
                roles.add(roleOpt.get());
            } else {
                return ResponseEntity.status(400).body("Invalid role ID: " + roleId);
            }
        }

        user.setRoles(roles);
        User updatedUser = userRepository.save(user);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/id/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable("userId") Long userId) {
        Optional<User> userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(404).body("User not found with ID: " + userId);
        }
       

        User user = userOpt.get();

        ResponseDTO response = new ResponseDTO(
                "User found",
                user.getId().toString(),
                user.getUsername(),
                user.getFullname(),
                user.getRoles(),
                user.getEmail(),
                user.getIdentificationType(),
                user.getPhoneNbr()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isEmpty()) {
            return ResponseEntity.status(404).build();
        }

        userRepository.delete(user.get());
        return ResponseEntity.noContent().build();
    }
}
