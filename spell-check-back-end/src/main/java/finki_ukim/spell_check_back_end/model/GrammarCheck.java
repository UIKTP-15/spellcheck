package finki_ukim.spell_check_back_end.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;

import java.time.LocalDate;

@Entity
public class GrammarCheck {
    @Id
    private Long id;
    @OneToOne
    private User user;
    private String imageUrl;
    private LocalDate date;
    @OneToOne
    private ModelResponse modelResponse;

    public GrammarCheck(Long id, User user, String imageUrl, LocalDate date, ModelResponse modelResponse) {
        this.id = id;
        this.user = user;
        this.imageUrl = imageUrl;
        this.date = date;
        this.modelResponse = modelResponse;
    }

    public GrammarCheck() {

    }
}
