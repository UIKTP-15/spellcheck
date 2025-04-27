package finki_ukim.spell_check_back_end.service;

import finki_ukim.spell_check_back_end.model.GrammarCheck;
import finki_ukim.spell_check_back_end.model.User;
import finki_ukim.spell_check_back_end.repository.GrammarCheckRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GrammarCheckService {
    private final GrammarCheckRepository grammarCheckRepository;

    public GrammarCheckService(GrammarCheckRepository grammarCheckRepository) {
        this.grammarCheckRepository = grammarCheckRepository;
    }

    public List<GrammarCheck> findAllByUser(User user) {
        return this.grammarCheckRepository.findAllByUser(user);
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

