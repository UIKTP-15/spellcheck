package finki_ukim.spell_check_back_end.model;


import jakarta.persistence.*;
import java.util.*;

import java.time.LocalDate;

@Entity
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

    public GrammarCheck(Long id, User user, LocalDate date, ModelResponse modelResponse) {
        this.id = id;
        this.user = user;
       // this.imageUrl = imageUrl;
        this.date = date;
        this.modelResponse = modelResponse;
    }
    public GrammarCheck(User user, LocalDate date) {
        this.user = user;
        this.date = date;
    }

    public GrammarCheck() {

    }
}
