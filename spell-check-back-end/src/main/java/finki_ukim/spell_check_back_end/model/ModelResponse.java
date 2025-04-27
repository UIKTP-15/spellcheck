package finki_ukim.spell_check_back_end.model;

import jakarta.persistence.*;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userText;
    private String correctedText;
    @OneToOne(mappedBy = "modelResponse", cascade = CascadeType.ALL)
    private GrammarCheck grammarCheck;
}
