package centilytics.facebook_backend1.payload.request;

import lombok.Getter;

@Getter
public class DeleteUser {
    private Long id;
    private Long userId;
}