package bowczarek.videos.codec;

import bowczarek.videos.domain.VideoInfo;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.probe.FFmpegFormat;
import net.bramp.ffmpeg.probe.FFmpegProbeResult;
import net.bramp.ffmpeg.probe.FFmpegStream;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by bowczarek on 02.06.2017.
 */
public class FFmpegService implements CodecService {

    @Override
    public VideoInfo getMediaInformation(Path file) {
        FFprobe ffprobe = null;
        try {
            VideoInfo videoInfo = new VideoInfo();
            ffprobe = new FFprobe("");
            FFmpegProbeResult probeResult = ffprobe.probe("input.mp4");
            FFmpegFormat format = probeResult.getFormat();

            FFmpegStream videoStream = probeResult.getStreams().stream()
                    .filter(ffmpegStream -> ffmpegStream.codec_type == FFmpegStream.CodecType.VIDEO)
                    .findFirst()
                    .get();

            FFmpegStream audioStream = probeResult.getStreams().stream()
                    .filter(ffmpegStream -> ffmpegStream.codec_type == FFmpegStream.CodecType.AUDIO)
                    .findFirst()
                    .get();

            videoInfo.size = format.size;
            videoInfo.duration = format.duration;
            videoInfo.videoBitRate = videoStream.bit_rate;
            videoInfo.videoCodecName = videoStream.codec_name;
            videoInfo.audioBitRate = audioStream.bit_rate;
            videoInfo.audioCodecName = audioStream.codec_name;

            return videoInfo;

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
