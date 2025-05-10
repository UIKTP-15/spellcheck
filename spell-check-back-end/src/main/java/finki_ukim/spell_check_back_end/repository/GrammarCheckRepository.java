package finki_ukim.spell_check_back_end.repository;

import finki_ukim.spell_check_back_end.model.GrammarCheck;
import finki_ukim.spell_check_back_end.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarCheckRepository extends JpaRepository<GrammarCheck, Long> {
    List<GrammarCheck> findAllByUser(User user);
    List<GrammarCheck> findAllByFlaggedTrue();
    List<GrammarCheck> findAllByUserAndFlaggedTrue(User user);
}
