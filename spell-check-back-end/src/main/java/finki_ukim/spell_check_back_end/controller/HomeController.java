package finki_ukim.spell_check_back_end.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("")
public class HomeController {
    @GetMapping
    public String redirectToEntry() {
        return "redirect:/entry";
    }

    @GetMapping("/entry")
    public String getHomePage() {
        return "entry_page";
    }

    @GetMapping("/home/upload")
    public String getUploadPage() {
        return "home";
    }
}
