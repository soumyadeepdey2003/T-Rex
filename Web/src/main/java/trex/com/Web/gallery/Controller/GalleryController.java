package trex.com.Web.gallery.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trex.com.Web.gallery.Model.GalleryModel;
import trex.com.Web.gallery.Service.GalleryService;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1/gallery")
public class GalleryController {

    @Autowired
    private GalleryService service;

    @GetMapping("/all")
    public ResponseEntity<List<GalleryModel>> getAllGalleries(){
        try{
            List<GalleryModel> galleries = service.getAllGalleries();
            log.info("Received request to fetch all galleries [{}]", galleries.stream().toList());
            return ResponseEntity.ok(galleries);
        }catch (Exception e){
            log.error("Error fetching all galleries: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<GalleryModel> getGallery(@PathVariable Long id){
        try{
            GalleryModel gallery = service.getGallery(id);
            log.info("Received request to fetch gallery with ID: {}", id);
            return ResponseEntity.ok(gallery);
        }catch (Exception e){
            log.error("Error fetching gallery with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/add")
    public ResponseEntity<GalleryModel> addGallery(@Valid @RequestBody GalleryModel gallery){
        try{
            GalleryModel addedGallery = service.addGallery(gallery);
            log.info("Received request to add gallery: {}", gallery);
            return new ResponseEntity<>(addedGallery, HttpStatus.CREATED);
        }catch (Exception e){
            log.error("Error adding gallery: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteGallery(@PathVariable Long id){
        try{
            service.deleteGallery(id);
            log.info("Received request to delete gallery with ID: {}", id);
            return ResponseEntity.noContent().build();
        }catch (Exception e){
            log.error("Error deleting gallery with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
