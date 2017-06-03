package bowczarek.videos.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.*;

/**
 * Created by bowczarek on 03.06.2017.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
public class VideoFile {

    @Id
    @GeneratedValue
    private long id;

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @OneToOne
    @JoinColumn(name = "videoMediaInfoId")
    @RestResource(path = "libraryAddress", rel="metadata")
    private VideoMediaInfo videoMediaInfo;

    protected VideoFile() {
    }

    public VideoFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
