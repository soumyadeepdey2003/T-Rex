package trex.com.Web.Sales.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import trex.com.Web.Sales.Model.ItemModel;
import trex.com.Web.Sales.Repository.ItemRepository;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class ItemService {

    @Autowired
    private ItemRepository repository;

    @Cacheable(value = "ItemCache")
    public List<ItemModel> getAllItems() throws Exception {
        log.info("Fetching all items");
        try {
            return repository.findAll();
        } catch (Exception e) {
            log.error("Error fetching all items: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch all items", e);
        }
    }

    @Cacheable(value = "ItemCache", key = "#id")
    public Optional<ItemModel> getItem(Long id) throws Exception {
        log.info("Fetching item with ID: {}", id);
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching item with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Unable to fetch item", e);
        }
    }

    @CacheEvict(value = "ItemCache", allEntries = true)
    public ItemModel addItem(ItemModel item) throws Exception {
        log.info("Adding new item: {}", item);
        try {
            ItemModel savedItem = repository.save(item);
            log.info("Added item: {}", savedItem);
            return savedItem;
        } catch (Exception e) {
            log.error("Error adding item: {}", e.getMessage());
            throw new RuntimeException("Unable to add item", e);
        }
    }

    @CachePut(value = "ItemCache", key = "#id")
    public ItemModel updateItem(Long id, ItemModel updatedItem) throws Exception {
        log.info("Updating item with ID: {}", id);
        try {
            Optional<ItemModel> existingItem = repository.findById(id);
            if (existingItem.isPresent()){
                updatedItem.setId(id);
                updatedItem.setName(existingItem.get().getName());
                updatedItem.setPrice(existingItem.get().getPrice());
                updatedItem.setDescription(existingItem.get().getDescription());
                updatedItem.setImg(existingItem.get().getImg());
                updatedItem.setSpecification(existingItem.get().getSpecification());
                ItemModel savedItem = repository.save(updatedItem);
                log.info("Updated item: {}", savedItem);
                return savedItem;
            } else {
                log.warn("Item with ID {} not found", id);
                throw new RuntimeException("Item not found");
            }
        } catch (Exception e) {
            log.error("Error updating item with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Unable to update item", e);
        }
    }

    @CacheEvict(value = "ItemCache", key = "#id")
    public boolean deleteItem(Long id) throws Exception {

        log.info("Deleting item with ID: {}", id);
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                log.info("Deleted item with ID: {}", id);
                return true;
            } else {
                log.warn("Item with ID {} not found for deletion", id);
                return false;
            }
        } catch (Exception e) {
            log.error("Error deleting item with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Unable to delete item", e);
        }
    }
}
