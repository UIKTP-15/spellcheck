package finki_ukim.spell_check_back_end.controller;

import finki_ukim.spell_check_back_end.model.User;
import finki_ukim.spell_check_back_end.service.ImgBBService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

// @RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {

    private final ImgBBService imgBBService;

    public ImageController(ImgBBService imgBBService) {
        this.imgBBService = imgBBService;
    }

    @GetMapping
    public String redirectToUpload() {
        return "redirect:/images/upload";
    }

    @GetMapping("/upload")
    public String showUploadPage(Model model) {
        return "home";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImages(@RequestParam("images") MultipartFile[] files, HttpSession session) {
        try {
            imgBBService.uploadImage(files, (User) session.getAttribute("user"));
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
