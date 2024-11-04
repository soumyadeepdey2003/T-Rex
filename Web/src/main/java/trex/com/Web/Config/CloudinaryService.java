package trex.com.Web.Config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Slf4j
public class CloudinaryService {



    @Autowired
    private  Cloudinary cloudinary;

    public String uploadFile(MultipartFile file) throws IOException {
        log.info("Uploading file to Cloudinary: {}", file.getOriginalFilename());
        Map<String, Object> uploadResult = cloudinary.uploader().upload(file.getBytes(),
                ObjectUtils.asMap("folder", "items"));  // Use "items" folder for organization

        if (uploadResult == null) {
            throw new IllegalStateException("File upload failed. Check logs for details.");
        }

        log.info("File uploaded to Cloudinary: {}", uploadResult.get("url"));
        return uploadResult.get("url").toString();  // Return the image URL from Cloudinary
    }

    public void deleteFile(String imageUrl) throws IOException {
        log.info("Deleting file from Cloudinary: {}", imageUrl);
        cloudinary.uploader().destroy(imageUrl, ObjectUtils.emptyMap());
        log.info("File deleted from Cloudinary: {}", imageUrl);
    }
}
