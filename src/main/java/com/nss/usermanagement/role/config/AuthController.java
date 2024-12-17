package com.nss.usermanagement.role.config;

import com.nss.usermanagement.role.constants.UsermangementConstants;
import com.nss.usermanagement.role.entity.LoginDetails;
import com.nss.usermanagement.role.entity.RolePermission;
import com.nss.usermanagement.role.entity.User;
import com.nss.usermanagement.role.exception.ResourceNotFoundException;
import com.nss.usermanagement.role.model.ModulePermissionDTO;
import com.nss.usermanagement.role.model.RolePermissionDTO;
import com.nss.usermanagement.role.repository.LoginDetailsRepository;
import com.nss.usermanagement.role.repository.RolePermissionRepository;
import com.nss.usermanagement.role.repository.UserRepository;
import com.nss.usermanagement.role.responce.RolePermissionSummary;
import com.nss.usermanagement.role.responce.TokenResponse;
import com.nss.usermanagement.role.service.CustomUserDetailsService;
import com.nss.usermanagement.role.service.RolePermissionService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping(UsermangementConstants.BASE_URL)
@Slf4j
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    RolePermissionRepository rolePermissionRepository;

    @Autowired
    private LoginDetailsRepository loginDetailsRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    public void saveLoginDetails(String email, String token, String refreshToken) {
        try {
            LoginDetails loginDetails = new LoginDetails();
            loginDetails.setEmail(email);
            loginDetails.setToken(token);
            loginDetails.setRefreshToken(refreshToken);
            loginDetailsRepository.save(loginDetails);
            log.info("Login details saved for email: {}", email);
        } catch (Exception e) {
            log.error("Error saving login details for email: {}", email, e);
            throw new RuntimeException("Failed to save login details");
        }
    }

    @PostMapping(UsermangementConstants.LOGIN)
    public ResponseEntity<TokenResponse> login(@RequestBody User user) {
        log.info("Login attempt for user: {}", user.getEmail());
        try {
            // Authenticate the user
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword()));


            List<LoginDetails> loginDetailsList = loginDetailsRepository.findAllByEmail(user.getEmail());
            if (!loginDetailsList.isEmpty()) {
                loginDetailsRepository.deleteAll(loginDetailsList);
            }
            // Generate JWT tokens
            String token = jwtUtil.generateToken(user.getEmail());
            String refreshToken = jwtUtil.generateRefreshToken(user.getEmail());
            log.info("Login successful, token generated for user: {}", user.getEmail());

            // Save login details to the database
            saveLoginDetails(user.getEmail(), token, refreshToken);

            // Fetch the user details
            Optional<User> validUserOpt = userRepository.findByEmail(user.getEmail());
            if (validUserOpt.isEmpty()) {
                throw new ResourceNotFoundException("User not found:  " + user.getEmail());
            }
            if(validUserOpt.get().getStatus()==0){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
            }

            User validUser = validUserOpt.get();

            // Prepare TokenResponse object
            TokenResponse tokenResponse = new TokenResponse();
            tokenResponse.setToken(token);
            tokenResponse.setRefreshToken(refreshToken);
            tokenResponse.setUsername(validUser.getUsername());
            tokenResponse.setEmail(validUser.getEmail());
            tokenResponse.setEmployeeId(validUser.getEmployeeId());
            tokenResponse.setSubscriptionId(validUser.getSubscriptionId());
            tokenResponse.setFullName(validUser.getFirstName() + " " + validUser.getLastName());

            log.info("RolePermissionIds for user: {}", validUser.getRolePermissionIds());

            // Process RolePermissionIds and Modules
            Set<ModulePermissionDTO> uniqueModules = new LinkedHashSet<>();
            List<RolePermissionSummary> rolePermissionSummaries = new ArrayList<>();

            String rolePermissionIds = validUser.getRolePermissionIds();
            if (rolePermissionIds != null && !rolePermissionIds.isEmpty()) {
                List<Long> roleIds = Arrays.stream(rolePermissionIds.split(","))
                        .map(String::trim)
                        .map(Long::valueOf)
                        .collect(Collectors.toList());

                log.info("Processing Role IDs: {}", roleIds);

                List<RolePermission> validRoles = rolePermissionRepository.findAllById(roleIds);
                if (validRoles.size() != roleIds.size()) {
                    throw new ResourceNotFoundException("Some RolePermission IDs are invalid.");
                }

                // Map IDs and Names to RolePermissionSummary
                rolePermissionSummaries = validRoles.stream()
                        .map(role -> new RolePermissionSummary(role.getId(), role.getRole()))
                        .collect(Collectors.toList());

                for (Long roleId : roleIds) {
                    RolePermissionDTO rolePermissionDTO = rolePermissionService.getRolePermissionById(roleId);
                    if (rolePermissionDTO != null) {
                        uniqueModules.addAll(rolePermissionDTO.getModulePermissions());
                    }
                }
            } else {
                log.warn("No RolePermissionIds found for user: {}", validUser.getEmail());
            }

            tokenResponse.setModules(new ArrayList<>(uniqueModules)); // Convert Set to List
            tokenResponse.setRolePermissionSummaries(rolePermissionSummaries); // Add ID and Name pairs

            return ResponseEntity.ok(tokenResponse);
        } catch (AuthenticationException e) {
            log.error("Authentication failed for user: {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        } catch (Exception e) {
            log.error("Login failed for user: {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @PostMapping(UsermangementConstants.FORGOT_PASSWORD)
    public ResponseEntity<String> forgotPassword(@RequestBody String username, String email) {
        log.info("Forgot password request for username: {} and email: {}", username, email);
        try {
            if (username == null || email == null) {
                log.warn("Username or email missing in forgot password request");
                return ResponseEntity.badRequest().body("Username and email are required");
            }

            User user = userRepository.findByUsernameOrEmail(username, email)
                    .orElseThrow(() -> new RuntimeException("Email/Username is mandatory to reset the password"));

            sendResetPasswordEmail(user.getEmail());

            log.info("Reset password email sent to: {}", user.getEmail());

            return ResponseEntity.ok("Reset password email sent");
        } catch (Exception e) {
            log.error("Error processing forgot password request for username: {}", username, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(UsermangementConstants.GENERATE_TOKEN)
    public ResponseEntity<String> generateToken(@RequestBody User user) {
        log.info("Token generation request for email: {}", user.getEmail());
        try {
            String token = jwtUtil.generateToken(user.getEmail());
            log.info("Token generated successfully for email: {}", user.getEmail());
            return ResponseEntity.ok("Token: " + token);
        } catch (Exception e) {
            log.error("Error generating token for email: {}", user.getEmail(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    @PostMapping(UsermangementConstants.VALIDATE_TOKEN)
    public ResponseEntity<String> validateToken(@RequestBody Map<String, String> payload) {
        String token = payload.get("token");
        String email = payload.get("email");
        log.info("Token validation request for email: {}", email);

        try {
            // Check if token exists in the database
            Optional<LoginDetails> loginDetailsOptional = loginDetailsRepository.findByEmailAndToken(email, token);
            if (loginDetailsOptional.isEmpty()) {
                log.warn("Token not found or already invalidated for email: {}", email);
                return ResponseEntity.badRequest().body("Token is invalid or expired.");
            }

            // Validate the token
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(email);
            boolean isValid = jwtUtil.validateToken(token, userDetails);

            if (isValid) {
                log.info("Token is valid for email: {}", email);
                return ResponseEntity.ok("Token is valid.");
            } else {
                log.warn("Token is invalid or expired for email: {}", email);
                return ResponseEntity.badRequest().body("Token is invalid or expired.");
            }
        } catch (Exception e) {
            log.error("Error validating token for email: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }

    private void sendResetPasswordEmail(String email) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(email);
            message.setSubject("Reset Password Request");
            message.setText("Click the link to reset your password: [reset_link_here]"); // Implement reset link logic
            emailSender.send(message);
            log.info("Reset password email sent to: {}", email);
        } catch (Exception e) {
            log.error("Error sending reset password email to: {}", email, e);
            throw new RuntimeException("Failed to send reset password email");
        }
    }



    // Add the logout endpoint to the controller
    @PostMapping(UsermangementConstants.LOGOUT)
    public ResponseEntity<String> logout(@RequestBody Map<String, String> payload) {
        String email = payload.get("email");
        log.info("Logout attempt for user: {}", email);

        try {
            // Delete all login details for the given email from the database
            List<LoginDetails> loginDetailsList = loginDetailsRepository.findAllByEmail(email);
            if (!loginDetailsList.isEmpty()) {
                loginDetailsRepository.deleteAll(loginDetailsList);
                log.info("All login details cleared for user: {}", email);
            } else {
                log.warn("No login details found for user: {}", email);
            }

            // Respond indicating successful logout
            return ResponseEntity.ok("Logout successfully" + email);
        } catch (Exception e) {
            log.error("Error during logout for user: {}", email, e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error: " + e.getMessage());
        }
    }


}
