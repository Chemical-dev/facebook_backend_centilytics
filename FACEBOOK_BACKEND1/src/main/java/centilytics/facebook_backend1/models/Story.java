package centilytics.facebook_backend1.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

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
    @OneToOne
    private PrivateStory privateStory;
    @OneToOne
    private PublicStory publicStory;
    @ManyToOne
    private User user;

}
