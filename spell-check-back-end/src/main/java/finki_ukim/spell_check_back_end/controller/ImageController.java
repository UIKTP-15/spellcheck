package finki_ukim.spell_check_back_end.controller;

import finki_ukim.spell_check_back_end.service.ImgBBService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
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
    public String uploadImages(@RequestParam("images") MultipartFile[] files,
                               Model model){

        List<String> imageUrls = new ArrayList<>();


        try{
            for(MultipartFile file : files){
                String imageUrl = imgBBService.uploadImage(file);
                imageUrls.add(imageUrl);
            }
            model.addAttribute("imageUrls", imageUrls);
            return "home";
        }
        catch (Exception e){
            model.addAttribute("error", "Image upload failed: "+e.getMessage());
            return "home";
        }
    }
}
