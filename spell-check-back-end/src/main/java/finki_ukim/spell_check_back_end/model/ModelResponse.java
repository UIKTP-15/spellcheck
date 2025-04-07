package finki_ukim.spell_check_back_end.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ModelResponse {
    @Id
    private Long id;
    private String userText;
    private String correctedText;
    @OneToOne
    private GrammarCheck grammarCheck;
}
