package bowczarek.videos.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.jpa.domain.AbstractPersistable;
import org.springframework.data.rest.core.annotation.RestResource;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 * Created by bowczarek on 03.06.2017.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
@Entity
public class VideoFile extends AbstractPersistable<Long> {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private String filePath;

    @Column
    private long size;

    @Column
    private double duration;

    @Column
    private long videoBitRate;

    @Column
    private String videoCodecName;

    @Column
    private long audioBitRate;

    @Column
    private String audioCodecName;

    protected VideoFile() {
    }

    public VideoFile(String fileName, String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }
}
