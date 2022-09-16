package centilytics.facebook_backend1.dtos;

import centilytics.facebook_backend1.models.Role;
import lombok.Data;
import lombok.Getter;

import java.util.Date;
import java.util.Set;

@Getter
@Data
public class updateDto {
    private String firstName;
    private String lastName;
    private String username;
    private Date dob;
    private String gender;
    private String email;
    private Set<Role> roles;
    private String country;
}
