package centilytics.facebook_backend1.dtos;
import centilytics.facebook_backend1.models.BodyType;
import centilytics.facebook_backend1.models.Role;
import centilytics.facebook_backend1.models.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
@Data
public class StoryDto {
    private String heading;
    private String body;
    @Enumerated(EnumType.STRING)
    private BodyType type;
    private User user;
}
