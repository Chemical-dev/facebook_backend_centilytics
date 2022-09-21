package centilytics.facebook_backend1.controllers;

import centilytics.facebook_backend1.annotations.*;
import centilytics.facebook_backend1.dtos.updateDto;
import centilytics.facebook_backend1.models.ERole;
import centilytics.facebook_backend1.models.Role;
import centilytics.facebook_backend1.models.User;
import centilytics.facebook_backend1.payload.request.DeleteRequest;
import centilytics.facebook_backend1.payload.request.LoginRequest;
import centilytics.facebook_backend1.payload.request.SignupRequest;
import centilytics.facebook_backend1.payload.response.JwtResponse;
import centilytics.facebook_backend1.payload.response.MessageResponse;
import centilytics.facebook_backend1.payload.response.ResponseHandler;
import centilytics.facebook_backend1.repository.RoleRepository;
import centilytics.facebook_backend1.repository.UserRepository;
import centilytics.facebook_backend1.security.jwt.JwtUtils;
import centilytics.facebook_backend1.security.services.ServiceImpl;
import centilytics.facebook_backend1.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/facebook/user")
public class UserController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    private ServiceImpl userServiceImpl;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @DELETE("/") @PREAUTHORIZE("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteUser(@RequestBody DeleteRequest deleteRequest) throws Exception{
        if(deleteRequest == null) return ResponseHandler
                .generateResponse(HttpStatus.BAD_REQUEST, "FIELDS CANNOT BE EMPTY", null);
        this.userServiceImpl.deleteUser(deleteRequest.getId(), deleteRequest.getEmail());
        return ResponseHandler
                .generateResponse(HttpStatus.OK, "USER DELETED SUCCESSFULLY", null);
    }

    @GET("/users") @PREAUTHORIZE("hasRole('ADMIN')")
    public ResponseEntity<Object>  getAllUsers() throws Exception{
        List<User> userList = this.userServiceImpl.getAllUsers();
        return ResponseHandler
                .generateResponse(HttpStatus.OK, "USERS FETCHED SUCCESSFULLY", userList);
    }

    @PUT("/{id}") @PREAUTHORIZE("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long userId, @RequestBody updateDto updateDto) throws Exception{
      User updatedUser = this.userServiceImpl.updateUser(userId, updateDto);
      return ResponseHandler
              .generateResponse(HttpStatus.OK, "USERS UPDATED SUCCESSFULLY", updatedUser);
    }

    @GET("/{id}") @PREAUTHORIZE("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> userDetails(@PathVariable("id") Long userId) throws Exception{
        User user = this.userServiceImpl.userDetails(userId);
        return ResponseHandler
                .generateResponse(HttpStatus.OK, "USERS FETCHED SUCCESSFULLY", user);
    }

    @POST("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                roles));
    }

    @POST("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User user = new User(signUpRequest.getFirstName(), signUpRequest.getLastName(),signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()), signUpRequest.getDob());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

}
