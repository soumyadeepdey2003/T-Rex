package trex.com.Web.Sales.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trex.com.Web.Sales.Model.ItemModel;
import trex.com.Web.Sales.Service.ItemService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    @Autowired
    private ItemService service;

    @GetMapping("/all")
    public ResponseEntity<List<ItemModel>> getAllItems() {
        try{
            List<ItemModel> items = service.getAllItems();
            log.info("Received request to fetch all items [{}]", items.stream().toList());
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error fetching all items: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ItemModel> getItem(@PathVariable Long id) {
        try{
            ItemModel item = service.getItem(id).get();
            log.info("Received request to fetch item with ID: {}", id);
            return ResponseEntity.ok(item);
        } catch (Exception e) {
            log.error("Error fetching item with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ItemModel> addItem(@Valid @RequestBody ItemModel item) {
        try{
            ItemModel savedItem = service.addItem(item);
            log.info("Received request to add item: {}", savedItem);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            log.error("Error adding item: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ItemModel> updateItem(@PathVariable Long id, @Valid @RequestBody ItemModel updatedItem) {
        try{
            ItemModel savedItem = service.updateItem(id, updatedItem);
            log.info("Received request to update item with ID: {}", id);
            return ResponseEntity.ok(savedItem);
        } catch (Exception e) {
            log.error("Error updating item with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        try{
            service.deleteItem(id);
            log.info("Received request to delete item with ID: {}", id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting item with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}