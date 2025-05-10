package finki_ukim.spell_check_back_end.service;

import finki_ukim.spell_check_back_end.model.GrammarCheck;
import finki_ukim.spell_check_back_end.model.Image;
import finki_ukim.spell_check_back_end.model.User;
import finki_ukim.spell_check_back_end.repository.GrammarCheckRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class ImgBBService {

    @Value("${imgbb.api.key}")
    private String imgbbApiKey;

    @Value("${model.api.key}")
    private String api;

    private final RestTemplate restTemplate;

    private final OpenAiService openAiService;

    private final GrammarCheckRepository grammarCheckRepository;


    public void uploadImage(MultipartFile[] files, User user) {
        String apiUrl = "https://api.imgbb.com/1/upload?key=" + imgbbApiKey;

        List<Image> images = new ArrayList<>();
        GrammarCheck grammarCheck = new GrammarCheck();
        try {
            for (MultipartFile file : files) {
                ByteArrayResource imageAsResource = new ByteArrayResource(file.getBytes()) {
                    @Override
                    public String getFilename() {
                        return file.getOriginalFilename(); // ensures a filename is included
                    }
                };
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.MULTIPART_FORM_DATA);

                MultiValueMap<String, Object> bodyImgBB = new LinkedMultiValueMap<>();
                MultiValueMap<String, Object> bodyModel = new LinkedMultiValueMap<>();

                bodyModel.add("files", imageAsResource);
                bodyImgBB.add("image", imageAsResource);

                HttpEntity<MultiValueMap<String, Object>> requestEntityImgBB = new HttpEntity<>(bodyImgBB, headers);
                HttpEntity<MultiValueMap<String, Object>> requestEntityModel = new HttpEntity<>(bodyModel, headers);

                ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntityImgBB, Map.class);
                ResponseEntity<Map> modelsTextResponse = restTemplate.exchange(api, HttpMethod.POST, requestEntityModel, Map.class);

                if (modelsTextResponse.getStatusCode() == HttpStatus.OK) {
                    String modelsResponse = modelsTextResponse.getBody().get("text").toString();
                    System.out.println(modelsResponse);
                    String correctedText = openAiService.correctText((String) modelsTextResponse.getBody().get("text"));
                    System.out.println("Corrected text: " + correctedText);

                    if(grammarCheck.getInputText()==null){
                        grammarCheck.setInputText("");
                        grammarCheck.setCorrectedText("");
                    }
                    grammarCheck.setInputText(grammarCheck.getInputText()+ modelsResponse);
                    grammarCheck.setCorrectedText(grammarCheck.getCorrectedText()+ correctedText);
                }
                if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
                    Image image = new Image(data.get("url").toString());
                    images.add(image);
                } else {
                    throw new RuntimeException("Image upload failed! Status: " + response.getStatusCode());
                }
            }

            grammarCheck.setImages(images);
            grammarCheck.setName("Default Name");
            grammarCheck.setUser(user);
            grammarCheck.setDate(LocalDate.now());
            grammarCheck.setFlagged(false);

            grammarCheckRepository.save(grammarCheck);

        } catch (Exception e) {
            throw new RuntimeException("Error while preparing image for upload", e);
        }
    }
}
