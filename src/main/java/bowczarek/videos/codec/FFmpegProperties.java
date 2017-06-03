package bowczarek.videos.codec;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Created by bowczarek on 03.06.2017.
 */
@ConfigurationProperties("ffmpeg")
public class FFmpegProperties {

    private String mpegPath = "c:\\tools\\ffmpeg\\bin\\ffmpeg.exe";

    private String probePath = "c:\\tools\\ffmpeg\\bin\\ffprobe.exe";

    public String getMpegPath() {
        return mpegPath;
    }

    public void setMpegPath(String mpegPath) {
        this.mpegPath = mpegPath;
    }

    public String getProbePath() {
        return probePath;
    }

    public void setProbePath(String probePath) {
        this.probePath = probePath;
    }
}
