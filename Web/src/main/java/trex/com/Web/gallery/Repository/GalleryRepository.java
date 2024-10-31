package trex.com.Web.gallery.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trex.com.Web.gallery.Model.GalleryModel;

@Repository
public interface GalleryRepository extends JpaRepository<GalleryModel, Long> {
}
