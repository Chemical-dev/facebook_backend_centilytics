package centilytics.facebook_backend1.security.services;

import centilytics.facebook_backend1.dtos.updateDto;
import centilytics.facebook_backend1.models.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    String deleteUser(Long id, String email) throws Exception;
    User updateUser(Long id, updateDto updateDto) throws Exception;
    User userDetails(Long id) throws Exception;
    List<User> getAllUsers();


}
