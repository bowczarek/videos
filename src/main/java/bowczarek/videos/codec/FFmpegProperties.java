package bowczarek.videos.codec;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by bowczarek on 03.06.2017.
 */
@Getter
@Setter
@ConfigurationProperties("ffmpeg")
public class FFmpegProperties {

    private String mpegPath = "c:\\tools\\ffmpeg\\bin\\ffmpeg.exe";

    private String probePath = "c:\\tools\\ffmpeg\\bin\\ffprobe.exe";
}
