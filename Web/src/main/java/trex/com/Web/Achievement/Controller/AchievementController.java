package trex.com.Web.Achievement.Controller;


import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trex.com.Web.Achievement.Model.AchievementModel;
import trex.com.Web.Achievement.Service.AchievementService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/achievement")
@Slf4j
public class AchievementController {

    @Autowired
    private AchievementService service;

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

    @PostMapping("/add")
    public ResponseEntity<AchievementModel> addAchievement(@Valid @RequestBody AchievementModel model) {
        log.info("Received request to add achievement: {}", model);
        try {
            AchievementModel addedAchievement = service.addtoAchievement(model);
            return new ResponseEntity<>(addedAchievement, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error adding achievement: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<AchievementModel> updateAchievement(@PathVariable Long id, @Valid @RequestBody AchievementModel model) {
        try {
            Optional<AchievementModel> updatedAchievement = service.updateAchievement(id, model);
            return updatedAchievement
                    .map(achievement -> new ResponseEntity<>(achievement, HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            log.error("Error updating achievement: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteAchievement(@PathVariable Long id) {
        try {
            if (service.deleteAchievement(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error deleting achievement: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
