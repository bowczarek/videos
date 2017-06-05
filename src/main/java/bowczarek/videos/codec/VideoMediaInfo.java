package bowczarek.videos.codec;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by bowczarek on 02.06.2017.
 */
@Setter
@Getter
public class VideoMediaInfo {
    private long size;

    private double duration;

    private long videoBitRate;

    private String videoCodecName;

    private long audioBitRate;

    private String audioCodecName;
}
