package bowczarek.videos.codec;

import bowczarek.videos.domain.VideoMediaInfo;

import java.nio.file.Path;

/**
 * Created by bowczarek on 02.06.2017.
 */
public interface CodecService {
    VideoMediaInfo getMediaInformation(Path file);
}
