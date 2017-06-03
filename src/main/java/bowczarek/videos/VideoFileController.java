package bowczarek.videos;

import bowczarek.videos.storage.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.stream.Collectors;

/**
 * Created by bowczarek on 02.06.2017.
 */
@Controller
public class VideoFileController {

    private final StorageService storageService;

    @Autowired
    public VideoFileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/videos")
    public String getFiles(Model model) throws IOException {

        model.addAttribute("files", storageService
                .getAll()
                .map(path ->
                        MvcUriComponentsBuilder
                                .fromMethodName(VideoFileController.class, "getFile", path.getFileName().toString())
                                .build().toString())
                .collect(Collectors.toList()));

        return "upload";
    }

    @GetMapping("/videos/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {

        Resource file = storageService.getAsResource(filename);

        return ResponseEntity
                .ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"")
                .body(file);
    }

    @PostMapping("/videos")
    public String handleFileUpload(@RequestParam("file") MultipartFile file,
                                   RedirectAttributes redirectAttributes) {

        storageService.save(file);
        redirectAttributes.addFlashAttribute("message", "Ssuccessfully uploaded " + file.getOriginalFilename());

        return "redirect:/";
    }
}
