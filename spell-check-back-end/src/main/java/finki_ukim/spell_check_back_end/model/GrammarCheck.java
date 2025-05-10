package finki_ukim.spell_check_back_end.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrammarCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "grammarCheck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images;

    private LocalDate date;
    private Boolean flagged;

    @OneToOne(cascade = CascadeType.REMOVE)
    @JoinColumn(name = "model_response_id")
    private ModelResponse modelResponse;

    public void setName(String name) {
        this.name = name;
    }
}
