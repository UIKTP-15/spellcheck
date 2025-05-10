package finki_ukim.spell_check_back_end.service;

import finki_ukim.spell_check_back_end.model.GrammarCheck;
import finki_ukim.spell_check_back_end.model.User;
import finki_ukim.spell_check_back_end.repository.GrammarCheckRepository;
import finki_ukim.spell_check_back_end.repository.specification.GrammarCheckSpecification;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class GrammarCheckService {
    private final GrammarCheckRepository grammarCheckRepository;

    public GrammarCheckService(GrammarCheckRepository grammarCheckRepository) {
        this.grammarCheckRepository = grammarCheckRepository;
    }

    public List<GrammarCheck> findAllByUserFiltered(User user,
                                                    LocalDate from,
                                                    LocalDate to,
                                                    String nameSearch,
                                                    Boolean flagged) {
        Specification<GrammarCheck> spec = Specification.where(null);

        spec = spec.and(GrammarCheckSpecification.hasUser(user));

        if (from != null) {
            spec = spec.and(GrammarCheckSpecification.hasDateAfter(from));
        }
        if (to != null) {
            spec = spec.and(GrammarCheckSpecification.hasDateBefore(to));
        }
        if (nameSearch != null && !nameSearch.isEmpty()) {
            spec = spec.and(GrammarCheckSpecification.hasNameLike(nameSearch));
        }
        if (flagged != null) {
            spec = spec.and(GrammarCheckSpecification.hasFlagged(flagged));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "date");

        return grammarCheckRepository.findAll(spec, sort);
    }


    public void deleteItem(Long id) {
        grammarCheckRepository.deleteById(id);
    }

    public void update(Long id, String newName) {
        GrammarCheck item = grammarCheckRepository.findById(id).orElseThrow(() -> new RuntimeException(String.format("Item with id %d not found", id)));
        item.setName(newName);
        grammarCheckRepository.save(item);
    }
}

