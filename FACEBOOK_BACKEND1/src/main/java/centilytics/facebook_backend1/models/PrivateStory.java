package centilytics.facebook_backend1.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class PrivateStory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private  String heading;
    private  String body;
    @NotEmpty(message = "type field is empty")
    @Enumerated(EnumType.STRING)
    private BodyType type;
    @OneToOne
    Story story;
}
