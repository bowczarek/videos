package bowczarek.videos.codec;

import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by bowczarek on 02.06.2017.
 */
@Service
public class FFmpegService implements CodecService {

    private final Logger logger = LoggerFactory.getLogger(FFmpegService.class);

    private final FFmpegProperties ffmpegProperties;

    @Autowired
    public FFmpegService(FFmpegProperties ffmpegProperties) {
        this.ffmpegProperties = ffmpegProperties;
    }

    @Override
    public VideoMediaInfo getMediaInformation(Path file) {
        FFprobe ffprobe = null;
        try {
            VideoMediaInfo videoInfo = new VideoMediaInfo();
            ffprobe = new FFprobe(this.ffmpegProperties.getProbePath());
            FFmpegProbeResult probeResult = ffprobe.probe(file.toString());
            FFmpegFormat format = probeResult.getFormat();

            FFmpegStream videoStream = probeResult.getStreams().stream()
                    .filter(ffmpegStream -> ffmpegStream.codec_type == FFmpegStream.CodecType.VIDEO)
                    .findFirst()
                    .orElseThrow(() -> new FFmpegException("Couldn't find any video stream in probe"));

            FFmpegStream audioStream = probeResult.getStreams().stream()
                    .filter(ffmpegStream -> ffmpegStream.codec_type == FFmpegStream.CodecType.AUDIO)
                    .findFirst()
                    .orElseThrow(() -> new FFmpegException("Couldn't find any audio codec in probe"));

            videoInfo.setSize(format.size);
            videoInfo.setDuration(format.duration);
            videoInfo.setVideoBitRate(videoStream.bit_rate);
            videoInfo.setVideoCodecName(videoStream.codec_name);
            videoInfo.setAudioBitRate(audioStream.bit_rate);
            videoInfo.setAudioCodecName(audioStream.codec_name);

            return videoInfo;

        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new FFmpegException("Couldn't read file", e);
        } catch (FFmpegException e) {
            logger.error(e.getMessage());
            throw e;
        }
    }
}
