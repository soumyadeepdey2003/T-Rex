package trex.com.Web.Team.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import trex.com.Web.Team.Model.TeamModel;
import trex.com.Web.Team.Service.TeamService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/team")
@Slf4j
public class TeamController {

    @Autowired
    private TeamService service;

    @Cacheable(value = "TeamCache")
    @GetMapping("/all")
    public ResponseEntity<List<TeamModel>> getAllTeamMembers() {
        log.info("Fetching all team members");
        try {
            List<TeamModel> teamMembers = service.getAllTeamMembers();
            return ResponseEntity.ok(teamMembers);
        } catch (Exception e) {
            log.error("Error fetching team members: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Cacheable(value = "TeamCache", key = "#id")
    @GetMapping("/{id}")
    public ResponseEntity<TeamModel> getTeamMember(@PathVariable Long id) {
        log.info("Fetching team member with ID: {}", id);
        try {
            return service.getTeamMember(id)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("Team member with ID {} not found", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Error fetching team member with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CacheEvict(value = "TeamCache", allEntries = true)
    @PostMapping("/add")
    public ResponseEntity<TeamModel> addTeamMember(@RequestBody TeamModel teamMember) {
        log.info("Adding new team member");
        try {
            TeamModel savedMember = service.addTeamMember(teamMember);
            return new ResponseEntity<>(savedMember, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error adding team member: {}", e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CachePut(value = "TeamCache", key = "#id")
    @PutMapping("/update/{id}")
    public ResponseEntity<TeamModel> updateTeamMember(@PathVariable Long id, @RequestBody TeamModel updatedData) {
        log.info("Updating team member with ID: {}", id);
        try {
            return service.updateTeamMember(id, updatedData)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> {
                        log.warn("Team member with ID {} not found for update", id);
                        return ResponseEntity.notFound().build();
                    });
        } catch (Exception e) {
            log.error("Error updating team member with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @CacheEvict(value = "TeamCache", key = "#id")
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTeamMember(@PathVariable Long id) {
        log.info("Deleting team member with ID: {}", id);
        try {
            if (service.deleteTeamMember(id)) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
                log.warn("Team member with ID {} not found for deletion", id);
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            log.error("Error deleting team member with ID {}: {}", id, e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    
}
