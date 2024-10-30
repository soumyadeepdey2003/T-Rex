package trex.com.Web.Team.Service;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import trex.com.Web.Team.Model.TeamModel;
import trex.com.Web.Team.Repository.TeamRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TeamService {

    @Autowired
    private TeamRepository repository;

    @Cacheable(value = "Team")
    public List<TeamModel> getTeamByJoiningDate() {
        log.info("Fetching all achievements sorted by date");
        try {
            return repository.findAll().stream()
                    .sorted(Comparator.comparing(TeamModel::getName))
                    .sorted(Comparator.comparing(TeamModel::getJoining))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching achievements: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch achievements", e);
        }
    }

    @CachePut(value = "Team", key = "#model.id")
    public TeamModel addtoTeam(TeamModel model) {
        log.info("Attempting to add Team: {}", model);
        try {
            TeamModel savedTeam = repository.save(model);
            log.info("Added Team: {}", savedTeam);
            return savedTeam;
        } catch (Exception e) {
            log.error("Error adding Team: {}", e.getMessage());
            throw new RuntimeException("Unable to add Team", e);
        }
    }

    @CachePut(value = "Team", key = "#id")
    public Optional<TeamModel> updateTeam(Long id, TeamModel model) {
        log.info("Updating Team with ID: {}", id);
        try {
            return repository.findById(id).map(existingmember -> {
                existingmember.setId(model.getId());
                existingmember.setImg(model.getImg());
                existingmember.setName(model.getName());
                existingmember.setJoining(model.getJoining());
                existingmember.setLinkedin(model.getLinkedin());
                existingmember.setFacebook(model.getFacebook());
                existingmember.setInstagram(model.getInstagram());
                TeamModel updatedTeam = repository.save(existingmember);
                log.info("Updated Team: {}", updatedTeam);
                return updatedTeam;
            }).or(() -> {
                log.warn("Team with ID {} not found", id);
                throw new RuntimeException("Team not found");
            });
        } catch (Exception e) {
            log.error("Error updating Team: {}", e.getMessage());
            throw new RuntimeException("Unable to update Team", e);
        }
    }

    @CacheEvict(value = "Team", key = "#id")
    public boolean deleteTeam(Long id) {
        log.info("Deleting Team with ID: {}", id);
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                log.info("Deleted Team with ID: {}", id);
                return true;
            } else {
                log.warn("Team with ID {} not found for deletion", id);
                return false;
            }
        } catch (EmptyResultDataAccessException e) {
            log.error("Error deleting Team: {}", e.getMessage());
            throw new RuntimeException("Unable to delete Team", e);
        }
    }

    @Cacheable(value = "member", key = "#id")
    public Optional<TeamModel> getTeam(Long id) {
        log.info("Fetching Team with ID: {}", id);
        return repository.findById(id);
    }
}
