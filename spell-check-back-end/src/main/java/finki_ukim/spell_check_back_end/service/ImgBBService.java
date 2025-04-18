package finki_ukim.spell_check_back_end.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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


    public String uploadImage(MultipartFile file) {
        String apiUrl = "https://api.imgbb.com/1/upload?key=" + imgbbApiKey;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        MultiValueMap<String, Object> bodyImgBB = new LinkedMultiValueMap<>();
        MultiValueMap<String, Object> bodyModel = new LinkedMultiValueMap<>();

        try {
            ByteArrayResource imageAsResource = new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename(); // ensures a filename is included
                }
            };

            bodyModel.add("files", imageAsResource);
            bodyImgBB.add("image", imageAsResource);
        } catch (Exception e) {
            throw new RuntimeException("Error while preparing image for upload", e);
        }

        HttpEntity<MultiValueMap<String, Object>> requestEntityImgBB = new HttpEntity<>(bodyImgBB, headers);
        HttpEntity<MultiValueMap<String, Object>> requestEntityModel = new HttpEntity<>(bodyModel, headers);

        ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST, requestEntityImgBB, Map.class);
        ResponseEntity<Map> correctedTextResponse = restTemplate.exchange(api, HttpMethod.POST, requestEntityModel, Map.class);

        if(correctedTextResponse.getStatusCode() == HttpStatus.OK) {
            System.out.println((String) correctedTextResponse.getBody().get("text"));
            String correctedText = openAiService.correctText((String) correctedTextResponse.getBody().get("text"));
            System.out.println("Corrected text: " + correctedText);

            // Persist in db
            //return (String) correctedTextResponse.getBody().get("text");
        }else {
        }
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            Map<String, Object> data = (Map<String, Object>) response.getBody().get("data");
            return (String) data.get("url");
        } else {
            throw new RuntimeException("Image upload failed! Status: " + response.getStatusCode());
        }
    }
}
