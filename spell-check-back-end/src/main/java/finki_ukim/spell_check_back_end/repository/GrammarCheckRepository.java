package finki_ukim.spell_check_back_end.repository;

import finki_ukim.spell_check_back_end.model.GrammarCheck;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GrammarCheckRepository extends JpaRepository<GrammarCheck, Long> {
    List<GrammarCheck> findAll(Specification<GrammarCheck> spec, Sort sort);
}
