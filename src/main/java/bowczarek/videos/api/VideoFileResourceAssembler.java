package bowczarek.videos.api;

import bowczarek.videos.api.resources.VideoFileResource;
import bowczarek.videos.domain.VideoFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;
import org.springframework.stereotype.Service;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

/**
 * Created by bowczarek on 05.06.2017.
 */
@Service
public class VideoFileResourceAssembler extends ResourceAssemblerSupport<VideoFile, VideoFileResource> {

    private final EntityLinks entityLinks;

    @Autowired
    public VideoFileResourceAssembler(EntityLinks entityLinks) {
        super(VideoFileController.class, VideoFileResource.class);
        this.entityLinks = entityLinks;
    }

    public Link linkToSingleResource(VideoFile videoFile) {
        return entityLinks.linkToSingleResource(VideoFileResource.class, videoFile.getId());
    }

    @Override
    public VideoFileResource toResource(VideoFile videoFile) {
        VideoFileResource videoFileResource = createResourceWithId(videoFile.getId(), videoFile);
        videoFileResource.add(linkTo(methodOn(VideoFileController.class).getStream(videoFile.getId())).withRel("download"));

        return videoFileResource;
    }

    @Override
    protected VideoFileResource instantiateResource(VideoFile entity) {
        return new VideoFileResource(entity.getFileName(), entity.getFilePath(), entity.getSize(), entity.getDuration(),
                entity.getVideoBitRate(), entity.getVideoCodecName(), entity.getAudioBitRate(), entity.getAudioCodecName());
    }
}
