package bowczarek.videos;

import bowczarek.videos.codec.FFmpegService;
import bowczarek.videos.domain.*;
import bowczarek.videos.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;

/**
 * Created by bowczarek on 02.06.2017.
 */
@RestController
public class VideoFileController {

    private final Logger logger = LoggerFactory.getLogger(VideoFileController.class);

    private final String[] allowedContentTypes = new String[]{"video/mp4", "video/x-msvideo", "video/x-ms-wmv", "video/mpeg"};

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
    public Iterable<VideoFile> getFiles(Pageable pageable) throws IOException {
        return this.videoFileRepository.findAll(pageable);
    }

    @GetMapping("/videos/{id}")
    public VideoFile getFile(@PathVariable long id) {
        VideoFile videoFile = videoFileRepository.findById(id);

        if (videoFile == null) {
            throw new EntityNotFoundException(String.format("Video file with %d id was not found.", id));
        }

        return videoFile;
    }

    @GetMapping("/videos/{id}/metadata")
    public VideoMediaInfo getMetadata(@PathVariable long id) {
        VideoFile videoFile = videoFileRepository.findById(id);

        if (videoFile == null) {
            throw new EntityNotFoundException(String.format("Video file with %d id was not found.", id));
        }

        return videoFile.getVideoMediaInfo();
    }

    @GetMapping("/videos/{id}/stream")
    public ResponseEntity<Resource> getStream(@PathVariable long id) {
        VideoFile videoFile = videoFileRepository.findById(id);

        if (videoFile == null) {
            throw new EntityNotFoundException(String.format("Video file with %d id was not found.", id));
        }

        Resource resource = storageService.getAsResource(videoFile.getFilePath());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + videoFile.getFileName() + "\"")
                .body(resource);
    }

    @PostMapping("/videos")
    public Iterable<VideoFile> upload(@RequestParam("file") MultipartFile file) {

        if (!isContentTypeAllowed(file.getContentType())) {
            throw new NotAllowedFormatException(String.format("%s is not allowed format. Allowed formats: %s",
                    file.getContentType(), String.join(",", allowedContentTypes)));
        }

        Path path = storageService.save(file);

        VideoMediaInfo info = ffmpegService.getMediaInformation(path);
        videoMediaInfoRepository.save(info);

        VideoFile videoFile = new VideoFile(path.getFileName().toString(), path.toString());
        videoFile.setVideoMediaInfo(info);
        videoFileRepository.save(videoFile);

        return this.videoFileRepository.findAll();
    }

    private boolean isContentTypeAllowed(String contentType) {
        return Arrays.stream(allowedContentTypes).anyMatch(s -> s.equals(contentType));
    }
}
