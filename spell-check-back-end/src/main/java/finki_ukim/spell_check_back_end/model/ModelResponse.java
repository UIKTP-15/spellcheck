package finki_ukim.spell_check_back_end.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

@Entity
public class ModelResponse {
    @Id
    private Long id;
    private String userText;
    private String correctedText;
    @OneToOne
    private GrammarCheck grammarCheck;

    public ModelResponse(Long id, String userText, String correctedText, GrammarCheck grammarCheck) {
        this.id = id;
        this.userText = userText;
        this.correctedText = correctedText;
        this.grammarCheck = grammarCheck;
    }

    public ModelResponse() {

    }
}
