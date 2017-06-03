package bowczarek.videos.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by bowczarek on 02.06.2017.
 */
@Setter
@Getter
@Entity
public class VideoMediaInfo {

    @Id
    @GeneratedValue
    private long id;

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
}
