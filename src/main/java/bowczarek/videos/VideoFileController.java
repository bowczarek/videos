package bowczarek.videos;

import bowczarek.videos.codec.FFmpegService;
import bowczarek.videos.domain.VideoFile;
import bowczarek.videos.domain.VideoFileRepository;
import bowczarek.videos.domain.VideoMediaInfo;
import bowczarek.videos.domain.VideoMediaInfoRepository;
import bowczarek.videos.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Created by bowczarek on 02.06.2017.
 */
@RestController
public class VideoFileController {

    private final StorageService storageService;
    private final FFmpegService ffmpegService;
    private final VideoFileRepository videoFileRepository;
    private final VideoMediaInfoRepository videoMediaInfoRepository;

    @Autowired
    public VideoFileController(StorageService storageService, FFmpegService ffmpegService,
                               VideoFileRepository videoFileRepository,
                               VideoMediaInfoRepository videoMediaInfoRepository) {
        this.storageService = storageService;
        this.ffmpegService = ffmpegService;
        this.videoFileRepository = videoFileRepository;
        this.videoMediaInfoRepository = videoMediaInfoRepository;
    }

    @GetMapping("/videos")
    public Iterable<VideoFile> getFiles() throws IOException {
        return this.videoFileRepository.findAll();
    }

    @GetMapping("/videos/{id}")
    public VideoFile getFile(@PathVariable long id) {
        return videoFileRepository.findById(id);
    }

    @GetMapping("/videos/{id}/metadata")
    public VideoMediaInfo getMetadata(@PathVariable long id) {
        return videoFileRepository.findById(id).getVideoMediaInfo();
    }

    @PostMapping("/videos")
    public Iterable<VideoFile> upload(@RequestParam("file") MultipartFile file,
                                      RedirectAttributes redirectAttributes) {

        Path path = storageService.save(file);

        VideoMediaInfo info = ffmpegService.getMediaInformation(path);
        videoMediaInfoRepository.save(info);

        VideoFile videoFile = new VideoFile(path.getFileName().toString(), path.toString());
        videoFile.setVideoMediaInfo(info);
        videoFileRepository.save(videoFile);

        return this.videoFileRepository.findAll();
    }
}
