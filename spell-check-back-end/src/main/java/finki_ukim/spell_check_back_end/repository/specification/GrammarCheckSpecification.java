package finki_ukim.spell_check_back_end.repository.specification;

import finki_ukim.spell_check_back_end.model.GrammarCheck;
import finki_ukim.spell_check_back_end.model.User;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;

public class GrammarCheckSpecification {
    public static Specification<GrammarCheck> hasUser(User user) {
        return (root, query, cb) -> cb.equal(root.get("user"), user);
    }

    public static Specification<GrammarCheck> hasDateAfter(LocalDate dateFrom) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("date"), dateFrom);
    }

    public static Specification<GrammarCheck> hasDateBefore(LocalDate dateTo) {
        return (root, query, cb) -> cb.lessThanOrEqualTo(root.get("date"), dateTo);
    }

    public static Specification<GrammarCheck> hasNameLike(String nameSearch) {
        return (root, query, cb) -> cb.like(cb.lower(root.get("name")), "%" + nameSearch.toLowerCase() + "%");
    }

    public static Specification<GrammarCheck> hasFlagged(Boolean flagged) {
        return (root, query, cb) -> cb.equal(root.get("flagged"), flagged);
    }
}