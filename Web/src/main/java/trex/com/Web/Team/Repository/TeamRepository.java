package trex.com.Web.Team.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trex.com.Web.Team.Model.TeamModel;


@Repository
public interface TeamRepository extends JpaRepository<TeamModel,Long> {
}
