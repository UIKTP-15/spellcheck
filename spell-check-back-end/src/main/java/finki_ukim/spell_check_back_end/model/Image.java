package finki_ukim.spell_check_back_end.model;

import jakarta.persistence.*;

@Entity
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String imageUrl;  // Link to the image stored in ImgBB

    @ManyToOne
    private GrammarCheck grammarCheck;

    public Image(String imageUrl, GrammarCheck grammarCheck) {
        this.imageUrl = imageUrl;
        this.grammarCheck = grammarCheck;
    }

    public Image() {}

}
