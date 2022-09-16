package centilytics.facebook_backend1.security.services;

import centilytics.facebook_backend1.dtos.updateDto;
import centilytics.facebook_backend1.models.Role;
import centilytics.facebook_backend1.models.User;
import centilytics.facebook_backend1.repository.MyUserRepository;
import centilytics.facebook_backend1.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Component @Transactional
public class ServiceImpl implements UserService {

    @Autowired
    private MyUserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public String deleteUser(Long id, String email) throws Exception {
        Optional<User> userModel = this.userRepository.findById(id);
        User user = userModel.orElseThrow(() ->
                new Exception("No user found with email : " + email));
        if (!user.getEmail().equalsIgnoreCase(email)) return "Wrong Credentials";
        try {
            this.userRepository.delete(user);
        } catch (Exception exception) {
            return "delete failed";
        }
        return "user successfully deleted";
    }

    @Override
    public User updateUser(Long id, updateDto updateDto) throws Exception {
        Optional<User> userModel = this.userRepository.findById(id);
        User user = userModel.orElseThrow(() ->
                new Exception("No user found with id  : " + id));
        if (user.equals(null)) return null;
        user.setFirstName(updateDto.getFirstName());
        user.setLastName(updateDto.getLastName());
        user.setDob(updateDto.getDob());
        user.setUsername(updateDto.getUsername());
        user.setEmail(updateDto.getEmail());
        user.setRoles(updateDto.getRoles());
        return user;
    }

    @Override
    public User userDetails(Long id) throws Exception {
        Optional<User> userDetails = this.userRepository.findById(id);
        User user = userDetails.orElseThrow(() ->
                new Exception("No user found with id : " + id));
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = this.userRepository.findAll();
        return userList;
    }

//    @Bean
//    public BCryptPasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
}
