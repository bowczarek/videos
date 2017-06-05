package bowczarek.videos.api;

import bowczarek.videos.api.resources.VideoFileResource;
import bowczarek.videos.codec.FFmpegService;
import bowczarek.videos.codec.VideoMediaInfo;
import bowczarek.videos.domain.VideoFile;
import bowczarek.videos.domain.VideoFileRepository;
import bowczarek.videos.storage.StorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Persistable;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

/**
 * Created by bowczarek on 02.06.2017.
 */
@ExposesResourceFor(VideoFileResource.class)
@RestController
@RequestMapping("/videos")
public class VideoFileController {

    private final Logger logger = LoggerFactory.getLogger(VideoFileController.class);

    private final String[] allowedContentTypes = new String[]{"video/mp4", "video/x-msvideo", "video/x-ms-wmv", "video/mpeg"};

    private final StorageService storageService;
    private final FFmpegService ffmpegService;
    private final VideoFileRepository videoFileRepository;
    private final VideoFileResourceAssembler videoFileResourceAssembler;

    @Autowired
    public VideoFileController(StorageService storageService, FFmpegService ffmpegService,
                               VideoFileRepository videoFileRepository,
                               VideoFileResourceAssembler videoFileResourceAssembler) {
        this.storageService = storageService;
        this.ffmpegService = ffmpegService;
        this.videoFileRepository = videoFileRepository;
        this.videoFileResourceAssembler = videoFileResourceAssembler;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Resources<VideoFileResource>> getFiles() throws IOException {
        Iterable<VideoFile> videoFiles = this.videoFileRepository.findAll();
        List<VideoFileResource> resources = videoFileResourceAssembler.toResources(videoFiles);
        Resources wrapped = new Resources<>(resources, linkTo(VideoFileController.class).withSelfRel());

        return ResponseEntity.ok(wrapped);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VideoFileResource> getFile(@PathVariable long id) {
        VideoFile videoFile = entityOrNotFoundException(videoFileRepository.findById(id));
        VideoFileResource videoFileResource = videoFileResourceAssembler.toResource(videoFile);

        return ResponseEntity.ok(videoFileResource);
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> getStream(@PathVariable long id) {
        VideoFile videoFile = entityOrNotFoundException(videoFileRepository.findById(id));

        Resource resource = storageService.getAsResource(videoFile.getFilePath());

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + videoFile.getFileName() + "\"")
                .body(resource);
    }

    @PostMapping
    public ResponseEntity<Void> upload(@RequestParam("file") MultipartFile file) {

        if (!isContentTypeAllowed(file.getContentType())) {
            String msg = String.format("%s is not allowed format. Allowed formats: %s",
                    file.getContentType(), String.join(",", allowedContentTypes));
            logger.error(msg);

            throw new NotAllowedFormatException(msg);
        }

        Path path = storageService.save(file);

        VideoMediaInfo info = ffmpegService.getMediaInformation(path);

        VideoFile videoFile = new VideoFile(path.getFileName().toString(), path.toString());
        videoFile.setSize(info.getSize());
        videoFile.setDuration(info.getDuration());
        videoFile.setVideoCodecName(info.getVideoCodecName());
        videoFile.setVideoBitRate(info.getVideoBitRate());
        videoFile.setAudioCodecName(info.getAudioCodecName());
        videoFile.setAudioBitRate(info.getAudioBitRate());

        videoFileRepository.save(videoFile);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, videoFileResourceAssembler.linkToSingleResource(videoFile).getHref());

        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    private boolean isContentTypeAllowed(String contentType) {
        return Arrays.stream(allowedContentTypes).anyMatch(s -> s.equals(contentType));
    }

    private static <T extends Persistable<?>> T entityOrNotFoundException(T entity) {
        if (entity == null) {
            throw new EntityNotFoundException();
        }

        return entity;
    }
}
