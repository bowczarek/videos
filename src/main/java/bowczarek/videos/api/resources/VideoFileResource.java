package bowczarek.videos.api.resources;

import lombok.Getter;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

/**
 * Created by bowczarek on 05.06.2017.
 */
@Getter
@Relation(value = "video", collectionRelation = "videos")
public class VideoFileResource extends ResourceSupport {
    private String fileName;

    private String filePath;

    private long size;

    private double duration;

    private long videoBitRate;

    private String videoCodecName;

    private long audioBitRate;

    private String audioCodecName;

    public VideoFileResource(String fileName, String filePath, long size, double duration,
                             long videoBitRate, String videoCodecName, long audioBitRate,
                             String audioCodecName) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.size = size;
        this.duration = duration;
        this.videoBitRate = videoBitRate;
        this.videoCodecName = videoCodecName;
        this.audioBitRate = audioBitRate;
        this.audioCodecName = audioCodecName;
    }
}
