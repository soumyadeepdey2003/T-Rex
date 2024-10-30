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
    public List<AchievementModel> getAllAchievementsSortedByDate() {
        log.info("Fetching all achievements sorted by date");
        try {
            return repository.findAll().stream()
                    .sorted(Comparator.comparing(AchievementModel::getDate))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching achievements: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch achievements", e);
        }
    }

    @CachePut(value = "achievements", key = "#model.id")
    public AchievementModel addtoAchievement(AchievementModel model) {
        log.info("Attempting to add achievement: {}", model);
        try {
            AchievementModel savedAchievement = repository.save(model);
            log.info("Added achievement: {}", savedAchievement);
            return savedAchievement;
        } catch (Exception e) {
            log.error("Error adding achievement: {}", e.getMessage());
            throw new RuntimeException("Unable to add achievement", e);
        }
    }

    @CachePut(value = "achievements", key = "#id")
    public Optional<AchievementModel> updateAchievement(Long id, AchievementModel model) {
        log.info("Updating achievement with ID: {}", id);
        try {
            return repository.findById(id).map(existingAchievement -> {
                existingAchievement.setDate(model.getDate());
                existingAchievement.setCollege(model.getCollege());
                existingAchievement.setEvent(model.getEvent());
                existingAchievement.setPosition(model.getPosition());
                AchievementModel updatedAchievement = repository.save(existingAchievement);
                log.info("Updated achievement: {}", updatedAchievement);
                return updatedAchievement;
            }).or(() -> {
                log.warn("Achievement with ID {} not found", id);
                throw new RuntimeException("Achievement not found");
            });
        } catch (Exception e) {
            log.error("Error updating achievement: {}", e.getMessage());
            throw new RuntimeException("Unable to update achievement", e);
        }
    }

    @CacheEvict(value = "achievements", key = "#id")
    public boolean deleteAchievement(Long id) {
        log.info("Deleting achievement with ID: {}", id);
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                log.info("Deleted achievement with ID: {}", id);
                return true;
            } else {
                log.warn("Achievement with ID {} not found for deletion", id);
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            log.error("Error deleting achievement: {}", e.getMessage());
            throw new RuntimeException("Unable to delete achievement", e);
        }
    }

    @Cacheable(value = "achievement", key = "#id")
    public Optional<AchievementModel> getAchievement(Long id) {
        log.info("Fetching achievement with ID: {}", id);
        return repository.findById(id);
    }
}
