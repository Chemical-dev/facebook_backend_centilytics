package centilytics.facebook_backend1.dtos;

import centilytics.facebook_backend1.models.BodyType;
import lombok.Data;
import lombok.Getter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Getter
@Data
public class PrivateStoryDto {
   private String heading;
    private String body;
    @Enumerated(EnumType.STRING)
    private BodyType type;
}
