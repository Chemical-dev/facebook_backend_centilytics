package centilytics.facebook_backend1.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(	name = "facebook_story",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "id"),
        })
public class Story {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @NotBlank
    @Size(max = 30)
    private  String heading;
    @NotBlank
    @Size(max = 100)
    private  String body;
    @NotEmpty(message = "type field is empty")
    @Enumerated(EnumType.STRING)
    private BodyType type;
    @ManyToOne
    private User user;
    @ManyToMany
    private List<EmailVault> emailVaults = new ArrayList<>();

}
