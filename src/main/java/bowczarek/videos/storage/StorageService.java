package bowczarek.videos.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * Created by bowczarek on 02.06.2017.
 */
public interface StorageService {
    Stream<Path> getAll();

    Resource getAsResource(String filename);

    Path save(MultipartFile file);

    void init();
}
