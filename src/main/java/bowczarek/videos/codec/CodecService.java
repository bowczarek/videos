package bowczarek.videos.codec;

import bowczarek.videos.domain.VideoInfo;

import java.nio.file.Path;

/**
 * Created by bowczarek on 02.06.2017.
 */
public interface CodecService {
    VideoInfo getMediaInformation(Path file);
}
