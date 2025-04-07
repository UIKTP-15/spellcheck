package finki_ukim.spell_check_back_end.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrammarCheck {
    @Id
    private Long id;
    @OneToOne
    private User user;
    private String imageUrl;
    private LocalDate date;
    @OneToOne
    private ModelResponse modelResponse;
}
