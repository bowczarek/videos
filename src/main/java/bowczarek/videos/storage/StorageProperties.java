package bowczarek.videos.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by bowczarek on 02.06.2017.
 */
@ConfigurationProperties("storage")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
