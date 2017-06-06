package bowczarek.videos.api;

import bowczarek.videos.VideosApplication;
import bowczarek.videos.codec.FFmpegService;
import bowczarek.videos.codec.VideoMediaInfo;
import bowczarek.videos.domain.VideoFile;
import bowczarek.videos.domain.VideoFileRepository;
import bowczarek.videos.storage.StorageService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

/**
 * Created by bowczarek on 05.06.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = VideosApplication.class)
@AutoConfigureMockMvc
@WebAppConfiguration
@DirtiesContext
public class VideoFileControllerTest {

    private MockMvc mockMvc;

    private List<VideoFile> videoFileList;

    @MockBean
    private StorageService storageService;

    @MockBean
    private FFmpegService ffmpegService;

    @Autowired
    private VideoFileRepository videoFileRepository;

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();

        videoFileRepository.deleteAll();
        this.videoFileList = new ArrayList<>();

        VideoFile videoFile = new VideoFile("test.avi", "upload-dir/1/test.avi");
        videoFile.setSize(10);
        videoFile.setDuration(4);
        videoFile.setVideoCodecName("h264");
        videoFile.setVideoBitRate(100);
        videoFile.setAudioCodecName("aac");
        videoFile.setAudioBitRate(200);
        videoFileList.add(videoFileRepository.save(videoFile));
    }

    @Test
    public void shouldReturnNotFoundIfVideoFileDoesNotExist() throws Exception {
        mockMvc.perform(get("/videos/666"))
                .andExpect(status().isNotFound());
    }

    @Test
    public void shouldGetSingleVideoFile() throws Exception {
        mockMvc.perform(get("/videos/" + videoFileList.get(0).getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fileName", is(videoFileList.get(0).getFileName())))
                .andExpect(jsonPath("$.size", is((int) videoFileList.get(0).getSize())))
                .andExpect(jsonPath("$.duration", is(videoFileList.get(0).getDuration())))
                .andExpect(jsonPath("$.videoBitRate", is((int) videoFileList.get(0).getVideoBitRate())))
                .andExpect(jsonPath("$.videoCodecName", is(videoFileList.get(0).getVideoCodecName())))
                .andExpect(jsonPath("$.audioBitRate", is((int) videoFileList.get(0).getAudioBitRate())))
                .andExpect(jsonPath("$.audioCodecName", is(videoFileList.get(0).getAudioCodecName())))
                .andExpect(jsonPath("$._links.self.href",
                        is("http://localhost/videos/" + videoFileList.get(0).getId())))
                .andExpect(jsonPath("$._links.download.href",
                        is("http://localhost/videos/" + videoFileList.get(0).getId() + "/download")));

    }

    @Test
    public void shouldSaveUploadedFile() throws Exception {
        MockMultipartFile multipartFile =
                new MockMultipartFile("file", "test.mp4", "video/mp4", "Spring Framework".getBytes());

        Path path = Paths.get("test");
        long id = 2L;
        given(this.storageService.save(multipartFile, id))
                .willReturn(path);

        VideoMediaInfo videoMediaInfo = new VideoMediaInfo();
        videoMediaInfo.setSize(1055736);
        videoMediaInfo.setDuration(5.312);
        videoMediaInfo.setVideoCodecName("h264");
        videoMediaInfo.setVideoBitRate(1205959);
        videoMediaInfo.setAudioCodecName("aac");
        videoMediaInfo.setAudioBitRate(384828);
        given(this.ffmpegService.getMediaInformation(path))
                .willReturn(videoMediaInfo);

        mockMvc.perform(fileUpload("/videos").file(multipartFile))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "http://localhost/videos/" + id));

        then(this.storageService).should().save(multipartFile, id);
    }

    @Test
    public void shouldListAllFiles() throws Exception {
        mockMvc.perform(get("/videos")).andExpect(status().isOk())
                .andExpect(jsonPath("$._embedded.videos[0].fileName", is(videoFileList.get(0).getFileName())))
                .andExpect(jsonPath("$._embedded.videos[0].size", is((int) videoFileList.get(0).getSize())))
                .andExpect(jsonPath("$._embedded.videos[0].duration", is(videoFileList.get(0).getDuration())))
                .andExpect(jsonPath("$._embedded.videos[0].videoBitRate", is((int) videoFileList.get(0).getVideoBitRate())))
                .andExpect(jsonPath("$._embedded.videos[0].videoCodecName", is(videoFileList.get(0).getVideoCodecName())))
                .andExpect(jsonPath("$._embedded.videos[0].audioBitRate", is((int) videoFileList.get(0).getAudioBitRate())))
                .andExpect(jsonPath("$._embedded.videos[0].audioCodecName", is(videoFileList.get(0).getAudioCodecName())))
                .andExpect(jsonPath("$._embedded.videos[0]._links.self.href",
                        is("http://localhost/videos/" + videoFileList.get(0).getId())))
                .andExpect(jsonPath("$._embedded.videos[0]._links.download.href",
                        is("http://localhost/videos/" + videoFileList.get(0).getId() + "/download")));
    }

}
