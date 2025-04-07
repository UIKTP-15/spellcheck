package finki_ukim.spell_check_back_end.controller;

import finki_ukim.spell_check_back_end.service.OpenAiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/openai")
public class OpenAIController {

    private final OpenAiService openAiService;

    public OpenAIController(OpenAiService openAiService) {
        this.openAiService = openAiService;
    }

    @PostMapping("/correcttext")
    public ResponseEntity<String> getCorrectedText(@RequestBody String inputText) {
        try {
            String correctedText = openAiService.correctText(inputText);
            return ResponseEntity.ok(correctedText);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error correcting text: " + e.getMessage());
        }
    }
}
