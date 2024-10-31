package trex.com.Web.ContactUs.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import trex.com.Web.ContactUs.Model.ContactModel;

@Repository
public interface ContactRepository extends JpaRepository<ContactModel, Long> {
}
