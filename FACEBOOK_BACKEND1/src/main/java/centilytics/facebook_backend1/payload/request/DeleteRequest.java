package centilytics.facebook_backend1.payload.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeleteRequest {
    private Long id;
    private String email;
}
