package finki_ukim.spell_check_back_end.controller;

import finki_ukim.spell_check_back_end.model.GrammarCheck;
import finki_ukim.spell_check_back_end.model.User;
import finki_ukim.spell_check_back_end.service.GrammarCheckService;
import jakarta.servlet.http.HttpSession;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/history")
public class HistoryController {
    private final GrammarCheckService grammarCheckService;

    public HistoryController(GrammarCheckService grammarCheckService) {
        this.grammarCheckService = grammarCheckService;
    }

    @GetMapping
    public String findAllByUser(@RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate from,
                                @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDate to,
                                @RequestParam(name = "nameSearch", required = false) String nameSearch,
                                @RequestParam(name = "flagged", required = false) Boolean flagged,
                                HttpSession session,
                                Model model) {
        User user = (User) session.getAttribute("user");
        List<GrammarCheck> grammarChecksByUser = grammarCheckService.findAllByUserFiltered(user, from, to, nameSearch, flagged);
        model.addAttribute("items", grammarChecksByUser);
        if (from != null) {
            model.addAttribute("from", from);
        }
        if (to != null) {
            model.addAttribute("to", to);
        }
        if (nameSearch != null) {
            model.addAttribute("nameSearch", nameSearch);
        }
        if (flagged != null) {
            model.addAttribute("flagged", flagged);
        }
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

    @GetMapping("/mark-as-flagged/{id}")
    public String markAsFlagged(@PathVariable Long id) {
        try {
            this.grammarCheckService.toggleFlag(id);
        } catch (RuntimeException ex) {
            System.out.println(ex.getMessage());
        }
        return "redirect:/history";
    }
}
