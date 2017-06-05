package bowczarek.videos.storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Created by bowczarek on 02.06.2017.
 */
@Service
public class FileSystemStorageService implements StorageService {

    private final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);

    private final Path rootLocation;

    @Autowired
    public FileSystemStorageService(StorageProperties storageProperties) {
        this.rootLocation = Paths.get(storageProperties.getLocation());
    }

    @Override
    public Resource getAsResource(String filePath) {
        try {
            Path file = Paths.get(filePath);
            Resource resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                String msg = "Could not read file: " + filePath;
                logger.error(msg);
                throw new FileNotFoundException(msg);
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
            throw new FileNotFoundException("Could not read file: " + filePath, e);
        }
    }

    @Override
    public Path save(MultipartFile file, Long id) {
        try {
            if (file.isEmpty()) {
                String msg = "Cannot store empty file " + file.getOriginalFilename();
                logger.error(msg);
                throw new StorageException(msg);
            }

            Files.createDirectory(this.rootLocation.resolve(id.toString()));
            Path filePath = this.rootLocation.resolve(Paths.get(id.toString(), file.getOriginalFilename()));
            Files.copy(file.getInputStream(), filePath);

            return filePath;
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            if (Files.notExists(rootLocation)) {
                Files.createDirectory(rootLocation);
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new StorageException("Couldn't create directory", e);
        }
    }
}
