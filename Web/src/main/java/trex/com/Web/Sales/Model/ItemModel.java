package trex.com.Web.Sales.Model;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Builder
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ItemModel {

    public ItemModel(String name, String description, String specification, String price, List<String> img) {
        this.name = name;
        this.description = description;
        this.specification = specification;
        this.price = price;
        this.img = img;
    }
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;
    private String description;
    private String specification;
    private String price;
    @ElementCollection
    private List<String> img ;


}
