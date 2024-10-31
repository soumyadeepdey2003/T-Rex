package trex.com.Web.Team.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
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

    @Cacheable(value = "TeamCache")
    public List<TeamModel> getAllTeamMembers() {
        log.info("Fetching all team members sorted by joining date and name");
        try {
            return repository.findAll().stream()
                    .sorted(Comparator.comparing(TeamModel::getJoining)
                            .thenComparing(TeamModel::getName))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            log.error("Error fetching team members: {}", e.getMessage());
            throw new RuntimeException("Unable to fetch team members", e);
        }
    }

    @Cacheable(value = "TeamCache", key = "#id")
    public Optional<TeamModel> getTeamMember(Long id) {
        log.info("Fetching team member with ID: {}", id);
        try {
            return repository.findById(id);
        } catch (Exception e) {
            log.error("Error fetching team member with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Unable to fetch team member", e);
        }
    }

    @CacheEvict(value = "TeamCache",allEntries = true )
    public TeamModel addTeamMember(TeamModel teamMember) {
        log.info("Adding new team member: {}", teamMember);
        try {
            TeamModel savedMember = repository.save(teamMember);
            log.info("Added team member: {}", savedMember);
            return savedMember;
        } catch (Exception e) {
            log.error("Error adding team member: {}", e.getMessage());
            throw new RuntimeException("Unable to add team member", e);
        }
    }

    @CachePut(value = "TeamCache", key = "#id")
    public Optional<TeamModel> updateTeamMember(Long id, TeamModel updatedData) {
        log.info("Updating team member with ID: {}", id);
        try {
            return repository.findById(id).map(member -> {
                member.setImg(updatedData.getImg());
                member.setName(updatedData.getName());
                member.setJoining(updatedData.getJoining());
                member.setLinkedin(updatedData.getLinkedin());
                member.setFacebook(updatedData.getFacebook());
                member.setInstagram(updatedData.getInstagram());
                TeamModel updatedMember = repository.save(member);
                log.info("Updated team member: {}", updatedMember);
                return updatedMember;
            }).or(() -> {
                log.warn("Team member with ID {} not found", id);
                throw new RuntimeException("Team member not found");
            });
        } catch (Exception e) {
            log.error("Error updating team member with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Unable to update team member", e);
        }
    }

    @CacheEvict(value = "TeamCache", key = "#id")
    public boolean deleteTeamMember(Long id) {
        log.info("Deleting team member with ID: {}", id);
        try {
            if (repository.existsById(id)) {
                repository.deleteById(id);
                log.info("Deleted team member with ID: {}", id);
                return true;
            } else {
                log.warn("Team member with ID {} not found for deletion", id);
                return false;
            }
        } catch (Exception e) {
            log.error("Error deleting team member with ID {}: {}", id, e.getMessage());
            throw new RuntimeException("Unable to delete team member", e);
        }
    }


}
