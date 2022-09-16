package centilytics.facebook_backend1.controllers;

import centilytics.facebook_backend1.dtos.updateDto;
import centilytics.facebook_backend1.models.User;
import centilytics.facebook_backend1.payload.request.DeleteRequest;
import centilytics.facebook_backend1.payload.response.ResponseHandler;
import centilytics.facebook_backend1.security.services.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/facebook/user")
public class UserController {

    @Autowired
    private ServiceImpl userServiceImpl;

    @DeleteMapping("/")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> deleteUser(@RequestBody DeleteRequest deleteRequest) throws Exception{
        if(deleteRequest == null) return ResponseHandler
                .generateResponse(HttpStatus.BAD_REQUEST, "FIELDS CANNOT BE EMPTY", null);
        this.userServiceImpl.deleteUser(deleteRequest.getId(), deleteRequest.getEmail());
        return ResponseHandler
                .generateResponse(HttpStatus.ACCEPTED, "USER DELETED SUCCESSFULLY", null);
    }

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Object>  getAllUsers() throws Exception{
        List<User> userList = this.userServiceImpl.getAllUsers();
        return ResponseHandler
                .generateResponse(HttpStatus.ACCEPTED, "USERS FETCHED SUCCESSFULLY", userList);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> updateUser(@PathVariable("id") Long userId, @RequestBody updateDto updateDto) throws Exception{
      User updatedUser = this.userServiceImpl.updateUser(userId, updateDto);
      return ResponseHandler
              .generateResponse(HttpStatus.ACCEPTED, "USERS UPDATED SUCCESSFULLY", updatedUser);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Object> userDetails(@PathVariable("id") Long userId) throws Exception{
        User user = this.userServiceImpl.userDetails(userId);
        return ResponseHandler
                .generateResponse(HttpStatus.ACCEPTED, "USERS FETCHED SUCCESSFULLY", user);
    }

}
