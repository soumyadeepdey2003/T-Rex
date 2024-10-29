package trex.com.Web.Achievement.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import trex.com.Web.Achievement.Model.AchievementModel;
import trex.com.Web.Achievement.Repository.AchievementRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class AchievementService {

    @Autowired
    private AchievementRepository repository;

    @Cacheable(value = "achievements")
    public List<AchievementModel> getAllAchievementsSortedByDate() throws Exception {
        try {
            log.info("Fetching all achievements sorted by date");
            return repository.findAll().stream()
                    .sorted(Comparator.comparing(AchievementModel::getDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching achievements: {}", e.getMessage());
            throw new Exception("Unable to fetch achievements", e);
        }
    }

    @CachePut(value = "achievements", key = "#model.id")
    public AchievementModel addtoAchievement(AchievementModel model) throws Exception {
        try {
            log.info("Attempting to add achievement: {}", model);

            new Thread(() -> {

                try {
                    Thread.sleep(2000); 
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                log.info("Added achievement: {}", model);
            }).start();
            return repository.save(model);
        } catch (Exception e) {
            log.error("Error adding achievement: {}", e.getMessage());
            throw new Exception("Unable to add achievement", e);
        }
    }

    @CachePut(value = "achievements", key = "#id")
    public Optional<AchievementModel> updateAchievement(Long id, AchievementModel model) throws Exception {
        try {
            return repository.findById(id).map(existingAchievement -> {
                existingAchievement.setDate(model.getDate());
                existingAchievement.setCollege(model.getCollege());
                existingAchievement.setEvent(model.getEvent());
                existingAchievement.setPosition(model.getPosition());
                log.info("Updating achievement with ID: {}", id);
                return repository.save(existingAchievement);
            }).or(() -> {
                log.warn("Achievement with ID {} not found", id);
                throw new RuntimeException("Achievement not found");
            });
        } catch (Exception e) {
            log.error("Error updating achievement: {}", e.getMessage());
            throw new Exception("Unable to update achievement", e);
        }
    }

    @CacheEvict(value = "achievements", key = "#id")
    public boolean deleteAchievement(Long id) throws Exception {
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                log.info("Deleted achievement with ID: {}", id);
                return true;
            } else {
                log.warn("Attempted to delete non-existing achievement with ID: {}", id);
                throw new RuntimeException("Achievement not found");
            }
        } catch (EmptyResultDataAccessException e) {
            log.error("Error deleting achievement: {}", e.getMessage());
            throw new Exception("Unable to delete achievement", e);
        }
    }
}
