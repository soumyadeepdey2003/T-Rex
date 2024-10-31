package trex.com.Web.gallery.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trex.com.Web.gallery.Model.GalleryModel;
import trex.com.Web.gallery.Repository.GalleryRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class GalleryService {

    @Autowired
    private GalleryRepository repository;


    public List<GalleryModel> getAllGalleries() throws Exception {
        try{
            log.info("Getting all galleries {}", repository.findAll().stream().toList());
            return repository.findAll();
        }catch (Exception e){
            log.error("Error getting all galleries: {}", e.getMessage());
            throw new RuntimeException("Error getting all galleries", e);
        }
    }
    public GalleryModel getGallery(Long id) throws Exception {
        try{
            Optional<GalleryModel> gallery = repository.findById(id);
            if (gallery.isPresent()) {
                return gallery.get();
            } else {
                log.error("Gallery not found");
                throw new Exception("Gallery not found");
            }
        }catch (Exception e){
            log.error("Error getting gallery: {}", e.getMessage());
            throw new RuntimeException("Error getting gallery", e);
        }

    }

    public GalleryModel addGallery(GalleryModel gallery) throws Exception {
        try {
            GalleryModel savedGallery = repository.save(gallery);
            log.info("Added gallery: {}", savedGallery);
            return savedGallery;
        } catch (Exception e) {
            log.error("Error adding gallery: {}", e.getMessage());
            throw new RuntimeException("Unable to add gallery", e);
        }
    }



    public boolean deleteGallery(Long id) throws Exception {
        try{
            repository.deleteById(id);
            log.info("Deleted gallery with id: {}", id);
            return true;
        }catch (Exception e){
            log.error("Error deleting gallery: {}", e.getMessage());
            throw new RuntimeException("Unable to delete gallery", e);
        }
    }
}
