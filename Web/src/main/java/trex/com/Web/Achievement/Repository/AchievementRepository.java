package trex.com.Web.Achievement.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trex.com.Web.Achievement.Model.AchievementModel;

@Repository
public interface AchievementRepository extends JpaRepository<AchievementModel, Long> {
}
