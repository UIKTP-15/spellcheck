package finki_ukim.spell_check_back_end.controller;

import finki_ukim.spell_check_back_end.model.GrammarCheck;
import finki_ukim.spell_check_back_end.model.User;
import finki_ukim.spell_check_back_end.service.GrammarCheckService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {
    private final GrammarCheckService grammarCheckService;

    public HistoryController(GrammarCheckService grammarCheckService) {
        this.grammarCheckService = grammarCheckService;
    }

    @GetMapping
    public String findAllByUser(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<GrammarCheck> grammarChecksByUser = grammarCheckService.findAllByUser(user);
        model.addAttribute("items", grammarChecksByUser);
        return "history";
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateItem(@PathVariable Long id, @RequestParam String newName) {
        try {
            this.grammarCheckService.update(id, newName);
            return ResponseEntity.ok("Item successfully updated");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id) {
        this.grammarCheckService.deleteItem(id);
        return "redirect:/history";
    }
    @PutMapping("/toggle-flag/{id}")
    public ResponseEntity<?> toggleFlag(@PathVariable Long id) {
        try {
            grammarCheckService.toggleFlag(id);
            return ResponseEntity.ok("Flag toggled");
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(ex.getMessage());
        }
    }

    @GetMapping("/flagged")
    public String findFlaggedByUser(HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        List<GrammarCheck> flaggedChecks = grammarCheckService.findFlaggedByUser(user);
        model.addAttribute("items", flaggedChecks);
        return "history";
    }
}
