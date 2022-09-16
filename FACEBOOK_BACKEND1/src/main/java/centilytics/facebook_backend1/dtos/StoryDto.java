package centilytics.facebook_backend1.dtos;
import centilytics.facebook_backend1.models.PrivateStory;
import centilytics.facebook_backend1.models.PublicStory;
import centilytics.facebook_backend1.models.User;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
public class StoryDto {
    private PublicStory publicStory;
    private PrivateStory privateStory;
    private User user;
}
