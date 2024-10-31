package trex.com.Web.ContactUs.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trex.com.Web.ContactUs.Model.ContactModel;
import trex.com.Web.ContactUs.Service.ContactService;

@RestController
@Slf4j
@RequestMapping("/api/v1/contact")
public class ContanctController {

    @Autowired
    private ContactService service;

    @GetMapping("/{id}")
    public ResponseEntity<ContactModel> getContact(@PathVariable Long id) {
        try{
            ContactModel contact = service.getContact(id);
            return ResponseEntity.ok(contact);
        } catch (Exception e) {
            log.error("Error fetching contact with ID {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/add")
    public ResponseEntity<ContactModel> addContact( @Valid @RequestBody ContactModel contact) {
        try{
            ContactModel savedContact = service.addContact(contact);
            return ResponseEntity.ok(savedContact);
        } catch (Exception e) {
            log.error("Error adding contact: {}", e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        try{
            service.deleteContact(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            log.error("Error deleting contact with ID {}: {}", id, e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }
}
