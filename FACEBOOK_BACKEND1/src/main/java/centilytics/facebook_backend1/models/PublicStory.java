package centilytics.facebook_backend1.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "public_story")
public class PublicStory {
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
