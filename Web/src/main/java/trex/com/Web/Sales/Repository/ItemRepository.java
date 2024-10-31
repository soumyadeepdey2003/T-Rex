package trex.com.Web.Sales.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trex.com.Web.Sales.Model.ItemModel;

@Repository
public interface ItemRepository extends JpaRepository<ItemModel, Long> {
}
