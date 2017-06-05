package bowczarek.videos.storage;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by bowczarek on 02.06.2017.
 */
@Getter
@Setter
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location where files will be stored
     */
    private String location = "upload-dir";
}
