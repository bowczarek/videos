package bowczarek.videos.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

/**
 * Created by bowczarek on 02.06.2017.
 */
public interface StorageService {

    Resource getAsResource(String filename);

    Path save(MultipartFile file);

    void deleteAll();

    void init();
}
