package trex.com.Web.Team.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Data
@Entity
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TeamModel {


    public TeamModel( String img, String name, LocalDate joining, String linkedin, String facebook, String instagram) {

        this.img = img;
        this.name = name;
        this.joining = joining;
        Linkedin = linkedin;
        Facebook = facebook;
        Instagram = instagram;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String img;
    private String name;
    private LocalDate joining;
    private String Linkedin;
    private String Facebook;
    private String Instagram;

}
