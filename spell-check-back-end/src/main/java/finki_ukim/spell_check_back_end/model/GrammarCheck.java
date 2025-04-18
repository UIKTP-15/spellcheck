package finki_ukim.spell_check_back_end.model;


import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.util.*;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GrammarCheck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    //@OneToOne
    //
    @ManyToOne
    private User user;

    @OneToMany(mappedBy = "grammarCheck", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Image> images;

    //private String imageUrl;
    private LocalDate date;
    @OneToOne
    private ModelResponse modelResponse;
}
