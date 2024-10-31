package trex.com.Web.Sales.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

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

    public ItemModel(String name, String description, String specification, String price, String img) {
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
    private String img;

}
