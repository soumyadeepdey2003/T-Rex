package trex.com.Web.Achievement.Controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trex.com.Web.Achievement.Model.AchievementModel;
import trex.com.Web.Achievement.Service.AchievementService;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/achievement")
@Slf4j
public class AchievementController {

    @Autowired
    private AchievementService service;

    @Cacheable(value = "achievement", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<AchievementModel> getAchievements(@PathVariable Long id) {
        try {
            log.info("Received request to fetch achievement by id {}", id);
            Optional<AchievementModel> achievement = service.getAchievement(id);
            return achievement
                    .map(value -> new ResponseEntity<>(value, HttpStatus.OK))
                    .orElseThrow(NoSuchElementException::new);
        } catch (NoSuchElementException e) {
            log.error("Achievement not found with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error fetching achievement: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Cacheable(value = "achievements")
    @GetMapping("/all")
    public ResponseEntity<List<AchievementModel>> getAllAchievements() {
        try {
            log.info("Received request to fetch all achievements");
            List<AchievementModel> achievements = service.getAllAchievementsSortedByDate();
            return new ResponseEntity<>(achievements, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error fetching achievements: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CacheEvict(value = "achievements", allEntries = true)
    @PostMapping("/add")
    public ResponseEntity<AchievementModel> addAchievement(@Valid @RequestBody AchievementModel model) {
        log.info("Received request to add achievement: {}", model);
        try {
            AchievementModel addedAchievement = service.addtoAchievement(model);
            return new ResponseEntity<>(addedAchievement, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            log.error("Invalid data provided for adding achievement: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            log.error("Error adding achievement: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CacheEvict(value = "achievement", key = "#id")
    @PutMapping("/update/{id}")
    public ResponseEntity<AchievementModel> updateAchievement(@PathVariable Long id, @Valid @RequestBody AchievementModel model) {
        try {
            Optional<AchievementModel> updatedAchievement = service.updateAchievement(id, model);
            return updatedAchievement
                    .map(achievement -> new ResponseEntity<>(achievement, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (NoSuchElementException e) {
            log.error("Achievement not found for updating with id: {}", id);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            log.error("Error updating achievement: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CacheEvict(value = "achievement", key = "#id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        try {
            if (service.deleteAchievement(id)) {
                log.info("Deleted achievement with id: {}", id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                log.warn("Achievement not found for deletion with id: {}", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error deleting achievement: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
