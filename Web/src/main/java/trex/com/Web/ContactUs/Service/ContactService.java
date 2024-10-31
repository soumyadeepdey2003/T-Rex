package trex.com.Web.ContactUs.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import trex.com.Web.ContactUs.Model.ContactModel;
import trex.com.Web.ContactUs.Repository.ContactRepository;

import java.util.Optional;

@Slf4j
@Service
public class ContactService {

    @Autowired
    private ContactRepository repository;

    public ContactModel addContact(ContactModel contact) throws Exception {
        try{
            log.info("Adding contact {}", contact);
            return repository.save(contact);
        }catch (Exception e){
            log.error("Error in adding contact", e);
            throw new Exception("Error in adding contact");
        }
    }

    public ContactModel getContact(Long id) throws Exception {
        try{
            Optional<ContactModel> contact = repository.findById(id);
            if(contact.isPresent()){
                return contact.get();
            }else{
                log.error("Contact not found");
                throw new Exception("Contact not found");
            }
        }catch (Exception e){
            log.error("Error in getting contact", e);
            throw new Exception("Error in getting contact");
        }
    }

    public boolean deleteContact(Long id) throws Exception {
        try{
            repository.deleteById(id);
            return true;
        }catch (Exception e){
            log.error("Error in deleting contact", e);
            throw new Exception("Error in deleting contact");
        }
    }
}
