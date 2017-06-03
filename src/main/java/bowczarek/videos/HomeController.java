package bowczarek.videos;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by bowczarek on 02.06.2017.
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String get() {
        return "redirect:/videos";
    }
}
